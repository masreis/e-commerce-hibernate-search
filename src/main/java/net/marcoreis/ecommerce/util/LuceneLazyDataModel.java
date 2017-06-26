package net.marcoreis.ecommerce.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.lucene.document.Document;
import org.apache.lucene.search.TopDocs;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

public class LuceneLazyDataModel
		extends LazyDataModel<Document> {
	private static final long serialVersionUID =
			1153244287993412470L;
	private String consulta;
	private long duracaoBusca;
	private TipoIndice tipo;

	public LuceneLazyDataModel(String consulta,
			TipoIndice tipo) {
		this.consulta = consulta;
		this.tipo = tipo;
	}

	@Override
	/**
	 * Recupera a lista de documentos a partir de uma nova consulta. Utiliza os
	 * parâmetros first e pageSize para filtrar os itens da página
	 * 
	 * @param first
	 *            O número do primeiro item que deve ser carregado
	 * @param pageSize
	 *            Quantidade de itens de uma página
	 * @param sortField
	 *            Campo usado para ordenação
	 * @param sortOrder
	 *            Ordem em que os itens devem ser mostrados
	 * @param filters
	 *            Filtros que serão aplicados
	 */
	public List<Document> load(int first, int pageSize,
			String sortField, SortOrder sortOrder,
			Map<String, Object> filters) {
		long inicio = System.currentTimeMillis();
		// Nova consulta cada vez que o usuário muda de página
		try {
			UtilIndice utilIndice =
					UtilIndice.getInstancia(tipo);
			String consultaTextoCompleto =
					"textoCompleto:(" + consulta + ")";
			TopDocs hits =
					utilIndice.buscar(consultaTextoCompleto);
			List<Document> lista = new ArrayList<Document>();
			// Carrega apenas os itens daquela página
			for (int i = first; i < first + pageSize; i++) {
				if (i >= hits.totalHits) {
					break;
				}
				int idDoc = hits.scoreDocs[i].doc;
				Document documento = utilIndice.doc(idDoc);
				lista.add(documento);
			}
			// Informa a quantidade total de itens da consulta
			setRowCount(hits.totalHits);
			duracaoBusca = System.currentTimeMillis() - inicio;
			return lista;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// if (hits.totalHits > quantidadeLimite) {
	// logger.warn(
	// "Muitos itens para mostrar no resultado -> "
	// + consulta);
	// setRowCount(quantidadeLimite);
	// } else {
	// setRowCount(hits.totalHits);
	// }

	public long getDuracaoBusca() {
		return duracaoBusca;
	}

}
