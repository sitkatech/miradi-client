/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;


public class TestObjective extends ObjectTestCase
{
	public TestObjective(String name)
	{
		super(name);
	}

	public void testData() throws Exception
	{
		verifyTextField(ObjectType.OBJECTIVE, Objective.TAG_SHORT_LABEL);
		verifyTextField(ObjectType.OBJECTIVE, Objective.TAG_FULL_TEXT);
	}
}
