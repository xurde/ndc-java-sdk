package org.iata.ndc.builder;

import static org.junit.Assert.*;

import java.io.StringWriter;
import java.math.BigInteger;
import java.util.*;

import javax.xml.bind.*;

import org.iata.iata.edist.*;
import org.iata.iata.edist.TravelerCoreType.PTC;
import org.iata.ndc.builder.AirShoppingRQBuilder.Traveler;
import org.junit.Before;
import org.junit.Test;

public class AirShoppingBuilderTest {
	private AirShoppingRQBuilder testedClass;

	@Before
	public void setUp() {
		testedClass = new AirShoppingRQBuilder();
	}

	@Test
	public void documentNode() {
		AirShoppingRQ request = testedClass.build();
		assertEquals("1.0", request.getDocument().getReferenceVersion());
		assertEquals("Java wrapper AirShoppingRQ Message", request.getDocument().getName());
	}

	@Test
	public void oneAnonymousAdult() {
		testedClass.addAnonymousTraveler(Traveler.ADT);
		AirShoppingRQ request = testedClass.build();

		List<org.iata.iata.edist.TravelersTraveler> travelers = request.getTravelers();
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

		List<org.iata.iata.edist.TravelersTraveler> travelers = request.getTravelers();
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
		org.iata.iata.edist.AirShopReqAttributeQueryTypeOriginDestination originDestination = request.getCoreQuery().getOriginDestinations().get(0);
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
		org.iata.iata.edist.AirShopReqAttributeQueryTypeOriginDestination originDestination = request.getCoreQuery().getOriginDestinations().get(0);
		assertEquals("Departure", originDestination.getDeparture().getAirportCode().getValue());
		assertEquals("Arrival", originDestination.getArrival().getAirportCode().getValue());
		assertNotNull("CalendarDates is not present", originDestination.getCalendarDates());
		assertEquals(Integer.valueOf(1), originDestination.getCalendarDates().getDaysBefore());
		assertEquals(Integer.valueOf(2), originDestination.getCalendarDates().getDaysAfter());
	}


	@Test
	public void createFullRequest() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2015, 9, 19);
		Date date = cal.getTime();

		AirShoppingRQ req = testedClass.addTravelAgencySender("Test Agency", "0000XXXX", "Test Agent")
		.addOriginDestination("CDG", "LHR", date).build();

		System.out.println(toXML(req));
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
