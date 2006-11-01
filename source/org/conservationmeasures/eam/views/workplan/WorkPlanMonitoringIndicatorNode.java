/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;

public class WorkPlanMonitoringIndicatorNode extends WorkPlanNode
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
		return indicator.getLabel();
	}

	public int getChildCount()
	{
		return 0;
	}

	public Object getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		return null;
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	Project project;
	Indicator indicator;
}
