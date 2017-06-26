package net.marcoreis.ecommerce.controlador;

import java.io.ByteArrayInputStream;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;
import javax.faces.view.ViewScoped;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.negocio.ProdutoService;

@ViewScoped
@ManagedBean
public class ProdutoBean extends BaseBean {
	private static final long serialVersionUID = -6475971812078805662L;
	private static Logger logger = Logger
			.getLogger(ProdutoBean.class);
	private Produto produto;
	private ProdutoService produtoService = new ProdutoService();
	private Collection<Produto> produtos;
	private UploadedFile especificacaoFabricante;
	private UploadedFile foto;

	public void preRender() {
		logger.info("=> preRender()");
	}

	@PostConstruct
	public void init() {
		logger.info("=> init()");
		produto = new Produto();
		// produto.setCategoria(new Categoria());
		carregarProdutos();
	}

	public void carregarProdutos() {
		produtos = produtoService.carregarColecao(Produto.class);
	}

	public void setProduto(Produto produto) {
		logger.info("=> setProduto - " + produto);
		this.produto = produto;
	}

	public Produto getProduto() {
		return produto;
	}

	public Collection<Produto> getProdutos() {
		return produtos;
	}

	public void salvar() {
		try {
			//
			if (getEspecificacaoFabricante() != null
					&& getEspecificacaoFabricante()
							.getSize() > 0) {
				byte[] dados = IOUtils
						.toByteArray(getEspecificacaoFabricante()
								.getInputstream());
				getProduto().setEspecificacaoFabricante(dados);
			}
			//
			if (getFoto() != null && getFoto().getSize() > 0) {
				byte[] dados = IOUtils
						.toByteArray(getFoto().getInputstream());
				// getProduto().setFoto(dados);
			}
			//
			produtoService.salvar(getProduto());
			infoMsg(MENSAGEM_SUCESSO_GRAVACAO);
		} catch (Exception e) {
			errorMsg(e);
		}
	}

	public void setEspecificacaoFabricante(
			UploadedFile especificacaoFabricante) {
		this.especificacaoFabricante = especificacaoFabricante;
	}

	public UploadedFile getEspecificacaoFabricante() {
		return especificacaoFabricante;
	}

	public String editar(Produto produto) {
		return "produto?faces-redirect=true";
	}

	public void excluir(Produto produto) {
		try {
			produtoService.remove(produto);
			carregarProdutos();
			infoMsg("Produto excluído: " + produto.getNome());
		} catch (Exception e) {
			errorMsg(e.getLocalizedMessage());
		}
	}

	public void carregarProdutosPorCategoria(
			ValueChangeEvent evento) {
		String filtro = "categoria.id = ?1";
		String newValue = evento.getNewValue().toString();
		Long idCategoria = Long.parseLong(newValue);
		produtos = produtoService.carregarColecao(Produto.class,
				filtro, idCategoria);
	}

	public void setFoto(UploadedFile foto) {
		this.foto = foto;
	}

	public UploadedFile getFoto() {
		return foto;
	}

	public StreamedContent getDownloadEspecificacaoFabricante() {
		return new DefaultStreamedContent(
				new ByteArrayInputStream(getProduto()
						.getEspecificacaoFabricante()),
				"application/pdf", "arquivo.pdf");
	}

	public boolean isExisteFoto() {
		try {
			return false;// getProduto().getFoto().length > 0;
		} catch (Exception e) {
			return false;
		}
	}
}
