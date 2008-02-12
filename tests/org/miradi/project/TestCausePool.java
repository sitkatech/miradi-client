/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.CausePool;

public class TestCausePool extends TestFactorPool
{
	public TestCausePool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getCausePool();
	}
	
	public void testBasics() throws Exception
	{
		super.testBasics();
		assertEquals("wrong direct threat count?", 0, pool.getDirectThreats().length);
	}
	
	public int getObjectType()
	{
		return ObjectType.CAUSE;
	}
	
	public void testDirectThreats() throws Exception
	{
		project.createThreat();
		assertEquals("wrong direct threat count?", 1, pool.getDirectThreats().length);
	}
	
	CausePool pool;
}
