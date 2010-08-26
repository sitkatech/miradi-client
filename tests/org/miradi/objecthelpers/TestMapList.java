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
package org.miradi.objecthelpers;

import java.text.ParseException;
import java.util.NoSuchElementException;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.MapList;

public class TestMapList extends EAMTestCase
{
	public TestMapList(String name)
	{
		super(name);
	}
	
	public void testBasic() throws Exception
	{
		MapList map = createTestData();
		
		String value2 = "v2"; 
		map.put("TAG1",value2);
		assertEquals("Simple get/add failed?", value2, map.get("TAG1"));
		
		map.remove("TAG1");
		try
		{
			map.get("TAG1");
			fail();
		}
		catch (NoSuchElementException e)
		{
		}
		
		assertFalse(map.contains("TAG1"));
		assertTrue(map.contains("TAG2"));
		
		assertEquals(1, map.size());
		
		map.clear();
		assertEquals(0, map.size());
	}

	public void testCompareList() throws Exception
	{
		MapList map1 = createTestData();
		MapList map2 = createTestData();
		assertEquals(map1.toString(), map2.toString());
		assertEquals(map1.hashCode(), map2.hashCode());
		
		map1.remove("TAG2");
		assertNotEquals(map1.toString(), map2.toString());
		assertNotEquals(map1.hashCode(), map2.hashCode());
	}
	

	private MapList createTestData() throws ParseException
	{
		MapList map = new MapList();
		String value = "v1"; 
		map.put("TAG1",value);
		assertEquals("Simple get/add failed?", value, map.get("TAG1"));
		map.put("TAG2",value);
		assertEquals("Simple get/add failed?", value, map.get("TAG2"));
		return map;
	}
	
}
