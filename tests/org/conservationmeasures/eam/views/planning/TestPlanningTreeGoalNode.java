/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeGoalNode;

public class TestPlanningTreeGoalNode extends TestPlanningTree
{
	public TestPlanningTreeGoalNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeGoalNodes() throws Exception
	{
		PlanningTreeGoalNode node = new PlanningTreeGoalNode(project, project.getDiagramObject(), getGoal().getRef());
		assertEquals("wrong child count?", 1, node.getChildCount());
		assertEquals("wrong child?", getObjective().getRef(), node.getChild(0).getObjectReference());
	}
}
