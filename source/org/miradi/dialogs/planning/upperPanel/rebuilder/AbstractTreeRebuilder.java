/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.util.Vector;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTaskNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeBaseObjectNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeErrorNode;
import org.miradi.dialogs.progressReport.FieldComparator;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.*;
import org.miradi.utils.CodeList;

abstract public class AbstractTreeRebuilder
{
	public AbstractTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		project = projectToUse;
		rowColumnProvider = rowColumnProviderToUse;
	}
	
	public void rebuildTree(AbstractPlanningTreeNode rootNode) throws Exception
	{
		CodeList rows = getRowColumnProvider().getRowCodesToShow();
		rebuildTree(null, rootNode, null);
		removeUnwantedLayersAndPromoteChildren(rootNode, rows);
		deleteUnclesAndTheirChildren(rootNode);
	}

	private void rebuildTree(AbstractPlanningTreeNode grandparentNode, AbstractPlanningTreeNode parentNode, DiagramObject diagram)
	{
		try
		{
			parentNode.clearChildren();
			ORef parentRef = parentNode.getObjectReference();
			if(DiagramObject.isDiagramObject(parentRef))
				diagram = DiagramObject.findDiagramObject(getProject(), parentRef);

            ORef grandparentRef = (grandparentNode != null) ? grandparentNode.getObjectReference() : ORef.INVALID;

			ORefList candidateChildRefs = getChildRefs(grandparentRef, parentNode.getObjectReference(), diagram);
			candidateChildRefs.addAll(parentNode.getObject().getResourceAssignmentRefs());
			candidateChildRefs.addAll(parentNode.getObject().getExpenseAssignmentRefs());
			ORefList childRefs = getListWithoutChildrenThatWouldCauseRecursion(parentNode, candidateChildRefs);
			createAndAddChildren(parentNode, childRefs);

			for(int index = 0; index < parentNode.getChildCount(); ++index)
			{
				AbstractPlanningTreeNode childNode = (AbstractPlanningTreeNode) parentNode.getChild(index);
				rebuildTree(parentNode, childNode, diagram);
			}
		}
		catch(Exception e)
		{
			EAM.panic(e);
		}
	}

	private ORefList getListWithoutChildrenThatWouldCauseRecursion(AbstractPlanningTreeNode parentNode, ORefList candidateChildRefs) throws Exception
	{
		TreeTableNode node = parentNode;
		ORefList hierarchySoFar = new ORefList();
		while(node != null)
		{
			hierarchySoFar.add(node.getObjectReference());
			node = node.getParentNode();
		}
	
		ORefList remainingChildren = new ORefList();
		for(int i = 0; i < candidateChildRefs.size(); ++i)
		{
			ORef childRef = candidateChildRefs.get(i);
			if(!hierarchySoFar.contains(childRef))
				remainingChildren.add(childRef);
		}
		return remainingChildren;
	}

	protected boolean shouldTargetsBeAtSameLevelAsDiagrams() throws Exception
	{
		return getRowColumnProvider().shouldPutTargetsAtTopLevelOfTree();
	}
	
	public static ORefList findRelevantGoals(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, GoalSchema.getObjectType());
	}

	public static ORefList findRelevantObjectives(Project projectToUse, ORef strategyRef) throws Exception
	{
		return Desire.findRelevantDesires(projectToUse, strategyRef, ObjectiveSchema.getObjectType());
	}
	
	private void createAndAddChildren(AbstractPlanningTreeNode parent, ORefList childRefsToAdd) throws Exception
	{
		for(int index = 0; index < childRefsToAdd.size(); ++index)
		{
			ORef childRef = childRefsToAdd.get(index);
			createAndAddChild(parent, childRef);
		}
	}

	private void createAndAddChild(AbstractPlanningTreeNode parent, ORef childRefToAdd) throws Exception
	{
		AbstractPlanningTreeNode childNode = createChildNode(parent, childRefToAdd);
		parent.addChild(childNode);
	}

	private AbstractPlanningTreeNode createChildNode(AbstractPlanningTreeNode parentNode, ORef refToAdd) throws Exception
	{
		int[] supportedTypes = new int[] {
			ConceptualModelDiagramSchema.getObjectType(),
			ResultsChainDiagramSchema.getObjectType(),
			
			StrategySchema.getObjectType(),
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			KeyEcologicalAttributeSchema.getObjectType(),

            BiophysicalFactorSchema.getObjectType(),
            BiophysicalResultSchema.getObjectType(),

			CauseSchema.getObjectType(),
			IntermediateResultSchema.getObjectType(),
			ThreatReductionResultSchema.getObjectType(),
			
			SubTargetSchema.getObjectType(),
			GoalSchema.getObjectType(),
			ObjectiveSchema.getObjectType(),
			IndicatorSchema.getObjectType(),
			TaskSchema.getObjectType(),
			
			MeasurementSchema.getObjectType(),
			ResourceAssignmentSchema.getObjectType(),
			ExpenseAssignmentSchema.getObjectType(),
			FutureStatusSchema.getObjectType(),
		};
		
		try
		{
			if(Task.is(refToAdd))
				return createPlanningTaskNode(getProject(), parentNode.getContextRef(), parentNode, refToAdd);
			
			int type = refToAdd.getObjectType();
			for(int i = 0; i < supportedTypes.length; ++i)
			{
				if(type == supportedTypes[i])
					return new PlanningTreeBaseObjectNode(getProject(), parentNode, refToAdd);
			}
			throw new Exception("Attempted to create node of unknown type: " + refToAdd);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return new PlanningTreeErrorNode(getProject(), parentNode, refToAdd);
		}
	}
	
	private void deleteUnclesAndTheirChildren(AbstractPlanningTreeNode rootNode)
	{
		Vector<AbstractPlanningTreeNode> childrenToKeep = new Vector<AbstractPlanningTreeNode>();
		for(AbstractPlanningTreeNode childNode : rootNode.getRawChildrenByReference())
		{
			boolean keepThisChild = true;
			for(AbstractPlanningTreeNode otherChildNode : rootNode.getRawChildrenByReference())
			{
				if(childNode.equals(otherChildNode))
				{
					continue;
				}
				if(otherChildNode.getRawChildrenByReference().contains(childNode))
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

	private void removeUnwantedLayersAndPromoteChildren(AbstractPlanningTreeNode node, CodeList objectTypesToShow)
	{
		if(node.isAnyChildAllocated())
			node.setAllocated();
		
		Vector<AbstractPlanningTreeNode> newChildren = new Vector<AbstractPlanningTreeNode>();
		for(int i = 0; i < node.getChildCount(); ++i)
		{
			AbstractPlanningTreeNode child = (AbstractPlanningTreeNode) node.getChild(i);
			removeUnwantedLayersAndPromoteChildren(child, objectTypesToShow);
			
			if(isVisible(objectTypesToShow, child))
			{
				mergeChildIntoList(newChildren, child);
			}
			else
			{
				addChildrenOfNodeToList(newChildren, child);
			}
		}

		sortChildren(node, newChildren);
		
		node.setRawChildren(newChildren);
	}

	protected boolean isVisible(CodeList objectTypesToShow, AbstractPlanningTreeNode child)
	{
		return objectTypesToShow.contains(child.getObjectTypeName());
	}
	
	private void mergeChildIntoList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode newChild)
	{
		AbstractPlanningTreeNode existingNode = findNodeWithRef(destination, newChild.getObjectReference());
		if(existingNode == null)
		{
			if (isChildOfAnyNodeInList(destination, newChild))
				return;

			destination.add(newChild);
			return;
		}
		
		destination = existingNode.getRawChildrenByReference();
		addChildrenOfNodeToList(destination, newChild);

		existingNode.addProportionShares(newChild);

		sortChildren(existingNode, destination);
	}

	protected void sortChildren(AbstractPlanningTreeNode parentNode, Vector<AbstractPlanningTreeNode> childNodes)
	{
		Collections.sort(childNodes, createNodeSorter(parentNode.getObjectReference()));
	}
	
	private boolean isChildOfAnyNodeInList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode newChild)
	{
		for(AbstractPlanningTreeNode parentNode : destination)
		{
			Vector<AbstractPlanningTreeNode> children = parentNode.getRawChildrenByReference();
			AbstractPlanningTreeNode foundMatchingChild = findNodeWithRef(children, newChild.getObjectReference());
			if (foundMatchingChild != null)
				return true;
		}
		
		return false;
	}
	
	private AbstractPlanningTreeNode findNodeWithRef(Vector<AbstractPlanningTreeNode> list, ORef ref)
	{
		for(AbstractPlanningTreeNode node : list)
		{
			if(ref.equals(node.getObjectReference()))
				return node;
		}
		
		return null;
	}
	
	protected void addChildrenOfNodeToList(Vector<AbstractPlanningTreeNode> destination, AbstractPlanningTreeNode otherNode)
	{
		for(AbstractPlanningTreeNode newChild : otherNode.getRawChildrenByReference())
		{
			mergeChildIntoList(destination, newChild);
		}
	}

	private NodeSorter createNodeSorter(ORef parentRefToUse)
	{
		return new TreeRebuilderNodeSorter(parentRefToUse);
	}

	protected PlanningTreeRowColumnProvider getRowColumnProvider()
	{
		return rowColumnProvider;
	}

	protected Project getProject()
	{
		return project;
	}
	
	public ORefList getSortedByDateMeasurementRefs(Indicator indicator)
	{
		return getSortedByFieldRefs(getProject(), indicator.getMeasurementRefs(), Measurement.TAG_DATE);
	}

	public static ORefList getSortedByFieldRefs(Project projectToUse, final ORefList refs, final String tag)
	{
		Vector<BaseObject> baseObjects = new Vector<BaseObject>();
		for(int index = 0; index < refs.size(); ++index)
		{
			baseObjects.add(BaseObject.find(projectToUse, refs.get(index)));
		}
		
		Collections.sort(baseObjects, new FieldComparator(tag));
		
		return new ORefList(baseObjects);
	}
	
	abstract protected ORefList getChildRefs(ORef grandparentRef, ORef parentRef, DiagramObject diagram) throws Exception;

	protected PlanningTaskNode createPlanningTaskNode(Project project, ORef contextNodeRef, TreeTableNode parentNode, ORef objectRef) throws Exception
	{
		return new PlanningTaskNode(project, contextNodeRef, parentNode, objectRef);
	}

	public static void dumpTreeToConsole(TreeTableNode node, int level)
	{
		for(int indent = 0; indent < level; ++indent)
			System.out.print("  ");

		ORef ref = node.getObjectReference();
		System.out.println(node.getObject().getTypeName()  + " (" + ref.getObjectId() + ") " + node.toString());
		for(int child = 0; child < node.getChildCount(); ++child)
			dumpTreeToConsole(node.getChild(child), level + 1);
	}


	private Project project;
	private PlanningTreeRowColumnProvider rowColumnProvider;
}
