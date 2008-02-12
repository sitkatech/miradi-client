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
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class PlanningTreeDirectThreatNode extends AbstractPlanningTreeNode
{
	public PlanningTreeDirectThreatNode(Project projectToUse, DiagramObject diagramToUse, ORef threatRef) throws Exception
	{
		super(projectToUse);
		diagramObject = diagramToUse;
		threat = (Cause)project.findObject(threatRef);
		rebuild();
	}
	
	public void rebuild() throws Exception
	{
		DiagramObject diagram = diagramObject;
		createAndAddChildren(threat.getUpstreamObjectives(diagram), diagram);
		
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
		return threat;
	}
	
	public String toString()
	{
		if (threat.getShortLabel().length() > 0)
			return threat.getShortLabel() + "." + getObject().getLabel();
		
		return super.toString();
	}

	private DiagramObject diagramObject;
	private Cause threat;
}
