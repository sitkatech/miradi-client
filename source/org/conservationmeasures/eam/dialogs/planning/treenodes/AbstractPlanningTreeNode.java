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
import org.conservationmeasures.eam.objects.Factor;
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

	protected void addMissingChildren(ORefList potentialChildRefs) throws Exception
	{
		HashSet<ORef> everythingInTree = getAllRefsInTree();
		for(int i = 0; i < potentialChildRefs.size(); ++i)
		{
			ORef ref = potentialChildRefs.get(i);
			if(everythingInTree.contains(ref))
				continue;
			
			createAndAddChild(ref, null);
		}
	}
	
	protected void createAndAddChildren(ORefList refsToAdd, DiagramObject diagram) throws Exception
	{
		for(int i = 0; i < refsToAdd.size(); ++i)
			createAndAddChild(refsToAdd.get(i), diagram);
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

	protected ORefList extractNonDraftStrategyRefs(Factor[] factors)
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList upstreamStrategyRefs = new ORefList();
		for(int i = 0; i < factors.length; ++i)
		{
			Factor factor = factors[i];
			if(!factor.isStrategy())
				continue;
			
			if(factor.isStatusDraft())
				continue;
			
			upstreamStrategyRefs.add(factor.getRef());
		}
		return upstreamStrategyRefs;
	}

	protected ORefList extractIndicatorRefs(Factor[] upstreamFactors)
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList potentialChildIndicatorRefs = new ORefList();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			ORefList indicatorRefs = new ORefList(Indicator.getObjectType(), factor.getDirectOrIndirectIndicators());
			potentialChildIndicatorRefs.addAll(indicatorRefs);
		}
		return potentialChildIndicatorRefs;
	}

	protected ORefList extractObjectiveRefs(Factor[] upstreamFactors)
	{
		// FIXME: Probably should use a HashSet to avoid dupes
		ORefList potentialChildObjectiveRefs = new ORefList();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			ORefList objectiveRefs = new ORefList(Objective.getObjectType(), factor.getObjectives());
			potentialChildObjectiveRefs.addAll(objectiveRefs);
		}
		return potentialChildObjectiveRefs;
	}

	protected void addMissingUpstreamObjectives(DiagramObject diagram) throws Exception
	{
		addMissingChildren(extractObjectiveRefs(getObject().getUpstreamFactors(diagram)));
	}

	protected void addMissingUpstreamNonDraftStrategies(DiagramObject diagram) throws Exception
	{
		addMissingChildren(extractNonDraftStrategyRefs(getObject().getUpstreamFactors(diagram)));
	}

	protected void addMissingUpstreamIndicators(DiagramObject diagram) throws Exception
	{
		addMissingChildren(extractIndicatorRefs(getObject().getUpstreamFactors(diagram)));
	}

	protected Project project;
	protected Vector<AbstractPlanningTreeNode> children;
}
