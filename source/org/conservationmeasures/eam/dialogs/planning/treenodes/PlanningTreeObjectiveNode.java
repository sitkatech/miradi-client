package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeObjectiveNode extends AbstractPlanningTreeNode
{
	public PlanningTreeObjectiveNode(Project projectToUse, ORef objectiveRef) throws Exception
	{
		super(projectToUse);
		objective = (Objective)project.findObject(objectiveRef);
		rebuild();
	}

	public void rebuild() throws Exception
	{
		ORefList strategies = objective.getUpstreamNonDraftStrategies();
		for(int i = 0; i < strategies.size(); ++i)
			children.add(new PlanningTreeStrategyNode(project, strategies.get(i)));
		
		ORefList indicatorRefs = objective.getUpstreamIndicators();
		for(int i = 0; i < indicatorRefs.size(); ++i)
			children.add(new PlanningTreeIndicatorNode(project, indicatorRefs.get(i)));
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		boolean wasAdded = attemptToAddToChildren(refToAdd);
		
		ORefList strategies = objective.getUpstreamNonDraftStrategies();
		if(strategies.contains(refToAdd))
		{
			children.add(new PlanningTreeStrategyNode(project, refToAdd));
			wasAdded = true;
		}

		ORefList indicators = objective.getUpstreamIndicators();
		if(indicators.contains(refToAdd))
		{
			children.add(new PlanningTreeIndicatorNode(project, refToAdd));
			wasAdded = true;
		}
		return wasAdded;
	}

	public BaseObject getObject()
	{
		return objective;
	}

	Objective objective;
}
