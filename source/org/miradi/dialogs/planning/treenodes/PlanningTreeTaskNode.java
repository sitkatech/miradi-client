/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{

	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef, double costAllocationProportionToUse, CodeList visibleRowsToUse) throws Exception
	{
		super(projectToUse, visibleRowsToUse);
		task = (Task)project.findObject(taskRef);
		costAllocationProportion = costAllocationProportionToUse;
		
		rebuild();
	}

	public void rebuild() throws Exception
	{
		// NOTE: Speed optimization
		if(!visibleRows.contains(Task.OBJECT_NAME))
			return;
		
		ORefList subtaskRefs = task.getSubtasks();
		for(int i = 0; i < subtaskRefs.size(); ++i)
		{
			ORef taskRef = subtaskRefs.get(i);
			children.add(new PlanningTreeTaskNode(project, taskRef, getCostAllocationProportion(), visibleRows));
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
	
	public double getCostAllocationProportion()
	{
		return costAllocationProportion;
	}
	
	boolean shouldSortChildren()
	{
		return false;
	}
     	
	private Task task;
	private double costAllocationProportion;
}
