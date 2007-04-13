/*
 * TestDatafileManager.java
 *
 * Created on 07 March 2007, 12:42
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.icat3.investigationmanager;

import java.util.Collection;
import java.util.Random;
import junit.framework.JUnit4TestAdapter;

import org.apache.log4j.Logger;
import uk.icat3.exceptions.ICATAPIException;
import static org.junit.Assert.*;
import org.junit.Test;
import uk.icat3.entity.Keyword;
import uk.icat3.exceptions.InsufficientPrivilegesException;
import uk.icat3.exceptions.NoSuchObjectFoundException;
import uk.icat3.exceptions.ValidationException;
import uk.icat3.manager.InvestigationManager;
import uk.icat3.util.AccessType;
import uk.icat3.util.BaseTestClassTX;
import static uk.icat3.util.TestConstants.*;

/**
 *
 * @author gjd37
 */
public class TestKeyword extends BaseTestClassTX {
    
    private static Logger log = Logger.getLogger(TestKeyword.class);
    private Random random = new Random();
    
    /**
     * Tests creating a file
     */
    @Test
    public void addKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for adding keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        Keyword validKeyword  = getKeyword(true);
        
        Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(VALID_USER_FOR_INVESTIGATION, validKeyword, VALID_DATASET_ID_FOR_INVESTIGATION, em);
        
        Keyword modified = em.find(Keyword.class,keywordInserted.getKeywordPK());
        
