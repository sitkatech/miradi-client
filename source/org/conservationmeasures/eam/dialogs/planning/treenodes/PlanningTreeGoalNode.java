package org.conservationmeasures.eam.dialogs.planning.treenodes;


import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeGoalNode extends AbstractPlanningTreeNode
{
	public PlanningTreeGoalNode(Project projectToUse, ORef goalRef) throws Exception
	{
		super(projectToUse);
		goal = (Goal)project.findObject(goalRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList objectives = goal.getUpstreamObjectives();
		for(int i = 0; i < objectives.size(); ++i)
			children.add(new PlanningTreeObjectiveNode(project, objectives.get(i)));
		
		addMissingStrategiesAsChildren();
		addMissingIndicatorsAsChildren();
	}
	
	protected ORefList getPotentialChildrenStrategyRefs()
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList upstreamStrategyRefs = new ORefList();
		Factor[] upstreamFactors = goal.getUpstreamFactors();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			if(!factor.isStrategy())
				continue;
			
			if(factor.isStatusDraft())
				continue;
			
			upstreamStrategyRefs.add(factor.getRef());
		}
		return upstreamStrategyRefs;
	}

	protected ORefList getPotentialChildrenIndicatorRefs()
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList potentialChildIndicatorRefs = new ORefList();
		Factor[] upstreamFactors = goal.getUpstreamFactors();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			ORefList indicatorRefs = new ORefList(Indicator.getObjectType(), factor.getDirectOrIndirectIndicators());
			potentialChildIndicatorRefs.addAll(indicatorRefs);
		}
		return potentialChildIndicatorRefs;
	}

	public boolean attemptToAdd(ORef refToAdd) throws Exception
	{
		boolean wasAdded = attemptToAddToChildren(refToAdd);
			
		ORefList objectives = goal.getUpstreamObjectives();
		if(objectives.contains(refToAdd))
		{
			children.add(new PlanningTreeObjectiveNode(project, refToAdd));
			wasAdded = true;
		}
		
		Target target = (Target)goal.getDirectOrIndirectOwningFactor();
		ORefList indicators = new ORefList(Indicator.getObjectType(), target.getDirectOrIndirectIndicators());
		if(indicators.contains(refToAdd))
		{
			children.add(new PlanningTreeIndicatorNode(project, refToAdd));
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
