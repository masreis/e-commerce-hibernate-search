package net.marcoreis.ecommerce.util;

import java.math.BigDecimal;
import org.hibernate.search.bridge.builtin.NumericFieldBridge;
import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;

public class BigDecimalNumericFieldBridge {
	// extends NumericFieldBridge {
	private static final BigDecimal storeFactor =
			BigDecimal.valueOf(100);

	// @Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		if (value != null) {
			BigDecimal decimalValue = (BigDecimal) value;
			long tmpLong = decimalValue.multiply(storeFactor)
					.longValue();
			Long indexedValue = Long.valueOf(tmpLong);
			luceneOptions.addNumericFieldToDocument(name,
					indexedValue, document);
		}
	}

	// @Override
	public Object get(String name, Document document) {
		String fromLucene = document.get(name);
		BigDecimal storedBigDecimal = new BigDecimal(fromLucene);
		return storedBigDecimal.divide(storeFactor);
	}
}