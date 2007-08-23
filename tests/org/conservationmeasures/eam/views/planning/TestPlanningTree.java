/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestPlanningTree extends EAMTestCase
{
	public TestPlanningTree(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
		setupFactors();
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
	}
	
	private void setupFactors()
	{
	}
	
	public void testPlanningTreeGoalNodes()
	{
		//TODO finish project
		//already asserted fail
	}
	
	ProjectForTesting project;
}
