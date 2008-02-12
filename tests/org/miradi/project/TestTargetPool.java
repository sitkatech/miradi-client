/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.TargetPool;

public class TestTargetPool extends TestFactorPool
{
	public TestTargetPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getTargetPool();
	}
	
	public int getObjectType()
	{
		return ObjectType.TARGET;
	}

	TargetPool pool;
}
