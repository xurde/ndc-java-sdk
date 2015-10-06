package org.iata.ndc.builder;

import static org.junit.Assert.assertEquals;

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

		List<org.iata.iata.edist.Travelers.Traveler> travelers = request.getTravelers().getTraveler();
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

		List<org.iata.iata.edist.Travelers.Traveler> travelers = request.getTravelers().getTraveler();
		assertEquals(3, travelers.size());
		for (Travelers.Traveler t : travelers) {
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
	public void createFullRequest() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2015, 9, 19);
		Date date = cal.getTime();

		AirShoppingRQ req = testedClass.addTravelAgencySender("Flyiin", "00002004", "flyiin-id")
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
