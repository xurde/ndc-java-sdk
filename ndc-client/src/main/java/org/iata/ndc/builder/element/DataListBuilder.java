package org.iata.ndc.builder.element;

import java.util.ArrayList;
import java.util.List;

import org.iata.ndc.schema.*;
import org.iata.ndc.schema.DataListType.Flight;
import org.iata.ndc.schema.FlightCOSCoreType.Code;

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
	 * Adds DataList elements using objects from AirShoppingRS for specific flight.
	 * Method can be called multiple times with different flight data.<br>
	 * {@link FlightReferences} in {@link OriginDestination} are cleared and set to flight.
	 * Flight is added to FlightList.
	 * Segments of the Flight are added to FlightSegmentList.
	 *
	 * @param originDestination OriginDestination object from AirShoppingRS/DataLists/OriginDestinationList
	 * @param flight Flight object from AirShoppingRS/DataLists/Flights
	 * @param cabinCode single letter cabinCode
	 * @return current builder instance
	 */
	public DataListBuilder setDataListForSeatAvailability(OriginDestination originDestination, Flight flight, String cabinCode) {
		dataList.getOriginDestinationList().add(originDestination);
		// Set the selected flight as the only flightReference for current originDestination
		originDestination.getFlightReferences().getValue().clear();
		originDestination.getFlightReferences().getValue().add(flight);

		// Adds flightSegments in the flight to FlightSegmentList
		dataList.getFlightList().add(flight);
		List<ListOfFlightSegmentType> listOfFlightSegments = getObjectListFromReferenceList(flight.getSegmentReferences().getValue(), ListOfFlightSegmentType.class);
		dataList.getFlightSegmentList().addAll(listOfFlightSegments);

		//Updates flightSegments with
		for (ListOfFlightSegmentType segment : listOfFlightSegments) {
			FlightCOSCoreType classOfService = factory.createFlightCOSCoreType();
			Code cosCode = factory.createFlightCOSCoreTypeCode();
			cosCode.setValue(cabinCode);
			classOfService.setCode(cosCode);
			segment.setClassOfService(classOfService);
		}

		return this;
	}

	public static <T> List<T> getObjectListFromReferenceList(List<Object> objects, Class<T> clazz) {
		List<T> list = new ArrayList<T>();
		for (Object o : objects) {
			if ( clazz.isInstance(o)) {
				T element = (T) o;
				list.add(element);
			}
		}
		return list;
	}

	/**
	 * Builds MsgPartiesType instance and returns it.
	 * @return constructed MsgPartiesType instance
	 */
	public DataListType build() {
		return dataList;
	}
}
