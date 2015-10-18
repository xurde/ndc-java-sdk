package org.iata.ndc.builder;

import org.iata.ndc.schema.*;
import org.iata.ndc.schema.ShoppingResponseIDType.ResponseID;

public class ServiceListRQBuilder {
	private static final ObjectFactory factory = new ObjectFactory();

	private ServiceListRQ request;

	/**
	 * Creates a new instance of ServiceListRQBuilder.
	 * A new instance can be created for each request or you can use the {@link #clear() clear()} method.<br>
	 */
	public ServiceListRQBuilder() {
		clear();
	}

	/**
	 * Re-initializes builder to empty state.
	 */
	public void clear() {
		request = Initializer.getObject(ServiceListRQ.class);
	}

	/**
	 * Sets a pre-built MsgPartiesType object to the request.
	 * @param party object which represents Party node
	 * @return current builder instance
	 */
	public ServiceListRQBuilder setParty(MsgPartiesType party) {
		request.setParty(party);
		return this;
	}

	public ServiceListRQBuilder setShoppingResponseId(String shoppingResponseId) {
		ShoppingResponseIDType shoppingResponseIDs = factory.createShoppingResponseIDType();
		ResponseID responseID = factory.createShoppingResponseIDTypeResponseID();
		responseID.setValue(shoppingResponseId);
		shoppingResponseIDs.setResponseID(responseID);
		request.setShoppingResponseIDs(shoppingResponseIDs);
		return this;
	}

	public ServiceListRQ build() {
		request.setVersion("1.1.5");
		addDocumentNode();
		return request;
	}

	private void addDocumentNode() {
		MsgDocumentType document = factory.createMsgDocumentType();
		document.setName("NDC ServiceListRQ Message");
		document.setReferenceVersion("1.0");
		request.setDocument(document);
	}

}