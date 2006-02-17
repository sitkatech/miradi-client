/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import java.awt.Color;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestThreatRatingValueOption extends EAMTestCase
{
	public TestThreatRatingValueOption(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		int id = 42;
		ThreatRatingValueOption empty = new ThreatRatingValueOption(id);
		assertEquals("didn't set id?", id, empty.getId());
		assertEquals("wrong default label?", "Unknown", empty.getLabel());
		assertEquals("wrong default numeric?", 0, empty.getNumericValue());
		assertEquals("wrong default color?", Color.BLACK, empty.getColor());
		
		String label = "Hello Kitty";
		int numeric = 7;
		Color color = Color.RED;
		ThreatRatingValueOption a = new ThreatRatingValueOption(id, label, numeric, color);
		assertEquals(id, a.getId());
		assertEquals(label, a.getLabel());
		assertEquals(numeric, a.getNumericValue());
		assertEquals(color, a.getColor());
	}
	
	public void testSetGetData()
	{
		
		verifySetGetData(ThreatRatingValueOption.TAG_LABEL, "Hi mom!");
		verifySetGetData(ThreatRatingValueOption.TAG_NUMERIC, "17");
		String color = Integer.toString(Color.CYAN.getRGB());
		verifySetGetData(ThreatRatingValueOption.TAG_COLOR, color);
	}
	
	public void testGetDataBadFieldTag()
	{
		int id = 6;
		ThreatRatingValueOption option = new ThreatRatingValueOption(id);
		try
		{
			option.getData("not a valid tag");
			fail("Should have thrown for invalid tag");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
	}
	
	public void testSetDataBadFieldTag()
	{
		int id = 6;
		ThreatRatingValueOption option = new ThreatRatingValueOption(id);
		try
		{
			option.setData("not a valid tag", "whatever");
			fail("Should have thrown for invalid tag");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
	}

	private void verifySetGetData(String tag, String value)
	{
		int id = 6;
		ThreatRatingValueOption option = new ThreatRatingValueOption(id);
		option.setData(tag, value);
		assertEquals(value, option.getData(tag));
	}

	public void testEquals() throws Exception
	{
		int id = 17;
		ThreatRatingValueOption a = new ThreatRatingValueOption(id, "abc", 1, Color.RED);
		ThreatRatingValueOption b = new ThreatRatingValueOption(id, "def", 2, Color.BLUE);
		ThreatRatingValueOption c = new ThreatRatingValueOption(id + 1, "abc", 1, Color.RED);
		assertTrue("same id isn't equal?", a.equals(b));
		assertTrue("same id isn't equal (reversed)?", b.equals(a));
		assertFalse("not comparing id?", a.equals(c));
		assertFalse("not comparing id (reversed)?", c.equals(a));
		assertFalse("equal to some other class?", a.equals(new Object()));
	}
	
	public void testJson() throws Exception
	{
		int id = 283;
		String label = "eifjjfi";
		int numeric = -234;
		Color color = Color.GRAY;
		ThreatRatingValueOption option = new ThreatRatingValueOption(id);
		option.setData(ThreatRatingValueOption.TAG_LABEL, label);
		option.setData(ThreatRatingValueOption.TAG_NUMERIC, Integer.toString(numeric));
		option.setData(ThreatRatingValueOption.TAG_COLOR, Integer.toString(color.getRGB()));
		
		ThreatRatingValueOption loaded = new ThreatRatingValueOption(option.toJson());
		assertEquals(option.getId(), loaded.getId());
		assertEquals(option.getLabel(), loaded.getLabel());
		assertEquals(option.getNumericValue(), loaded.getNumericValue());
		assertEquals(option.getColor(), loaded.getColor());
		
	}
	
}
