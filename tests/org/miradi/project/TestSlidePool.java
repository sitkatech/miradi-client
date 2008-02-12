/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.project;

import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objectpools.SlidePool;
import org.miradi.objects.Slide;

public class TestSlidePool extends EAMTestCase
{
	public TestSlidePool(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		pool = (SlidePool) project.getPool(ObjectType.SLIDE);
	}
	
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testBasics() throws Exception
	{
		assertEquals("wrong direct threat count?", 0, pool.size());
	}

	public void testSlide() throws Exception
	{
		project.createObjectAndReturnId(Slide.getObjectType());
		assertEquals("wrong direct threat count?", 1, pool.size());
	}
	
	SlidePool pool;
	ProjectForTesting project;
}