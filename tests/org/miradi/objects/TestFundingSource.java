/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.FundingSource;
import org.miradi.project.ProjectForTesting;

public class TestFundingSource extends ObjectTestCase
{
	public TestFundingSource(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		project.close();
		project = null;
		super.tearDown();
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
		FundingSource fundingSource = new FundingSource(getObjectManager(), new BaseId(22));
		assertEquals(tag + " didn't default properly?", "", fundingSource.getData(tag));
		fundingSource.setData(tag, value);
		FundingSource got = (FundingSource)FundingSource.createFromJson(project.getObjectManager(), fundingSource.getType(), fundingSource.toJson());
		assertEquals(tag + " didn't survive json?", fundingSource.getData(tag), got.getData(tag));
	}
	
	ProjectForTesting project;
}
