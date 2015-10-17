package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FlightPriceRSUnmarshallingIT extends AbstractUnmarshaller<FlightPriceRS> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/FlightPriceRS - ARN-LHR OneWay with one pax.xml", "ARN"},
			{"/Athena/RoundTrip/FlightPriceRS - CDG-RIX Round Trip with one pax.xml", "CDG"},
			{"/Kronos/OneWay/FlightPriceRS - OneWay with one pax.xml", "ARN"},
			{"/Kronos/RoundTrip/FlightPriceRS-RoundTrip with one pax.xml", "ARN"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String departure;

	@Test
	public void unmarshal() throws JAXBException {
		FlightPriceRS flightPriceRS =  unmarshal(resource);
		assertEquals(departure, flightPriceRS.getDataLists().getOriginDestinationList().get(0).getDepartureCode().getValue());
	}
}