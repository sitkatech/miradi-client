/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.TestCaseWithProject;

public class TestRatingCriterion extends TestCaseWithProject
{
	public TestRatingCriterion(String name)
	{
		super(name);
	}

	public void testSetGetData() throws Exception
	{
		
		verifySetGetData(RatingCriterion.TAG_LABEL, "Hi mom!");
	}
	
	public void testGetDataBadFieldTag()
	{
		BaseId id = new BaseId(6);
		RatingCriterion option = new RatingCriterion(getObjectManager(), id);
		try
		{
			option.getData("not a valid tag");
			fail("Should have thrown for invalid tag");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
	}
	
	public void testSetDataBadFieldTag() throws Exception
	{
		BaseId id = new BaseId(6);
		RatingCriterion option = new RatingCriterion(getObjectManager(), id);
		try
		{
			option.setData("not a valid tag", "whatever");
			fail("Should have thrown for invalid tag");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
	}

	private void verifySetGetData(String tag, String value) throws Exception
	{
		BaseId id = new BaseId(6);
		RatingCriterion option = new RatingCriterion(getObjectManager(), id);
		option.setData(tag, value);
		assertEquals(value, option.getData(tag));
	}

	public void testEquals() throws Exception
	{
		BaseId id = new BaseId(32);
		String label = "Text";
		RatingCriterion a = new RatingCriterion(getObjectManager(), id);
		a.setData(RatingCriterion.TAG_LABEL, label);
		RatingCriterion b = new RatingCriterion(getObjectManager(), id);
		b.setData(RatingCriterion.TAG_LABEL, "other label");
		RatingCriterion c = new RatingCriterion(getObjectManager(), new BaseId(id.asInt() + 1));
		c.setData(RatingCriterion.TAG_LABEL, label);
		assertEquals("id same not good enough?", a, b);
		assertNotEquals("id different still equals?", a, c);
		assertNotEquals("different type equals?", a, new Object());
	}
}
