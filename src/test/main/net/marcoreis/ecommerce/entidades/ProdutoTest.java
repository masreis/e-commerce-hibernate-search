package net.marcoreis.ecommerce.entidades;

import java.util.List;

import javax.persistence.EntityManager;

import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.marcoreis.ecommerce.util.JPAUtil;

public class ProdutoTest {
	private static EntityManager em;
	private static FullTextEntityManager ftem;

	@BeforeClass
	public static void inicializar() {
		em = JPAUtil.getInstance().getEntityManager();
		ftem = Search.getFullTextEntityManager(em);
	}

	@AfterClass
	public static void finalizar() {
		em.close();
	}

	@Test
	public void testBuscaPelaEspec() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query =
				qb.keyword().onField("especificacaoFabricante")
						.ignoreFieldBridge().matching("sentença")
						.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out
					.println(p.getNome() + " - " + p.getPreco());
		}
	}

	// @Test
	public void testBuscaPeloNome() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.range().onField("preco").above(100)
				.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out
					.println(p.getNome() + " - " + p.getPreco());
		}
	}

}
