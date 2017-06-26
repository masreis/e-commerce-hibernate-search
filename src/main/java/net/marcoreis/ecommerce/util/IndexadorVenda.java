package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

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
		}
		logger.info("Indexação concluída [Venda]");
	}
}
