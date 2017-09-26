package net.marcoreis.ecommerce.util;

import java.math.BigDecimal;

import org.apache.lucene.document.Document;
import org.hibernate.search.bridge.LuceneOptions;
import org.hibernate.search.bridge.MetadataProvidingFieldBridge;
import org.hibernate.search.bridge.TwoWayFieldBridge;
import org.hibernate.search.bridge.spi.FieldMetadataBuilder;
import org.hibernate.search.bridge.spi.FieldType;

public class BigDecimalNumericFieldBridge implements
		MetadataProvidingFieldBridge, TwoWayFieldBridge {

	@Override
	public void set(String name, Object value, Document document,
			LuceneOptions luceneOptions) {
		if (value != null) {
			BigDecimal decimalValue = (BigDecimal) value;
			Double indexedValue = decimalValue.doubleValue();
			luceneOptions.addNumericFieldToDocument(name,
					indexedValue, document);
		}
	}

	@Override
	public Object get(String name, Document document) {
		String fromLucene = document.get(name);
		BigDecimal storedBigDecimal = new BigDecimal(fromLucene);
		return storedBigDecimal;
	}

	@Override
	public String objectToString(Object object) {
		return object.toString();
	}

	@Override
	public void configureFieldMetadata(String name,
			FieldMetadataBuilder builder) {
		builder.field(name, FieldType.DOUBLE);
	}
}