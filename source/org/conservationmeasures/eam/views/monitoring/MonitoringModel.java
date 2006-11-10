/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableNode;

public class MonitoringModel extends GenericTreeTableModel
{
	public MonitoringModel(Project projectToUse)
	{
		super(new MonitoringRootNode(projectToUse));
		project = projectToUse;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.INDICATOR, columnTags[column]);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}
	
	public TreeTableNode getCurrentExpandedNode()
	{
		return null;
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
