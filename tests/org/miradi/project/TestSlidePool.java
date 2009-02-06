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