package org.iata.ndc.schema;

import java.io.InputStream;

import javax.xml.bind.*;

import org.iata.ndc.NdcClient;

abstract class AbstractUnmarshaller<T> {

	@SuppressWarnings("unchecked")
	public T unmarshal(String resource) throws JAXBException {
		InputStream inputStream = this.getClass().getResourceAsStream(resource);
		JAXBContext context = NdcClient.getJaxbContext();
		Unmarshaller unmarshaller = context.createUnmarshaller();
		return (T) unmarshaller.unmarshal(inputStream);
	}

}
