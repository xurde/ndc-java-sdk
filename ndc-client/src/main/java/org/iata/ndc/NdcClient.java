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

/**
 * JAXB-based implementation of IATA's NDC standard.
 *
 * This implementation can handle a subset of all NDC calls.
 */
public class NdcClient {
	private static final Logger LOG = LoggerFactory.getLogger(NdcClient.class);
	/** NDC Client version */
	private static final String VERSION = "0.1.0";
	/** User agent passed to server */
	private static final String USER_AGENT = "NDC Java Wrapper / " + VERSION;
	/** JaxbContext for NDC schema*/
	private static final JAXBContext context = null;
	/** Server URL */
	private final String url;
	/** Authorization key */
	private final String key;
	/** ContentType for the request */
	private final ContentType contentType;

	/**
	 * Creates client for querying a specific NDC service endpoint
	 * @param serviceUrl url for NDC REST XML service
	 * @param authorizationKey authorizationKey for service
	 */
	public NdcClient(String serviceUrl, String authorizationKey) {
		this.url = serviceUrl;
		this.key = authorizationKey;
		this.contentType = ContentType.create(ContentType.APPLICATION_XML.getMimeType(), Consts.UTF_8);
	}

	/**
	 * Sends AirShopping request to server, receives and processes the response.
	 *
	 * Request Object is marshalled into
	 *
	 * @param airShoppingRQ request Object
	 * @return AirShoppingRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public AirShoppingRS airShopping(AirShoppingRQ airShoppingRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(airShoppingRQ, "AirShopping");
	}

	/**
	 * Sends FlightPrice request to server, receives and processes the response.
	 *
	 * @param flightPriceRQ request Object
	 * @return FlightPriceRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public FlightPriceRS flightPrice(FlightPriceRQ flightPriceRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(flightPriceRQ, "FlightPrice");
	}

	/**
	 * Sends SeatAvailability request to server, receives and processes the response.
	 *
	 * @param seatAvailabilityRQ request Object
	 * @return SeatAvailabilityRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public SeatAvailabilityRS seatAvailability(SeatAvailabilityRQ seatAvailabilityRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(seatAvailabilityRQ, "SeatAvailability");
	}

	/**
	 * Sends ServiceList request to server, receives and processes the response.
	 *
	 * @param serviceListRQ request Object
	 * @return ServiceListRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public ServiceListRS serviceList(ServiceListRQ serviceListRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(serviceListRQ, "ServiceList");
	}

	/**
	 * Sends ServicePrice request to server, receives and processes the response.
	 *
	 * @param servicePriceRQ request Object
	 * @return ServicePriceRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public ServicePriceRS servicePrice(ServicePriceRQ servicePriceRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(servicePriceRQ, "ServicePrice");
	}

	/**
	 * Sends OrderCreate request to server, receives and processes the response.
	 *
	 * @param orderCreateRQ request Object
	 * @return OrderViewRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public OrderViewRS orderCreate(OrderCreateRQ orderCreateRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(orderCreateRQ, "OrderCreate");
	}

	/**
	 * Sends OrderList request to server, receives and processes the response.
	 *
	 * @param orderListRQ request Object
	 * @return OrderListRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if unmarshalling server response to Java Object fails
	 */
	public OrderListRS orderList(OrderListRQ orderListRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(orderListRQ, "OrderList");
	}

	/**
	 * Sends OrderRetrieve request to server, receives and processes the response.
	 *
	 * @param orderRetrieveRQ request Object
	 * @return OrderViewRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public OrderViewRS orderRetrieve(OrderRetrieveRQ orderRetrieveRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(orderRetrieveRQ, "OrderRetrieve");
	}

	/**
	 * Sends OrderChange request to server, receives and processes the response.
	 *
	 * @param orderChangeRQ request Object
	 * @return OrderViewRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public OrderViewRS orderChange(OrderChangeRQ orderChangeRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(orderChangeRQ, "OrderChange");
	}

	/**
	 * Sends OrderCancel request to server, receives and processes the response.
	 *
	 * @param orderCancelRQ request Object
	 * @return OrderCancelRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public OrderCancelRS orderCancel(OrderCancelRQ orderCancelRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(orderCancelRQ, "OrderCancel");
	}

	/**
	 * Sends ItinReshop request to server, receives and processes the response.
	 *
	 * @param itinReshopRQ request Object
	 * @return ItinReshopRS instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	public ItinReshopRS itinReshop(ItinReshopRQ itinReshopRQ) throws ClientProtocolException, IOException, ClientException {
		return marshalAndSend(itinReshopRQ, "ItinReshop");
	}

	/**
	 * Internal method which is used by all request methods
	 *
	 * @param request request Object
	 * @param method request method name
	 * @return Object instance containing the response from server
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if marshalling the request or unmarshalling server response to Java Object fails
	 */
	private <S, T> S marshalAndSend(T request, String method) throws ClientProtocolException, IOException, ClientException {
		return sendRequest(marshallRequest(request), method);
	}

	/**
	 * Marshals request object into String
	 *
	 * @param request request object
	 * @return marshalled request
	 * @throws ClientException if marshalling the request to XML fails
	 */
	private <T> String marshallRequest(T request) throws ClientException {
		try {
			Marshaller marshaller = getJaxbContext().createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);
			return writer.toString();
		} catch (JAXBException e) {
			throw new ClientException(e);
		}
	}

	/**
	 * Sends request and returns marshalled response Object.
	 *
	 * @param request String containing XML request
	 * @param method request method name
	 * @return Response object returned by sender
	 * @throws ClientProtocolException if HTTP protocol exception is encountered during service call
	 * @throws IOException if error is encountered while processing server response
	 * @throws ClientException if unmarshalling server response to Java Object fails
	 */
	private <T> T sendRequest(String request, String method) throws ClientProtocolException, IOException, ClientException {
		LOG.debug("{} request:\n{}", method, request);
		return Request
				.Post(url)
				.userAgent(USER_AGENT)
				.addHeader("Authorization-key", key)
				.addHeader("X-NDC-Method", method)
				.addHeader("Accept", ContentType.APPLICATION_XML.getMimeType())
				.bodyString(request, contentType)
				.execute()
				.handleResponse(new UnmarshallingResponseHandler<T>());
	}

	/**
	 * Returns JAXBContext for IATA NDC Schema.
	 * Creates the context if it does not exist.
	 *
	 * @return JAXBContext instance representing NDC schema
	 * @throws ClientException if context creation failed.
	 */
	public static JAXBContext getJaxbContext() throws ClientException {
		if (context != null) return context;
		try {
			return JAXBContext.newInstance("org.iata.ndc.schema");
		} catch (JAXBException e) {
			LOG.error("Failure creating JAXB context", e);
			throw new ClientException(e);
		}
	}
}
