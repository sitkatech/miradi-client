/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestObjective extends EAMTestCase
{
	public TestObjective(String name)
	{
		super(name);
	}

	public void testData() throws Exception
	{
		final String sampleShortLabel = "2b";
		Objective objective = new Objective(929);
		assertEquals("didn't default Indentifier blank?", "", objective.getData(Objective.TAG_SHORT_LABEL));
		objective.setData(Objective.TAG_SHORT_LABEL, sampleShortLabel);
		assertEquals("did't set ShortLabel?", sampleShortLabel, objective.getData(Objective.TAG_SHORT_LABEL));
		Objective got = new Objective(objective.toJson());
		assertEquals("didn't jsonize ShortLabel?", objective.getData(Objective.TAG_SHORT_LABEL), got.getData(Objective.TAG_SHORT_LABEL));

	}
}
