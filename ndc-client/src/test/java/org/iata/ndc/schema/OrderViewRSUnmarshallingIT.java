package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class OrderViewRSUnmarshallingIT {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/OrderViewRS for OrderCreate - OneWay with one pax.xml", "1#C#109987187#110031494"},
			{"/OrderViewRS for OrderCreate - Roundtrip with one pax.xml", "1#M#108183266"},
			{"/OrderViewRS of OrderChange  - AddPassenger - OneWay.xml", "1#C#109987187#110031494"},
			{"/OrderViewRS of OrderChange  - AddPassenger - RoundTrip.xml", "1#M#108183266"}
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
			JAXBContext context = JAXBContext.newInstance(OrderViewRS.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			OrderViewRS orderViewRS =  (OrderViewRS) unmarshaller.unmarshal(is);
			assertEquals(offerItemID, orderViewRS.getResponse().getOrder().get(0).getOrderItems().getOrderItem().get(0).getOrderItemID().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}