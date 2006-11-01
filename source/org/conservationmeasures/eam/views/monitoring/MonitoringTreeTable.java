/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;


import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

public class MonitoringTreeTable extends TreeTableWithIcons
{
	public MonitoringTreeTable(GenericTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		monitoringModel = monitoringModelToUse;
	}

	public GenericTreeTableModel monitoringModel;
}
