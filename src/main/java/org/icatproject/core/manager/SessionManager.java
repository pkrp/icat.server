package org.icatproject.core.manager;

import javax.ejb.DependsOn;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.log4j.Logger;
import org.icatproject.core.entity.Session;

@DependsOn("LoggingConfigurator")
@Singleton
public class SessionManager {

	private static final Logger logger = Logger.getLogger(SessionManager.class);

	private EntityManagerFactory managerFactory = javax.persistence.Persistence.createEntityManagerFactory("icat");;
	
	private EntityManager manager = managerFactory.createEntityManager();
	
	// Run every hour
	@Schedule(hour = "*")
	public void removeExpiredSessions() {
		try {
			EntityTransaction entityTransaction = manager.getTransaction();
	        entityTransaction.begin();
			int n = manager.createNamedQuery(Session.DELETE_EXPIRED).executeUpdate();
			entityTransaction.commit();
			logger.debug(n + " sessions were removed");
		} catch (Throwable e) {
			logger.error(e.getClass() + " " + e.getMessage());
		}
	}
}