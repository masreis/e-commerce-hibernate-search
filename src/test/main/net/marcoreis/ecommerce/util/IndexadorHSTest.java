package net.marcoreis.ecommerce.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import net.marcoreis.ecommerce.entidades.Categoria;
import net.marcoreis.ecommerce.entidades.Cliente;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.entidades.Venda;

public class IndexadorHSTest {
	private static IndexadorHS indexador;

	@BeforeClass
	public static void inicializar() {
		indexador = new IndexadorHS();
	}

	@AfterClass
	public static void finalizar() {
		indexador.close();
	}

	@Test
	public void testIndexarCategoria()
			throws InterruptedException {
		indexador.indexar(Categoria.class);
	}

	@Test
	public void testIndexarCliente()
			throws InterruptedException {
		indexador.indexar(Cliente.class);
	}

	@Test
	public void testIndexarProduto()
			throws InterruptedException {
		indexador.indexar(Produto.class);
	}

	@Test
	public void testIndexarVenda() throws InterruptedException {
		indexador.indexar(Venda.class);
	}

}