        checkKeyword(modified);
        assertFalse("Deleted must be false", modified.isDeleted());
    }
    
    /**
     * Tests creating a file
     */
    @Test(expected=ValidationException.class)
    public void addDuplicateKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for adding invalid keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword duplicateKeyword = getKeywordDuplicate(true);
        
        try {
            Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(VALID_USER_FOR_INVESTIGATION, duplicateKeyword, VALID_DATASET_ID_FOR_INVESTIGATION, em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'unique'", ex.getMessage().contains("unique"));
            throw ex;
        }
    }
    
    /**
     * Tests creating a file
     */
    @Test
    public void deleteKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for rmeoving keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        Keyword validKeyword  = getKeywordDuplicate(true);
        
        
        InvestigationManager.deleteInvestigationObject(VALID_USER_FOR_INVESTIGATION, validKeyword,  AccessType.DELETE, em);
        
        Keyword modified = em.find(Keyword.class,validKeyword.getKeywordPK() );
        
        checkKeyword(modified);
        assertTrue("Deleted must be true", modified.isDeleted());
    }
    
    /**
     * Tests creating a file
     */
    @Test
    public void addDeletedKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for adding deleted keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword duplicateKeyword = getKeywordDuplicate(true);
        
        Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(VALID_USER_FOR_INVESTIGATION, duplicateKeyword, VALID_DATASET_ID_FOR_INVESTIGATION, em);
        
        Keyword modified = em.find(Keyword.class,keywordInserted.getKeywordPK() );
        
        checkKeyword(modified);
        assertFalse("Deleted must be false", modified.isDeleted());
    }
    
    /**
     * Tests creating a file
     */
    @Test
    public void removeKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for rmeoving keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword duplicateKeyword = getKeywordDuplicate(true);
        
        InvestigationManager.deleteInvestigationObject(VALID_USER_FOR_INVESTIGATION, duplicateKeyword, AccessType.REMOVE, em);
        
        Keyword modified = em.find(Keyword.class,duplicateKeyword.getKeywordPK() );
        
        assertNull("Keyword must not be found in DB "+duplicateKeyword, modified);
    }
    
    /**
     * Tests creating a file
     */
    @Test(expected=InsufficientPrivilegesException.class)
    public void addKeywordInvalidUser() throws ICATAPIException {
        log.info("Testing  user: "+INVALID_USER+ " for adding keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        Keyword validKeyword  = getKeyword(true);
        
        try {
            Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(INVALID_USER, validKeyword, VALID_DATASET_ID_FOR_INVESTIGATION, em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'does not have permission'", ex.getMessage().contains("does not have permission"));
            
            throw ex;
        }
    }
    
    /**
     * Tests creating a file
     */
    @Test(expected=NoSuchObjectFoundException.class)
    public void addKeywordInvalidInvestigation() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for adding keyword to invalid investigation Id");
        
        Keyword validKeyword  = getKeyword(true);
        
        try {
            Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(VALID_USER_FOR_INVESTIGATION, validKeyword, random.nextLong(), em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'not found'", ex.getMessage().contains("not found"));
            
            throw ex;
        }
    }
    
    /**
     * Tests creating a file
     */
    @Test(expected=ValidationException.class)
    public void addInvalidKeyword() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for adding invalid keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword invalidKeyword = getKeyword(false);
        
        try {
            Keyword keywordInserted = (Keyword)InvestigationManager.addInvestigationObject(VALID_USER_FOR_INVESTIGATION, invalidKeyword, VALID_DATASET_ID_FOR_INVESTIGATION, em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'cannot be null'", ex.getMessage().contains("cannot be null"));
            throw ex;
        }
    }    
   
    
    /**
     * Tests creating a file
     */
    @Test(expected=InsufficientPrivilegesException.class)
    public void deleteKeywordProps() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for deleting a props keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword propsKeyword = getKeywordDuplicate(false);
        
        try {
            InvestigationManager.deleteInvestigationObject(VALID_USER_FOR_INVESTIGATION, propsKeyword, AccessType.DELETE, em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'cannot be modified'", ex.getMessage().contains("cannot be modified"));
            throw ex;
        }
    }
    
    /**
     * Tests creating a file
     */
    @Test(expected=InsufficientPrivilegesException.class)
    public void removeKeywordProps() throws ICATAPIException {
        log.info("Testing  user: "+VALID_USER_FOR_INVESTIGATION+ " for removing a props keyword to investigation Id: "+VALID_INVESTIGATION_ID);
        
        //create invalid keyword, no name
        Keyword propsKeyword = getKeywordDuplicate(false);
        log.trace(propsKeyword);
        try {
            InvestigationManager.deleteInvestigationObject(VALID_USER_FOR_INVESTIGATION, propsKeyword, AccessType.REMOVE, em);
        } catch (ICATAPIException ex) {
            log.warn("caught: "+ex.getClass()+" "+ex.getMessage());
            assertTrue("Exception must contain 'cannot be modified'", ex.getMessage().contains("cannot be modified"));
            throw ex;
        }
    }
    
    
    
    private Keyword getKeywordDuplicate(boolean last){
        Keyword keyword = null;
        if(!last){
            Collection<Keyword> keywords = (Collection<Keyword>)executeListResultCmd("select d from Keyword d where d.createId LIKE '%PROP%'");
            keyword = keywords.iterator().next();
        } else {
            Collection<Keyword> keywords = (Collection<Keyword>)executeListResultCmd("select d from Keyword d where d.createId NOT LIKE '%PROP%' order by d.modTime desc");
            keyword = keywords.iterator().next();
        }
        log.trace(keyword);
        return keyword;
    }
    
    private Keyword getKeyword(boolean valid){
        if(valid){
            //create valid keyword
            Keyword keyword = new Keyword("test "+random.nextInt(), VALID_INVESTIGATION_ID);
            return keyword;
        } else {
            //create invalid keyword
            Keyword keyword = new Keyword();
            return keyword;
        }
    }
    
    private void checkKeyword(Keyword keyword){
        assertNotNull("PK cannot be null", keyword.getKeywordPK());
        assertNotNull("name cannot be null", keyword.getKeywordPK().getName());
        assertNotNull("investigation id cannot be null", keyword.getKeywordPK().getInvestigationId());
        assertEquals("Create id must be "+VALID_FACILITY_USER_FOR_INVESTIGATION, keyword.getCreateId(), VALID_FACILITY_USER_FOR_INVESTIGATION);
        assertEquals("Mod id must be "+VALID_FACILITY_USER_FOR_INVESTIGATION, keyword.getModId(), VALID_FACILITY_USER_FOR_INVESTIGATION);
        assertEquals("Investigation id must be "+VALID_INVESTIGATION_ID, keyword.getInvestigation().getId(), VALID_INVESTIGATION_ID);
        
    }
    
    public static junit.framework.Test suite(){
        return new JUnit4TestAdapter(TestKeyword.class);
    }
}