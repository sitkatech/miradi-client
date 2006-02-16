/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestThreatRatingCriterion extends EAMTestCase
{
	public TestThreatRatingCriterion(String name)
	{
		super(name);
	}

	public void testEquals() throws Exception
	{
		int id = 32;
		String label = "Text";
		ThreatRatingCriterion a = new ThreatRatingCriterion(id, label);
		ThreatRatingCriterion b = new ThreatRatingCriterion(id, "other label");
		ThreatRatingCriterion c = new ThreatRatingCriterion(id + 1, label);
		assertEquals("id same not good enough?", a, b);
		assertNotEquals("id different still equals?", a, c);
		assertNotEquals("different type equals?", a, new Object());
	}
}
