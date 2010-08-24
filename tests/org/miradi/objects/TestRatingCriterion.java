/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.RatingCriterion;

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
