package net.marcoreis.ecommerce.util;

import javax.persistence.EntityManager;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;

public class IndexadorHS {

	public void indexarTudo() throws InterruptedException {
		EntityManager em =
				JPAUtil.getInstance().getEntityManager();
		FullTextEntityManager fullTextEntityManager =
				Search.getFullTextEntityManager(em);
		fullTextEntityManager.createIndexer().startAndWait();
	}
}
