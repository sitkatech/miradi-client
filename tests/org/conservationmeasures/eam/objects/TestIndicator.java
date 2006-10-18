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

	public void testData() throws Exception
	{
		verifyTextField(ObjectType.INDICATOR, Indicator.TAG_SHORT_LABEL);
		verifyTextField(ObjectType.INDICATOR, Indicator.TAG_METHOD);
		verifyIdListField(ObjectType.INDICATOR, Indicator.TAG_RESOURCE_IDS);
		verifyTextField(ObjectType.INDICATOR, Indicator.TAG_LOCATION);
	}
}
