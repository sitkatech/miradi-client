package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeResultsChainNode extends AbstractPlanningTreeDiagramNode
{
	public PlanningTreeResultsChainNode(Project projectToUse, ORef refToUse)
	{
		super(projectToUse);
		object = (ResultsChainDiagram)project.findObject(refToUse);
	}

	public boolean attemptToAdd(ORef refToAdd)
	{
		return attemptToAddToPage(object, refToAdd);
	}

	public BaseObject getObject()
	{
		return object;
	}

	ResultsChainDiagram object;
}
