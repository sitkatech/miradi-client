/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanRoot extends TreeTableNode
{
	public WorkPlanRoot(Project projectToUse)
	{
		project = projectToUse;
		rebuild();
	}

	public TreeTableNode getChild(int index)
	{
		return (TreeTableNode)children.get(index);
	}

	public int getChildCount()
	{
		return children.size();
	}

	public int getType()
	{
		return ObjectType.MODEL_NODE;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	public String toString()
	{
		return null;
	}

	private void rebuild()
	{
		Vector vector = new Vector();
		vector.add(new WorkPlanStrategyRoot(project));
		vector.add(new WorkPlanMonitoringRoot(project));
		children = vector;
	}

	Vector children;
	Project project;
}
