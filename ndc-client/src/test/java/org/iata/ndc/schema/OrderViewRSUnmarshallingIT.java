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
public class OrderViewRSUnmarshallingIT extends AbstractUnmarshaller<OrderViewRS> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/OrderViewRS - OrderCreateRQ - OneWay with one pax.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/OrderViewRS - OrderCreateRQ - Round Trip with one pax.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/OrderViewRS - OrderCreate.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/OrderViewRS - OrderCreateRQ.xml", "1#M#109953106#109939177"},

			{"/Athena/OneWay/OrderViewRS - OrderChangeRQ.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/OrderViewRS - OrderChangeRQ - Round Trip with one pax.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/OrderViewRS - OrderChange.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/OrderViewRS - OrderChangeRQ.xml", "2#M#109953106#109939177"},

			{"/Athena/OneWay/OrderViewRS - OrderRetrieveRQ.xml", "1#M#111198795#111162714"},
			{"/Athena/RoundTrip/OrderViewRS - OrderRetrieveRQ.xml", "1#M#111186373#111192206"},
			{"/Kronos/OneWay/OrderViewRS - OrderChange.xml", "1#M#109939774#109952838"},
			{"/Kronos/RoundTrip/OrderViewRS - OrderRetrieveRQ.xml", "2#M#109953106#109939177"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String offerItemID;

	@Test
	public void unmarshal() throws JAXBException {
		OrderViewRS orderViewRS = unmarshal(resource);
		assertEquals(offerItemID, orderViewRS.getResponse().getOrder().get(0).getOrderItems().getOrderItem().get(0).getOrderItemID().getValue());
	}
}