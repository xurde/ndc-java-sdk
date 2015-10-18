package org.iata.ndc;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import javax.xml.bind.*;

import org.iata.ndc.builder.*;
import org.iata.ndc.builder.element.DataListBuilder;
import org.iata.ndc.builder.element.PartyBuilder;
import org.iata.ndc.schema.*;
import org.iata.ndc.schema.AirShoppingRS.DataLists;
import org.iata.ndc.schema.DataListType.Flight;
import org.junit.*;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NdcClientIT {
	private static final Logger LOG = LoggerFactory.getLogger(NdcClientIT.class);
	private final String server = System.getProperty("server.url");
	private final String apiKey = System.getProperty("api.key");
	private final NdcClient client = new NdcClient(server, apiKey);

	private MsgPartiesType party;
	private static AirShoppingRS airSoppingRS;

	@Before
	public void before() {
		party = new PartyBuilder()
				.setTravelAgencySender("Test sender", "00002004", "test")
				.build();

	}

	@Test
	public void a_serverIsSet() {
		if (server == null) {
			String msg = "System property server.uri is not set.";
			LOG.error(msg);
			fail(msg);
		}
	}

	@Test
	public void b_apiKeyIsSet() {
		if (server == null) {
			String msg = "System property api.key is not set.";
			LOG.error(msg);
			fail(msg);
		}
	}

	@Test
	public void c_existingAirShoppingRQ() throws JAXBException {
		InputStream is = this.getClass().getResourceAsStream("/Kronos/OneWay/AirShoppingRQ - OneWay with one pax.xml");
		JAXBContext context = JAXBContext.newInstance("org.iata.ndc.schema");
		Unmarshaller unmarshaller = context.createUnmarshaller();
		AirShoppingRQ airShoppingRQ =  (AirShoppingRQ) unmarshaller.unmarshal(is);

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
	public void d_builtAirShoppingRQ() {
		Calendar cal = Calendar.getInstance();
		cal.clear();
		cal.set(2015, 10, 19);
		Date date = cal.getTime();

		AirShoppingRQ request = new AirShoppingRQBuilder()
				.setParty(party)
				.addOriginDestination("CDG", "LHR", date, 3, 3)
				.addAirlinePreference("C9")
				.addCabinPreference("M")
				.addFarePreference("759")
				.build();

		AirShoppingRS response = null;
		try {
			response = client.airShopping(request);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		airSoppingRS = response;
		assertNotNull(response.getSuccess());
	}

	@Test
	public void e_builtSeatAvailabilityRQ() {
		DataLists lists = airSoppingRS.getDataLists();
		OriginDestination originDestination = lists.getOriginDestinationList().get(0);
		Flight flight = DataListBuilder
				.getObjectListFromReferenceList(originDestination.getFlightReferences().getValue(), Flight.class).get(0);

		DataListType dataList = new DataListBuilder()
				.setDataListForSeatAvailability(originDestination, flight, "M")
				.build();

		SeatAvailabilityRQ request = new SeatAvailabilityRQBuilder()
				.setParty(party)
				.setDataList(dataList)
				.addOriginDestinationToQuery(dataList.getOriginDestinationList().get(0))
				.build();

		SeatAvailabilityRS response = null;
		try {
			response = client.seatAvailability(request);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		processErrors(response.getErrors());
		assertNotNull(response.getSuccess());
	}

	@Test
	public void f_serviceListRQ() {
		String shoppingResponseId = airSoppingRS.getShoppingResponseIDs().getResponseID().getValue();
		ServiceListRQ request = new ServiceListRQBuilder()
				.setParty(party)
				.setShoppingResponseId(shoppingResponseId)
				.build();
		ServiceListRS response = null;
		try {
			response = client.serviceList(request);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		processErrors(response.getErrors());
		assertNotNull(response.getSuccess());
	}

	@Test
	public void g_servicePriceRQ() {
		String shoppingResponseId = airSoppingRS.getShoppingResponseIDs().getResponseID().getValue();
		ServicePriceRQ request = new ServicePriceRQBuilder()
				.setParty(party)
				.setShoppingResponseId(shoppingResponseId)
				.build();
		ServicePriceRS response = null;
		try {
			response = client.servicePrice(request);
		} catch (IOException e) {
			LOG.error("Unexpected exception encountered during service call", e);
			fail(e.toString());
		}
		processErrors(response.getErrors());
		assertNotNull(response.getSuccess());
	}



	private void processErrors(List<ErrorType> errors) {
		if (errors == null || errors.isEmpty()) {
			return;
		}
		String message = "";
		for (ErrorType e : errors) {
			if (e.getValue() != null && !e.getValue().isEmpty()) {
				message += e.getValue();
				LOG.error(e.getValue());
			}
			if (e.getShortText() != null && !e.getShortText().isEmpty()) {
				message += e.getShortText();
				LOG.error(e.getShortText());
			}
		}
		fail("Server returned errors");
	}

}
