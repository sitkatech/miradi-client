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
package org.miradi.dialogs.planning.treenodes;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{
	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		task = (Task)project.findObject(taskRef);
		proportionShares = 1;
		
		rebuild();
	}

	@Override
	public void rebuild() throws Exception
	{
		buildResourceAssignmentNodes(task.getAssignmentRefs());
		buildExpenseAssignmentNodes(task.getExpenseRefs());
		buildTaskNodes();
	}

	private void buildExpenseAssignmentNodes(ORefList expenseAssignmentRefs) throws Exception
	{
		// NOTE: Speed optimization
		if(!visibleRows.contains(ExpenseAssignment.OBJECT_NAME))
			return;

		for (int index = 0; index < expenseAssignmentRefs.size(); ++index)
		{
			children.add(new PlanningTreeExpenseAssignmentNode(project, expenseAssignmentRefs.get(index), visibleRows));
		}
	}

	private void buildResourceAssignmentNodes(ORefList assignmentRefs) throws Exception
	{
		// NOTE: Speed optimization
		if(!visibleRows.contains(ResourceAssignment.OBJECT_NAME))
			return;

		for (int index = 0; index < assignmentRefs.size(); ++index)
		{
			children.add(new PlanningTreeResourceAssignmentNode(project, assignmentRefs.get(index), visibleRows));
		}
	}

	private void buildTaskNodes() throws Exception
	{
		// NOTE: Speed optimization
		if(!visibleRows.contains(Task.OBJECT_NAME))
			return;
		
		ORefList subtaskRefs = task.getSubtaskRefs();
		for(int i = 0; i < subtaskRefs.size(); ++i)
		{
			ORef taskRef = subtaskRefs.get(i);
			children.add(new PlanningTreeTaskNode(project, taskRef, visibleRows));
		}
	}

	public BaseObject getObject()
	{
		return task;
	}
	
	public Task getTask()
	{
		return (Task) getObject();
	}
	
	@Override
	boolean shouldSortChildren()
	{
		return false;
	}
     	
	@Override
	public int getProportionShares()
	{
		return proportionShares;
	}
	
	@Override
	public void addProportionShares(TreeTableNode otherNode)
	{
		proportionShares += otherNode.getProportionShares();
	}

	private Task task;
	private int proportionShares;

}
