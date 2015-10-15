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
public class AirShoppingRSUnmarshallingIT extends AbstractUnmarshaller<AirShoppingRS> {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/AirShoppingRS - ARN-LHR OneWay with one pax.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/AirShoppingRS - CDG-RIX Round Trip with one pax.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/AirShoppingRS-ARNRIX - OneWay with one pax.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/AirShoppingRS-ARNRIX - RoundTrip with one pax.xml", "1#M#108161700#108170568"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String offerItemID;

	@Test
	public void unmarshal() throws JAXBException {
		AirShoppingRS object = unmarshal(resource);
		String result = object.getOffersGroup().getAirlineOffers().get(0).getAirlineOffer().get(0).getPricedOffer().getOfferPrice().get(0).getOfferItemID();
		assertEquals(offerItemID, result);
	}
}