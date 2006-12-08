/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestFundingSource extends ObjectTestCase
{
	public TestFundingSource(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.FUNDING_SOURCE);
		verifyTagBehavior(FundingSource.TAG_LABEL);
		verifyTagBehavior(FundingSource.TAG_LABEL);
		verifyTagBehavior(FundingSource.TAG_COMMENTS);
	}

	private void verifyTagBehavior(String tag) throws Exception
	{
		String value = "ifislliefj";
		FundingSource fundingSource = new FundingSource(new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", fundingSource.getData(tag));
		fundingSource.setData(tag, value);
		FundingSource got = (FundingSource)FundingSource.createFromJson(fundingSource.getType(), fundingSource.toJson());
		assertEquals(tag + " didn't survive json?", fundingSource.getData(tag), got.getData(tag));
	}
}
