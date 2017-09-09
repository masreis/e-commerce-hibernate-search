package net.marcoreis.ecommerce.util;

import java.text.ParseException;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.time.DateUtils;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.FullTextQuery;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import net.marcoreis.ecommerce.entidades.Cliente;

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
	public void testBuscaClientePeloNome() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
		Query query = qb.phrase().onField("nome")
				.sentence("marco reis").createQuery();
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
	public void testBuscaClientePelaDataLogin()
			throws ParseException {
		String dataInicio = "20170909150000";
		String dataFim = "2017090915250000";
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Cliente.class).get();
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

	public void test() {
		// SearchFactory searchFactory = fullTextSession.getSearchFactory();
		// org.apache.lucene.queryParser.QueryParser parser =
		// new QueryParser("title", searchFactory.getAnalyzer(Myth.class) );
		// try {
		// org.apache.lucene.search.Query luceneQuery = parser.parse(
		// "history:storm^3" );
		// }
		// catch (ParseException e) {
		// //handle parsing failure
		// }
		//
		// org.hibernate.Query fullTextQuery =
		// fullTextSession.createFullTextQuery(luceneQuery);
		// List result = fullTextQuery.list(); //return a list of managed
		// objects
	}
}
