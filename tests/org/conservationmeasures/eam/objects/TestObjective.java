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
		verifyField(Objective.TAG_SHORT_LABEL);
		verifyField(Objective.TAG_FULL_TEXT);
	}
	
	public void verifyField(String tag) throws Exception
	{
		final String sampleData = "Blah blah";
		Objective objective = new Objective(929);
		assertEquals("didn't default " + tag + " blank?", "", objective.getData(tag));
		objective.setData(tag, sampleData);
		assertEquals("did't set " + tag + "?", sampleData, objective.getData(tag));
		Objective got = new Objective(objective.toJson());
		assertEquals("didn't jsonize " + tag + "?", objective.getData(tag), got.getData(tag));

	}
}
