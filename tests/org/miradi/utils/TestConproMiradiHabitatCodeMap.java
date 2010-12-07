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

import java.util.HashMap;

import org.miradi.main.EAMTestCase;

public class TestConproMiradiHabitatCodeMap extends EAMTestCase
{
	public TestConproMiradiHabitatCodeMap(String name)
	{
		super(name);
	}
	
	public void testLoadMap() throws Exception
	{
		HashMap<String, String> habitatCodeMap = new ConproMiradiHabitatCodeMap().loadMap();
		assertTrue("map has no values?", habitatCodeMap.size() > 0);
		assertEquals("map has wrong size?", 128, habitatCodeMap.size());
		
		assertEquals("wrong code found?", "H010.000", habitatCodeMap.get("1"));
		assertEquals("wrong code found?", "H030.040", habitatCodeMap.get("3.4"));
		assertEquals("wrong code found?", "H240.000", habitatCodeMap.get("18"));
	}
}
