package org.conservationmeasures.eam.dialogs.planning.treenodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Vector;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.ConceptualModelDiagram;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ResultsChainDiagram;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.TreeTableNode;

public abstract class AbstractPlanningTreeNode extends TreeTableNode
{
	abstract public boolean attemptToAdd(ORef refToAdd) throws Exception;

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

	protected boolean attemptToAddToChildren(ORef refToAdd) throws Exception
	{
		boolean wasAdded = false;
		
		for(int i = 0; i < children.size(); ++i)
		{
			if(children.get(i).attemptToAdd(refToAdd))
				wasAdded = true;
		}
		return wasAdded;
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
			ORef refA = nodeA.getObjectReference();
			ORef refB = nodeB.getObjectReference();

			int typeSortLocationA = getTypeSortLocation(refA);
			int typeSortLocationB = getTypeSortLocation(refB);
			int diff = typeSortLocationA - typeSortLocationB;
			if(diff != 0)
				return diff;
			
			String labelA = project.getObjectData(refA, BaseObject.TAG_LABEL);
			String labelB = project.getObjectData(refB, BaseObject.TAG_LABEL);
			return labelA.compareTo(labelB);
		}
		
		int getTypeSortLocation(ORef ref)
		{
			int[] sortOrder = new int[] {
				ConceptualModelDiagram.getObjectType(),
				ResultsChainDiagram.getObjectType(),
				Goal.getObjectType(),
				Objective.getObjectType(),
				Strategy.getObjectType(),
				Indicator.getObjectType(),
				Task.getObjectType(),
			};
			
			int type = ref.getObjectType();
			for(int i = 0; i < sortOrder.length; ++i)
				if(type == sortOrder[i])
					return i;
			EAM.logError("NodeSorter unknown type: " + type);
			return sortOrder.length;
		}
		
	}
	
	private Vector<AbstractPlanningTreeNode> getChildren()
	{
		return children;
	}

	protected Project project;
	protected Vector<AbstractPlanningTreeNode> children;
}
