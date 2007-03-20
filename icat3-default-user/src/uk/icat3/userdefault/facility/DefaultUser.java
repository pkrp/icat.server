/*
 * DefaultUser.java
 *
 * Created on 20 March 2007, 09:16
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.icat3.userdefault.facility;

import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.UUID;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import org.apache.log4j.Logger;
import org.globus.myproxy.MyProxyException;
import org.ietf.jgss.GSSCredential;
import uk.icat3.exceptions.LoginException;
import uk.icat3.exceptions.NoSuchUserException;
import uk.icat3.user.User;
import uk.icat3.user.UserDetails;
import uk.icat3.userdefault.cog.DelegateCredential;
import uk.icat3.userdefault.cog.PortalCredential;
import uk.icat3.userdefault.entity.*;
import uk.icat3.userdefault.exception.LoginError;

/**
 *
 * @author gjd37
 */
public class DefaultUser implements User{
    
    private EntityManager manager;
    
    // Global class logger
    static Logger log = Logger.getLogger(DefaultUser.class);
    
    /** Creates a new instance of DefaultUser */
    public DefaultUser(EntityManager manager) {
        this.manager = manager;
    }
    
    public String getUserIdFromSessionId(String sessionId) throws LoginException {
        log.trace("getUserIdFromSessionId("+sessionId+")");
        try {
            Session session = (Session)manager.createNamedQuery("Session.findByUserSessionId").setParameter("userSessionId", sessionId).getSingleResult();
            
            //is valid
            if(session.getExpireDateTime().before(new Date())) throw new LoginException(sessionId+" has expired");
            
            //check if session id is running as admin, if so, return runAs userId
            if(session.isAdmin()){
                log.debug("user: "+session.getRunAs()+" is associated with: "+sessionId);
                return session.getRunAs();
            } else {
                log.debug("user: "+session.getUserId().getUserId()+" is associated with: "+sessionId);
                return session.getUserId().getUserId();
            }
            
        } catch(NoResultException ex) {
            throw new LoginException("Invalid sessionid: "+sessionId);
        }catch(LoginException ex) {
            log.warn(sessionId+" has expired");
            throw ex
        } catch(Exception ex) {
            if(ex instanceof LoginException) throw (LoginException)ex;
            else throw new LoginException("Unable to find user by sessionid: "+sessionId);
        }
    }
    
    public String login(String username, String password) throws LoginException {
        return login(username, password, 2);
    }
    
    public String login(String username, String password, int lifetime) throws LoginException {
        if(username == null || username.equals("")) throw new IllegalArgumentException("Username cannot be null or empty.");
        if(password == null || password.equals("")) throw new IllegalArgumentException("Password cannot be null or empty.");
        
        GSSCredential myproxy_proxy;
        try {
            //lookup proxy and contact for users credential
            ProxyServers proxyserver = (ProxyServers)manager.createNamedQuery("ProxyServers.findByActive").setParameter("active", true).getSingleResult();
            myproxy_proxy = DelegateCredential.getProxy(username, password, lifetime, PortalCredential.getPortalProxy(),
                    proxyserver.getProxyServerAddress(),proxyserver.getPortNumber(),proxyserver.getCaRootCertificate());
            
            //insert proxy into DB
            String sid = insertSessionImpl(username, myproxy_proxy);
            
            log.info("Logged in for user: "+username+" with sessionid:" +sid);
            return sid;
            
        } catch(NoResultException ex) {
            throw new LoginException("MyProxy servers set up incorrectly");
        } catch (MyProxyException mex) {
            log.warn("Error from myproxy server: "+mex.getMessage(),mex);
            throw new LoginException(handleMyProxyException(mex));
        } catch (LoginException lex) {
            throw lex;
        } catch (Exception e) {
            log.warn("Unexpected error from myproxy: "+e.getMessage(),e);
            throw new LoginException(e.getMessage(),e);
        }
        
    }
    
    public boolean logout(String sessionId) {
        try {
            Session session = (Session)manager.createNamedQuery("Session.findByUserSessionId").setParameter("userSessionId", sessionId).getSingleResult();
            manager.remove(session);
            return true;
        } catch(NoResultException ex) {
            log.warn(sessionId+" not in DB");
            return false;
        }
    }
    
    public UserDetails getUserDetails(String sessionId, String user) throws LoginException, NoSuchUserException {
        throw new UnsupportedOperationException("Method not supported.");
    }
    
    /**
     *
     */
    public String login(String adminUsername, String AdminPassword, String runAsUser) throws LoginException {
        //find admin user first
        uk.icat3.userdefault.entity.User user =  null;
        try{
            user = (uk.icat3.userdefault.entity.User)manager.createNamedQuery("User.findByUserId").setParameter("userId","admin").getSingleResult();
        } catch(NoResultException ex) {
            log.warn("Admin user not set up in DB");
            throw new LoginException("Admin user not set up in DB");
        }
        
        //check that password is the same, should really hash it
        if(!AdminPassword.equals(user.getPassword())){
            log.warn("Invalid admin password: "+AdminPassword);
        } else  log.info("Admin password correct");
        
        //create session
        //create UUID for session
        String sid = UUID.randomUUID().toString();
        
        //create a session to put in DB
        Session session = new Session();
        Calendar cal =  GregorianCalendar.getInstance();
        cal.add(GregorianCalendar.HOUR,2); //add 2 hours
        session.setExpireDateTime(cal.getTime());
        session.setUserSessionId(sid);
        session.setRunAs(runAsUser);
        session.setCredential("ADMIN_CREDENTIAL");
        
        user.addSession(session);
        manager.persist(session);
        
        log.info("Logged in for user: "+runAsUser+" running as admin with sessionid:" +sid);
        
        return sid;
        
    }
    
