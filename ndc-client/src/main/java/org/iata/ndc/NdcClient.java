package org.iata.ndc;


import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.*;

import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.iata.iata.edist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdcClient {
	private static final Logger LOG = LoggerFactory.getLogger(NdcClient.class);
	private static final String VERSION = "0.1.0";
	private static final String USER_AGENT = "NDC Java Wrapper / " + VERSION;
	private final String uri;
	private final ContentType contentType;

	public NdcClient(String serviceURI) {
		this.uri = serviceURI;
		this.contentType = ContentType.create(ContentType.APPLICATION_XML.getMimeType(), Consts.UTF_8);
	}

	public AirShoppingRS airShopping(AirShoppingRQ airShoppingRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(AirShoppingRQ.class, airShoppingRQ);
		return sendRequest(request, "AirShopping");
	}

	public OrderViewRS createOrder(OrderCreateRQ orderCreateRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(OrderCreateRQ.class, orderCreateRQ);
		return sendRequest(request, "OrderCreate");
	}

	private <T> String marshallRequest(Class<T> clazz, T request) {
		try {
			JAXBContext context = JAXBContext.newInstance(clazz);
			Marshaller marshaller = context.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new ClientException(e);
		}
	}

	private <T> T sendRequest(String request, String method) throws ClientProtocolException, IOException {
		LOG.debug("{} request:\n{}", method, request);
		return Request
				.Post(uri)
				.userAgent(USER_AGENT)
				.addHeader("X-NDC-Method", method)
				.addHeader("Accept", ContentType.APPLICATION_XML.getMimeType())
				.bodyString(request, contentType)
				.execute()
				.handleResponse(new UnmarshallingResponseHandler<T>());
	}
}
