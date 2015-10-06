package org.iata.ndc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.*;

import org.apache.http.client.ClientProtocolException;
import org.iata.iata.edist.AirShoppingRQ;
import org.iata.iata.edist.AirShoppingRS;
import org.iata.ndc.builder.AirShoppingRQBuilder;
import org.junit.Test;

public class NdcClientIT {
	private final String KRONOS = "http://kronos.jrtechnologies.com/dondc";

	@Test
	public void existingRequestAgainstServer() throws JAXBException {
		InputStream is = this.getClass().getResourceAsStream("/AirShoppingRQ - OneWay with all Cabin.xml");
		JAXBContext context = JAXBContext.newInstance(AirShoppingRQ.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AirShoppingRQ airShoppingRQ =  (AirShoppingRQ) unmarshaller.unmarshal(is);

		NdcClient client = new NdcClient(KRONOS);
		AirShoppingRS response = null;
		try {
			response = client.airShopping(airShoppingRQ);
		} catch (IOException e) {
			fail(e.toString());
		}
		assertNotNull(response);
	}

	@Test
	public void builtRequestAgainstServer() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2015, 10, 19);
		Date date = cal.getTime();

		AirShoppingRQBuilder builder = new AirShoppingRQBuilder();
		builder.addTravelAgencySender("Test sender", "00002004", "test");
		builder.addOriginDestination("CDG", "LHR", date);
		AirShoppingRQ request = builder.build();

		NdcClient client = new NdcClient(KRONOS);
		AirShoppingRS response = null;
		try {
			response = client.airShopping(request);
		} catch (ClientProtocolException e) {
			fail(e.toString());
			e.printStackTrace();
		} catch (IOException e) {
			fail(e.toString());
			e.printStackTrace();
		}
		assertNotNull(response.getSuccess());
	}

}
