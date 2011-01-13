/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import org.miradi.dialogs.planning.treenodes.NewAbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeErrorNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.SubTarget;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;

public class TreeRebuilder
{
	public TreeRebuilder(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void rebuildTree(NewAbstractPlanningTreeNode parentNode)
	{
		try
		{
			parentNode.clearChildren();
			ORef parentRef = parentNode.getObjectReference();
			if(ProjectMetadata.is(parentRef))
				createChildrenOfProjectNode(parentNode);
			if(DiagramObject.isDiagramObject(parentRef))
				createChildrenOfDiagramNode(parentNode);
			if(AbstractTarget.isAbstractTarget(parentRef))
				createChildrenOfAbstractTarget(parentNode);

			for(int i = 0; i < parentNode.getChildCount(); ++i)
				rebuildTree((NewAbstractPlanningTreeNode) parentNode.getChild(i));
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private void createChildrenOfProjectNode(NewAbstractPlanningTreeNode parentNode) throws Exception
	{
		ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getORefList();
		createAndAddChildren(parentNode, conceptualModelRefs);

		ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
		createAndAddChildren(parentNode, resultsChainRefs);
	}

	private void createChildrenOfDiagramNode(NewAbstractPlanningTreeNode diagramNode) throws Exception
	{
		DiagramObject diagramObject = (DiagramObject)diagramNode.getObject(); 
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)project.findObject(diagramFactorRefs.get(i));
			Factor factor = diagramFactor.getWrappedFactor();
			if(shouldIncludeFactorWithinDiagram(factor))
			{
				createAndAddChild(diagramNode, factor.getRef());
			}
		}
		
	}

	private boolean shouldIncludeFactorWithinDiagram(Factor factor)
	{
		if (AbstractTarget.isAbstractTarget(factor) && !shouldTargetsBeAtSameLevelAsDiagrams())
			return true;

		if (factor.isDirectThreat())
			return true;
		
		if (factor.isContributingFactor())
			return true;
		
		if (factor.isThreatReductionResult())
			return true;
		
		if (factor.isIntermediateResult())
			return true;
		
		return false;
	}

	protected boolean shouldTargetsBeAtSameLevelAsDiagrams()
	{
		return getProject().getMetadata().shouldPutTargetsAtTopLevelOfTree();
	}
	
	private void createChildrenOfAbstractTarget(NewAbstractPlanningTreeNode parentNode) throws Exception
	{
		AbstractTarget target = (AbstractTarget) parentNode.getObject();
		ORefList subTargetRefs = target.getSubTargetRefs();
		createAndAddChildren(parentNode, subTargetRefs);
	}


	
	
	private void createAndAddChildren(NewAbstractPlanningTreeNode parent, ORefList refsToAdd) throws Exception
	{
		for(int i = 0; i < refsToAdd.size(); ++i)
			createAndAddChild(parent, refsToAdd.get(i));
	}

	private void createAndAddChild(NewAbstractPlanningTreeNode parent, ORef refToAdd) throws Exception
	{
		NewAbstractPlanningTreeNode childNode = createChildNode(refToAdd);
		parent.addChild(childNode);
	}

	private NewAbstractPlanningTreeNode createChildNode(ORef refToAdd) throws Exception
	{
		int type = refToAdd.getObjectType();
		try
		{
			if(type == ConceptualModelDiagram.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			if(type == ResultsChainDiagram.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			
			if(AbstractTarget.isAbstractTarget(type))
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			if(type == Cause.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			if(type == ThreatReductionResult.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			if(type == IntermediateResult.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);
			if (SubTarget.is(type))
				return new NewPlanningTreeBaseObjectNode(getProject(), refToAdd);

			// TODO: Remove comments as these get implemented
//			if(type == Strategy.getObjectType())
//				return new PlanningTreeStrategyNode(project, refToAdd, visibleRows);
//			if(AbstractTarget.isAbstractTarget(type))
//				return new PlanningTreeTargetNode(project, diagram, refToAdd, visibleRows);
//			if(type == Goal.getObjectType())
//				return new PlanningTreeGoalNode(project, diagram, refToAdd, visibleRows);
//			if(type == Objective.getObjectType())
//				return new PlanningTreeObjectiveNode(project, diagram, refToAdd, visibleRows);
//			if(type == Indicator.getObjectType())
//				return new PlanningTreeIndicatorNode(project, refToAdd, visibleRows);
//			if (type == Measurement.getObjectType())
//				return new PlanningTreeMeasurementNode(project, refToAdd, visibleRows);
//			if (type == Task.getObjectType())
//				throw new RuntimeException(EAM.text("This method is not responsible for creating task nodes."));
//			if (type == ResourceAssignment.getObjectType())
//				return new PlanningTreeResourceAssignmentNode(project, refToAdd, visibleRows);
//			if (type == ExpenseAssignment.getObjectType())
//				return new PlanningTreeExpenseAssignmentNode(project, refToAdd, visibleRows);
			
			throw new Exception("Attempted to create node of unknown type: " + refToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new NewPlanningTreeErrorNode(getProject(), refToAdd);
		}
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
}
