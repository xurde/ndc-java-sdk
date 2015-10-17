package org.iata.ndc.builder;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.*;

import javax.xml.bind.*;
import javax.xml.namespace.QName;

import org.iata.ndc.builder.AirShoppingRQBuilder.Traveler;
import org.iata.ndc.schema.*;
import org.iata.ndc.schema.MsgPartiesType.Participants;
import org.iata.ndc.schema.MsgPartiesType.Participants.Participant;
import org.iata.ndc.schema.TravelerCoreType.PTC;
import org.junit.Before;
import org.junit.Test;

public class AirShoppingRQBuilderTest {
	private static final ObjectFactory factory = new ObjectFactory();
	private AirShoppingRQBuilder testedClass;


	@Before
	public void setUp() {
		testedClass = new AirShoppingRQBuilder();
	}

	@Test
	public void documentNode() {
		AirShoppingRQ request = testedClass.build();
		assertEquals("1.0", request.getDocument().getReferenceVersion());
		assertEquals("NDC AirShoppingRQ Message", request.getDocument().getName());
	}

	@Test
	public void setParty() {
		MsgPartiesType party = Initializer.getObject(MsgPartiesType.class);
		Participants participants = factory.createMsgPartiesTypeParticipants();
		party.setParticipants(new JAXBElement<MsgPartiesType.Participants>(new QName("Participants"), Participants.class, participants));
		Participant participant = Initializer.getObject(Participant.class);
		EnabledSysParticipantType enabled = factory.createEnabledSysParticipantType();
		enabled.setName("Enabled System");
		participant.setEnabledSystemParticipant(enabled);
		party.getParticipants().getValue().getParticipant().add(participant);
		AirShoppingRQ request = testedClass.setParty(party).build();
		assertEquals("Enabled System", request.getParty().getParticipants().getValue().getParticipant().get(0).getEnabledSystemParticipant().getName());
	}

	@Test
	public void setPartyAddSenderReturnsPartyWithSenderOverridden() {
		MsgPartiesType party = Initializer.getObject(MsgPartiesType.class);
		Participants participants = factory.createMsgPartiesTypeParticipants();
		party.setParticipants(new JAXBElement<MsgPartiesType.Participants>(new QName("Participants"), Participants.class, participants));
		Participant participant = Initializer.getObject(Participant.class);
		EnabledSysParticipantType enabled = factory.createEnabledSysParticipantType();
		enabled.setName("Enabled System");
		participant.setEnabledSystemParticipant(enabled);
		party.getParticipants().getValue().getParticipant().add(participant);

		AirShoppingRQ request = testedClass.setParty(party).addTravelAgencySender("a", "b", "c").build();

		assertEquals("Enabled System", request.getParty().getParticipants().getValue().getParticipant().get(0).getEnabledSystemParticipant().getName());
		assertEquals("a", request.getParty().getSender().getTravelAgencySender().getName());
	}

	@Test
	public void oneAnonymousAdult() {
		testedClass.addAnonymousTraveler(Traveler.ADT);
		AirShoppingRQ request = testedClass.build();

		List<org.iata.ndc.schema.TravelersTraveler> travelers = request.getTravelers();
		assertEquals(1, travelers.size());
		PTC ptc = travelers.get(0).getAnonymousTraveler().getPTC();
		assertEquals(BigInteger.valueOf(1), ptc.getQuantity());
		assertEquals(Traveler.ADT.name(), ptc.getValue());
	}

	@Test
	public void multipleAnonymousTravellers() {
		Map<Traveler, Integer> data = new HashMap<Traveler, Integer>(3);
		data.put(Traveler.ADT, 2);
		data.put(Traveler.CHD, 3);
		data.put(Traveler.INF, 1);
		for (Traveler t : data.keySet()) {
			testedClass.addAnonymousTravelers(t, data.get(t));
		}

		AirShoppingRQ request = testedClass.build();

		List<org.iata.ndc.schema.TravelersTraveler> travelers = request.getTravelers();
		assertEquals(3, travelers.size());
		for (TravelersTraveler t : travelers) {
			PTC ptc = t.getAnonymousTraveler().getPTC();
			Traveler key = Traveler.valueOf(ptc.getValue());
			assertEquals(BigInteger.valueOf(data.get(key)), ptc.getQuantity());
			assertEquals(key.name(), ptc.getValue());
		}
	}

	@Test
	public void addTravelAgencySender() {
		testedClass.addTravelAgencySender("name", "IATA", "ID");

		AirShoppingRQ request = testedClass.build();

		TravelAgencySenderType travelAgencySender = request.getParty().getSender().getTravelAgencySender();
		assertEquals("name", travelAgencySender.getName());
		assertEquals("IATA", travelAgencySender.getIATANumber());
		assertEquals("ID", travelAgencySender.getAgencyID().getValue());
	}

