package net.marcoreis.ecommerce.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/**
 * Indexa os últimos registros atualizados na base de dados
 * 
 * @author Marco Reis (http://marcoreis.net)
 *
 */
public class TemporizadorIndiceVenda implements Runnable {
	private static Logger logger =
			Logger.getLogger(TemporizadorIndiceVenda.class);

	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);

	public void iniciar() {
		scheduler.scheduleAtFixedRate(this, 1, 1,
				TimeUnit.MINUTES);
	}

	public void run() {
		IndexadorVenda indexador = new IndexadorVenda();
		try {
			logger.info("Iniciando temporizador "
					+ Thread.currentThread().getName());
			indexador.inicializar();
			int um_minuto = 1;
			indexador.atualizarIndice(um_minuto);
			indexador.fechar();
			logger.info("Finalizando temporizador "
					+ Thread.currentThread().getName());
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
