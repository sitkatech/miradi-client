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

import org.miradi.main.EAMTestCase;
import org.miradi.utils.CodeList;
import org.miradi.utils.EnhancedJsonObject;

public class TestCodeList extends EAMTestCase
{
	public TestCodeList(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		CodeList list = new CodeList();
		assertEquals("wrong initial size?", 0, list.size());
		String id1 = new String("RoleA");
		String id2 = new String("RoleB");
		list.add(id1);
		list.add(id2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", id1, list.get(0));
		assertEquals("bad get 2?", id2, list.get(1));
	}
	
	public void testJson()
	{
		CodeList list = createSampleCodeList();
		EnhancedJsonObject json = list.toJson();
		
		CodeList loaded = new CodeList(json);
		assertEquals("wrong size?", list.size(), loaded.size());
		for(int i =0; i < list.size(); ++i)
			assertEquals("wrong member?", list.get(i), loaded.get(i));
	}
	
	public void testRemove()
	{
		CodeList list = createSampleCodeList();
		list.removeCode(list.get(1));
		assertEquals(2, list.size());
		assertEquals("RoleC", list.get(1));
		
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
		CodeList list = createSampleCodeList();
		assertEquals("Can't rount trip?", list, new CodeList(list.toString()));
	}

	private CodeList createSampleCodeList()
	{
		CodeList list = new CodeList();
		list.add("RoleA");
		list.add("RoleB");
		list.add("RoleC");
		return list;
	}
	
	public void testEquals()
	{
		CodeList list = createSampleCodeList();
		CodeList identical = createSampleCodeList();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		CodeList different = new CodeList();
		different.add(list.get(0));
		different.add(list.get(2));
		different.add(list.get(1));
		assertNotEquals("didn't compare order?", list, different);
		assertNotEquals("didn't hash everything?", list.hashCode(), different.hashCode());
		
		assertNotEquals("didn't check type?", list, new Object());
	}
	
	public void testSubtract()
	{
		CodeList list12345 = new CodeList();
		list12345.add("Role1");
		list12345.add("Role2");
		list12345.add("Role3");
		list12345.add("Role4");
		list12345.add("Role5");
		
		CodeList list654 = new CodeList();
		list654.add("Role6");
		list654.add("Role5");
		list654.add("Role4");
		
		CodeList list123 = new CodeList(list12345);
		list123.subtract(list654);
		assertEquals(3, list123.size());
		assertEquals(new String("Role1"), list123.get(0));
		assertEquals(new String("Role2"), list123.get(1));
		assertEquals(new String("Role3"), list123.get(2));
		
		CodeList list6 = new CodeList(list654);
		list6.subtract(list12345);
		assertEquals(1, list6.size());
		assertEquals(new String("Role6"), list6.get(0));
	}
	
	public void testFind()
	{
		String[] ids = new String[] { new String("Role1"), new String("Role19"), new String("Role3"), };
		CodeList list = new CodeList();
		for(int i = 0; i < ids.length; ++i)
			list.add(ids[i]);
		for(int i = 0; i < ids.length; ++i)
			assertEquals("Couldn't find " + i + "?", i, list.find(ids[i]));
		assertEquals("Found non-existant?", -1, list.find(new String("Role27")));

	}
}

