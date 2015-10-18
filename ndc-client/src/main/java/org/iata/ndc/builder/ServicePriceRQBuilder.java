package org.iata.ndc.builder;

import org.iata.ndc.schema.*;
import org.iata.ndc.schema.ShoppingResponseIDType.ResponseID;

public class ServicePriceRQBuilder {
	private static final ObjectFactory factory = new ObjectFactory();

	private ServicePriceRQ request;

	/**
	 * Creates a new instance of ServicePriceRQBuilder.
	 * A new instance can be created for each request or you can use the {@link #clear() clear()} method.<br>
	 */
	public ServicePriceRQBuilder() {
		clear();
	}

	/**
	 * Re-initializes builder to empty state.
	 */
	public void clear() {
		request = Initializer.getObject(ServicePriceRQ.class);
	}

	/**
	 * Sets a pre-built MsgPartiesType object to the request.
	 * @param party object which represents Party node
	 * @return current builder instance
	 */
	public ServicePriceRQBuilder setParty(MsgPartiesType party) {
		request.setParty(party);
		return this;
	}

	/**
	 *
	 */
	public ServicePriceRQBuilder setShoppingResponseId(String shoppingResponseId) {
		ShoppingResponseIDType shoppingResponseIDs = factory.createShoppingResponseIDType();
		ResponseID responseID = factory.createShoppingResponseIDTypeResponseID();
		responseID.setValue(shoppingResponseId);
		shoppingResponseIDs.setResponseID(responseID);
		request.setShoppingResponseIDs(shoppingResponseIDs);
		return this;
	}

	public ServicePriceRQ build() {
		request.setVersion("1.1.5");
		addDocumentNode();
		return request;
	}

	private void addDocumentNode() {
		MsgDocumentType document = factory.createMsgDocumentType();
		document.setName("NDC ServicePriceRQ Message");
		document.setReferenceVersion("1.0");
		request.setDocument(document);
	}

}