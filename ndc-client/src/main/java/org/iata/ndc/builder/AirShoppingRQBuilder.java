package org.iata.ndc.builder;

import java.math.BigInteger;
import java.util.*;

import javax.xml.datatype.*;

import org.iata.ndc.ClientException;
import org.iata.ndc.builder.element.PartyBuilder;
import org.iata.ndc.schema.*;
import org.iata.ndc.schema.AirShopReqAttributeQueryTypeOriginDestination.CalendarDates;
import org.iata.ndc.schema.FarePreferencesType.Type;
import org.iata.ndc.schema.FlightDepartureType.AirportCode;
import org.iata.ndc.schema.MsgPartiesType.Sender;
import org.iata.ndc.schema.TravelerCoreType.PTC;

/**
 * This class provides a simple way to create AirShoppingRQ objects. It implements fluent interface, thus allowing to chain methods.<br>
 * Since the object returned by {@link #build build()} can be modified any further customization can be performed manually.
 */
public class AirShoppingRQBuilder {

	/**
	 * Traveler enum represents possible traveler types.
	 */
	public enum Traveler {
		/** Adult */
		ADT,
		/** Child */
		CNN,
		/** Child */
		CHD,
		/** Infant */
		INF,
		/** Government */
		GVT,
		/** State government */
		GST
	};

	private static final ObjectFactory factory = new ObjectFactory();

	private AirShoppingRQ request;

	private Map<Traveler, Integer> anonymousTravelers;
	private Sender sender;
	private MsgPartiesType party;

	private Set<String> airlines;
	private Set<String> fares;
	private Set<String> cabins;

	/**
	 * Creates a new instance of AirShoppingRQBuilder.
	 * A new instance can be created for each request or you can use the {@link #clear() clear()} method.<br>
	 *
	 * Defaults:<ol>
	 * <li> One adult traveler.
	 * </ol>
	 */
	public AirShoppingRQBuilder() {
		clear();
	}

	/**
	 * Re-initializes builder to empty state.
	 */
	public void clear() {
		anonymousTravelers = new HashMap<AirShoppingRQBuilder.Traveler, Integer>();
		airlines = new LinkedHashSet<String>();
		fares = new LinkedHashSet<String>();
		cabins = new LinkedHashSet<String>();

		request = Initializer.getObject(AirShoppingRQ.class);
		sender = null;
		party = null;
	}

	/**
	 * Sets a pre-built MsgPartiesType object to the request.
	 * @param party object which represents Party node
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder setParty(MsgPartiesType party) {
		this.party = party;
		return this;
	}

	/**
	 * Creates TravelAgencySender representation and sets it as request sender.
	 * Second invocation will override previous value.<br>
	 * If both this method and {@link #setParty(MsgPartiesType)} are called, party data will be set.
	 * Sender in the party will be overridden by one created in {@link #addTravelAgencySender(String, String, String)}
	 *
	 * <p> Consider using {@link #setParty(MsgPartiesType)} with {@link PartyBuilder}.
	 *
	 * @param name Travel agency name
	 * @param iataNumber IATA number for the agency
	 * @param agencyId agency ID
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addTravelAgencySender(String name, String iataNumber, String agencyId) {
		MsgPartiesType p = new PartyBuilder().setTravelAgencySender(name, iataNumber, agencyId).build();
		sender = p.getSender();
		return this;
	}

	/**
	 * Adds anonymous traveler of type {@link Traveler} to traveler list.<br>
	 * <strong>Note:</strong> if this type of traveler already exists, increments the count for this type of traveler.
	 *
	 * @param traveler type of traveler
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addAnonymousTraveler(Traveler traveler) {
		return addAnonymousTravelers(traveler, 1);
	}

	/**
	 * Adds multiple anonymous travelers of type {@link Traveler} to traveler list.<br>
	 * <strong>Note:</strong> if this type of traveler already exists, increments the count for this type of traveler.
	 *
	 * @param traveler type of traveler
	 * @param count number of travelers
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addAnonymousTravelers(Traveler traveler, int count) {
		if (!anonymousTravelers.containsKey(traveler)) {
			anonymousTravelers.put(traveler, count);
			return this;
		}
		Integer total = anonymousTravelers.get(traveler) + count;
		anonymousTravelers.put(traveler, total);
		return this;
	}

	/**
	 * Adds prebuilt {@link AirShopReqAttributeQueryTypeOriginDestination} instance to the list of OriginDestinations.
	 * @param originDestination originDestination to add
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addOriginDestination(AirShopReqAttributeQueryTypeOriginDestination originDestination) {
		request.getCoreQuery().getOriginDestinations().add(originDestination);
		return this;
	}

	/**
	 * Creates a new instance of {@link AirShopReqAttributeQueryTypeOriginDestination} using supplied data and adds it to the list of OriginDestinations.
	 * @param origin three letter airport code for the departure airport
	 * @param destination three letter airport code for the arrival airport
	 * @param date date of the flight
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addOriginDestination(String origin, String destination, Date date) {
		return addOriginDestination(origin, destination, date, 0, 0);
	}

	/**
	 * Creates a new instance of {@link AirShopReqAttributeQueryTypeOriginDestination} using supplied data and adds it to the list of OriginDestinations.
	 * This method allows to specify a period of dates when the flight date is flexible.
	 *
	 * @param origin three letter airport code for the departure airport
	 * @param destination three letter airport code for the arrival airport
	 * @param date date of the flight
	 * @param daysBefore request flights for a period of daysBefore before the specified flight date
	 * @param daysAfter request flights for a period of daysAfter after the specified flight date
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addOriginDestination(String origin, String destination, Date date, int daysBefore, int daysAfter) {
		AirShopReqAttributeQueryTypeOriginDestination originDestination = Initializer.getObject(AirShopReqAttributeQueryTypeOriginDestination.class);

		AirportCode airportCode = factory.createFlightDepartureTypeAirportCode();
		originDestination.getDeparture().setAirportCode(airportCode);
		originDestination.getDeparture().getAirportCode().setValue(origin);
		originDestination.getArrival().getAirportCode().setValue(destination);
		originDestination.getDeparture().setDate(getDate(date));

		if( daysBefore != 0 || daysAfter != 0) {
			CalendarDates dates = factory.createAirShopReqAttributeQueryTypeOriginDestinationCalendarDates();
			dates.setDaysBefore(daysBefore);
			dates.setDaysAfter(daysAfter);
			originDestination.setCalendarDates(dates);
		}

		return addOriginDestination(originDestination);
	}


	/**
	 * Adds airline preference to the set of preferred airlines.
	 * @param airlineId two letter code for the airline
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addAirlinePreference(String airlineId) {
		airlines.add(airlineId);

		return this;
	}

	/**
	 * Adds preferred fare code to the set of fare preferences.
	 * @param fareCode fare code
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addFarePreference(String fareCode) {
		fares.add(fareCode);

		return this;
	}

	/**
	 * Adds cabin preference to the set of cabin preferences
	 * @param cabinCode
	 * @return current builder instance
	 */
	public AirShoppingRQBuilder addCabinPreference(String cabinCode) {
		cabins.add(cabinCode);

		return this;
	}

