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

import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{
	public PlanningTreeTaskNode(Project projectToUse, ORef contextNodeRefToUse, ORef taskRef, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		
		task = (Task)project.findObject(taskRef);
		contextNodeRef = contextNodeRefToUse;
		contextNodeRefs = new ORefSet();
		contextNodeRefs.add(contextNodeRef);
		
		rebuild();
	}

	@Override
	public void rebuild() throws Exception
	{
		// NOTE: IF is for Speed optimization
		if(visibleRows.contains(ResourceAssignment.OBJECT_NAME))
			addChildren(buildResourceAssignmentNodes(task.getResourceAssignmentRefs()));
		
		// NOTE: IF is for Speed optimization
		if(visibleRows.contains(ExpenseAssignment.OBJECT_NAME))
			addChildren(buildExpenseAssignmentNodes(task.getExpenseAssignmentRefs()));

		// NOTE: IF is for Speed optimization
		if(visibleRows.contains(Task.OBJECT_NAME))
			addChildren(buildTaskNodes(task.getSubTaskRefs()));
	}

	private Vector<AbstractPlanningTreeNode> buildTaskNodes(ORefList subtaskRefs) throws Exception
	{
		Vector<AbstractPlanningTreeNode> subTaskNodes = new Vector();
		for(int i = 0; i < subtaskRefs.size(); ++i)
		{
			ORef taskRef = subtaskRefs.get(i);
			subTaskNodes.add(new PlanningTreeTaskNode(project, contextNodeRef, taskRef, visibleRows));
		}
		
		return subTaskNodes;
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
		return contextNodeRefs.size();
	}
	
	@Override
	public void addProportionShares(TreeTableNode rawNode)
	{
		PlanningTreeTaskNode taskNode = (PlanningTreeTaskNode) rawNode;
		contextNodeRefs.addAll(taskNode.contextNodeRefs);
	}

	private Task task;
	private ORefSet contextNodeRefs;
	private ORef contextNodeRef;
}
