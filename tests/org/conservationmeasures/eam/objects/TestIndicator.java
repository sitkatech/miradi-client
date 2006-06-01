/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestIndicator extends EAMTestCase
{
	public TestIndicator(String name)
	{
		super(name);
	}

	public void testData() throws Exception
	{
		final String sampleIdentifier = "17";
		Indicator indicator = new Indicator(29);
		assertEquals("didn't default Indentifier blank?", "", indicator.getData(Indicator.TAG_IDENTIFIER));
		indicator.setData(Indicator.TAG_IDENTIFIER, sampleIdentifier);
		assertEquals("did't set Identifier?", sampleIdentifier, indicator.getData(Indicator.TAG_IDENTIFIER));
		Indicator got = new Indicator(indicator.toJson());
		assertEquals("didn't jsonize Identifier?", indicator.getData(Indicator.TAG_IDENTIFIER), got.getData(Indicator.TAG_IDENTIFIER));
	}
}
