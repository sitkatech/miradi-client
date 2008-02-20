/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning;

import org.miradi.dialogs.planning.treenodes.PlanningTreeConceptualModelPageNode;
import org.miradi.utils.CodeList;

public class TestPlanningTreeConceptualModelPageNode extends TestPlanningTree
{
	public TestPlanningTreeConceptualModelPageNode(String name)
	{
		super(name);
	}

	public void testPlanningTreeConceptualModelPageNode() throws Exception
	{
		PlanningTreeConceptualModelPageNode node = new PlanningTreeConceptualModelPageNode(project, project.getDiagramObject().getRef(), new CodeList());
		assertEquals(1, node.getChildCount());
		assertEquals(getTarget().getRef(), node.getChild(0).getObjectReference());
	}
}
