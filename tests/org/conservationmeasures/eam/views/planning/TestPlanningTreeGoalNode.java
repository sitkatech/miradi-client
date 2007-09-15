/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Objective;

public class TestPlanningTreeGoalNode extends TestPlanningTree
{
	public TestPlanningTreeGoalNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeGoalNodes() throws Exception
	{
		ORefList objectiveRefs = getGoal().getUpstreamObjectives();
		assertEquals("wrong objective count?", 1, objectiveRefs.size());
		assertEquals("wrong type returned?", Objective.getObjectType(), objectiveRefs.get(0).getObjectType());
	}
}
