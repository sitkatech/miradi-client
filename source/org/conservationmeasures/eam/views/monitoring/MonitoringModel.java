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

public class MonitoringModel extends GenericTreeTableModel
{
	public MonitoringModel(Project projectToUse) throws Exception
	{
		super(new MonitoringRootNode(projectToUse));
		project = projectToUse;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.INDICATOR, MonitoringIndicatorNode.COLUMN_TAGS[column]);
	}

	public int getColumnCount()
	{
		return MonitoringIndicatorNode.COLUMN_TAGS.length;
	}
	
	Project project;
}
