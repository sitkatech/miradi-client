/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeTargetNode extends AbstractPlanningTreeNode
{
	public PlanningTreeTargetNode(Project projectToUse, DiagramObject diagramToUse, ORef targetRef) throws Exception
	{
		super(projectToUse);
		diagram = diagramToUse;
		target = (Target)project.findObject(targetRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		ORefList goals = target.getOwnedObjects(Goal.getObjectType());
		for(int i = 0; i < goals.size(); ++i)
			children.add(new PlanningTreeGoalNode(project, diagram, goals.get(i)));
		
		addMissingStrategiesAsChildren();
		addMissingIndicatorsAsChildren();
	}
	
	protected ORefList getPotentialChildrenStrategyRefs()
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList upstreamStrategyRefs = new ORefList();
		Factor[] upstreamFactors = target.getUpstreamFactors(diagram);
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
		Factor[] upstreamFactors = target.getUpstreamFactors(diagram);
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			ORefList indicatorRefs = new ORefList(Indicator.getObjectType(), factor.getDirectOrIndirectIndicators());
			potentialChildIndicatorRefs.addAll(indicatorRefs);
		}
		return potentialChildIndicatorRefs;
	}
	
	int[] getNodeSortOrder()
	{
		return new int[] {
				Goal.getObjectType(),
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Objective.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return target;
	}

	DiagramObject diagram;
	Target target;
}
