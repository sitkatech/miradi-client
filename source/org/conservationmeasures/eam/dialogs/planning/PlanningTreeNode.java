/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeNode extends TreeTableNode
{
	public PlanningTreeNode(Project projectToUse, ORef nodeRefToUse)
	{
		project = projectToUse;
		nodeRef = nodeRefToUse;
		rebuild();
	}
	
	public TreeTableNode getChild(int index)
	{
		return subNodes[index];
	}

	public int getChildCount()
	{
		return subNodes.length;
	}

	public BaseObject getObject()
	{
		return nodeObject;
	}

	public ORef getObjectReference()
	{
		return nodeObject.getRef();
	}

	public int getType()
	{
		return getObjectReference().getObjectType();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild()
	{
		nodeObject = project.findObject(nodeRef);
		ORefList subNodeObjectRefs = retrieveSubNodes(nodeObject);
		
		subNodes = new PlanningTreeNode[subNodeObjectRefs.size()];
		for (int i = 0; i < subNodeObjectRefs.size(); ++i)
		{
			subNodes[i] = new PlanningTreeNode(project, subNodeObjectRefs.get(i));
		}
	}

	public String toString()
	{
		return nodeObject.getLabel();
	}
	
	public ORefList retrieveSubNodes(BaseObject node)
	{
		switch (node.getType())
		{
			case ObjectType.PROJECT_METADATA :
				return getChildrenOfProject(node);
			
			case ObjectType.GOAL :
				return getChildrenOfGoal(node);
			
			case ObjectType.OBJECTIVE :
				return getChildrenOfObjective(node);
				
			case ObjectType.STRATEGY :
				return getChildrenOfStrategy(node);
				
			case ObjectType.INDICATOR:
				return getChildrenOfIndicator(node);
			
			case ObjectType.TASK :
				return getChildrenOfTask(node);
			
			default :
				throw new RuntimeException("Could not find sub node of type " + node.getType());
		}
	}

	private ORefList getChildrenOfProject(BaseObject node)
	{
		return ((ProjectMetadata) node).getAllGoalRefs();
	}

	private ORefList getChildrenOfGoal(BaseObject node)
	{
		return ((Goal) node).getObjectivesUpstreamOfGoal();
	}

	private ORefList getChildrenOfObjective(BaseObject node)
	{
		ORefList relatedItems = new ORefList();
		relatedItems.addAll(((Objective) node).getRelatedStrategies());
		relatedItems.addAll(((Objective) node).getRelatedIndicators());
		return relatedItems;
	}

	private ORefList getChildrenOfStrategy(BaseObject node)
	{
		return ((Strategy) node).getActivities();
	}

	private ORefList getChildrenOfIndicator(BaseObject node)
	{
		return ((Indicator) node).getMethods();
	}

	private ORefList getChildrenOfTask(BaseObject node)
	{
		return ((Task) node).getSubtasks();
	}
	
	Project project;
	TreeTableNode[] subNodes;
	ORef nodeRef;
	BaseObject nodeObject;
}
