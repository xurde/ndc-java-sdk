package org.iata.ndc.builder;

import org.iata.ndc.schema.*;

/**
 * This class provides a simple way to create SeatAvailabilityRQ objects. It implements fluent interface, thus allowing to chain methods.<br>
 * Since the object returned by {@link #build build()} can be modified any further customization can be performed manually.
 */
public class SeatAvailabilityRQBuilder {
	private static final ObjectFactory factory = new ObjectFactory();

	private SeatAvailabilityRQ request;

	private MsgPartiesType party;

	/**
	 * Creates a new instance of SeatAvailabilityRQBuilder.
	 * A new instance can be created for each request or you can use the {@link #clear() clear()} method.<br>
	 *
	 * Defaults:<ol>
	 * <li> One adult traveler.
	 * </ol>
	 */
	public SeatAvailabilityRQBuilder() {
		clear();
	}

	/**
	 * Re-initializes builder to empty state.
	 */
	public void clear() {

		request = factory.createSeatAvailabilityRQ();
		request.setQuery(factory.createSeatAvailabilityRQQuery());
		party = null;
	}

	/**
	 * Sets a pre-built MsgPartiesType object to the request.
	 * @param party object which represents Party node
	 * @return current builder instance
	 */
	public SeatAvailabilityRQBuilder setParty(MsgPartiesType party) {
		request.setParty(party);
		return this;
	}

	/**
	 * Sets a pre-build DataListType object to the request.
	 * @param dataList object which represents Party node
	 * @return current builder instance
	 */
	public SeatAvailabilityRQBuilder setDataList(DataListType dataList) {
		request.setDataList(dataList);
		return this;
	}

	public SeatAvailabilityRQBuilder addOriginDestinationToQuery(OriginDestination originDestination) {
		FlightInfoAssocType originDest = factory.createFlightInfoAssocType();
		originDest.getOriginDestinationReferences().add(originDestination);
		request.getQuery().getOriginDestination().add(originDest);

		return this;
	}

	/**
	 * Builds SeatAvailabilityRQ instance and returns it.
	 * @return constructed SeatAvailabilityRQ instance
	 */
	public SeatAvailabilityRQ build() {
		request.setVersion("1.1.5");
		addDocumentNode();
		addPreferencesNode();

		return request;
	}

	private void addPreferencesNode() {
		SeatAvailReqParamsType parameters = factory.createSeatAvailReqParamsType();
		CurrCode currency = factory.createCurrCode();
		currency.setValue("EUR");
		parameters.getCurrCodes().add(currency);
		request.setParameters(parameters);
	}

	private void addDocumentNode() {
		MsgDocumentType document = factory.createMsgDocumentType();
		document.setName("NDC SeatAvailabilityRQ Message");
		document.setReferenceVersion("1.0");
		request.setDocument(document);
	}
}
