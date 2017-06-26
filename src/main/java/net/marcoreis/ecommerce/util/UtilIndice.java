package net.marcoreis.ecommerce.util;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.br.BrazilianAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.SearcherManager;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.vectorhighlight.FastVectorHighlighter;
import org.apache.lucene.search.vectorhighlight.FieldQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * Utilitário para facilitar as operações de consulta e gravação no índice do
 * Lucene.
 * 
 * Permite a reutilização do IndexSearcher e IndexReader através do
 * SearcherManager.
 * 
 * @author Marco Reis (http://marcoreis.net)
 * 
 */

public class UtilIndice {
	private static Logger logger =
			Logger.getLogger(UtilIndice.class);
	private IndexWriter writer;
	private Integer quantidadeLimiteRegistros = 1000;
	private Analyzer analyzer = new BrazilianAnalyzer();
	private SearcherManager sm;
	private TipoIndice tipo;

	private static final Map<TipoIndice, UtilIndice> instancias =
			new HashMap<TipoIndice, UtilIndice>();

	public static synchronized UtilIndice getInstancia(
			TipoIndice tipo) throws IOException {
		UtilIndice utilIndice = instancias.get(tipo);
		if (utilIndice == null) {
			utilIndice = new UtilIndice(tipo);
			instancias.put(tipo, utilIndice);
		}
		return utilIndice;
	}

	private UtilIndice(TipoIndice tipo) throws IOException {
		this.tipo = tipo;
		abrirIndice(tipo.diretorio());
		inicializaSm();
	}

	private void abrirIndice(String diretorioIndice)
			throws IOException {
		Directory diretorio =
				FSDirectory.open(Paths.get(diretorioIndice));
		//
		analyzer = new BrazilianAnalyzer();
		IndexWriterConfig conf = new IndexWriterConfig(analyzer);
		writer = new IndexWriter(diretorio, conf);
		logger.info("IndexWriter aberto");
	}

	private void inicializaSm() throws IOException {
		sm = new SearcherManager(writer, null);
		logger.info("SearcherManager aberto");
	}

	public void atualizarDoc(Term termo, Document doc)
			throws IOException {
		atualizarDoc(termo, doc, true);
	}

	public void atualizarDoc(Term termo, Document doc,
			boolean commit) throws IOException {
		verificarSeAberto();
		writer.updateDocument(termo, doc);
		if (commit) {
			commit();
		}
	}

	public void adicionarDoc(Document doc, boolean commit)
			throws IOException {
		verificarSeAberto();
		writer.addDocument(doc);
		if (commit) {
			commit();
		}
	}

	public void adicionarDoc(Document doc) throws IOException {
		adicionarDoc(doc, true);
	}

	public void removerDoc(String campo, String id)
			throws IOException {
		removerDoc(campo, id, true);
	}

	public void removerDoc(String campo, String id,
			boolean commit) throws IOException {
		verificarSeAberto();
		Term termo = new Term(campo, id);
		writer.deleteDocuments(termo);
		if (commit) {
			commit();
		}
	}

	public void commit() throws IOException {
		writer.commit();
	}

	public Document doc(int docID) throws IOException {
		verificarSeAberto();
		sm.maybeRefresh();
		IndexSearcher searcher = sm.acquire();
		try {
			Document doc = searcher.doc(docID);
			return doc;
		} finally {
			sm.release(searcher);
		}
	}

	private void verificarSeAberto() throws IOException {
		if (sm == null) {
			abrirIndice(tipo.diretorio());
			inicializaSm();
			logger.info("Índice reaberto");
		}
	}

	public int getNumDocs() throws IOException {
		verificarSeAberto();
		IndexSearcher searcher = sm.acquire();
		try {
			sm.maybeRefresh();
			return searcher.getIndexReader().numDocs();
		} finally {
			sm.release(searcher);
		}
	}

	public void fechar() throws IOException {
		synchronized (this) {
			if (sm != null) {
				sm.close();
				sm = null;
				logger.info("SearcherManager fechado");
			}
			if (writer != null) {
				writer.close();
				writer = null;
				logger.info("IndexWriter fechado");
			}
		}
	}

	public TopDocs buscar(String consulta)
			throws IOException, ParseException {
		verificarSeAberto();
		sm.maybeRefresh();
		// ou sm.maybeRefreshBlocking();
		IndexSearcher searcher = sm.acquire();
		try {
			QueryParser queryParser =
					new QueryParser("", analyzer);
			queryParser.setDefaultOperator(Operator.AND);
			Query query = queryParser.parse(consulta);
			TopDocs hits = searcher.search(query,
					quantidadeLimiteRegistros);
			return hits;
		} finally {
			sm.release(searcher);
		}
	}

	/**
	 * 
	 * @experimental
	 * 
	 */
//	public Document highlight(Document doc, int docID,
//			Query query) throws IOException {
//		// Criar campo com highlight
//		IndexSearcher searcher = sm.acquire();
//		try {
//			FastVectorHighlighter fhl =
//					new FastVectorHighlighter();
//			FieldQuery fq = fhl.getFieldQuery(query);
//			String fragHighlight = fhl.getBestFragment(fq,
//					searcher.getIndexReader(), docID,
//					"textoCompleto",
//					doc.get("textoCompleto").length());
//			if (fragHighlight == null)
//				fragHighlight = doc.get("textoCompleto");
//			doc.add(new StringField("textoCompleto.hl",
//					fragHighlight, Store.NO));
//			return doc;
//		} finally {
//			sm.release(searcher);
//		}
//	}

	// private String removeAcentos(String texto) {
	// String textoSemAcento = Normalizer
	// .normalize(texto, Normalizer.Form.NFD)
	// .replaceAll("\\p{InCombiningDiacriticalMarks}+",
	// "");
	// return textoSemAcento;
	// }

	// TODO Verificar se usa ou nao esse bloco
	// Péssima ideia para índices muito grandes
	// MatchAllDocsQuery m = new MatchAllDocsQuery();
	// hits = searcher.search(m, 1);
	// quantidadeLimiteRegistros = hits.totalHits;
	//

}
