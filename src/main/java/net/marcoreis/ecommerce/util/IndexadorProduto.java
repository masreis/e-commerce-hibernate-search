package net.marcoreis.ecommerce.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.DateTools.Resolution;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.ecommerce.entidades.Categoria;
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
			indexarProduto(prod);
		}
	}

	private void indexarProduto(Produto produto)
			throws IOException, TikaException {
		Document doc = new Document();
		StringBuilder textoCompleto = new StringBuilder();
		//
		preencherDadosProduto(produto, doc, textoCompleto);
		preencherDadosCategoria(produto, doc, textoCompleto);
		preencherDadosTextoCompleto(doc, textoCompleto);
		//
		utilIndice.atualizarDoc(new Term("produtoId",
				produto.getId().toString()), doc, false);
	}

	private void preencherDadosTextoCompleto(Document doc,
			StringBuilder textoCompleto) {
		doc.add(new TextField("textoCompleto",
				textoCompleto.toString(), Store.YES));
	}

	private void preencherDadosCategoria(Produto produto,
			Document doc, StringBuilder textoCompleto) {
		for (Categoria categoria : produto.getCategorias()) {
			doc.add(new TextField("categoriaNome",
					categoria.getNome(), Store.YES));
			textoCompleto.append(" ");
			textoCompleto.append(categoria.getNome());
		}
	}

	private void preencherDadosProduto(Produto produto,
			Document doc, StringBuilder textoCompleto)
			throws IOException, TikaException {
		doc.add(new StringField("produtoId",
				produto.getId().toString(), Store.YES));
		String descricao = produto.getDescricao() == null ? ""
				: produto.getDescricao();
		doc.add(new TextField("produtoDescricao", descricao,
				Store.YES));
		doc.add(new TextField("produtoNome", produto.getNome(),
				Store.YES));
		String especFabricante = getEspecProduto(produto);
		if (!"".equals(especFabricante)) {
			doc.add(new TextField("especFabricante",
					especFabricante, Store.YES));
		}
		doc.add(new TextField("produtoPreco",
				produto.getPreco().toString(), Store.YES));
		doc.add(new DoublePoint("produtoPrecoPoint",
				produto.getPreco().doubleValue()));
		doc.add(new TextField("dataAtualizacao",
				DateTools.dateToString(
						produto.getDataAtualizacao(),
						Resolution.MINUTE),
				Store.YES));
		//
		textoCompleto.append(" ");
		textoCompleto.append(produto.getNome());
		if (!"".equals(especFabricante)) {
			textoCompleto.append(" ");
			textoCompleto.append(especFabricante);
		}
		textoCompleto.append(" ");
		textoCompleto.append(produto.getDescricao());
	}

	public String getEspecProduto(Produto produto)
			throws IOException, TikaException {
		if (produto.getEspecificacaoFabricante() != null) {
			ByteArrayInputStream bytes =
					new ByteArrayInputStream(produto
							.getEspecificacaoFabricante());
			return tika.parseToString(bytes);
		}
		return "";
	}

	/**
	 * Indexa os produtos atualizados nos últimos minutos
	 * 
	 * @param tempoEmMinutos
	 * @throws IOException
	 * @throws TikaException
	 */
	public void atualizarIndice(int tempoEmMinutos)
			throws IOException, TikaException {
		List<Produto> produtos = produtoService
				.consultarAtualizacoes(tempoEmMinutos);
		logger.info(produtos.size() + " produtos alterados");
		for (Produto produto : produtos) {
			if (produto.isAtivo()) {
				indexarProduto(produto);
			} else {
				removerProdutoIndice(produto);
			}
		}
		logger.info("Indexação concluída [Produto]");
	}

	private void removerProdutoIndice(Produto produto)
			throws IOException {
		utilIndice.removerDoc("produtoId",
				produto.getId().toString());
	}

	public void fechar() throws IOException {
		utilIndice.fechar();
	}

	public void inicializar() throws IOException {
		utilIndice = UtilIndice.getInstancia(TipoIndice.PRODUTO);
	}

}
