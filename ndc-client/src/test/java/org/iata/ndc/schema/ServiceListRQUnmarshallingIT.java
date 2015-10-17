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
public class ServiceListRQUnmarshallingIT extends AbstractUnmarshaller<ServiceListRQ> {

	@Parameters(name = "{index}: {0}")
	public static Collection<String[]> sampleFiles() {
		return Arrays.asList(new String[][] {
			{"/Athena/OneWay/ServiceListRQ.xml", "RE6ac7227515a047a581d722971b9fa28f"},
			{"/Athena/RoundTrip/ServiceListRQ.xml", "RE80143224f0c2497988fe6d1b2925569e"},
			{"/Kronos/OneWay/ServiceListRQ.xml", "RE1e8b4f83ec9f4e759191924d85127ac4"},
			{"/Kronos/RoundTrip/ServiceListRQ.xml", "REcd7f1a73480f488681ca7fc9a6c04a8d"}
		});
	}

	@Parameter
	public String resource;

	@Parameter(value=1)
	public String responseID;

	@Test
	public void unmarshal() throws JAXBException {
		ServiceListRQ serviceListRQ = unmarshal(resource);
		assertEquals(responseID, serviceListRQ.getShoppingResponseIDs().getResponseID().getValue());

	}
}