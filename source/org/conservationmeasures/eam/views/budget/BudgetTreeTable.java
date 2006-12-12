/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class BudgetTreeTable extends TreeTableWithStateSaving
{
	public BudgetTreeTable(Project projectToUse, BudgetTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		setModel(treeTableModelAdapter);
		setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		getTree().setShowsRootHandles(true);
	}

}
