package org.iata.ndc.builder;

import java.math.BigInteger;
import java.util.*;

import javax.xml.datatype.*;

import org.apache.commons.codec.digest.DigestUtils;
import org.iata.iata.edist.*;
import org.iata.iata.edist.AirShopReqAttributeQueryTypeOriginDestination.CalendarDates;
import org.iata.iata.edist.FlightDepartureType.AirportCode;
import org.iata.iata.edist.MsgPartiesType.Sender;
import org.iata.iata.edist.TravelerCoreType.PTC;
import org.iata.ndc.ClientException;


public class AirShoppingRQBuilder {

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

	private AirShoppingRQ request;
	private static final ObjectFactory factory = new ObjectFactory();

	private Map<Traveler, Integer> anonymousTravelers;
	private Sender sender;

	private Set<String> airlinePreferences;


	public AirShoppingRQBuilder() {
		anonymousTravelers = new HashMap<AirShoppingRQBuilder.Traveler, Integer>();
		airlinePreferences = new LinkedHashSet<String>();

		request = Initializer.getObject(AirShoppingRQ.class);
		sender = null;
	}

	public AirShoppingRQBuilder addTravelAgencySender(String name, String iataNumber, String agencyId) {
		sender = factory.createMsgPartiesTypeSender();
		TravelAgencySenderType agency = factory.createTravelAgencySenderType();
		agency.setName(name);
		agency.setIATANumber(iataNumber);
		AgencyIDType agencyIDType = factory.createAgencyIDType();
		agencyIDType.setValue(agencyId);
		agency.setAgencyID(agencyIDType);
		sender.setTravelAgencySender(agency);
		return this;
	}

	public AirShoppingRQBuilder addAnonymousTraveler(Traveler traveler) {
		return addAnonymousTravelers(traveler, 1);
	}

	public AirShoppingRQBuilder addAnonymousTravelers(Traveler traveler, int count) {
		if (!anonymousTravelers.containsKey(traveler)) {
			anonymousTravelers.put(traveler, count);
			return this;
		}
		Integer total = anonymousTravelers.get(traveler) + count;
		anonymousTravelers.put(traveler, total);
		return this;
	}

	public AirShoppingRQBuilder addOriginDestination(AirShopReqAttributeQueryTypeOriginDestination originDestination) {
		request.getCoreQuery().getOriginDestinations().add(originDestination);
		return this;
	}

	public AirShoppingRQBuilder addOriginDestination(String origin, String destination, Date date) {
		return addOriginDestination(origin, destination, date, 0, 0);
	}

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


	public AirShoppingRQBuilder addAirlinePreference(String airlineId) {
		airlinePreferences.add(airlineId);

		return this;
	}

	public AirShoppingRQ build() {
		setDefaults();

		addRequestAttributes();
		addDocumentNode();
		addPartyNode();
		addTravelers();

		addAirlinePreferences();

		return request;
	}

	private void addAirlinePreferences() {
		if (airlinePreferences.size() == 0) {
			return;
		}
		AirlinePreferencesType preferences = factory.createAirlinePreferencesType();
		for(String code : airlinePreferences) {
			AirlinePreferencesType.Airline airline = factory.createAirlinePreferencesTypeAirline();
			AirlineID airlineID = factory.createAirlineID();
			airlineID.setValue(code);
			airline.setAirlineID(airlineID);
			preferences.getAirline().add(airline);
		}

		request.getPreference().add(preferences);
	}

	private void setDefaults() {
		if (anonymousTravelers.size() == 0) {
			addAnonymousTraveler(Traveler.ADT);
		}
	}

	private void addPartyNode() {
		MsgPartiesType party = factory.createMsgPartiesType();

		party.setSender(sender);

		request.setParty(party);
	}

	private void addTravelers() {
		for (Traveler t: anonymousTravelers.keySet()) {
			org.iata.iata.edist.TravelersTraveler traveler = factory.createTravelersTraveler();
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
		XMLGregorianCalendar xmlgc = toUTC(new GregorianCalendar());
		request.setTimeStamp(xmlgc);
		request.setEchoToken(byteToHex(DigestUtils.sha1(xmlgc.toString())));
	}

	private static XMLGregorianCalendar toUTC(GregorianCalendar gregorianCalendar) {
		XMLGregorianCalendar xmlgc = null;
		DatatypeFactory df = null;
		try {
			df = DatatypeFactory.newInstance();
		} catch (DatatypeConfigurationException e) {
			throw new ClientException(e);
		}
		xmlgc = df.newXMLGregorianCalendar(gregorianCalendar);
		xmlgc = xmlgc.normalize();
		xmlgc.setMillisecond(DatatypeConstants.FIELD_UNDEFINED);
		return xmlgc;
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

	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
	        formatter.format("%02x", b);
	    }
	    String result = formatter.toString();
	    formatter.close();
	    return result;
	}
}
