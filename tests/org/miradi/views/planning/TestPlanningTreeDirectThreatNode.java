/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.dialogs.planning.treenodes.PlanningTreeDirectThreatNode;
import org.miradi.utils.CodeList;

public class TestPlanningTreeDirectThreatNode extends TestPlanningTree
{
	public TestPlanningTreeDirectThreatNode(String name)
	{
		super(name);
	}

	public void testPlanningTreeDirectThreatNodes() throws Exception
	{
		PlanningTreeDirectThreatNode node = new PlanningTreeDirectThreatNode(project, project.getDiagramObject(), getThreat().getRef(), new CodeList());
		assertEquals("wrong child count?", 1, node.getChildCount());
		assertEquals("wrong child?", getObjective().getRef(), node.getChild(0).getObjectReference());
	}
}
