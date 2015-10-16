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
public class ServiceListRqUnmarshallingIT {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/ServiceListRQ.xml", "REae1dd9dced3b4ceb9fc57f276531a451"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String responseID;

	@Test
	public void unmarshal() {
		InputStream is = this.getClass().getResourceAsStream(resource);
		try {
			JAXBContext context = JAXBContext.newInstance(ServiceListRQ.class);
			Unmarshaller unmarshaller = context.createUnmarshaller();
			ServiceListRQ serviceListRQ =  (ServiceListRQ) unmarshaller.unmarshal(is);
			assertEquals(responseID, serviceListRQ.getShoppingResponseIDs().getResponseID().getValue());
		} catch (JAXBException e) {
			e.printStackTrace();
			fail(e.toString());
		}
	}
}