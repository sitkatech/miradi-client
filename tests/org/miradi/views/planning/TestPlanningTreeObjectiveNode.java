/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.dialogs.planning.treenodes.PlanningTreeObjectiveNode;
import org.miradi.utils.CodeList;


public class TestPlanningTreeObjectiveNode extends TestPlanningTree
{
	public TestPlanningTreeObjectiveNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeObjectiveNode() throws Exception
	{	
		PlanningTreeObjectiveNode node = new PlanningTreeObjectiveNode(project, project.getDiagramObject(), getObjective().getRef(), new CodeList());
		assertEquals("wrong child count?", 2, node.getChildCount());
		assertEquals("wrong child?", getStrategy().getRef(), node.getChild(0).getObjectReference());
		assertEquals("wrong child?", getIndicator().getRef(), node.getChild(1).getObjectReference());
	}
}
