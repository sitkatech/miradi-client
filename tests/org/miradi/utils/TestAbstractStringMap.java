/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.MiradiTestCase;
import org.miradi.objecthelpers.AbstractStringKeyMap;

abstract public class TestAbstractStringMap extends MiradiTestCase
{
	public TestAbstractStringMap(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		AbstractStringKeyMap list = createAbstractMap();
		assertEquals("wrong initial size?", 0, list.size());
		String key1 = new String("A");
		String value1 = new String("RoleA");
		String key2 = new String("B");
		String value2 = new String("RoleB");
		list.put(key1, value1);
		list.put(key2, value2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", value1, list.get(key1));
		assertEquals("bad get 2?", value2, list.get(key2));
	}
	
	public void testEquals()
	{
		AbstractStringKeyMap list = createMapWithSampleData();
		AbstractStringKeyMap identical = createMapWithSampleData();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		AbstractStringKeyMap different = createAbstractMap();
		different.put("A", list.get("A"));
		different.put("C", list.get("C"));
		different.put("B", list.get("B"));
		assertEquals(true, list.equals(different));
		assertEquals(list.hashCode(), different.hashCode());
		assertNotEquals("didn't check type?", list, new Object());
		
		different.removeCode("A");
		assertEquals(false, list.equals(different));
	}
	
	protected AbstractStringKeyMap createMapWithSampleData()
	{
		AbstractStringKeyMap list = createAbstractMap();
		list.put("A", "RoleA");
		list.put("B", "RoleB");
		list.put("C", "RoleC");
		return list;
	}
	
	public void testRemove()
	{
		AbstractStringKeyMap list = createMapWithSampleData();
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
	
	public void testFind()
	{
		String[] values = new String[] { new String("Role1"), new String("Role19"), new String("Role3"), };
		String[] keys = new String[] { new String("1"), new String("19"), new String("3"), };
		AbstractStringKeyMap list = createAbstractMap();
		for(int i = 0; i < values.length; ++i)
			list.put(keys[i], values[i]);
		for(int i = 0; i < values.length; ++i)
			assertEquals("Couldn't find " + i + "?", values[i], list.get(keys[i]));
		assertEquals("Found non-existant?", null, list.find(new String("Role27")));
	}
	
	public void testJson()
	{
		AbstractStringKeyMap list = createMapWithSampleData();
		EnhancedJsonObject json = list.toJson();
		
		AbstractStringKeyMap loaded = createAbstractMap(json);
		assertEquals("wrong size?", list.size(), loaded.size());
		Iterator iterator = list.toHashMap().keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			assertEquals("wrong member?", list.get(key), loaded.get(key));
		}
	}

	abstract protected AbstractStringKeyMap createAbstractMap(EnhancedJsonObject json);
	
	abstract protected AbstractStringKeyMap createAbstractMap();
}
