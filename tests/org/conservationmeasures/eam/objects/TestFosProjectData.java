/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestFosProjectData extends ObjectTestCase
{
	public TestFosProjectData(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(ObjectType.FOS_PROJECT_DATA);
	}
}
