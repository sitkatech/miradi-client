/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringIndicatorNode extends MonitoringNode
{
	public MonitoringIndicatorNode(Project projectToUse, Indicator indicatorToUse)
	{
		project = projectToUse;
		indicator = indicatorToUse;
	}
	
	public EAMObject getObject()
	{
		return indicator;
	}

	public ORef getObjectReference()
	{
		return indicator.getRef();
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
		if(column == COLUMN_TARGETS)
			return getChainManager().getRelatedTargetsAsHtml(indicator.getId());
		if(column == COLUMN_THREATS)
			return getChainManager().getRelatedDirectThreatsAsHtml(indicator.getId());
		if(column == COLUMN_ITEM_LABEL)
			return indicator.toString();
		return null;
	}

	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	public void rebuild() throws Exception
	{
	}
	
	Project project;
	Indicator indicator;
	
}
