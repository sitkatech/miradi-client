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

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.diagram.ChainWalker;
import org.miradi.dialogs.planning.treenodes.NewAbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTaskNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.NewPlanningTreeErrorNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FactorSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.AccountingCode;
import org.miradi.objects.BudgetCategoryOne;
import org.miradi.objects.BudgetCategoryTwo;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Factor;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.PlanningTreeConfiguration;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class TreeRebuilder
{
	public TreeRebuilder(Project projectToUse, PlanningTreeConfiguration rowColumnProviderToUse)
	{
		project = projectToUse;
		rowColumnProvider = rowColumnProviderToUse;
	}
	
	public void rebuildTree(NewAbstractPlanningTreeNode rootNode) throws Exception
	{
		CodeList rows = getRowColumnProvider().getRowCodesToShow();
		rebuildTree(rootNode, null, rows);
		deleteUnclesAndTheirChildren(rootNode);
		removeUnwantedLayersAndPromoteChildren(rootNode, rows);
	}

	private void rebuildTree(NewAbstractPlanningTreeNode parentNode, DiagramObject diagram, CodeList rows)
	{
		try
		{
			parentNode.clearChildren();
			ORef parentRef = parentNode.getObjectReference();
			if(DiagramObject.isDiagramObject(parentRef))
				diagram = DiagramObject.findDiagramObject(getProject(), parentRef);

			ORefList candidateChildRefs = getChildRefs(parentNode.getObjectReference(), diagram);
			candidateChildRefs.addAll(parentNode.getObject().getResourceAssignmentRefs());
			candidateChildRefs.addAll(parentNode.getObject().getExpenseAssignmentRefs());
			ORefList childRefs = getListWithoutChildrenThatWouldCauseRecursion(parentNode, candidateChildRefs);
			createAndAddChildren(parentNode, childRefs);

			for(int index = 0; index < parentNode.getChildCount(); ++index)
			{
				NewAbstractPlanningTreeNode childNode = (NewAbstractPlanningTreeNode) parentNode.getChild(index);
				rebuildTree(childNode, diagram, rows);
			}
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private ORefList getListWithoutChildrenThatWouldCauseRecursion(NewAbstractPlanningTreeNode parentNode, ORefList candidateChildRefs) throws Exception
	{
		TreeTableNode node = parentNode;
		HashSet<Integer> hierarchySoFar = new HashSet<Integer>();
		while(node != null)
		{
			hierarchySoFar.add(node.getObjectReference().getObjectType());
			node = node.getParentNode();
		}
	
		ORefList remainingChildren = new ORefList();
		for(int i = 0; i < candidateChildRefs.size(); ++i)
		{
			ORef childRef = candidateChildRefs.get(i);
			int childType = childRef.getObjectType();
			if(!hierarchySoFar.contains(childType))
				remainingChildren.add(childRef);
			else
				EAM.logDebug("omitting " + childRef + " because hierarchy is: " + hierarchySoFar);
		}
		return remainingChildren;
	}

	private ORefList getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception
	{
		final ORefList noChildren = new ORefList();
		if(ProjectMetadata.is(parentRef))
			return getChildrenOfProjectNode(parentRef);
		
		if(DiagramObject.isDiagramObject(parentRef))
			return getChildrenOfDiagramNode(parentRef);
		
		if(AbstractTarget.isAbstractTarget(parentRef))
			return getChildrenOfAbstractTarget(parentRef, diagram);
		
		if(Cause.is(parentRef))
			return getChildrenOfBasicFactor(parentRef, diagram);
		
		if(Strategy.is(parentRef))
			return getChildrenOfStrategy(parentRef, diagram);
		
		if(Desire.isDesire(parentRef))
			return getChildrenOfDesire(parentRef, diagram);
		
		if(Indicator.is(parentRef))
			return getChildrenOfIndicator(parentRef, diagram);
		
		if(Task.is(parentRef))
			return getChildrenOfTask(parentRef, diagram);
		
		if(Measurement.is(parentRef))
			return noChildren;
		
		if(ResourceAssignment.is(parentRef))
			return noChildren;
		
		EAM.logDebug("Don't know how to get children of " + parentRef);
		return new ORefList();
	}

	private ORefList getChildrenOfProjectNode(ORef parentRef) throws Exception
	{
		ORefList childRefs = new ORefList();
		if(getRowColumnProvider().shouldIncludeConceptualModelPage())
		{
			ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getORefList();
			childRefs.addAll(conceptualModelRefs);
		}
		if(getRowColumnProvider().shouldIncludeResultsChain())
		{
			ORefList resultsChainRefs = getProject().getResultsChainDiagramPool().getORefList();
			childRefs.addAll(resultsChainRefs);
		}
		if(shouldTargetsBeAtSameLevelAsDiagrams())
		{
			childRefs.addAll(getProject().getTargetPool().getRefList());
			if(getProject().getMetadata().isHumanWelfareTargetMode())
				childRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());
		}
		return childRefs;
	}

	private ORefList getChildrenOfDiagramNode(ORef diagramRef) throws Exception
	{
		ORefList childRefs = new ORefList();
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
	
	private ORefList getChildrenOfAbstractTarget(ORef targetRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		childRefs.addAll(target.getSubTargetRefs());
		childRefs.addAll(target.getOwnedObjects(Goal.getObjectType()));
		childRefs.addAll(new ORefList(Indicator.getObjectType(), target.getDirectOrIndirectIndicators()));
		childRefs.addAll(getDirectlyLinkedNonDraftStrategies(target, diagram));
		return childRefs;
	}

	private ORefList getDirectlyLinkedNonDraftStrategies(AbstractTarget target, DiagramObject diagram)
	{
		if(diagram == null)
			return new ORefList();
		
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
	
	private ORefList getChildrenOfBasicFactor(ORef parentRef, DiagramObject diagram)
	{
		ORefList childRefs = new ORefList();
		Factor factor = Factor.findFactor(getProject(), parentRef);
		childRefs.addAll(factor.getObjectiveRefs());
		childRefs.addAll(factor.getDirectOrIndirectIndicatorRefs());
		return childRefs;
	}

	private ORefList getChildrenOfStrategy(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		Strategy strategy = Strategy.find(getProject(), parentRef);
		childRefs.addAll(strategy.getActivityRefs());
		if (doStrategiesContainObjectives())
			childRefs.addAll(findRelevantObjectivesAndGoals(parentRef));
		
		childRefs.addAll(strategy.getOwnedObjects(Indicator.getObjectType()));
		return childRefs;
	}

	private boolean doObjectivesContainStrategies() throws Exception
	{
		return getRowColumnProvider().doObjectivesContainStrategies();
	}
	
	private boolean doStrategiesContainObjectives() throws Exception
	{
		return !doObjectivesContainStrategies();
	}

	private ORefList findRelevantObjectivesAndGoals(ORef strategyRef) throws Exception
	{
		ORefList relevant = new ORefList();
		relevant.addAll(findRelevantObjectives(getProject(), strategyRef));
		relevant.addAll(findRelevantGoals(getProject(), strategyRef));
		return relevant;
	}
	
	public static ORefList findRelevantGoals(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, Goal.getObjectType());
	}

	public static ORefList findRelevantObjectives(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, Objective.getObjectType());
	}
	
	private ORefList getChildrenOfDesire(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		Desire desire = Desire.findDesire(getProject(), parentRef);
		if (doObjectivesContainStrategies())
			childRefs.addAll(desire.getRelevantStrategyAndActivityRefs());
		
		childRefs.addAll(desire.getRelevantIndicatorRefList());
		return childRefs;
	}

	private ORefList getChildrenOfIndicator(ORef parentRef, DiagramObject diagram) throws Exception
	{
		ORefList childRefs = new ORefList();
		Indicator indicator = Indicator.find(getProject(), parentRef);
		childRefs.addAll(indicator.getRelevantDesireRefs());
		childRefs.addAll(indicator.getMeasurementRefs());
		childRefs.addAll(indicator.getMethodRefs());
		return childRefs;
	}

	private ORefList getChildrenOfTask(ORef parentRef, DiagramObject diagram)
	{
		ORefList childRefs = new ORefList();
		Task task = Task.find(getProject(), parentRef);
		childRefs.addAll(task.getSubTaskRefs());
		return childRefs;
	}
	
	private void createAndAddChildren(NewAbstractPlanningTreeNode parent, ORefList childRefsToAdd) throws Exception
	{
		for(int index = 0; index < childRefsToAdd.size(); ++index)
		{
			ORef childRef = childRefsToAdd.get(index);
			createAndAddChild(parent, childRef);
		}
	}

	private void createAndAddChild(NewAbstractPlanningTreeNode parent, ORef childRefToAdd) throws Exception
	{
		NewAbstractPlanningTreeNode childNode = createChildNode(parent, childRefToAdd);
		parent.addChild(childNode);
	}

	private NewAbstractPlanningTreeNode createChildNode(NewAbstractPlanningTreeNode parentNode, ORef refToAdd) throws Exception
	{
		int[] supportedTypes = new int[] {
			ConceptualModelDiagram.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			
			Strategy.getObjectType(),
			Target.getObjectType(),
			HumanWelfareTarget.getObjectType(),
			Cause.getObjectType(),
			IntermediateResult.getObjectType(),
			ThreatReductionResult.getObjectType(),
			
			SubTarget.getObjectType(),
			Goal.getObjectType(),
			Objective.getObjectType(),
			Indicator.getObjectType(),
			Task.getObjectType(),
			
			Measurement.getObjectType(),
			ResourceAssignment.getObjectType(),
			ExpenseAssignment.getObjectType(),
		};
		
		try
		{
			if(Task.is(refToAdd))
				return new NewPlanningTaskNode(getProject(), parentNode.getContextRef(), parentNode, refToAdd);
			
			int type = refToAdd.getObjectType();
			for(int i = 0; i < supportedTypes.length; ++i)
			{
				if(type == supportedTypes[i])
					return new NewPlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			}
			throw new Exception("Attempted to create node of unknown type: " + refToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new NewPlanningTreeErrorNode(getProject(), parentNode, refToAdd);
		}
	}
	
	private void deleteUnclesAndTheirChildren(NewAbstractPlanningTreeNode rootNode)
	{
		Vector<NewAbstractPlanningTreeNode> childrenToKeep = new Vector<NewAbstractPlanningTreeNode>();
		for(NewAbstractPlanningTreeNode childNode : rootNode.getRawChildren())
		{
			boolean keepThisChild = true;
			for(NewAbstractPlanningTreeNode otherChildNode : rootNode.getRawChildren())
			{
				if(childNode.equals(otherChildNode))
				{
					continue;
				}
				if(otherChildNode.getRawChildren().contains(childNode))
				{
					keepThisChild = false;
					break;
				}
			}
			
			if(keepThisChild)
			{
				childrenToKeep.add(childNode);
				deleteUnclesAndTheirChildren(childNode);
			}
		}
		
		rootNode.setRawChildren(childrenToKeep);
	}

	private void removeUnwantedLayersAndPromoteChildren(NewAbstractPlanningTreeNode node, CodeList objectTypesToShow)
	{
		if(node.isAnyChildAllocated())
			node.setAllocated();
		
		Vector<NewAbstractPlanningTreeNode> newChildren = new Vector<NewAbstractPlanningTreeNode>();
		for(int i = 0; i < node.getChildCount(); ++i)
		{
			NewAbstractPlanningTreeNode child = (NewAbstractPlanningTreeNode) node.getChild(i);
			removeUnwantedLayersAndPromoteChildren(child, objectTypesToShow);
			
			boolean isChildVisible = objectTypesToShow.contains(child.getObjectTypeName());
			if(isChildVisible)
			{
				mergeChildIntoList(newChildren, child);
			}
			else
			{
				addChildrenOfNodeToList(newChildren, child);
			}
		}

		if(shouldSortChildren(node))
			Collections.sort(newChildren, createNodeSorter());
		
		node.setRawChildren(newChildren);
	}
	
	private void mergeChildIntoList(Vector<NewAbstractPlanningTreeNode> destination, NewAbstractPlanningTreeNode newChild)
	{
		NewAbstractPlanningTreeNode existingNode = findNodeWithRef(destination, newChild.getObjectReference());
		if(existingNode == null)
		{
			if (isChildOfAnyNodeInList(destination, newChild))
				return;

			destination.add(newChild);
			return;
		}
		
		destination = existingNode.getRawChildren();
		addChildrenOfNodeToList(destination, newChild);

		existingNode.addProportionShares(newChild);

		if(shouldSortChildren(existingNode))
			Collections.sort(destination, createNodeSorter());
	}
	
	private boolean isChildOfAnyNodeInList(Vector<NewAbstractPlanningTreeNode> destination, NewAbstractPlanningTreeNode newChild)
	{
		for(NewAbstractPlanningTreeNode parentNode : destination)
		{
			Vector<NewAbstractPlanningTreeNode> children = parentNode.getRawChildren();
			NewAbstractPlanningTreeNode foundMatchingChild = findNodeWithRef(children, newChild.getObjectReference());
			if (foundMatchingChild != null)
				return true;
		}
		
		return false;
	}
	
	private NewAbstractPlanningTreeNode findNodeWithRef(Vector<NewAbstractPlanningTreeNode> list, ORef ref)
	{
		for(NewAbstractPlanningTreeNode node : list)
		{
			if(ref.equals(node.getObjectReference()))
				return node;
		}
		
		return null;
	}
	
	private void addChildrenOfNodeToList(Vector<NewAbstractPlanningTreeNode> destination, NewAbstractPlanningTreeNode otherNode)
	{
		for(NewAbstractPlanningTreeNode newChild : otherNode.getRawChildren())
		{
			mergeChildIntoList(destination, newChild);
		}
	}

	private boolean shouldSortChildren(NewAbstractPlanningTreeNode parentNode)
	{
		ORef parentRef = parentNode.getObjectReference();
		if(Task.is(parentRef))
			return false;
		
		if(Strategy.is(parentRef))
			return false;
		
		if(Indicator.is(parentRef))
			return false;
		
		return true;
	}

	private NodeSorter createNodeSorter()
	{
		return new NodeSorter();
	}

	private class NodeSorter implements Comparator<NewAbstractPlanningTreeNode>
	{
		public int compare(NewAbstractPlanningTreeNode nodeA, NewAbstractPlanningTreeNode nodeB)
		{

			int typeSortLocationA = getTypeSortLocation(nodeA.getType());
			int typeSortLocationB = getTypeSortLocation(nodeB.getType());
			int diff = typeSortLocationA - typeSortLocationB;
			if(diff != 0)
				return diff;

			ORef refA = nodeA.getObjectReference();
			ORef refB = nodeB.getObjectReference();
			if(refA.isValid() && refB.isInvalid())
				return -1;
			
			if(refA.isInvalid() && refB.isValid())
				return 1;
			
			String labelA = nodeA.toString();
			String labelB = nodeB.toString();
			return labelA.compareToIgnoreCase(labelB);
		}
		
		private int getTypeSortLocation(int type)
		{
			int[] sortOrder = getNodeSortOrder();
			for(int index = 0; index < sortOrder.length; ++index)
			{
				if(type == sortOrder[index])
				{
					return index;
				}
			}
			
			EAM.logError("NodeSorter unknown type: " + type);
			return sortOrder.length;
		}
	}
	
	private int[] getNodeSortOrder()
	{
		return new int[] {
			Target.getObjectType(),
			HumanWelfareTarget.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			ConceptualModelDiagram.getObjectType(),
			Goal.getObjectType(),
			SubTarget.getObjectType(),
			Cause.getObjectType(),
			ThreatReductionResult.getObjectType(),
			IntermediateResult.getObjectType(),
			Objective.getObjectType(),
			Strategy.getObjectType(),
			Indicator.getObjectType(),
			ProjectResource.getObjectType(),
			AccountingCode.getObjectType(),
			FundingSource.getObjectType(),
			BudgetCategoryOne.getObjectType(),
			BudgetCategoryTwo.getObjectType(),
			Task.getObjectType(),
			Measurement.getObjectType(),
			ResourceAssignment.getObjectType(),
			ExpenseAssignment.getObjectType(),
		};
	}
	
	private PlanningTreeConfiguration getRowColumnProvider()
	{
		return rowColumnProvider;
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	private PlanningTreeConfiguration rowColumnProvider;
}
