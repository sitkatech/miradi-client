/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
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
		ORefList strategies = objective.getRelevantStrategyRefList();
		createAndAddChildren(getStrategiesInDiagram(strategies), diagram);
		
		ORefList indicatorRefs = objective.getRelevantIndicatorRefList();
		createAndAddChildren(getIndicatorsInDiagram(indicatorRefs), diagram);
	}

	private ORefList getStrategiesInDiagram(ORefList strategies)
	{
		ORefList strategiesInDiagram = new ORefList();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for (int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(i));
			if (strategies.contains(diagramFactor.getWrappedORef()))
				strategiesInDiagram.add(diagramFactor.getWrappedORef());
		}
		
		return strategiesInDiagram;
	}

	private ORefList getIndicatorsInDiagram(ORefList indicatorRefs)
	{
		ORefList indicatarRefsInDiagram = new ORefList();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for (int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(i));
			ORefList thisIndicatorRefs = diagramFactor.getWrappedFactor().getIndicatorRefs();
			indicatarRefsInDiagram.addAll(thisIndicatorRefs.getOverlappingRefs(indicatorRefs));
		}
		
		return indicatarRefsInDiagram;
	}

	public BaseObject getObject()
	{
		return objective;
	}

	private DiagramObject diagram;
	private Objective objective;
}
