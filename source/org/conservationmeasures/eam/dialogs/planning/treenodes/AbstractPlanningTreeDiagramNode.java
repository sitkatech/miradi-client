/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.HashSet;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramFactor;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;


public abstract class AbstractPlanningTreeDiagramNode extends AbstractPlanningTreeNode
{
	public AbstractPlanningTreeDiagramNode(Project projectToUse)
	{
		super(projectToUse);
	}

	public void rebuild() throws Exception
	{
		ORefList diagramFactorRefs = object.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)project.findObject(diagramFactorRefs.get(i));
			if(diagramFactor.getWrappedType() != Target.getObjectType())
				continue;
			
			children.add(new PlanningTreeTargetNode(project, object, diagramFactor.getWrappedORef()));
		}
		addMissingObjectivesAsChildren(object);
		addMissingStrategiesAsChildren();
		addMissingIndicatorsAsChildren();
	}

	public BaseObject getObject()
	{
		return object;
	}

	boolean isGoalOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Goal.getObjectType())
			return false;
		
		return page.getAllGoalRefs().contains(refToAdd);
	}
	
	boolean isObjectiveOnThisPage(DiagramObject page, ORef refToAdd)
	{
		if(refToAdd.getObjectType() != Objective.getObjectType())
			return false;
		
		return page.getAllObjectiveRefs().contains(refToAdd);
	}

	protected void addMissingObjectivesAsChildren(DiagramObject diagram) throws Exception
	{
		HashSet<ORef> everythingInTree = getAllRefsInTree();
		ORefList objectivesInPage = diagram.getAllObjectiveRefs();
		for(int i = 0; i < objectivesInPage.size(); ++i)
		{
			ORef ref = objectivesInPage.get(i);
			if(everythingInTree.contains(ref))
				continue;
			
			children.add(new PlanningTreeObjectiveNode(project, diagram, ref));
		}
	}
	
	protected ORefList getPotentialChildStrategyRefs(DiagramObject diagram)
	{
		ORefList strategyRefs = new ORefList();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(diagramFactorRefs.get(i));
			ORef factorRef = diagramFactor.getWrappedORef();
			Factor factor = (Factor) project.findObject(factorRef);
			if(!factor.isStrategy())
				continue;
			
			if(factor.isStatusDraft())
				continue;
			
			strategyRefs.add(factor.getRef());

		}
		
		return strategyRefs;
	}
	
	protected ORefList getPotentialChildrenIndicatorRefs(DiagramObject diagram)
	{
		ORefList potentialChildrenRefs = new ORefList();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) project.findObject(diagramFactorRefs.get(i));
			ORef factorRef = diagramFactor.getWrappedORef();
			Factor factor = (Factor) project.findObject(factorRef);
			ORefList indicatorRefs = new ORefList(Indicator.getObjectType(), factor.getDirectOrIndirectIndicators());
			potentialChildrenRefs.addAll(indicatorRefs);
		}
		
		return potentialChildrenRefs;
	}

	protected ORefList getPotentialChildrenStrategyRefs()
	{
		return getPotentialChildStrategyRefs(object);
	}

	protected ORefList getPotentialChildrenIndicatorRefs()
	{
		return getPotentialChildrenIndicatorRefs(object);
	}

	protected DiagramObject object;
}
