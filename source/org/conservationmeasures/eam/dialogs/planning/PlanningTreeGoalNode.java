package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeGoalNode extends AbstractPlanningTreeNode
{
	public PlanningTreeGoalNode(Project projectToUse, ORef goalRef)
	{
		super(projectToUse);
		goal = (Goal)project.findObject(goalRef);
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		boolean wasAdded = attemptToAddToChildren(refToAdd);
			
		ORefList objectives = goal.getObjectivesUpstreamOfGoal();
		if(objectives.contains(refToAdd))
		{
			children.add(new PlanningTreeObjectiveNode(project, refToAdd));
			wasAdded = true;
		}
		
		return wasAdded;
	}

	public BaseObject getObject()
	{
		return goal;
	}

	Goal goal;
}