    /**
     * To support all method in User interface, throws Runtime UnsupportedOperationException as this method
     * will never be support by the default implementation
     */
    public String login(String credential) throws LoginException {
        throw new UnsupportedOperationException("Method not supported.");
    }
    
    
    ///////////////////////////  Private Methods /////////////////////////////////////////////////////////////
    
    private String insertSessionImpl(String username, GSSCredential credential) throws LoginException {
        
        log.trace("Starting insertSessionImpl");
        
        Certificate certificate =  null;
        String DN = null;
        boolean lifetimeLeft = false;
        
        uk.icat3.userdefault.entity.User user = null;
        
        try {
            certificate = new Certificate(credential);
            DN  = certificate.getDn();
            lifetimeLeft = certificate.isLifetimeLeft();
            
            log.debug("Loaded credential, user "+DN);
            
        } catch (CertificateExpiredException ce) {
            log.warn("Certificate has expired.",ce);
            throw new LoginException(LoginError.CREDENTIALS_EXPIRED.toString(),ce);
        } catch (CertificateException ex) {
            log.warn("Unable to load certificate",ex);
            throw new LoginException(LoginError.UNKNOWN.toString(),ex);
            
        }
        
        if (lifetimeLeft) {
            //set up new session
            
            //create UUID for session
            String sid = UUID.randomUUID().toString();
            
            //create a session to put in DB
            Session session = new Session();
            
            session.setCredential(certificate.getStringRepresentation());
            
            //set expire time on session
            Calendar cal =  GregorianCalendar.getInstance();
            try {
                cal.add(GregorianCalendar.SECOND,(int)certificate.getLifetime()-60*5); //minus 5 mins
                log.trace("Lifetime left is: "+certificate.getLifetime()+" secs and "+certificate.getLifetime()/3600);
                log.trace("Setting expire time as: "+ cal.getTime());
            } catch (CertificateException ex) {
                log.warn("Unable to load certificate",ex);
                throw new LoginException(LoginError.UNKNOWN.toString(),ex);
                
            }
            session.setExpireDateTime(cal.getTime());
            session.setUserSessionId(sid);
            
            //get user
            try {
                log.trace("Getting user.");
                //need to get user corresponding to DN
                user = (uk.icat3.userdefault.entity.User) manager.createNamedQuery("User.findByDn").setParameter("dn",DN).getSingleResult();
                
                //user is owner so set it there
                user.addSession(session);
                
            } catch(Exception enfe){
                //no entity found, so create one
                log.info("No user found, creating new user.");
                user = new uk.icat3.userdefault.entity.User();
                user.setDn(DN);
                user.setUserId(username);
                
                manager.persist(user);
                //add new user to session
                //user is owner so set it there
                user.addSession(session);
                //session.setUserId(user);
            }
            
            //save session
            log.trace("Persiting session.");
            manager.persist(session);
            log.info("New session created for user: "+DN+" sid: "+sid);
            
            return sid;
            
        } else {
            log.warn("No session created. Proxy has expired for "+DN);
            throw new LoginException(LoginError.CREDENTIALS_EXPIRED.toString());
        }
    }
    
    private String handleMyProxyException( MyProxyException e ) {
        String trace = e.getLocalizedMessage().trim();
        
        String errMsg = null;
        log.trace("MyProxy Trace is : '"+trace+"'");
        if( trace.compareToIgnoreCase( "MyProxy get failed. [Root error message: invalid pass phrase]") == 0) {
            errMsg = "Invalid Passphrase Please Try Again";
            
        } else if( trace.compareToIgnoreCase( "MyProxy get failed. [Root error message: Bad password invalid pass phrase]" )==0 ) {
            errMsg = "Invalid Passphrase Please Try Again";
            
        } else if( trace.compareToIgnoreCase( "MyProxy get failed. [Root error message: requested credentials have expired]" )==0 ) {
            errMsg = "Credentials have expired on MyProxy server. Upload a new proxy and try again";
            
        } else if(  trace.compareToIgnoreCase( "MyProxy get failed. [Root error message: Credentials do not exist Unable to retrieve credential information]" )==0 ) {
            errMsg = "No credentials on MyProxy server. Upload a proxy and try again";
            
        } else if(  trace.compareToIgnoreCase( "MyProxy" )==0 ) {
            errMsg = "No credentials on MyProxy server. Upload a proxy and try again";
            
        } else if(  trace.contains( "Authentication failure invalid pass phrase")  || trace.contains( "Authentication failureinvalid pass phrase") ) {
            errMsg = "Invalid Password. Please Try Again";
        } else {
            errMsg = "Unknown exception - " + trace;
        }
        return errMsg;
    }
    
}