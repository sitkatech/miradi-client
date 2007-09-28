package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
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

	public void rebuild() throws Exception
	{
		ResultsChainDiagram diagram = (ResultsChainDiagram) project.findObject(getObjectReference());
		rebuild(diagram);
	}

	public BaseObject getObject()
	{
		return object;
	}

	protected ORefList getPotentialChildrenStrategyRefs()
	{
		return getPotentialChildStrategyRefs(object);
	}

	protected ORefList getPotentialChildrenIndicatorRefs()
	{
		return getPotentialChildrenIndicatorRefs(object);
	}

	ResultsChainDiagram object;
}
