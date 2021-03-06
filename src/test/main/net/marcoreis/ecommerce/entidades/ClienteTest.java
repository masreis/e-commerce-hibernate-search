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

public class ClienteTest {
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

	// @Test
	public void testBuscaPorEmailECpf() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
		Query queryEmail = qb.keyword().onField("email")
				.matching("fulano@abc.net").createQuery();
		Query queryCpf = qb.keyword().onField("cpfCnpj")
				.matching("-1807802635").createQuery();
		Query query = qb.bool().must(queryEmail).must(queryCpf)
				.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Cliente.class);
		List<Cliente> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Clientes:");
		for (Cliente c : lista) {
			System.out.println(
					c.getNome() + " - " + c.getUltimoLogin());
		}
	}

	// @Test
	public void testBuscaPorEmailOuCpf() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
		Query queryEmail = qb.keyword().onField("email")
				.matching("fulano@abc.net").createQuery();
		Query queryCpf = qb.keyword().onField("cpfCnpj")
				.matching("1962149214").createQuery();
		Query query = qb.bool().should(queryEmail)
				.should(queryCpf).createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Cliente.class);
		List<Cliente> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Clientes:");
		for (Cliente c : lista) {
			System.out.println(
					c.getNome() + " - " + c.getUltimoLogin());
		}
	}

	// @Test
	public void testBuscaPeloNomeParcial() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
		Query query = qb.keyword().wildcard().onField("nome")
				.matching("marc*").createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Cliente.class);
		List<Cliente> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Clientes:");
		for (Cliente c : lista) {
			System.out.println(c.getNome());
		}
	}

	@Test
	public void testBuscaPeloLogin() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
		String dataInicio = "20170910000000";
		String dataFim = "20170911000000";
		Query query = qb.range().onField("ultimoLogin")
				.ignoreFieldBridge().from(dataInicio).to(dataFim)
				.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Cliente.class);
		List<Cliente> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Clientes:");
		for (Cliente c : lista) {
			System.out.println(
					c.getNome() + " - " + c.getUltimoLogin());
		}
	}

}
