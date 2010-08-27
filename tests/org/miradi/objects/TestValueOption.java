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

import java.awt.Color;

import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ValueOption;

public class TestValueOption extends TestCaseWithProject
{
	public TestValueOption(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		BaseId id = new BaseId(42);
		ValueOption empty = new ValueOption(getObjectManager(), id);
		assertEquals("didn't set id?", id, empty.getId());
		assertEquals("wrong default label?", "", empty.getLabel());
		assertEquals("wrong default numeric?", 0, empty.getNumericValue());
		
		String label = "Hello Kitty";
		int numeric = 7;
		Color color = Color.RED;
		ValueOption a = new ValueOption(getObjectManager(), id, label, numeric, color);
		assertEquals(id, a.getId());
		assertEquals(label, a.getLabel());
		assertEquals(numeric, a.getNumericValue());
	}
	
	public void testSetGetData() throws Exception
	{
		
		verifySetGetData(ValueOption.TAG_LABEL, "Hi mom!");
		verifySetGetData(ValueOption.TAG_NUMERIC, "17");
	}
	
	public void testGetDataBadFieldTag() throws Exception
	{
		BaseId id = new BaseId(6);
		ValueOption option = new ValueOption(getObjectManager(), id);
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
		ValueOption option = new ValueOption(getObjectManager(), id);
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
		ValueOption option = new ValueOption(getObjectManager(), id);
		option.setData(tag, value);
		assertEquals(value, option.getData(tag));
	}

	public void testEquals() throws Exception
	{
		BaseId id = new BaseId(17);
		ValueOption a = new ValueOption(getObjectManager(), id, "abc", 1, Color.RED);
		ValueOption b = new ValueOption(getObjectManager(), id, "def", 2, Color.BLUE);
		ValueOption c = new ValueOption(getObjectManager(), new BaseId(id.asInt() + 1), "abc", 1, Color.RED);
		assertTrue("same id isn't equal?", a.equals(b));
		assertTrue("same id isn't equal (reversed)?", b.equals(a));
		assertFalse("not comparing id?", a.equals(c));
		assertFalse("not comparing id (reversed)?", c.equals(a));
		assertFalse("equal to some other class?", a.equals(new Object()));
	}
	
	public void testJson() throws Exception
	{
		BaseId id = new BaseId(283);
		String label = "eifjjfi";
		int numeric = -234;
		Color color = Color.GRAY;
		ValueOption option = new ValueOption(getObjectManager(), id);
		option.setData(ValueOption.TAG_LABEL, label);
		option.setData(ValueOption.TAG_NUMERIC, Integer.toString(numeric));
		option.setData(ValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
		
		ValueOption loaded = (ValueOption)BaseObject.createFromJson(getObjectManager(), option.getType(), option.toJson());
		assertEquals(option.getId(), loaded.getId());
		assertEquals(option.getLabel(), loaded.getLabel());
		assertEquals(option.getNumericValue(), loaded.getNumericValue());
		assertEquals(option.getColor(), loaded.getColor());
		
	}
}
