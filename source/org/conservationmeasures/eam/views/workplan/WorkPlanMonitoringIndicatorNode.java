/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class WorkPlanMonitoringIndicatorNode extends WorkPlanTreeTableNode
{
	public WorkPlanMonitoringIndicatorNode(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
	}

	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.toString();
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		return "";
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	public boolean canInsertActivityHere()
	{
		return false;
	}

	public BaseId getId()
	{
		return null;
	}

	public void rebuild()
	{
	}
	
	Project project;
	Indicator indicator;
}
