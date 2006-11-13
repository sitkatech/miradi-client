/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EAMTreeTableModelAdapter;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

public class WorkPlanTreeTable extends TreeTableWithIcons 
{
	public WorkPlanTreeTable(Project projectToUse, WorkPlanTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		treeTableModelAdapter = new EAMTreeTableModelAdapter(projectToUse, monitoringModelToUse, tree);
		setModel(treeTableModelAdapter);
	}
	
	public EAMTreeTableModelAdapter getModelAdapter()
	{
		return treeTableModelAdapter;
	}
	
	private EAMTreeTableModelAdapter treeTableModelAdapter;

}
