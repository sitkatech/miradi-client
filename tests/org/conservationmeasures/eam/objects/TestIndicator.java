/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.objecthelpers.ObjectType;


public class TestIndicator extends ObjectTestCase
{
	public TestIndicator(String name)
	{
		super(name);
	}

	public void testFields() throws Exception
	{
		verifyFields(ObjectType.INDICATOR);
	}
}
