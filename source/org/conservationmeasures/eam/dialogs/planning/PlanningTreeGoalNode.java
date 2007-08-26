/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class PlanningTreeGoalNode extends TreeTableNode
{
	public PlanningTreeGoalNode(Project projectToUse, Goal goalToUse)
	{
		project = projectToUse; 
		goal = goalToUse;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}

	public int getChildCount()
	{
		return 0;
	}

	public BaseObject getObject()
	{
		return goal;
	}

	public ORef getObjectReference()
	{
		return goal.getRef();
	}

	public int getType()
	{
		return Goal.getObjectType();
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public void rebuild() throws Exception
	{
	}

	public String toString()
	{
		return goal.getLabel();
	}
	
	Project project;
	Goal goal;
}
