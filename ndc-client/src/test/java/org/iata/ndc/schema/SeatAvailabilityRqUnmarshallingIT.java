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
public class SeatAvailabilityRqUnmarshallingIT {

	@Parameters(name = "{index}: {0} {1}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/SeatAvailabilityRQ- Oneway.xml", "BCN"}
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
			JAXBContext context = JAXBContext.newInstance(SeatAvailabilityRQ.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			SeatAvailabilityRQ seatAvailabilityRQ =  (SeatAvailabilityRQ) unmarshaller.unmarshal(is);
			assertEquals(departure, seatAvailabilityRQ.getDataList().getFlightSegmentList().get(0).getDeparture().getAirportCode().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}