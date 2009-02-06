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

import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Task;
import org.miradi.utils.CodeList;

public class TestPlanningTreeActivityNode extends TestPlanningTree
{
	public TestPlanningTreeActivityNode(String name)
	{
		super(name);
	}
	
	public void testPlanningTreeActivityNode() throws Exception
	{
		ORefList taskRefs = getTask().getSubtaskRefs();
		assertEquals("wrong subtask count?", 1, taskRefs.size());
		assertEquals("wrong type returned?", Task.getObjectType(), taskRefs.get(0).getObjectType());
	}

	public void testMerging() throws Exception
	{
		CodeList rowCodes = new CodeList();
		rowCodes.add(Task.ACTIVITY_NAME);
		PlanningTreeRootNode root = new PlanningTreeRootNode(project, rowCodes);
		assertEquals(1, root.getChildCount());

		final TreeTableNode firstChild = root.getChild(0);
		assertEquals(Task.getObjectType(), firstChild.getType());
		assertEquals(2, firstChild.getProportionShares());
		assertFalse("Full proportion task is allocated?", firstChild.areBudgetValuesAllocated());
	}

}
