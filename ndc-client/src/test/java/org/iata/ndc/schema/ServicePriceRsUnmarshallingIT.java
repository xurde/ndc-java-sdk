package org.iata.ndc.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;
import java.math.BigDecimal;
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
public class ServicePriceRsUnmarshallingIT {

	@Parameters(name = "{index}: {0}")
	public static Collection<Object[]> sampleFiles() {
		return Arrays.asList(new Object[][] {
			{"/ServicePriceRS.xml", BigDecimal.valueOf(170)}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public BigDecimal expectedAmount;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(ServicePriceRS.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ServicePriceRS servicePrice =  (ServicePriceRS) unmarshaller.unmarshal(is);
			assertEquals(expectedAmount, servicePrice.getDataLists().getServiceList().get(0).getPrice().get(0).getTotal().value);
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}