package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeObjectiveNode extends AbstractPlanningTreeNode
{
	public PlanningTreeObjectiveNode(Project projectToUse, ORef objectiveRef)
	{
		super(projectToUse);
		objective = (Objective)project.findObject(objectiveRef);
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		boolean wasAdded = attemptToAddToChildren(refToAdd);
		
		ORefList strategies = objective.getRelatedStrategies();
		if(strategies.contains(refToAdd))
		{
			children.add(new PlanningTreeStrategyNode(project, refToAdd));
			wasAdded = true;
		}

		// FIXME: For some reason, this is not returning indicators that 
		// are attached to KEA's in Targets using detailed viability method
		ORefList indicators = objective.getUpstreamAndDownstreamIndicators();
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
