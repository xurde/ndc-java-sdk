package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.JAXBException;

import org.iata.ndc.schema.ShoppingResponseOrderType.Offer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class OrderCreateRQUnmarshallingIT extends AbstractUnmarshaller<OrderCreateRQ> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/OrderCreateRQ - OneWay with one pax.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/OrderCreateRQ - Round Trip with one pax.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/OrderCreateRQ - OneWay with one pax.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/OrderCreateRQ - RoundTrip with one pax.xml", "1#M#108161700#108170568"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String offerItemID;

	@Test
	public void unmarshal() throws JAXBException {
		OrderCreateRQ orderCreateRQ = unmarshal(resource);
		Offer offer = orderCreateRQ.getQuery().getOrderItems().getShoppingResponse().getOffers().get(0);
		assertEquals(offerItemID, offer.getOfferItems().get(0).getOfferItemID().getValue());
	}
}