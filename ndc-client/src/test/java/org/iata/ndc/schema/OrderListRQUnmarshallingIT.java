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
public class OrderListRQUnmarshallingIT extends AbstractUnmarshaller<OrderListRQ> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/OrderListRQ_AirlineFilter.xml", "9A"},
			{"/Athena/RoundTrip/OrderListRQ_AirlineFilter.xml", "9A"},
			{"/Kronos/OneWay/OrderListRQ_AirlineFilter.xml", "C9"},
			{"/Kronos/RoundTrip/OrderListRQ_AirlineFilter.xml", "C9"},

		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String airlineID;

	@Test
	public void unmarshal() throws JAXBException {
		OrderListRQ orderListRQ = unmarshal(resource);
		assertEquals(airlineID, orderListRQ.getQuery().getFilters().getAirline().getAirlineID().value);
	}
}