	@Test
	public void addOriginDestination() {
		Date date = Calendar.getInstance().getTime();
		testedClass.addOriginDestination("Departure", "Arrival", date);

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getCoreQuery().getOriginDestinations().size());
		AirShopReqAttributeQueryTypeOriginDestination originDestination = request.getCoreQuery().getOriginDestinations().get(0);
		assertEquals("Departure", originDestination.getDeparture().getAirportCode().getValue());
		assertEquals("Arrival", originDestination.getArrival().getAirportCode().getValue());
		assertNull("CalendarDates is present", originDestination.getCalendarDates());
	}

	@Test
	public void addOriginDestinationWithCalendarDates() {
		Date date = Calendar.getInstance().getTime();
		testedClass.addOriginDestination("Departure", "Arrival", date, 1, 2);

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getCoreQuery().getOriginDestinations().size());
		AirShopReqAttributeQueryTypeOriginDestination originDestination = request.getCoreQuery().getOriginDestinations().get(0);
		assertEquals("Departure", originDestination.getDeparture().getAirportCode().getValue());
		assertEquals("Arrival", originDestination.getArrival().getAirportCode().getValue());
		assertNotNull("CalendarDates is not present", originDestination.getCalendarDates());
		assertEquals(Integer.valueOf(1), originDestination.getCalendarDates().getDaysBefore());
		assertEquals(Integer.valueOf(2), originDestination.getCalendarDates().getDaysAfter());
	}

	@Test
	public void noPreferences() {
		AirShoppingRQ request = testedClass.build();
		assertEquals(0, request.getPreferences().size());
	}

	@Test
	public void addAirlinePreference() {
		testedClass.addAirlinePreference("A1");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		AirlinePreferencesType airlinePreference = request.getPreferences().get(0).getAirlinePreferences();
		assertEquals(1, airlinePreference.getAirline().size());
		assertEquals("A1", airlinePreference.getAirline().get(0).getAirlineID().getValue());
	}

	@Test
	public void addMultipleAirlinePreferences() {
		testedClass.addAirlinePreference("A1");
		testedClass.addAirlinePreference("A2");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		AirlinePreferencesType airlinePreference = request.getPreferences().get(0).getAirlinePreferences();
		assertEquals(2, airlinePreference.getAirline().size());
		assertEquals("A1", airlinePreference.getAirline().get(0).getAirlineID().getValue());
		assertEquals("A2", airlinePreference.getAirline().get(1).getAirlineID().getValue());
	}

	@Test
	public void addMultipleIdenticalAirlinePreferences() {
		testedClass.addAirlinePreference("A1");
		testedClass.addAirlinePreference("A1");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		AirlinePreferencesType airlinePreference = request.getPreferences().get(0).getAirlinePreferences();
		assertEquals(1, airlinePreference.getAirline().size());
		assertEquals("A1", airlinePreference.getAirline().get(0).getAirlineID().getValue());
	}

	@Test
	public void addFarePreference() {
		testedClass.addFarePreference("F1");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		FarePreferencesType farePreference = request.getPreferences().get(0).getFarePreferences();
		assertEquals(1, farePreference.getTypes().size());
		assertEquals("F1", farePreference.getTypes().get(0).getCode());
	}

	@Test
	public void addMultipleFarePreferences() {
		testedClass.addFarePreference("F1");
		testedClass.addFarePreference("F2");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		FarePreferencesType farePreference = request.getPreferences().get(0).getFarePreferences();
		assertEquals(2, farePreference.getTypes().size());
		assertEquals("F1", farePreference.getTypes().get(0).getCode());
		assertEquals("F2", farePreference.getTypes().get(1).getCode());
	}

	@Test
	public void addMultipleIdenticalFarePreferences() {
		testedClass.addFarePreference("F1");
		testedClass.addFarePreference("F1");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		FarePreferencesType farePreference = request.getPreferences().get(0).getFarePreferences();
		assertEquals(1, farePreference.getTypes().size());
		assertEquals("F1", farePreference.getTypes().get(0).getCode());
	}

	@Test
	public void addCabinPreference() {
		testedClass.addCabinPreference("A");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		CabinPreferencesType cabinPreference = request.getPreferences().get(0).getCabinPreferences();
		assertEquals(1, cabinPreference.getCabinType().size());
		assertEquals("A", cabinPreference.getCabinType().get(0).getCode());
	}

	@Test
	public void addMultipleCabinPreferences() {
		testedClass.addCabinPreference("A");
		testedClass.addCabinPreference("B");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		CabinPreferencesType cabinPreference = request.getPreferences().get(0).getCabinPreferences();
		assertEquals(2, cabinPreference.getCabinType().size());
		assertEquals("A", cabinPreference.getCabinType().get(0).getCode());
		assertEquals("B", cabinPreference.getCabinType().get(1).getCode());
	}

	@Test
	public void addMultipleIdenticalCabinPreferences() {
		testedClass.addCabinPreference("A");
		testedClass.addCabinPreference("A");

		AirShoppingRQ request = testedClass.build();

		assertEquals(1, request.getPreferences().size());
		CabinPreferencesType cabinPreference = request.getPreferences().get(0).getCabinPreferences();
		assertEquals(1, cabinPreference.getCabinType().size());
		assertEquals("A", cabinPreference.getCabinType().get(0).getCode());
	}

	private static String toXML(AirShoppingRQ request) {
		JAXBContext context;
		try {
			context = JAXBContext.newInstance(AirShoppingRQ.class);
			Marshaller marshaller = context.createMarshaller();
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);
			return writer.toString();
		} catch (JAXBException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		return "Exception occured";
	}

}
