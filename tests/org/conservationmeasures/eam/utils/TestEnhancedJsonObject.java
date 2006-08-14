/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;

import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestEnhancedJsonObject extends EAMTestCase
{
	public TestEnhancedJsonObject(String name)
	{
		super(name);
	}

	public void testColors() throws Exception
	{
		String tag = "Color";
		Color red = Color.red;
		EnhancedJsonObject json = new EnhancedJsonObject();
		assertEquals("didn't default?", Color.WHITE, json.optColor(tag, Color.WHITE));
		json.put(tag, red);
		assertEquals("didn't set/get?", red, json.getColor(tag));
		assertEquals("didn't optGet?", red, json.optColor(tag, Color.WHITE));
		
		EnhancedJsonObject got = new EnhancedJsonObject(json.toString());
		assertEquals("Didn't stringize?", json.getColor(tag), got.getColor(tag));
	}
}
