/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.BoldedStrategyIndicatorTableTree;

public class WorkPlanTreeTable extends BoldedStrategyIndicatorTableTree 
{
	public WorkPlanTreeTable(Project projectToUse, WorkPlanTreeTableModel monitoringModelToUse)
	{
		super(projectToUse, monitoringModelToUse);
		setModel(treeTableModelAdapter);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
	}
}
