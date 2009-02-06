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

import java.util.Collection;
import java.util.Set;

import org.miradi.main.EAMTestCase;

public class TestMiradiMap extends EAMTestCase
{
	public TestMiradiMap(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		Object[][] data = {{1, "one"}, {2, "two"}, {3, "three"}};
		MiradiMap map = new MiradiMap(data); 
		
		assertEquals("wrong map size?", 3, map.size());
		Set keys = map.keySet();
		assertTrue("contains key?", keys.contains(1));
		assertTrue("contains key?", keys.contains(2));
		assertTrue("contains key?", keys.contains(3));
		
		Collection values = map.values();
		assertTrue("contains value one?", values.contains("one"));
		assertTrue("contains value two?", values.contains("two"));
		assertTrue("contains value three?", values.contains("three"));
		
		Object[][] faultyData = {{1, "one", 1}, {2, "two"}};
		try
		{
			new MiradiMap(faultyData);
			fail();
		}
		catch (Exception e)
		{
		}
	}
}
