package net.marcoreis.ecommerce.util;

import java.io.Closeable;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class IndexadorHS implements Closeable {
	private EntityManager em;
	private FullTextEntityManager ftem;

	public IndexadorHS() {
		em = JPAUtil.getInstance().getEntityManager();
		ftem = Search.getFullTextEntityManager(em);
	}

	public void indexar(Class<?>... classes)
			throws InterruptedException {
		em.getTransaction().begin();
		ftem.createIndexer(classes).startAndWait();
		em.getTransaction().commit();
	}

	public void close() {
		em.close();
	}
}
