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
		verifyTextField(type, Indicator.TAG_SHORT_LABEL);
		verifyTextField(type, Indicator.TAG_METHOD);
		verifyIdListField(type, Indicator.TAG_RESOURCE_IDS);
		verifyTextField(type, Indicator.TAG_LOCATION);
		verifyRatingField(type, Indicator.TAG_PRIORITY);
		//verifyTextField(type, Indicator.TAG_FUNDING_SOURCE);
	}

	private static final int type = ObjectType.INDICATOR;

}
