package net.marcoreis.ecommerce.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
	private EntityManagerFactory emf;
	private EntityManager entityManager;
	private static JPAUtil instance;

	private JPAUtil() {
		emf = Persistence
				.createEntityManagerFactory("e-commerce-pu");
	}

	private void inicializar() {
		if (entityManager == null || !entityManager.isOpen()) {
			entityManager = emf.createEntityManager();
		}
	}

	public static JPAUtil getInstance() {
		if (instance == null) {
			instance = new JPAUtil();
		}
		instance.inicializar();
		return instance;
	}

	public EntityManager getEntityManager() {
		return entityManager;
	}
}
