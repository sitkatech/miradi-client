/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.GroupBoxPool;

public class TestGroupBoxPool extends TestFactorPool
{
	public TestGroupBoxPool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		pool = project.getGroupBoxPool();
	}
	
	public int getObjectType()
	{
		return ObjectType.GROUP_BOX;
	}

	GroupBoxPool pool;
}
