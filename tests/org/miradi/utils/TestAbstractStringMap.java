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

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.AbstractStringMap;

abstract public class TestAbstractStringMap extends EAMTestCase
{
	public TestAbstractStringMap(String name)
	{
		super(name);
	}
	
	public void testBasics()
	{
		AbstractStringMap list = createAbstractMap();
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
	
	public void testEquals()
	{
		AbstractStringMap list = createMapWithSampleData();
		AbstractStringMap identical = createMapWithSampleData();
		assertEquals(list, identical);
		assertEquals(list.hashCode(), identical.hashCode());
		
		AbstractStringMap different = createAbstractMap();
		different.add("A", list.get("A"));
		different.add("C", list.get("C"));
		different.add("B", list.get("B"));
		assertEquals(true, list.equals(different));
		assertEquals(list.hashCode(), different.hashCode());
		assertNotEquals("didn't check type?", list, new Object());
		
		different.removeCode("A");
		assertEquals(false, list.equals(different));
		
	}
	
	protected AbstractStringMap createMapWithSampleData()
	{
		AbstractStringMap list = createAbstractMap();
		list.add("A", "RoleA");
		list.add("B", "RoleB");
		list.add("C", "RoleC");
		return list;
	}
	
	abstract protected AbstractStringMap createAbstractMap();
}
