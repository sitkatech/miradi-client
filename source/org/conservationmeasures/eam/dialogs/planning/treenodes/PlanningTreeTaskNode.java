/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.RowManager;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{

	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef) throws Exception
	{
		super(projectToUse);
		task = (Task)project.findObject(taskRef);
		
		rebuild();
	}

	public void rebuild() throws Exception
	{
		// NOTE: Speed optimization
		ViewData viewData = project.getCurrentViewData();
		CodeList objectTypesToShow = RowManager.getVisibleRowCodes(viewData);
		if(!objectTypesToShow.contains(Task.OBJECT_NAME))
			return;
		
		createAndAddChildren(task.getSubtasks(), null);
	}

	public boolean attemptToAdd(ORef refToAdd)
	{
		return false;
	}

	public BaseObject getObject()
	{
		return task;
	}
	
	boolean shouldSortChildren()
	{
		return false;
	}

	Task task;
}