	/**
	 * Builds AirShoppingRQ instance and returns it.
	 * @return constructed AirShoppingRQ instance
	 */
	public AirShoppingRQ build() {
		setDefaults();

		addRequestAttributes();
		addDocumentNode();
		addPartyNode();
		addTravelers();

		addAirlinePreferences();
		addFarePreferences();
		addCabinPreferences();

		return request;
	}

	private void addCabinPreferences() {
		if (cabins.size() == 0) {
			return;
		}
		CabinPreferencesType cabinPreferencesType = factory.createCabinPreferencesType();
		for (String code: cabins) {
			CabinType cabin = factory.createCabinType();
			cabin.setCode(code);
			cabinPreferencesType.getCabinType().add(cabin);
		}
		org.iata.ndc.schema.AirShoppingRQ.Preference preferenceElement = factory.createAirShoppingRQPreference();
		preferenceElement.setCabinPreferences(cabinPreferencesType);
		request.getPreferences().add(preferenceElement);
	}

	private void addFarePreferences() {
		if (fares.size() == 0) {
			return;
		}
		FarePreferencesType farePreferences = factory.createFarePreferencesType();
		for (String code : fares) {
			Type type = factory.createFarePreferencesTypeType();
			type.setCode(code);
			farePreferences.getTypes().add(type);
		}
		org.iata.ndc.schema.AirShoppingRQ.Preference preferenceElement = factory.createAirShoppingRQPreference();
		preferenceElement.setFarePreferences(farePreferences);
		request.getPreferences().add(preferenceElement);
	}

	private void addAirlinePreferences() {
		if (airlines.size() == 0) {
			return;
		}
		AirlinePreferencesType airlinePreferences = factory.createAirlinePreferencesType();
		for (String code : airlines) {
			AirlinePreferencesType.Airline airline = factory.createAirlinePreferencesTypeAirline();
			AirlineID airlineID = factory.createAirlineID();
			airlineID.setValue(code);
			airline.setAirlineID(airlineID);
			airlinePreferences.getAirline().add(airline);
		}
		org.iata.ndc.schema.AirShoppingRQ.Preference preferenceElement = factory.createAirShoppingRQPreference();
		preferenceElement.setAirlinePreferences(airlinePreferences);
		request.getPreferences().add(preferenceElement);
	}

	private void setDefaults() {
		if (anonymousTravelers.size() == 0) {
			addAnonymousTraveler(Traveler.ADT);
		}
	}

	private void addPartyNode() {
		if (party == null) {
			party = factory.createMsgPartiesType();
		}
		party.setSender(sender);

		request.setParty(party);
	}

	private void addTravelers() {
		for (Traveler t: anonymousTravelers.keySet()) {
			org.iata.ndc.schema.TravelersTraveler traveler = factory.createTravelersTraveler();
			traveler.setAnonymousTraveler(factory.createAnonymousTravelerType());
			PTC ptc = factory.createTravelerCoreTypePTC();
			ptc.setValue(t.name());
			ptc.setQuantity(BigInteger.valueOf(anonymousTravelers.get(t)));
			traveler.getAnonymousTraveler().setPTC(ptc);
			request.getTravelers().add(traveler);
		}
	}

	private void addDocumentNode() {
		MsgDocumentType document = factory.createMsgDocumentType();
		document.setName("NDC AirShoppingRQ Message");
		document.setReferenceVersion("1.0");
		request.setDocument(document);
	}

	private void addRequestAttributes() {
		request.setVersion("1.1.5");
	}

	private static XMLGregorianCalendar getDate(Date date) {
		XMLGregorianCalendar xmlgc = null;
		DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new ClientException(e);
		}
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		xmlgc = df.newXMLGregorianCalendar(gc);
		xmlgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		xmlgc.setSecond(DatatypeConstants.FIELD_UNDEFINED);
		xmlgc.setMinute(DatatypeConstants.FIELD_UNDEFINED);
		xmlgc.setHour(DatatypeConstants.FIELD_UNDEFINED);
		xmlgc.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return xmlgc;
	}
}
