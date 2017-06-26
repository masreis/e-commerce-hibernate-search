package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.negocio.ProdutoService;

public class IndexadorProduto {
	private static Logger logger =
			Logger.getLogger(IndexadorProduto.class);
	private ProdutoService produtoService = new ProdutoService();
	private Tika tika = new Tika();
	private UtilIndice utilIndice;

	public void indexarProdutos()
			throws IOException, TikaException {
		List<Produto> produtos =
				produtoService.carregarColecao(Produto.class);
		for (Produto prod : produtos) {
		}
	}

}
