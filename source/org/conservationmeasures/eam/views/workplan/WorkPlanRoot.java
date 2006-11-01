/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.util.Vector;

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
		return 0;
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
		try
		{
			Vector vector = new Vector();
			vector.add(new WorkPlanStrategyRoot(project));
			vector.add(new WorkPlanMonitoringRoot(project));
			children = vector;
		}
		catch(Exception e)
		{
		}
	}

	Vector children;
	Project project;
}
