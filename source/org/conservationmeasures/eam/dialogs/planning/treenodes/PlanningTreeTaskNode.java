package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{

	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef) throws Exception
	{
		super(projectToUse);
		task = (Task)project.findObject(taskRef);
		
		ViewData viewData = project.getCurrentViewData();
		CodeList objectTypesToShow = viewData.getCodeList(ViewData.TAG_PLANNING_VISIBLE_ROW_TYPES);
		if(objectTypesToShow.contains(Task.OBJECT_NAME))
			addAllSubtasks();
	}

	public boolean attemptToAdd(ORef refToAdd)
	{
		return false;
	}

	public BaseObject getObject()
	{
		return task;
	}
	
	private void addAllSubtasks() throws Exception
	{
		ORefList subtaskRefs = task.getSubtasks();
		for(int i = 0; i < subtaskRefs.size(); ++i)
			children.add(new PlanningTreeTaskNode(project, subtaskRefs.get(i)));
	}

	Task task;
}
