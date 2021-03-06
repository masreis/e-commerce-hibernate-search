package net.marcoreis.ecommerce.entidades;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import javax.persistence.EntityManager;

import org.apache.lucene.search.NumericRangeQuery;
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
	private static Double precoMinimo;
	private static Double precoMaximo;

	@BeforeClass
	public static void inicializar() {
		em = JPAUtil.getInstance().getEntityManager();
		ftem = Search.getFullTextEntityManager(em);
		precoMinimo =
				ThreadLocalRandom.current().nextDouble(500);
		precoMaximo = precoMinimo
				+ ThreadLocalRandom.current().nextDouble(20);
	}

	@AfterClass
	public static void finalizar() {
		System.out.println("Preço mínimo/máximo: " + precoMinimo
				+ "/" + precoMaximo);
		em.close();
	}

	// @Test
	public void testBuscaPelaEspec() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query =
				qb.keyword().onField("especificacaoFabricante")
						.ignoreFieldBridge().matching("xbox")
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
	public void testBuscaPorIntervaloNRQ() {
		NumericRangeQuery<Double> query =
				NumericRangeQuery.newDoubleRange("preco",
						precoMinimo, precoMaximo, true, true);
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out
					.println(p.getNome() + " - " + p.getPreco());
		}
		System.out.println("==================");
	}

	// @Test
	public void testBuscaPorIntervaloAboveBelow() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query queryAbove = qb.range().onField("preco")
				.above(precoMinimo).createQuery();
		Query queryBelow = qb.range().onField("preco")
				.below(precoMaximo).createQuery();
		Query query = qb.bool().must(queryAbove).must(queryBelow)
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
		System.out.println("==================");
	}

	// @Test
	public void testBuscaPorIntervaloFromTo() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.range().onField("preco")
				.from(precoMinimo).to(precoMaximo).createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out
					.println(p.getNome() + " - " + p.getPreco());
		}
		System.out.println("==================");
	}

	// @Test
	public void testBuscaPelaCategoria() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.keyword().onField("categorias.nome")
				.ignoreFieldBridge().matching("periféricos")
				.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out.println(p.getNome());
		}
	}

	// @Test
	public void testBuscaOperadorAnd() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query queryNome = qb.keyword().onField("nome")
				.matching("xbox").createQuery();
		Query queryData = qb.keyword().onField("dataAtualizacao")
				.matching("2017-09-10").createQuery();
		Query query = qb.bool().must(queryNome).must(queryData)
				.createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos:");
		for (Produto p : lista) {
			System.out.println(p.getNome() + " - "
					+ p.getDataAtualizacao());
		}
	}

	// @Test
	public void testBuscaFuzzy() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.keyword().fuzzy()
				.withEditDistanceUpTo(1).onField("nome")
				.matching("xbox").createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos [" + lista.size() + "]:");
		for (Produto p : lista) {
			System.out.println(p.getNome());
		}
	}

	// @Test
	public void testBuscaPorProximidade() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.phrase().withSlop(3).onField("nome")
				.sentence("xbox war").createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos [" + lista.size() + "]:");
		for (Produto p : lista) {
			System.out.println(p.getNome());
		}
	}

	@Test
	public void testBuscaPeloNome() {
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Produto.class).get();
		Query query = qb.keyword().onField("nome")
				.matching("jogo").createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Produto.class);
		List<Produto> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Produtos [" + lista.size() + "]:");
		for (Produto p : lista) {
			System.out.println(p.getNome());
		}
	}
}
