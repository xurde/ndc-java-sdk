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
public class AirShoppingRsUnmarshallingIT {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> sampleFiles() {
		return Arrays.asList(new Object[][] {
			{"/AirShoppingRS - OneWay with all Cabin.xml", 42},
			{"/AirShoppingRS - OneWay with Calendar.xml", 21},
			{"/AirShoppingRS - OneWay with Direct Flight.xml", 15},
			{"/AirShoppingRS - OneWay with multiple pax - Direct Flight.xml", 14},
			{"/AirShoppingRS - OneWay with multiple pax.xml", 20},
			{"/AirShoppingRS - OneWay with one pax.xml", 21},
			{"/AirShoppingRS - RoundTrip with all Cabin.xml", 34},
			{"/AirShoppingRS - RoundTrip with Calendar - Direct Flight.xml", 33},
			{"/AirShoppingRS - RoundTrip with Calendar.xml", 16},
			{"/AirShoppingRS - RoundTrip with Direct Flight.xml", 33},
			{"/AirShoppingRS - RoundTrip with multiple pax.xml", 16},
			{"/AirShoppingRS - RoundTrip with one pax.xml", 16}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value = 1)
	public Integer offerCount;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(AirShoppingRS.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			AirShoppingRS airShoppingRS = (AirShoppingRS) unmarshaller.unmarshal(is);
			assertEquals(offerCount.intValue(), airShoppingRS.getOffersGroup().getAirlineOffers().get(0).getTotalOfferQuantity().intValue());

		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}
