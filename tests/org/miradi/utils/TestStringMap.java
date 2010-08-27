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

import java.util.Iterator;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.StringMap;
import org.miradi.utils.EnhancedJsonObject;

public class TestStringMap  extends EAMTestCase
{
	public TestStringMap(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		StringMap list = new StringMap();
		assertEquals("wrong initial size?", 0, list.size());
		String key1 = new String("A");
		String value1 = new String("RoleA");
		String key2 = new String("B");
		String value2 = new String("RoleB");
		list.add(key1, value1);
		list.add(key2, value2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", value1, list.get(key1));
		assertEquals("bad get 2?", value2, list.get(key2));
	}
	
	public void testJson()
	{
		StringMap list = createSampleStringMap();
		EnhancedJsonObject json = list.toJson();
		
		StringMap loaded = new StringMap(json);
		assertEquals("wrong size?", list.size(), loaded.size());
		Iterator iterator = list.toHashMap().keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			assertEquals("wrong member?", list.get(key), loaded.get(key));
		}
	}
	
	public void testRemove()
	{
		StringMap list = createSampleStringMap();
		list.removeCode("A");
		assertEquals(2, list.size());
		assertEquals("RoleC", list.get("C"));
		
		try
		{
			list.removeCode("RolaB");
			fail("Should have thrown removing non-existant id");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
	}
	
	public void testToString() throws Exception
	{
		StringMap list = createSampleStringMap();
		assertEquals("Can't rount trip?", list, new StringMap(list));
	}

	private StringMap createSampleStringMap()
	{
		StringMap list = new StringMap();
		list.add("A", "RoleA");
		list.add("B", "RoleB");
		list.add("C", "RoleC");
		return list;
	}

	public void testEquals()
	{
		StringMap list = createSampleStringMap();
		StringMap identical = createSampleStringMap();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		StringMap different = new StringMap();
		different.add("A", list.get("A"));
		different.add("C", list.get("C"));
		different.add("B", list.get("B"));
		assertEquals(true, list.equals(different));
		assertEquals(list.hashCode(), different.hashCode());
		assertNotEquals("didn't check type?", list, new Object());
		
		different.removeCode("A");
		assertEquals(false, list.equals(different));
		
	}
	

	public void testFind()
	{
		String[] values = new String[] { new String("Role1"), new String("Role19"), new String("Role3"), };
		String[] keys = new String[] { new String("1"), new String("19"), new String("3"), };
		StringMap list = new StringMap();
		for(int i = 0; i < values.length; ++i)
			list.add(keys[i], values[i]);
		for(int i = 0; i < values.length; ++i)
			assertEquals("Couldn't find " + i + "?", values[i], list.get(keys[i]));
		assertEquals("Found non-existant?", null, list.find(new String("Role27")));
	}
}

