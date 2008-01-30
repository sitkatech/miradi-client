/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.TestCaseWithProject;

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
