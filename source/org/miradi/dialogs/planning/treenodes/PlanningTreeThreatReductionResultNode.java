/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;

public class PlanningTreeThreatReductionResultNode extends AbstractPlanningTreeNode
{
	public PlanningTreeThreatReductionResultNode(Project projectToUse, DiagramObject diagramToUse, ORef threatReductionResultRef) throws Exception
	{
		super(projectToUse);
		diagramObject = diagramToUse;
		threatReductionResult = (ThreatReductionResult)project.findObject(threatReductionResultRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		createAndAddChildren(threatReductionResult.getUpstreamObjectives(diagram), diagram);
		
		addMissingUpstreamNonDraftStrategies(diagram);
		addMissingUpstreamIndicators(diagram);
	}

	int[] getNodeSortOrder()
	{
		return new int[] {
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Objective.getObjectType(),
				Task.getObjectType(),
				Measurement.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return threatReductionResult;
	}

	DiagramObject diagramObject;
	ThreatReductionResult threatReductionResult;
}
