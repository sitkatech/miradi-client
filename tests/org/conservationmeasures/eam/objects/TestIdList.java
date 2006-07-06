/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.json.JSONObject;

public class TestIdList extends EAMTestCase
{
	public TestIdList(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		IdList list = new IdList();
		assertEquals("wrong initial size?", 0, list.size());
		int id1 = 7;
		int id2 = 19;
		list.add(id1);
		list.add(id2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", id1, list.get(0));
		assertEquals("bad get 2?", id2, list.get(1));
	}
	
	public void testJson()
	{
		IdList list = createSampleIdList();
		JSONObject json = list.toJson();
		
		IdList loaded = new IdList(json);
		assertEquals("wrong size?", list.size(), loaded.size());
		for(int i =0; i < list.size(); ++i)
			assertEquals("wrong member?", list.get(i), loaded.get(i));
	}
	
	public void testRemove()
	{
		IdList list = createSampleIdList();
		list.removeId(list.get(1));
		assertEquals(2, list.size());
		assertEquals(9998, list.get(1));
		
		try
		{
			list.removeId(3333333);
			fail("Should have thrown removing non-existant id");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
		
	}
	
	public void testToString() throws Exception
	{
		IdList list = createSampleIdList();
		assertEquals("Can't rount trip?", list, new IdList(list.toString()));
	}

	private IdList createSampleIdList()
	{
		IdList list = new IdList();
		list.add(25);
		list.add(13);
		list.add(9998);
		return list;
	}
	
	public void testEquals()
	{
		IdList list = createSampleIdList();
		IdList identical = createSampleIdList();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		IdList different = new IdList();
		different.add(list.get(0));
		different.add(list.get(2));
		different.add(list.get(1));
		assertNotEquals("didn't compare order?", list, different);
		assertNotEquals("didn't hash everything?", list.hashCode(), different.hashCode());
		
		assertNotEquals("didn't check type?", list, new Object());
	}
	
	public void testSubtract()
	{
		IdList list12345 = new IdList();
		list12345.add(1);
		list12345.add(2);
		list12345.add(3);
		list12345.add(4);
		list12345.add(5);
		
		IdList list654 = new IdList();
		list654.add(6);
		list654.add(5);
		list654.add(4);
		
		IdList list123 = new IdList(list12345);
		list123.subtract(list654);
		assertEquals(3, list123.size());
		assertEquals(1, list123.get(0));
		assertEquals(2, list123.get(1));
		assertEquals(3, list123.get(2));
		
		IdList list6 = new IdList(list654);
		list6.subtract(list12345);
		assertEquals(1, list6.size());
		assertEquals(6, list6.get(0));
	}
}
