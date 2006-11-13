/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ObjectReference;
import org.conservationmeasures.eam.objectpools.ResourcePool;
import org.conservationmeasures.eam.objects.EAMBaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.ProjectResource;
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

	public ObjectReference getObjectReference()
	{
		return indicator.getObjectReference();
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
		if(column == COLUMN_METHODS)
			return indicator.getData(Indicator.TAG_METHOD);
		if(MonitoringModel.columnTags[column].equals(Indicator.TAG_RESOURCE_IDS))
		{
			ProjectResource[] resources = getResourcesForIndicator(project, indicator);
			return EAMBaseObject.toHtml(resources);
		}
		return null;
	}

	public static ProjectResource[] getResourcesForIndicator(Project project, Indicator indicator)
	{
		ResourcePool resourcePool = project.getResourcePool();
		IdList resourceIds = indicator.getResourceIdList();
		ProjectResource[] resources = new ProjectResource[resourceIds.size()];
		for(int i = 0; i < resourceIds.size(); ++i)
			resources[i] = resourcePool.find(resourceIds.get(i));
		return resources;
	}
	
	ChainManager getChainManager()
	{
		return new ChainManager(project);
	}
	
	Project project;
	Indicator indicator;
}
