/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;


public class TestPlanningViewConfigeration extends ObjectTestCase
{
	public TestPlanningViewConfigeration(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(PlanningViewConfigeration.getObjectType());
	}
}
