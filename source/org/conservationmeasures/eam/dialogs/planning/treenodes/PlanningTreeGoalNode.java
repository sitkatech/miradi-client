/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;


import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;

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
		createAndAddChildren(extractDirectThreatRefs(goal.getUpstreamFactors(diagram)), diagram);
		
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
				Objective.getObjectType(),
				Task.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return goal;
	}

	DiagramObject diagramObject;
	Goal goal;
}
