package net.marcoreis.ecommerce.util;

public enum TipoIndice {
	PRODUTO(System.getProperty("user.home")
			+ "/livro-lucene/indice-produto"), VENDA(
					System.getProperty("user.home")
							+ "/livro-lucene/indice-venda");
	private String diretorio;

	TipoIndice(String diretorio) {
		this.diretorio = diretorio;
	}

	public String diretorio() {
		return diretorio;
	}
}
