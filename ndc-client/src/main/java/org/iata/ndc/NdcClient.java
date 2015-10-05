package org.iata.ndc;


import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.*;

import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.iata.iata.edist.AirShoppingRQ;
import org.iata.iata.edist.AirShoppingRS;

public class NdcClient {
	private final String uri;
	private final ContentType contentType;

	public NdcClient(String serviceURI) {
		this.uri = serviceURI;
		this.contentType = ContentType.create(ContentType.APPLICATION_XML.getMimeType(), Consts.UTF_8);
	}

	public AirShoppingRS airShopping(AirShoppingRQ airShoppingRQ) throws ClientProtocolException, IOException {
		//TODO: replace with streams
		String request = marshallRequest(AirShoppingRQ.class, airShoppingRQ);
		return sendRequest(request);
	}

	private <T> String marshallRequest(Class<T> clazz, T request) {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller marshaller = context.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new ClientException(e);
		}
	}

	private <T> T sendRequest(String request) throws ClientProtocolException, IOException {
		return Request.Post(uri).bodyString(request, contentType).execute().handleResponse(new UnmarshallingResponseHandler<T>());
	}
}
