/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.dialogs.planning.treenodes.PlanningTreeGoalNode;
import org.miradi.utils.CodeList;

public class TestPlanningTreeGoalNode extends TestPlanningTree
{
	public TestPlanningTreeGoalNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeGoalNodes() throws Exception
	{
		PlanningTreeGoalNode node = new PlanningTreeGoalNode(project, project.getDiagramObject(), getGoal().getRef(), new CodeList());
		assertEquals("wrong child count?", 1, node.getChildCount());
		assertEquals("wrong child?", getThreat().getRef(), node.getChild(0).getObjectReference());
	}
}
