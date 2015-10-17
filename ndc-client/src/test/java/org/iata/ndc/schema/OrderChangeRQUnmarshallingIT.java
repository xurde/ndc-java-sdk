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
public class OrderChangeRQUnmarshallingIT extends AbstractUnmarshaller<OrderChangeRQ> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/OrderChangeRQ_OneWay AddPassenger.xml", "2#M#111198795#111162714"},
			{"/Athena/RoundTrip/OrderChangeRQ_Add Passanger Round Trip with one pax.xml", "2#M#111186373#111192206"},
			{"/Kronos/OneWay/OrderChangeRQ_OneWay AddPassenger.xml", "2#M#109939774#109952838"},
			{"/Kronos/RoundTrip/OrderChangeRQ_RoundTrip AddPassenger.xml", "2#M#108161700#108170568"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String orderItemID;

	@Test
	public void unmarshal() throws JAXBException {
		OrderChangeRQ orderChangeRQ = unmarshal(resource);
		assertEquals(orderItemID, orderChangeRQ.getQuery().getOrder().getOrderItems().get(0).getOrderItemID().value);
	}
}