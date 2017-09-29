package net.marcoreis.ecommerce.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.apache.log4j.Logger;

public class JPAUtil {
	private static EntityManagerFactory emf = null;
	private static JPAUtil instance;
<<<<<<< HEAD
	private static Logger logger = Logger
			.getLogger(JPAUtil.class);
=======
	private static Logger logger =
			Logger.getLogger(JPAUtil.class);
>>>>>>> 6eb07e45198f33a0f11dcdd98a755324151e7d53

	static {
		instance = new JPAUtil();
		emf = Persistence
				.createEntityManagerFactory("e-commerce-pu");
	}

	public static JPAUtil getInstance() {
		return instance;
	}

	public EntityManager getEntityManager() {
		return emf.createEntityManager();
	}
}
