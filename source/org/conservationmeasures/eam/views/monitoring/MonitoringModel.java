/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

import com.java.sun.jtreetable.AbstractTreeTableModel;
import com.java.sun.jtreetable.TreeTableModel;

public class MonitoringModel extends AbstractTreeTableModel
{
	public MonitoringModel(Project projectToUse)
	{
		super(new MonitoringRootNode(projectToUse));
		project = projectToUse;
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.INDICATOR, columnTags[column]);
	}

	public Class getColumnClass(int column)
	{
		if(column == 0)
			return TreeTableModel.class;
		return String.class;
	}
	
	public Object getValueAt(Object rawNode, int column)
	{
		MonitoringNode node = (MonitoringNode)rawNode;
		return node.getValueAt(column);
	}

	public int getChildCount(Object rawNode)
	{
		MonitoringNode node = (MonitoringNode)rawNode;
		return node.getChildCount();
	}

	public Object getChild(Object rawNode, int index)
	{
		MonitoringNode node = (MonitoringNode)rawNode;
		return node.getChild(index);
	}

	public static String[] columnTags = {
		"Item", 
		"Target(s)", 
		"Threat(s)", 
		"Method", 
		"Priority", 
		"Status", 
		"When", 
		"Where", 
		"ResourceIds", 
		"Cost", 
		"Funding",	
	};
	Project project;
}
