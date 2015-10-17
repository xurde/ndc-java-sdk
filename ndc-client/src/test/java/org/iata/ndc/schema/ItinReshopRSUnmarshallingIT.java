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
public class ItinReshopRSUnmarshallingIT extends AbstractUnmarshaller<ItinReshopRS> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/ItineraryReshopRS - OneWay.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/ItineraryReshopRS - Round Trip with one pax.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/ItineraryReshopRS - OneWay.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/ItineraryReshopRS - RoundTrip.xml", "1#M#108161700#108170568"},
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String OrderItemID;

	@Test
	public void unmarshal() throws JAXBException {
		ItinReshopRS itinReshopRS = unmarshal(resource);
		assertEquals(OrderItemID, itinReshopRS.getResponse().getOrder().getOrderItems().get(0).getOrderItemID().value);
	}
}