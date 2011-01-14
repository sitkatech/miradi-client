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

import org.miradi.diagram.ChainWalker;
import org.miradi.dialogs.planning.treenodes.NewAbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeErrorNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.RowManager;

public class TreeRebuilder
{
	public TreeRebuilder(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void rebuildTree(NewAbstractPlanningTreeNode parentNode)
	{
		rebuildTree(parentNode, null);
	}
	
	private void rebuildTree(NewAbstractPlanningTreeNode parentNode, DiagramObject diagram)
	{
		CodeList rows = RowManager.getStrategicPlanRows();
		try
		{
			parentNode.clearChildren();
			ORef parentRef = parentNode.getObjectReference();
			if(DiagramObject.isDiagramObject(parentRef))
				diagram = DiagramObject.findDiagramObject(getProject(), parentRef);

			ORefSet candidateChildRefs = getChildRefs(parentNode.getObjectReference(), diagram);
			ORefSet childRefs = pruneChildRefsAlreadyDone(parentNode, candidateChildRefs, rows);
			createAndAddChildren(parentNode, childRefs);

			for(int i = 0; i < parentNode.getChildCount(); ++i)
				rebuildTree((NewAbstractPlanningTreeNode) parentNode.getChild(i), diagram);
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private ORefSet pruneChildRefsAlreadyDone(NewAbstractPlanningTreeNode parentNode, ORefSet candidateChildRefs, CodeList rows)
	{
		ORefSet remainingChildren = new ORefSet();
		int parentAt = rows.find(parentNode.getObjectTypeName());
		for(ORef childRef : candidateChildRefs)
		{
			BaseObject object = BaseObject.find(getProject(), childRef);
			String childTypeName = object.getTypeName();
			int childAt = rows.find(childTypeName);
			if(childAt < 0 || parentAt < 0 || childAt >= parentAt)
				remainingChildren.add(childRef);
		}
		return remainingChildren;
	}

	private ORefSet getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception
	{
		if(ProjectMetadata.is(parentRef))
			return getChildrenOfProjectNode(parentRef);
		if(DiagramObject.isDiagramObject(parentRef))
			return getChildrenOfDiagramNode(parentRef);
		
		if(AbstractTarget.isAbstractTarget(parentRef))
			return getChildrenOfAbstractTarget(parentRef, diagram);
		else if(Cause.is(parentRef))
			return getChildrenOfBasicFactor(parentRef, diagram);
		else if(Strategy.is(parentRef))
			return getChildrenOfStrategy(parentRef, diagram);
		
		if(Desire.isDesire(parentRef))
			return getChildrenOfDesire(parentRef, diagram);
		if(Indicator.is(parentRef))
			return getChildrenOfIndicator(parentRef, diagram);
		
		if(Task.is(parentRef))
			return getChildrenOfTask(parentRef, diagram);
		
		EAM.logDebug("Don't know how to get children of " + parentRef);
		return new ORefSet();
	}

	private ORefSet getChildrenOfProjectNode(ORef parentRef) throws Exception
	{
		ORefSet childRefs = new ORefSet();
		ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getORefList();
		childRefs.addAllRefs(conceptualModelRefs);
		ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
		childRefs.addAllRefs(resultsChainRefs);
		return childRefs;
	}

	private ORefSet getChildrenOfDiagramNode(ORef diagramRef) throws Exception
	{
		ORefSet childRefs = new ORefSet();
		DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), diagramRef); 
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor)project.findObject(diagramFactorRefs.get(i));
			Factor factor = diagramFactor.getWrappedFactor();
			if(shouldIncludeFactorWithinDiagram(factor))
			{
				childRefs.add(factor.getRef());
			}
		}
		return childRefs;
	}

	private boolean shouldIncludeFactorWithinDiagram(Factor factor)
	{
		if (AbstractTarget.isAbstractTarget(factor) && !shouldTargetsBeAtSameLevelAsDiagrams())
			return true;

		if (factor.isStrategy() && !factor.isStatusDraft())
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
	
	private ORefSet getChildrenOfAbstractTarget(ORef targetRef, DiagramObject diagram) throws Exception
	{
		ORefSet childRefs = new ORefSet();
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		childRefs.addAllRefs(target.getSubTargetRefs());
		childRefs.addAllRefs(target.getOwnedObjects(Goal.getObjectType()));
		childRefs.addAllRefs(new ORefList(Indicator.getObjectType(), target.getDirectOrIndirectIndicators()));
		childRefs.addAllRefs(getDirectlyLinkedNonDraftStrategies(target, diagram));
		return childRefs;
	}

	private ORefList getDirectlyLinkedNonDraftStrategies(AbstractTarget target, DiagramObject diagram)
	{
		ORefList strategyRefs = new ORefList();
		
		ChainWalker chain = diagram.getDiagramChainWalker();
		DiagramFactor targetDiagramFactor = diagram.getDiagramFactor(target.getRef());
		FactorSet factors = chain.buildDirectlyLinkedUpstreamChainAndGetFactors(targetDiagramFactor);
		for(Factor factor : factors)
		{
			if(factor.isStrategy() && !factor.isStatusDraft())
				strategyRefs.add(factor.getRef());
		}
		
		return strategyRefs;
	}
	
	private ORefSet getChildrenOfBasicFactor(ORef parentRef, DiagramObject diagram)
	{
		ORefSet childRefs = new ORefSet();
		Factor factor = Factor.findFactor(getProject(), parentRef);
		childRefs.addAllRefs(factor.getObjectiveRefs());
		childRefs.addAllRefs(factor.getDirectOrIndirectIndicatorRefs());
		return childRefs;
	}

	private ORefSet getChildrenOfStrategy(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefSet childRefs = new ORefSet();
		Strategy strategy = Strategy.find(getProject(), parentRef);
		childRefs.addAllRefs(strategy.getActivityRefs());
		childRefs.addAll(findRelevantObjectivesAndGoals(parentRef));
		childRefs.addAllRefs(strategy.getOwnedObjects(Indicator.getObjectType()));
		return childRefs;
	}

	private ORefSet findRelevantObjectivesAndGoals(ORef strategyRef) throws Exception
	{
		ORefSet relevant = new ORefSet();
		relevant.addAll(findRelevantDesires(strategyRef, getProject().getObjectivePool().getRefSet()));
		relevant.addAll(findRelevantDesires(strategyRef, getProject().getGoalPool().getRefSet()));
		return relevant;
	}
	
	private ORefSet findRelevantDesires(ORef strategyRef, ORefSet desireRefs) throws Exception
	{
		ORefSet relevant = new ORefSet();
		for(ORef desireRef: desireRefs)
		{
			Desire desire = Desire.findDesire(getProject(), desireRef);
			if(desire.getRelevantStrategyRefs().contains(strategyRef))
				relevant.add(desire.getRef());
		}
		return relevant;
	}

	private ORefSet getChildrenOfDesire(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefSet childRefs = new ORefSet();
		Desire desire = Desire.findDesire(getProject(), parentRef);
		childRefs.addAllRefs(desire.getRelevantStrategyAndActivityRefs());
		childRefs.addAllRefs(desire.getRelevantIndicatorRefList());
		return childRefs;
	}

	private ORefSet getChildrenOfIndicator(ORef parentRef, DiagramObject diagram)
	{
		ORefSet childRefs = new ORefSet();
		Indicator indicator = Indicator.find(getProject(), parentRef);
		childRefs.addAllRefs(indicator.getMeasurementRefs());
		childRefs.addAllRefs(indicator.getMethodRefs());
		return childRefs;
	}

	private ORefSet getChildrenOfTask(ORef parentRef, DiagramObject diagram)
	{
		ORefSet childRefs = new ORefSet();
		Task task = Task.find(getProject(), parentRef);
		childRefs.addAllRefs(task.getSubTaskRefs());
		return childRefs;
	}


	
	
	private void createAndAddChildren(NewAbstractPlanningTreeNode parent, ORefSet childRefsToAdd) throws Exception
	{
		for(ORef childRef : childRefsToAdd)
		{
			createAndAddChild(parent, childRef);
		}
	}

	private void createAndAddChild(NewAbstractPlanningTreeNode parent, ORef childRefToAdd) throws Exception
	{
		if(wouldCreateRecursiveTree(parent, childRefToAdd))
			return;
		
		NewAbstractPlanningTreeNode childNode = createChildNode(parent, childRefToAdd);
		parent.addChild(childNode);
	}

	private boolean wouldCreateRecursiveTree(TreeTableNode node, ORef childRefToAdd) throws Exception
	{
		if(node.getObjectReference().equals(childRefToAdd))
			return true;

		TreeTableNode parentNode = node.getParentNode();
		if(parentNode == null)
			return false;
		
		return wouldCreateRecursiveTree(parentNode, childRefToAdd);
	}

	private NewAbstractPlanningTreeNode createChildNode(TreeTableNode parentNode, ORef refToAdd) throws Exception
	{
		int type = refToAdd.getObjectType();
		try
		{
			if(type == ConceptualModelDiagram.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == ResultsChainDiagram.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			
			if(type == Strategy.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(AbstractTarget.isAbstractTarget(type))
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == Cause.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == ThreatReductionResult.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == IntermediateResult.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if (SubTarget.is(type))
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == Goal.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == Indicator.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if(type == Objective.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if (type == Measurement.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			if (type == Task.getObjectType())
				return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);

			// TODO: Remove comments as these get implemented
//			if (type == ResourceAssignment.getObjectType())
//				return new PlanningTreeResourceAssignmentNode(project, refToAdd, visibleRows);
//			if (type == ExpenseAssignment.getObjectType())
//				return new PlanningTreeExpenseAssignmentNode(project, refToAdd, visibleRows);
			
			throw new Exception("Attempted to create node of unknown type: " + refToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new NewPlanningTreeErrorNode(getProject(), parentNode, refToAdd);
		}
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
}
