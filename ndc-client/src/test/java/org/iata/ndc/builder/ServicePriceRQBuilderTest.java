package org.iata.ndc.builder;

import static org.junit.Assert.assertEquals;

import org.iata.ndc.schema.ObjectFactory;
import org.iata.ndc.schema.ServicePriceRQ;
import org.junit.Before;
import org.junit.Test;

public class ServicePriceRQBuilderTest {
	private static final ObjectFactory factory = new ObjectFactory();
	private ServicePriceRQBuilder testedClass;


	@Before
	public void setUp() {
		testedClass = new ServicePriceRQBuilder();
	}

	@Test
	public void documentNode() {
		ServicePriceRQ request = testedClass.build();
		assertEquals("1.0", request.getDocument().getReferenceVersion());
		assertEquals("NDC ServicePriceRQ Message", request.getDocument().getName());
	}

	@Test
	public void setShoppingResponseId() {
		ServicePriceRQ request = testedClass.setShoppingResponseId("responseID").build();
		assertEquals("responseID", request.getShoppingResponseIDs().getResponseID().getValue());
	}
}
