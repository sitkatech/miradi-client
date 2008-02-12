/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;


import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;

public class PlanningTreeGoalNode extends AbstractPlanningTreeNode
{
	public PlanningTreeGoalNode(Project projectToUse, DiagramObject diagramToUse, ORef goalRef) throws Exception
	{
		super(projectToUse);
		diagramObject = diagramToUse;
		goal = (Goal)project.findObject(goalRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		Factor[] upstreamFactors = goal.getUpstreamFactors(diagram);
		if(diagram.isResultsChain())
			createAndAddChildren(extractThreatReductionResultRefs(upstreamFactors), diagram);
		else
			createAndAddChildren(extractDirectThreatRefs(upstreamFactors), diagram);
		
		addMissingUpstreamObjectives(diagram);
		addMissingUpstreamNonDraftStrategies(diagram);
		addMissingUpstreamIndicators(diagram);
	}

	int[] getNodeSortOrder()
	{
		return new int[] {
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Cause.getObjectType(),
				ThreatReductionResult.getObjectType(),
				Objective.getObjectType(),
				Task.getObjectType(),
				Measurement.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return goal;
	}
	
	private DiagramObject diagramObject;
	private Goal goal;
}
