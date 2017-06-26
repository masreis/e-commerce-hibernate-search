package net.marcoreis.ecommerce.controlador;

import java.math.BigDecimal;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import net.marcoreis.ecommerce.util.LuceneLazyDataModel;
import net.marcoreis.ecommerce.util.TipoIndice;

@ManagedBean
@ViewScoped
public class BuscaLivreProdutoBean extends BaseBean {
	private static final long serialVersionUID =
			-7508553590263034662L;
	private String consulta;
	private LuceneLazyDataModel docs;
	private TipoIndice tipo = TipoIndice.PRODUTO;

	public LuceneLazyDataModel getDocs() {
		if (docs == null) {
			docs = new LuceneLazyDataModel(getConsulta(),
					getTipo());
		}
		return docs;
	}

	public void setDocs(LuceneLazyDataModel docs) {
		this.docs = docs;
	}

	public void consultar() {
		docs = new LuceneLazyDataModel(getConsulta(), getTipo());
	}

	public void setConsulta(String consulta) {
		this.consulta = consulta;
	}

	public String getConsulta() {
		return consulta;
	}

	public BigDecimal getDuracaoBusca() {
		Double d = getDocs().getDuracaoBusca() / 1000d;
		BigDecimal bd = new BigDecimal(d).setScale(4,
				BigDecimal.ROUND_CEILING);
		return bd;
	}

	public String getDescricaoFormatada() {
		return getDocs().getRowData().get("produtoDescricao")
				.replaceAll("\n", "<br />");
	}

	public String abrirPaginaBusca() {
		return "consultar";
	}

	public TipoIndice getTipo() {
		return tipo;
	}
}
