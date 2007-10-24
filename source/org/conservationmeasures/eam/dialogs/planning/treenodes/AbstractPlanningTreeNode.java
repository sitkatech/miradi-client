/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.TreeTableNode;

public abstract class AbstractPlanningTreeNode extends TreeTableNode
{
	public AbstractPlanningTreeNode(Project projectToUse)
	{
		project = projectToUse;
		children = new Vector();
	}
	
	public ORef getObjectReference()
	{
		if(getObject() == null)
			return ORef.INVALID;
		return getObject().getRef();
	}
	
	public String getObjectTypeName()
	{
		return getObject().getTypeName();
	}
	
	public TreeTableNode getChild(int index)
	{
		return children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild() throws Exception
	{
		throw new Exception("Can't call rebuild on " + getClass().getCanonicalName());
	}

	public String toString()
	{
		return getObject().getLabel();
	}

	protected HashSet<ORef> getAllRefsInTree()
	{
		HashSet<ORef> refs = new HashSet();
		for(int i = 0; i < children.size(); ++i)
		{
			AbstractPlanningTreeNode child = children.get(i);
			refs.addAll(child.getAllRefsInTree());
		}
		
		refs.add(getObjectReference());
		
		return refs;
	}
	
	protected ORefList getPotentialChildrenStrategyRefs()
	{
		return new ORefList();
	}

	protected ORefList getPotentialChildrenIndicatorRefs()
	{
		return new ORefList();
	}
	
	protected void pruneUnwantedLayers(CodeList objectTypesToShow)
	{
		Vector<AbstractPlanningTreeNode> newChildren = new Vector();
		HashSet<ORef> newChildRefs = new HashSet();
		for(int i = 0; i < children.size(); ++i)
		{
			AbstractPlanningTreeNode child = children.get(i);
			child.pruneUnwantedLayers(objectTypesToShow);
			boolean isChildVisible = objectTypesToShow.contains(child.getObjectTypeName());
			if(isChildVisible)
			{
				if(!newChildRefs.contains(child.getObjectReference()))
				{
					newChildren.add(child);
					newChildRefs.add(child.getObjectReference());
				}
			}
			else
			{
				for(int grandchild = 0; grandchild < child.getChildCount(); ++grandchild)
				{
					AbstractPlanningTreeNode newChild = child.getChildren().get(grandchild);
					if(!newChildRefs.contains(newChild.getObjectReference()))
					{
						newChildren.add(newChild);
						newChildRefs.add(newChild.getObjectReference());
					}
				}
			}
		}
		if(shouldSortChildren())
			Collections.sort(newChildren, new NodeSorter());
		children = newChildren;
	}
	
	boolean shouldSortChildren()
	{
		return true;
	}

	class NodeSorter implements Comparator<AbstractPlanningTreeNode>
	{
		public int compare(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
		{

			int typeSortLocationA = getTypeSortLocation(nodeA.getType());
			int typeSortLocationB = getTypeSortLocation(nodeB.getType());
			int diff = typeSortLocationA - typeSortLocationB;
			if(diff != 0)
				return diff;
			
			ORef refA = nodeA.getObjectReference();
			ORef refB = nodeB.getObjectReference();
			String labelA = project.getObjectData(refA, BaseObject.TAG_LABEL);
			String labelB = project.getObjectData(refB, BaseObject.TAG_LABEL);
			return labelA.compareTo(labelB);
		}
		
		int getTypeSortLocation(int type)
		{
			int[] sortOrder = getNodeSortOrder();
			
			for(int i = 0; i < sortOrder.length; ++i)
				if(type == sortOrder[i])
					return i;
			EAM.logError("NodeSorter unknown type: " + type);
			return sortOrder.length;
		}

	}
	
	int[] getNodeSortOrder()
	{
		return new int[] {
			ConceptualModelDiagram.getObjectType(),
			ResultsChainDiagram.getObjectType(),
			Goal.getObjectType(),
			Objective.getObjectType(),
			Strategy.getObjectType(),
			Indicator.getObjectType(),
			Task.getObjectType(),
		};
	}
	
	private Vector<AbstractPlanningTreeNode> getChildren()
	{
		return children;
	}

	protected void addMissingStrategiesAsChildren() throws Exception
	{
		HashSet<ORef> everythingInTree = getAllRefsInTree();
		ORefList upstreamStrategyRefs = getPotentialChildrenStrategyRefs();
		for(int i = 0; i < upstreamStrategyRefs.size(); ++i)
		{
			ORef ref = upstreamStrategyRefs.get(i);
			if(everythingInTree.contains(ref))
				continue;
			
			children.add(new PlanningTreeStrategyNode(project, ref));
		}
	}

	protected void addMissingIndicatorsAsChildren() throws Exception
	{
		HashSet<ORef> everythingInTree = getAllRefsInTree();
		ORefList potentialChildrenIndicatorRefs = getPotentialChildrenIndicatorRefs();
		for(int i = 0; i < potentialChildrenIndicatorRefs.size(); ++i)
		{
			ORef ref = potentialChildrenIndicatorRefs.get(i);
			if(everythingInTree.contains(ref))
				continue;
			
			children.add(new PlanningTreeIndicatorNode(project, ref));
		}
	}
	
	protected void createAndAddChild(ORef refToAdd, DiagramObject diagram) throws Exception
	{
		children.add(createChildNode(refToAdd, diagram));
	}

	private AbstractPlanningTreeNode createChildNode(ORef refToAdd, DiagramObject diagram) throws Exception
	{
		int type = refToAdd.getObjectType();
		if(type == ConceptualModelDiagram.getObjectType())
			return new PlanningTreeConceptualModelPageNode(project, refToAdd);
		if(type == ResultsChainDiagram.getObjectType())
			return new PlanningTreeResultsChainNode(project, refToAdd);
		if(type == Target.getObjectType())
			return new PlanningTreeTargetNode(project, diagram, refToAdd);
		if(type == Goal.getObjectType())
			return new PlanningTreeGoalNode(project, diagram, refToAdd);
		if(type == Objective.getObjectType())
			return new PlanningTreeObjectiveNode(project, diagram, refToAdd);
		if(type == Strategy.getObjectType())
			return new PlanningTreeStrategyNode(project, refToAdd);
		if(type == Indicator.getObjectType())
			return new PlanningTreeIndicatorNode(project, refToAdd);
		if(type == Task.getObjectType())
			return new PlanningTreeTaskNode(project, refToAdd);
		
		
		throw new Exception("Attempted to create node of unknown type: " + refToAdd);
	}

	protected Project project;
	protected Vector<AbstractPlanningTreeNode> children;
}
