package org.iata.ndc;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.bind.*;

import org.apache.http.Consts;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.iata.ndc.schema.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdcClient {
	private static final Logger LOG = LoggerFactory.getLogger(NdcClient.class);
	private static final String VERSION = "0.1.0";
	private static final String USER_AGENT = "NDC Java Wrapper / " + VERSION;
	private final String uri;
	private final String key;
	private final ContentType contentType;

	public NdcClient(String serviceURI, String authorizationKey) {
		this.uri = serviceURI;
		this.key = authorizationKey;
		this.contentType = ContentType.create(ContentType.APPLICATION_XML.getMimeType(), Consts.UTF_8);
	}

	public AirShoppingRS airShopping(AirShoppingRQ airShoppingRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(airShoppingRQ);
		return sendRequest(request, "AirShopping");
	}

	public FlightPriceRS flightPrice(FlightPriceRQ flightPriceRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(flightPriceRQ);
		return sendRequest(request, "FlightPrice");
	}

	public SeatAvailabilityRS seatAvailability(SeatAvailabilityRQ seatAvailabilityRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(seatAvailabilityRQ);
		return sendRequest(request, "SeatAvailability");
	}

	public ServiceListRS serviceList(ServiceListRQ serviceListRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(serviceListRQ);
		return sendRequest(request, "ServiceList");
	}

	public ServicePriceRS servicePrice(ServicePriceRQ serviceListRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(serviceListRQ);
		return sendRequest(request, "ServicePrice");
	}

	public OrderViewRS orderCreate(OrderCreateRQ orderCreateRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(orderCreateRQ);
		return sendRequest(request, "OrderCreate");
	}

	public OrderListRS orderList(OrderListRQ orderListRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(orderListRQ);
		return sendRequest(request, "OrderList");
	}

	public OrderViewRS orderRetrieve(OrderRetrieveRQ orderRetrieveRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(orderRetrieveRQ);
		return sendRequest(request, "OrderRetrieve");
	}

	public OrderCancelRS orderCancel(OrderCancelRQ orderCancelRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(orderCancelRQ);
		return sendRequest(request, "OrderCancel");
	}

	public ItinReshopRS itinReshop(ItinReshopRQ itinReshopRQ) throws ClientProtocolException, IOException {
		String request = marshallRequest(itinReshopRQ);
		return sendRequest(request, "ItinReshop");
	}

	private <T> String marshallRequest(T request) {
		try {
			JAXBContext context = JAXBContext.newInstance("org.iata.ndc.schema");
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
				.addHeader("Authorization-key", key)
				.addHeader("X-NDC-Method", method)
				.addHeader("Accept", ContentType.APPLICATION_XML.getMimeType())
				.bodyString(request, contentType)
				.execute()
				.handleResponse(new UnmarshallingResponseHandler<T>());
	}
}
