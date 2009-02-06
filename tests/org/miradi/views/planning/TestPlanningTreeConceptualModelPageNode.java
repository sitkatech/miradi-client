/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		PlanningTreeConceptualModelPageNode node = new PlanningTreeConceptualModelPageNode(project, project.getTestingDiagramObject().getRef(), new CodeList());
		assertEquals(3, node.getChildCount());
		assertEquals(getThreat().getRef(), node.getChild(0).getObjectReference());
		assertEquals(getTarget().getRef(), node.getChild(1).getObjectReference());
		assertEquals(getStrategy2().getRef(), node.getChild(2).getObjectReference());
	}
}
