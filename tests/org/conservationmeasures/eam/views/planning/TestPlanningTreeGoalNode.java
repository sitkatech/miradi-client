/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Goal;

public class TestPlanningTreeGoalNode extends TestPlanningTree
{
	public TestPlanningTreeGoalNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeGoalNodes() throws Exception
	{
		String objectiveChildren = getGoal().getPseudoData(Goal.PSEUDO_TAG_CHILD_OBJECTIVE_OREF_LIST);
		ORefList objectiveORefs = new ORefList(objectiveChildren);
		assertEquals("wrong objective count?", 1, objectiveORefs.size());
	}
}
