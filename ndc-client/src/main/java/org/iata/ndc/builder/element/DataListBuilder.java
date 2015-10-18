package org.iata.ndc.builder.element;

import java.util.List;

import org.iata.ndc.schema.*;
import org.iata.ndc.schema.DataListType.Flight;

public class DataListBuilder {
	private static final ObjectFactory factory = new ObjectFactory();
	private DataListType dataList;

	public DataListBuilder() {
		clear();
	}

	/**
	 * Re-initializes builder to empty state.
	 */
	public void clear() {
		dataList = factory.createDataListType();
	}

	/**
	 * Sets flightSegmentList to specified value
	 * @param flightSegmentList flightSegmentList to add
	 * @return current builder instance
	 */
	public DataListBuilder setFlightSegmentList(List<ListOfFlightSegmentType> flightSegmentList) {
		dataList.setFlightSegmentList(flightSegmentList);
		return this;
	}

	/**
	 * Sets flightList to specified value
	 * @param flightList flightList to add
	 * @return current builder instance
	 */
	public DataListBuilder setFlightList(List<Flight> flightList) {
		dataList.setFlightList(flightList);
		return this;
	}

	/**
	 * Sets originDestinationList to specified value
	 * @param originDestinationList
	 * @return current builder instance
	 */
	public DataListBuilder setOriginDestinationList(List<OriginDestination> originDestinationList) {
		dataList.setOriginDestinationList(originDestinationList);
		return this;
	}

	/**
	 * Builds MsgPartiesType instance and returns it.
	 * @return constructed MsgPartiesType instance
	 */
	public DataListType build() {
		return dataList;
	}
}
