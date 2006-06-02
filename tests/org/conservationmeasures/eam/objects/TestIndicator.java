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
		final String sampleShortLabel = "17";
		Indicator indicator = new Indicator(29);
		assertEquals("didn't default Indentifier blank?", "", indicator.getData(Indicator.TAG_SHORT_LABEL));
		indicator.setData(Indicator.TAG_SHORT_LABEL, sampleShortLabel);
		assertEquals("did't set ShortLabel?", sampleShortLabel, indicator.getData(Indicator.TAG_SHORT_LABEL));
		Indicator got = new Indicator(indicator.toJson());
		assertEquals("didn't jsonize ShortLabel?", indicator.getData(Indicator.TAG_SHORT_LABEL), got.getData(Indicator.TAG_SHORT_LABEL));
	}
}
