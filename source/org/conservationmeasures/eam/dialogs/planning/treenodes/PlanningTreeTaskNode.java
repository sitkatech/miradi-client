/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.RowManager;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{

	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef, double costAllocationProportionToUse) throws Exception
	{
		super(projectToUse);
		task = (Task)project.findObject(taskRef);
		costAllocationProportion = costAllocationProportionToUse;
		
		rebuild();
	}

	public void rebuild() throws Exception
	{
		// NOTE: Speed optimization
		ViewData viewData = project.getCurrentViewData();
		CodeList objectTypesToShow = RowManager.getVisibleRowCodes(viewData);
		if(!objectTypesToShow.contains(Task.OBJECT_NAME))
			return;
		
		ORefList subtaskRefs = task.getSubtasks();
		for(int i = 0; i < subtaskRefs.size(); ++i)
		{
			ORef taskRef = subtaskRefs.get(i);
			children.add(new PlanningTreeTaskNode(project, taskRef, getCostAllocationProportion()));
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
