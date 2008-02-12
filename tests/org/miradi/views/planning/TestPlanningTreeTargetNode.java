/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.dialogs.planning.treenodes.PlanningTreeTargetNode;

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
