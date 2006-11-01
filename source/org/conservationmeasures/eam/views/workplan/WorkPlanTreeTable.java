/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;


import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithIcons;

public class WorkPlanTreeTable extends TreeTableWithIcons 
{
	public WorkPlanTreeTable(WorkPlanTreeTableModel monitoringModelToUse)
	{
		super(monitoringModelToUse);
		workPlanTreeTableModel = monitoringModelToUse;
	}

	public void expandEverything()
	{
		TreeTableNode root = (TreeTableNode)getTreeTableModel().getRoot();
		TreePath rootPath = new TreePath(root);
		expandNode(rootPath);
	}
	
	public WorkPlanTreeTableModel getTreeTableModel()
	{
		return workPlanTreeTableModel;
	}

	WorkPlanTreeTableModel workPlanTreeTableModel;
}
