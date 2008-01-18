/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;


public class TestPlanningViewConfiguration extends ObjectTestCase
{
	public TestPlanningViewConfiguration(String name)
	{
		super(name);
	}
	
	public void testFields() throws Exception
	{
		verifyFields(PlanningViewConfiguration.getObjectType());
	}
}
