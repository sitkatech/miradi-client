/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeRoot extends TreeTableNode
{
	public PlanningTreeRoot(Project projectToUse, ORef nodeRefToUse)
	{
		project = projectToUse;
		nodeRef = nodeRefToUse;
		rebuild();
	}
	
	public TreeTableNode getChild(int index)
	{
		return goalNodes[index];
	}

	public int getChildCount()
	{
		return goalNodes.length;
	}

	public BaseObject getObject()
	{
		return null;
	}

	public ORef getObjectReference()
	{
		return project.getMetadata().getRef();
	}

	public int getType()
	{
		return ProjectMetadata.getObjectType();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild()
	{
		ORefList goalRefs = project.getMetadata().getAllGoalRefs();
		goalNodes = new PlanningTreeGoalNode[goalRefs.size()]; 
		for (int i = 0; i < goalRefs.size(); ++i)
		{
			ORef goalRef = goalRefs.get(i);
			Goal goal = (Goal) project.findObject(goalRef);
			goalNodes[i] = new PlanningTreeGoalNode(project, goal);
		}
	}

	public String toString()
	{
		//FIXME plannig - come up with better name (if visible)
		return EAM.text("root for now");
	}
	
	public class SubNodeRetriever
	{
		public SubNodeRetriever()
		{
			
		}
		
		//FIXME planning - finish this method
		public ORefList retrieveSubNodes(ORef nodeRefToUse)
		{
			switch (nodeRefToUse.getObjectType())
			{
				case ObjectType.GOAL :
					return new ORefList();
				
				default :
					throw new RuntimeException("Could not find sub node of type " + nodeRefToUse.getObjectType());
			}
		}
	}
	
	Project project;
	TreeTableNode[] goalNodes;
	ORef nodeRef;
}
