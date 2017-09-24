package net.marcoreis.ecommerce.entidades;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
		precoMinimo = new BigDecimal(
				ThreadLocalRandom.current().nextDouble(500))
						.setScale(2, RoundingMode.HALF_UP)
						.doubleValue();
		precoMaximo = precoMinimo + new BigDecimal(
				ThreadLocalRandom.current().nextDouble(20))
						.setScale(2, RoundingMode.HALF_UP)
						.doubleValue();
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

	@Test
	public void testBuscaPorIntervaloPoint() {
		NumericRangeQuery<Double> query =
				NumericRangeQuery.newDoubleRange("precoPoint",
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

	@Test
	public void testBuscaPorIntervalo() {
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
			System.out
					.println(p.getNome() + " - " + p.getPreco());
		}
	}
}
