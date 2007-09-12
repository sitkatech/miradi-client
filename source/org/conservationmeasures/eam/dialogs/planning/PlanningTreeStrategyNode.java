package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeStrategyNode extends AbstractPlanningTreeNode
{
	public PlanningTreeStrategyNode(Project projectToUse, ORef strategyRef)
	{
		super(projectToUse);
		strategy = (Strategy)project.findObject(strategyRef);
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		if(attemptToAddToChildren(refToAdd))
			return true;
		
		if(strategy.getActivities().contains(refToAdd))
		{
			children.add(new PlanningTreeTaskNode(project, refToAdd));
			return true;
		}

		return false;
	}

	public BaseObject getObject()
	{
		return strategy;
	}

	Strategy strategy;
}
