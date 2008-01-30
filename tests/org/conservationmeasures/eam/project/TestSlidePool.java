/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objectpools.SlidePool;
import org.conservationmeasures.eam.objects.Slide;

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