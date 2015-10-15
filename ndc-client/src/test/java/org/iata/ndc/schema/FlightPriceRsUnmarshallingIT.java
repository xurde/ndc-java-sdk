package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import javax.xml.bind.*;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

@Ignore
@RunWith(Parameterized.class)
public class FlightPriceRsUnmarshallingIT {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/FlightPriceRS - OneWay.xml", "BCN"},
			{"/FlightPriceRS - RoundTrip.xml", "ARN"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String departure;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(FlightPriceRS.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			FlightPriceRS flightPriceRS =  (FlightPriceRS) unmarshaller.unmarshal(is);

			assertEquals(departure, flightPriceRS.getDataLists().getOriginDestinationList().get(0).getDepartureCode().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}