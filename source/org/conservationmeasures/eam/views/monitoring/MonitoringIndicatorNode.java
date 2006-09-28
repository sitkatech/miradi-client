/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.strategicplan.IndicatorTableModel;

public class MonitoringIndicatorNode extends MonitoringNode
{
	public MonitoringIndicatorNode(Project projectToUse, Indicator indicatorToUse)
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
			return getChainManager().getRelatedTargetsAsHtml(indicator.getId());
		if(column == COLUMN_THREATS)
			return getChainManager().getRelatedDirectThreatsAsHtml(indicator.getId());
		if(column == COLUMN_METHODS)
			return indicator.getData(Indicator.TAG_METHOD);
		if(MonitoringModel.columnTags[column].equals(Indicator.TAG_RESOURCE_IDS))
		{
			ProjectResource[] resources = IndicatorTableModel.getResourcesForIndicator(project, indicator);
			return ProjectResource.getResourcesAsHtml(resources);
		}
		return null;
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	Project project;
	Indicator indicator;
}
