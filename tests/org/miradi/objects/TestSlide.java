/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.objects;

import org.miradi.objecthelpers.ObjectType;

public class TestSlide extends ObjectTestCase
{
	public TestSlide(String name)
	{
		super(name);
	}

	public void testCreateCommandsToClear() throws Exception
	{
		verifyFields(ObjectType.SLIDE);
	}
}
