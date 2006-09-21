/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;

public class MonitoringIndicator extends MonitoringNode
{
	public MonitoringIndicator(Project projectToUse, Indicator indicatorToUse)
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
		return indicator.getLabel() + " (" + indicator.getShortLabel() + ")";
	}

	public int getChildCount()
	{
		return 0;
	}

	public MonitoringNode getChild(int index)
	{
		return null;
	}

	public Object getValueAt(int column)
	{
		if(column == COLUMN_TARGETS)
			return getChainManager().getRelatedTargetsAsString(indicator.getId());
		if(column == COLUMN_THREATS)
			return getChainManager().getRelatedDirectThreatsAsString(indicator.getId());
		if(column == COLUMN_METHODS)
			return indicator.getData(Indicator.TAG_METHOD);
		return null;
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	Project project;
	Indicator indicator;
}
