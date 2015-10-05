package org.iata.ndc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.bind.*;

import org.iata.iata.edist.AirShoppingRQ;
import org.iata.iata.edist.AirShoppingRS;
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

}
