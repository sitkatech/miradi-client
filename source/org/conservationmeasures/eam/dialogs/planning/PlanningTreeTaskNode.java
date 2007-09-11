package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeTaskNode extends AbstractPlanningTreeNode
{

	public PlanningTreeTaskNode(Project projectToUse, ORef taskRef)
	{
		super(projectToUse);
		task = (Task)project.findObject(taskRef);
	}

	public boolean attemptToAdd(ORef refToAdd)
	{
		return false;
	}

	public BaseObject getObject()
	{
		return task;
	}

	Task task;
}
