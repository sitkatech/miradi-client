/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeObjectiveNode;


public class TestPlanningTreeObjectiveNode extends TestPlanningTree
{
	public TestPlanningTreeObjectiveNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeObjectiveNode() throws Exception
	{	
		PlanningTreeObjectiveNode node = new PlanningTreeObjectiveNode(project, project.getDiagramObject(), getObjective().getRef());
		assertEquals("wrong child count?", 2, node.getChildCount());
		assertEquals("wrong child?", getStrategy().getRef(), node.getChild(0).getObjectReference());
		assertEquals("wrong child?", getIndicator().getRef(), node.getChild(1).getObjectReference());
	}
}
