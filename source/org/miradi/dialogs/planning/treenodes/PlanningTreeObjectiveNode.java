/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.treenodes;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Objective;
import org.miradi.project.Project;

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
