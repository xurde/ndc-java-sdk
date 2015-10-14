package org.iata.ndc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;

import javax.xml.bind.*;

import org.iata.iata.edist.AirShoppingRQ;
import org.iata.iata.edist.AirShoppingRS;
import org.iata.ndc.builder.AirShoppingRQBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NdcClientIT {
	private static final Logger LOG = LoggerFactory.getLogger(NdcClientIT.class);
	private final String server = System.getProperty("server.url");
	private final String apiKey = System.getProperty("api.key");

	@Test
	public void serverIsSet() {
		if (server == null) {
			String msg = "System property server.uri is not set.";
			LOG.error(msg);
			fail(msg);
		}
	}

	@Test
	public void apiKeyIsSet() {
		if (server == null) {
			String msg = "System property api.key is not set.";
			LOG.error(msg);
			fail(msg);
		}
	}

	@Test
	public void existingRequestAgainstServer() throws JAXBException {
		InputStream is = this.getClass().getResourceAsStream("/AirShoppingRQ - OneWay with all Cabin.xml");
		JAXBContext context = JAXBContext.newInstance(AirShoppingRQ.class);
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AirShoppingRQ airShoppingRQ =  (AirShoppingRQ) unmarshaller.unmarshal(is);

		NdcClient client = new NdcClient(server, apiKey);
		AirShoppingRS response = null;
		try {
			response = client.airShopping(airShoppingRQ);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		assertNotNull(response.getSuccess());
	}

	@Test
	public void builtRequestAgainstServer() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2015, 10, 19);
		Date date = cal.getTime();

		AirShoppingRQBuilder builder = new AirShoppingRQBuilder();
		builder.addTravelAgencySender("Test sender", "00002004", "test");
		builder.addOriginDestination("CDG", "LHR", date, 3, 3);
		builder.addAirlinePreference("C9");
		builder.addCabinPreference("M");
		builder.addFarePreference("759");
		AirShoppingRQ request = builder.build();

		NdcClient client = new NdcClient(server, apiKey);
		AirShoppingRS response = null;
		try {
			response = client.airShopping(request);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		assertNotNull(response.getSuccess());
	}

}
