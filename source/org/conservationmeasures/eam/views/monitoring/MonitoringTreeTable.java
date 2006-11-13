/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;


import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

public class MonitoringTreeTable extends TreeTableWithIcons
{
	public MonitoringTreeTable(Project projectToUse, GenericTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(projectToUse, monitoringModelToUse, tree);
	}
	
	public EAMTreeTableModelAdapter getModelAdapter()
	{
		return treeTableModelAdapter;
	}
	
	private EAMTreeTableModelAdapter treeTableModelAdapter;
}
