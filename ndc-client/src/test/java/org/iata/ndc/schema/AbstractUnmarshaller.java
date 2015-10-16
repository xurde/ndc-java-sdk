package org.iata.ndc.schema;

import java.io.InputStream;

import javax.xml.bind.*;

abstract class AbstractUnmarshaller<T> {
	private static final String SCHEMA_PACKAGE = "org.iata.ndc.schema";

	@SuppressWarnings("unchecked")
	public T unmarshal(String resource) throws JAXBException {
		InputStream inputStream = this.getClass().getResourceAsStream(resource);
		JAXBContext context = JAXBContext.newInstance(SCHEMA_PACKAGE);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(inputStream);
	}

}
