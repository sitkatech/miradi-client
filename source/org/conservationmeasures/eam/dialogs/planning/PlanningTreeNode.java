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
		ORefList subNodeObjectRefs = new SubNodeRetriever().retrieveSubNodes(nodeObject);
		
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
	
	public class SubNodeRetriever
	{
		public SubNodeRetriever()
		{
			
		}
		
		public ORefList retrieveSubNodes(BaseObject node)
		{
			switch (node.getType())
			{
				case ObjectType.PROJECT_METADATA :
					return ((ProjectMetadata) node).getAllGoalRefs();
				
				case ObjectType.GOAL :
					return ((Goal) node).getObjectivesUpstreamOfGoal();
				
				case ObjectType.OBJECTIVE :
					return ((Objective) node).getRelatedStrategies();
					
				case ObjectType.STRATEGY :
					return ((Strategy) node).getActivities();
				
				case ObjectType.TASK :
					return ((Task) node).getSubtasks();
				
				default :
					throw new RuntimeException("Could not find sub node of type " + node.getType());
			}
		}
	}
	
	Project project;
	TreeTableNode[] subNodes;
	ORef nodeRef;
	BaseObject nodeObject;
}
