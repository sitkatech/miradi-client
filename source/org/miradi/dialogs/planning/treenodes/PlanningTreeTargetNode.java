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
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;

public class PlanningTreeTargetNode extends AbstractPlanningTreeNode
{
	public PlanningTreeTargetNode(Project projectToUse, DiagramObject diagramToUse, ORef targetRef) throws Exception
	{
		super(projectToUse);
		diagramObject = diagramToUse;
		target = (Target)project.findObject(targetRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		createAndAddChildren(target.getOwnedObjects(Goal.getObjectType()), diagram);

		addMissingUpstreamDirectThreats(diagram);
		addMissingUpstreamThreatReductionResults(diagram);
		addMissingUpstreamObjectives(diagram);
		addMissingUpstreamNonDraftStrategies(diagram);
		addMissingUpstreamIndicators(diagram);
	}
	
	int[] getNodeSortOrder()
	{
		return new int[] {
				Goal.getObjectType(),
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
		return target;
	}

	DiagramObject diagramObject;
	Target target;
}
