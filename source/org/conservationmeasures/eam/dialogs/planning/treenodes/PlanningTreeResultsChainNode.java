package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeResultsChainNode extends AbstractPlanningTreeDiagramNode
{
	public PlanningTreeResultsChainNode(Project projectToUse, ORef refToUse) throws Exception
	{
		super(projectToUse);
		object = (ResultsChainDiagram)project.findObject(refToUse);
		rebuild();
	}
}
