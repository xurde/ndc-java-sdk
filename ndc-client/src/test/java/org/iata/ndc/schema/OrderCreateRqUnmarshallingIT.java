package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.*;

import org.iata.ndc.schema.ShoppingResponseOrderType.Offer;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@Ignore
@RunWith(Parameterized.class)
public class OrderCreateRqUnmarshallingIT {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/OrderCreateRQ - OneWay with one pax.xml", "1#C#109987187#110031494"},
			{"/OrderCreateRQ - RoundTrip with one pax.xml", "1#M#108183266"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String offerItemID;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(OrderCreateRQ.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			OrderCreateRQ orderCreateRQ =  (OrderCreateRQ) unmarshaller.unmarshal(is);
			Offer offer = orderCreateRQ.getQuery().getOrderItems().getShoppingResponse().getOffers().get(0);
			assertEquals(offerItemID, offer.getOfferItems().get(0).getOfferItemID().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}