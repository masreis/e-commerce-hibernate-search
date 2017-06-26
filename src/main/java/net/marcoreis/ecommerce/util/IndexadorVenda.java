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
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.Term;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

import net.marcoreis.ecommerce.entidades.ItemVenda;
import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.entidades.Venda;
import net.marcoreis.ecommerce.negocio.VendaService;

public class IndexadorVenda {
	private static Logger logger =
			Logger.getLogger(IndexadorVenda.class);
	private VendaService vendaService = new VendaService();
	private Tika tika = new Tika();
	private UtilIndice utilIndice;

	public void indexarVendas()
			throws IOException, TikaException {
		List<Venda> vendas =
				vendaService.carregarColecao(Venda.class);
		logger.info(vendas.size() + " vendas para indexação");
		for (Venda venda : vendas) {
			indexarVenda(venda);
		}
		logger.info("Indexação concluída [Venda]");
	}

	private void indexarVenda(Venda venda)
			throws IOException, TikaException {
		Document doc = new Document();
		StringBuilder textoCompleto = new StringBuilder();
		//
		preencherDadosVenda(venda, doc);
		preencherDadosItemVenda(venda, doc, textoCompleto);
		preencherDadosCliente(venda, doc, textoCompleto);
		preencherDadosTextoCompleto(venda, doc, textoCompleto);
		utilIndice.atualizarDoc(
				new Term("vendaId", venda.getId().toString()),
				doc, false);
	}

	private void preencherDadosTextoCompleto(Venda venda,
			Document doc, StringBuilder textoCompleto) {
		doc.add(new TextField("textoCompleto",
				textoCompleto.toString(), Store.YES));
	}

	private void preencherDadosVenda(Venda venda, Document doc) {
		doc.add(new StringField("vendaId",
				venda.getId().toString(), Store.YES));
		doc.add(new TextField("dataAtualizacao",
				DateTools.dateToString(
						venda.getDataAtualizacao(),
						Resolution.MINUTE),
				Store.YES));
		doc.add(new TextField("data", DateTools.dateToString(
				venda.getData(), Resolution.MINUTE), Store.YES));
	}

	private void preencherDadosCliente(Venda venda, Document doc,
			StringBuilder textoCompleto) {
		doc.add(new StringField("clienteId",
				venda.getCliente().getId().toString(),
				Store.YES));
		doc.add(new TextField("clienteNome",
				venda.getCliente().getNome(), Store.YES));
		doc.add(new StringField("clienteEmail",
				venda.getCliente().getEmail(), Store.YES));
		textoCompleto.append(" ");
		textoCompleto.append(venda.getCliente().getNome());
		textoCompleto.append(" ");
		textoCompleto.append(venda.getCliente().getEmail());
	}

	private void preencherDadosItemVenda(Venda venda,
			Document doc, StringBuilder textoCompleto)
			throws IOException, TikaException {
		for (ItemVenda iv : venda.getItensVenda()) {
			doc.add(new TextField("itemQuantidade",
					iv.getQuantidade().toString(), Store.YES));
			doc.add(new TextField("itemValorUnitario",
					iv.getValorUnitario().toString(),
					Store.YES));
			doc.add(new TextField("itemValorTotal",
					iv.getValorTotal().toString(), Store.YES));
			//
			doc.add(new IntPoint("itemQuantidadePoint",
					iv.getQuantidade()));
			doc.add(new DoublePoint("itemValorUnitarioPoint",
					iv.getValorUnitario().doubleValue()));
			doc.add(new DoublePoint("itemValorTotalPoint",
					iv.getValorTotal().doubleValue()));
			//
			preencherDadosProduto(iv.getProduto(), doc,
					textoCompleto);
		}
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

	public void fechar() throws IOException {
		utilIndice.fechar();
	}

	public void inicializar() throws IOException {
		utilIndice = UtilIndice.getInstancia(TipoIndice.VENDA);
	}

	public void atualizarIndice(int tempoEmMinutos)
			throws IOException, TikaException {
		List<Venda> vendas = vendaService
				.consultarAtualizacoes(tempoEmMinutos);
		logger.info(vendas.size() + " vendas para indexação");
		for (Venda venda : vendas) {
			indexarVenda(venda);
		}
		logger.info("Indexação concluída [Venda]");
	}

}
