package net.marcoreis.ecommerce.controlador;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.marcoreis.ecommerce.entidades.Categoria;
import net.marcoreis.ecommerce.negocio.CategoriaService;

@ManagedBean
@ViewScoped
public class CategoriaBean extends BaseBean {
	private static final long serialVersionUID =
			861905629535769221L;
	private Categoria categoria;
	private CategoriaService categoriaService =
			new CategoriaService();
	private Collection<Categoria> categorias;

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public Collection<Categoria> getCategorias() {
		return categorias;
	}

	@PostConstruct
	public void init() {
		carregarCategorias();
		categoria = new Categoria();
	}

	public void salvar() {
		try {
			categoriaService.salvar(getCategoria());
			infoMsg("Dados gravados com sucesso");
		} catch (Exception e) {
			errorMsg(e);
		}
	}

	public void carregarCategorias() {
		categorias = categoriaService
				.carregarColecao(Categoria.class);
	}

	// public String editar(Categoria categoria) {
	// return "categoria";
	// }

	public void excluir(Categoria categoria) {
		try {
			categoriaService.remove(categoria);
			carregarCategorias();
			infoMsg("Categoria excluída: "
					+ categoria.getNome());
		} catch (Exception e) {
			errorMsg(e.getMessage());
		}
	}

}