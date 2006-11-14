/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class MonitoringTreeTable extends TreeTableWithStateSaving
{
	public MonitoringTreeTable(Project projectToUse, GenericTreeTableModel monitoringModelToUse)
	{
		super(projectToUse, monitoringModelToUse);
	}
}