/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

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
		ORefList goals = target.getOwnedObjects(Goal.getObjectType());
		createAndAddChildren(goals, diagramObject);
		
		addMissingChildren(getPotentialChildrenStrategyRefs(diagramObject));
		addMissingChildren(getPotentialChildrenIndicatorRefs(diagramObject));
	}
	
	protected ORefList getPotentialChildrenStrategyRefs(DiagramObject diagram)
	{
		return extractNonDraftStrategyRefs(getObject().getUpstreamFactors(diagram));
	}

	protected ORefList getPotentialChildrenIndicatorRefs(DiagramObject diagram)
	{
		return extractIndicatorRefs(getObject().getUpstreamFactors(diagram));
	}
	
	int[] getNodeSortOrder()
	{
		return new int[] {
				Goal.getObjectType(),
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Objective.getObjectType(),
			};
	}

	public BaseObject getObject()
	{
		return target;
	}

	DiagramObject diagramObject;
	Target target;
}
