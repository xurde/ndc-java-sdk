package org.iata.iata.edist;

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
public class ServiceListRsUnmarshallingIT {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/ServiceListRS.xml", "Baggage"},
			{"/ServiceListRS-Updated(14-Aug-2015).xml", "Baggage"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String service;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(ServiceListRS.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ServiceListRS serviceListRS =  (ServiceListRS) unmarshaller.unmarshal(is);
			assertEquals(service, serviceListRS.getDataLists().getServiceList().get(0).getName().value);
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}