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
public class OrderRetrieveRQUnmarshallingIT extends AbstractUnmarshaller<OrderRetrieveRQ> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/OrderRetrieveRQ.xml", "F9A2RI"},
			{"/Athena/RoundTrip/OrderRetrieveRQ.xml", "F9A2PI"},
			{"/Kronos/OneWay/OrderRetrieveRQ.xml", "L9A8W1"},
			{"/Kronos/RoundTrip/OrderRetrieveRQ.xml", "L9A8X1"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String orderID;

	@Test
	public void unmarshal() throws JAXBException {
		OrderRetrieveRQ orderRetrieveRQ = unmarshal(resource);
		assertEquals(orderID, orderRetrieveRQ.getQuery().getFilters().getOrderID().value);
	}
}