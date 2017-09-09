package net.marcoreis.ecommerce.util;

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

import net.marcoreis.ecommerce.entidades.Categoria;

public class CategoriaTest {
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
	public void testBuscaCategoriaPeloNome() {
		// QueryBuilder
		QueryBuilder qb =
				ftem.getSearchFactory().buildQueryBuilder()
						.forEntity(Categoria.class).get();
		// Query
		Query query = qb.keyword().onField("nome")
				.matching("jogos").createQuery();
		FullTextQuery ftQuery =
				ftem.createFullTextQuery(query, Categoria.class);
		// Resultado da consulta
		List<Categoria> lista = ftQuery.getResultList();
		Assert.assertTrue(lista.size() > 0);
		System.out.println("Categorias:");
		for (Categoria c : lista) {
			System.out.println(c.getNome());
		}
	}

}
