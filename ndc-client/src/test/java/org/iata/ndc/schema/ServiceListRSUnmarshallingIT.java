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
public class ServiceListRSUnmarshallingIT extends AbstractUnmarshaller<ServiceListRS> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/ServiceListRS.xml", "Advance Seat selection"},
			{"/Athena/RoundTrip/ServiceListRS.xml", "Meal"},
			{"/Kronos/OneWay/ServiceListRS.xml", "Excess Baggage  "},
			{"/Kronos/RoundTrip/ServiceListRS.xml", "Excess Baggage  "}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String service;

	@Test
	public void unmarshal() throws JAXBException {
		ServiceListRS serviceListRS = unmarshal(resource);
		assertEquals(service, serviceListRS.getDataLists().getServiceList().get(0).getName().value);
	}
}