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
package org.miradi.utils;

import java.awt.Color;

import org.miradi.ids.BaseId;
import org.miradi.main.EAMTestCase;
import org.miradi.utils.EnhancedJsonArray;
import org.miradi.utils.EnhancedJsonObject;

public class TestEnhancedJsonObject extends EAMTestCase
{
	public TestEnhancedJsonObject(String name)
	{
		super(name);
	}
	
	public void testIds() throws Exception
	{
		BaseId two = new BaseId(2);
		BaseId four = new BaseId(4);
		
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.putId("TWO", two);
		json.putId("FOUR", four);
		assertEquals("didn't get two?", two, json.getId("TWO"));
		assertEquals("didn't get four?", four, json.getId("FOUR"));
		
		
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
	
	public void testEnhancedJsonArray() throws Exception
	{
		EnhancedJsonObject small = new EnhancedJsonObject();
		small.putId("Id", new BaseId(14));
		
		EnhancedJsonArray array = new EnhancedJsonArray();
		array.put(small);
		
		EnhancedJsonObject bigObject = new EnhancedJsonObject();
		bigObject.put("Array", array);
		
		EnhancedJsonArray gotArray = bigObject.getJsonArray("Array");
		EnhancedJsonObject gotSmall = gotArray.getJson(0);
		assertEquals(gotSmall.getId("Id"), small.getId("Id"));
	}
}
