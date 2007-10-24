/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeDirectThreatNode;

public class TestPlanningTreeDirectThreatNode extends TestPlanningTree
{
	public TestPlanningTreeDirectThreatNode(String name)
	{
		super(name);
	}

	public void testPlanningTreeDirectThreatNodes() throws Exception
	{
		PlanningTreeDirectThreatNode node = new PlanningTreeDirectThreatNode(project, project.getDiagramObject(), getThreat().getRef());
		assertEquals("wrong child count?", 1, node.getChildCount());
		assertEquals("wrong child?", getObjective().getRef(), node.getChild(0).getObjectReference());
	}
}
