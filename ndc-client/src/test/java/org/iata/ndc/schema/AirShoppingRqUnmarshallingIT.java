package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.*;

import javax.xml.bind.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class AirShoppingRqUnmarshallingIT {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/AirShoppingRQ - OneWay with all Cabin.xml", "PRG"},
			{"/AirShoppingRQ - OneWay with Calendar.xml", "PRG"},
			{"/AirShoppingRQ - OneWay with Direct Flight.xml", "FRA"},
			{"/AirShoppingRQ - OneWay with multiple pax - Direct Flight.xml", "FRA"},
			{"/AirShoppingRQ - OneWay with multiple pax.xml", "PRG"},
			{"/AirShoppingRQ - OneWay with one pax.xml", "PRG"},
			{"/AirShoppingRQ - RoundTrip with all Cabin.xml", "BCN"},
			{"/AirShoppingRQ - RoundTrip with Calendar - Direct Flight.xml", "CDG"},
			{"/AirShoppingRQ - RoundTrip with Calendar.xml", "BCN"},
			{"/AirShoppingRQ - RoundTrip with Direct Flight.xml", "CDG"},
			{"/AirShoppingRQ - RoundTrip with multiple pax.xml", "BCN"},
			{"/AirShoppingRQ - RoundTrip with one pax.xml", "BCN"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String arrivalAirport;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(AirShoppingRQ.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AirShoppingRQ airShoppingRQ =  (AirShoppingRQ) unmarshaller.unmarshal(is);
			List<AirShopReqAttributeQueryTypeOriginDestination> originDestinations = airShoppingRQ.getCoreQuery().getOriginDestinations();
			assertEquals(arrivalAirport, originDestinations.get(0).getArrival().getAirportCode().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}