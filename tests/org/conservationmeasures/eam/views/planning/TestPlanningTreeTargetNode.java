/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTargetNode;

public class TestPlanningTreeTargetNode extends TestPlanningTree
{
	public TestPlanningTreeTargetNode(String name)
	{
		super(name);
	}

	public void testPlanningTreeTargetNode() throws Exception
	{
		PlanningTreeTargetNode node = new PlanningTreeTargetNode(project, project.getDiagramObject(), getTarget().getRef());
		assertEquals(1, node.getChildCount());
		assertEquals(getGoal().getRef(), node.getChild(0).getObjectReference());
	}
}
