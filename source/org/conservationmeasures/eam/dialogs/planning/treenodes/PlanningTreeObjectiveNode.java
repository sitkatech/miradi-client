package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class PlanningTreeObjectiveNode extends AbstractPlanningTreeNode
{
	public PlanningTreeObjectiveNode(Project projectToUse, DiagramObject diagramToUse, ORef objectiveRef) throws Exception
	{
		super(projectToUse);
		diagram = diagramToUse;
		objective = (Objective)project.findObject(objectiveRef);
		rebuild();
	}

	public void rebuild() throws Exception
	{
		ORefList strategies = objective.getUpstreamNonDraftStrategies(diagram);
		for(int i = 0; i < strategies.size(); ++i)
			children.add(new PlanningTreeStrategyNode(project, strategies.get(i)));
		
		ORefList indicatorRefs = objective.getUpstreamIndicators();
		for(int i = 0; i < indicatorRefs.size(); ++i)
			children.add(new PlanningTreeIndicatorNode(project, indicatorRefs.get(i)));
	}

	public BaseObject getObject()
	{
		return objective;
	}

	DiagramObject diagram;
	Objective objective;
}
