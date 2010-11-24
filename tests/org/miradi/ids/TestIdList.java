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
package org.miradi.ids;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAMTestCase;
import org.miradi.objects.Cause;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.utils.EnhancedJsonObject;

public class TestIdList extends EAMTestCase
{
	public TestIdList(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		IdList list = new IdList(0);
		assertEquals("wrong initial size?", 0, list.size());
		BaseId id1 = new BaseId(7);
		BaseId id2 = new BaseId(19);
		list.add(id1);
		list.add(id2);
		assertEquals("wrong size?", 2, list.size());
		assertEquals("bad get 1?", id1, list.get(0));
		assertEquals("bad get 2?", id2, list.get(1));
	}
	
	public void testJson()
	{
		IdList list = createSampleIdList();
		EnhancedJsonObject json = list.toJson();
		
		int arbitraryObjectType = 123;
		IdList loaded = new IdList(arbitraryObjectType, json);
		assertEquals("wrong size?", list.size(), loaded.size());
		for(int i =0; i < list.size(); ++i)
			assertEquals("wrong member?", list.get(i), loaded.get(i));
	}
	
	public void testRemove()
	{
		IdList list = createSampleIdList();
		list.removeId(list.get(1));
		assertEquals(2, list.size());
		assertEquals(new BaseId(9998), list.get(1));
		
		try
		{
			list.removeId(new BaseId(3333333));
			fail("Should have thrown removing non-existant id");
		}
		catch (RuntimeException ignoreExpected)
		{
		}
		
	}
	
	public void testToString() throws Exception
	{
		IdList list = createSampleIdList();
		assertEquals("Can't rount trip?", list, new IdList(list.getObjectType(), list.toString()));
	}

	private IdList createSampleIdList()
	{
		IdList list = new IdList(0);
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
		
		IdList different = new IdList(0);
		different.add(list.get(0));
		different.add(list.get(2));
		different.add(list.get(1));
		assertNotEquals("didn't compare order?", list, different);
		assertNotEquals("didn't hash everything?", list.hashCode(), different.hashCode());
		
		assertNotEquals("didn't check type?", list, new Object());
	}
	
	public void testSubtract()
	{
		IdList list12345 = new IdList(0);
		list12345.add(1);
		list12345.add(2);
		list12345.add(3);
		list12345.add(4);
		list12345.add(5);
		
		IdList list654 = new IdList(0);
		list654.add(6);
		list654.add(5);
		list654.add(4);
		
		IdList list123 = new IdList(list12345);
		list123.subtract(list654);
		assertEquals(3, list123.size());
		assertEquals(new BaseId(1), list123.get(0));
		assertEquals(new BaseId(2), list123.get(1));
		assertEquals(new BaseId(3), list123.get(2));
		
		IdList list6 = new IdList(list654);
		list6.subtract(list12345);
		assertEquals(1, list6.size());
		assertEquals(new BaseId(6), list6.get(0));
	}
	
	public void testFind()
	{
		BaseId[] ids = new BaseId[] { new BaseId(1), new BaseId(19), new BaseId(3), };
		IdList list = new IdList(0);
		for(int i = 0; i < ids.length; ++i)
			list.add(ids[i]);
		for(int i = 0; i < ids.length; ++i)
			assertEquals("Couldn't find " + i + "?", i, list.find(ids[i]));
		assertEquals("Found non-existant?", -1, list.find(new BaseId(27)));

	}
	
	public void testIdListWithType() throws Exception
	{
		Project project = new ProjectForTesting(getName());
		IdList idListWithStrategyType = new IdList(Strategy.getObjectType());
		Strategy strategy = new Strategy(project.getObjectManager(), new FactorId(1));	
		Cause cause = new Cause(project.getObjectManager(), new FactorId(2));
		idListWithStrategyType.addRef(strategy.getRef());
		
		try 
		{
			idListWithStrategyType.addRef(cause.getRef());
			fail();
		}
		catch(Exception ignoreExpected)
		{
			
		}
		
		assertTrue("does not contain strategy?", idListWithStrategyType.contains(strategy.getRef()));
		assertFalse("does contain cause?", idListWithStrategyType.contains(cause.getRef()));
		
		assertEquals("wrong size?", 1, idListWithStrategyType.size());
		assertEquals("wrong ref?", strategy.getRef(), idListWithStrategyType.getRef(0));
		
		project.close();
	}
}
