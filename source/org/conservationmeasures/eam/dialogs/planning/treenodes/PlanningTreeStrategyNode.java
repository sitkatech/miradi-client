package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeStrategyNode extends AbstractPlanningTreeNode
{
	public PlanningTreeStrategyNode(Project projectToUse, ORef strategyRef) throws Exception
	{
		super(projectToUse);
		strategy = (Strategy)project.findObject(strategyRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList activityRefs = strategy.getActivities();
		for(int i = 0; i < activityRefs.size(); ++i)
			children.add(new PlanningTreeTaskNode(project, activityRefs.get(i)));
	}

	public BaseObject getObject()
	{
		return strategy;
	}

	boolean shouldSortChildren()
	{
		return false;
	}

	Strategy strategy;
}
