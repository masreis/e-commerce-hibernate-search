package net.marcoreis.ecommerce.controlador;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import net.marcoreis.ecommerce.entidades.Produto;
import net.marcoreis.ecommerce.negocio.GenericService;

@ManagedBean
@RequestScoped
public class ImagemProdutoBean extends BaseBean {
	private static final long serialVersionUID =
			-7524476303834771432L;
	private Produto produto;

	@PostConstruct
	public void init() {
		produto = new Produto();
	}

	public StreamedContent getConteudoImagem() {
		try {
			FacesContext context =
					FacesContext.getCurrentInstance();
			if (context
					.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
				return new DefaultStreamedContent();
			}
			String idS = getParametro("id");
			Long id = Long.parseLong(idS);
			produto = (Produto) new GenericService()
					.findById(Produto.class, id);
			// InputStream is = new ByteArrayInputStream(produto.getFoto());
			// DefaultStreamedContent dsc = new DefaultStreamedContent(is);
			return null;
		} catch (Exception e) {
			return new DefaultStreamedContent();
		}
	}

}
