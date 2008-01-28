/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.util.Iterator;

import org.conservationmeasures.eam.main.EAMTestCase;

public class TestStringMapData  extends EAMTestCase
{
	public TestStringMapData(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		StringMapData list = new StringMapData("tag");
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
		StringMapData list = createSampleStringMapData();
		EnhancedJsonObject json = list.toJson();
		
		StringMapData loaded = new StringMapData("tag", json);
		assertEquals("wrong size?", list.size(), loaded.size());
		Iterator iterator = list.data.keySet().iterator();
		while (iterator.hasNext())
		{
			String key = (String)iterator.next();
			assertEquals("wrong member?", list.get(key), loaded.get(key));
		}
	}
	
	public void testRemove()
	{
		StringMapData list = createSampleStringMapData();
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
		StringMapData list = createSampleStringMapData();
		assertEquals("Can't rount trip?", list, new StringMapData("tag", list));
	}

	private StringMapData createSampleStringMapData()
	{
		StringMapData list = new StringMapData("tag");
		list.add("A", "RoleA");
		list.add("B", "RoleB");
		list.add("C", "RoleC");
		return list;
	}

	public void testEquals()
	{
		StringMapData list = createSampleStringMapData();
		StringMapData identical = createSampleStringMapData();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		StringMapData different = new StringMapData("tag");
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
		StringMapData list = new StringMapData("tag");
		for(int i = 0; i < values.length; ++i)
			list.add(keys[i], values[i]);
		for(int i = 0; i < values.length; ++i)
			assertEquals("Couldn't find " + i + "?", values[i], list.get(keys[i]));
		assertEquals("Found non-existant?", null, list.find(new String("Role27")));
	}
}

