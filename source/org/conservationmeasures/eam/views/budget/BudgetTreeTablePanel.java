/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import java.awt.BorderLayout;

import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.conservationmeasures.eam.views.workplan.WorkPlanTreeTable;
import org.martus.swing.UiScrollPane;

public class BudgetTreeTablePanel extends WorkPlanPanel
{
	public BudgetTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse)
	{
		super(mainWindowToUse, projectToUse);
		model = new BudgetTreeTableModel(projectToUse);
		tree = new WorkPlanTreeTable(projectToUse, model);
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.getTree().setShowsRootHandles(true);
		tree.getTree().addTreeSelectionListener(this);
		restoreTreeExpansionState();
		
		UiScrollPane uiScrollPane = new UiScrollPane(tree);
		add(uiScrollPane, BorderLayout.CENTER);
		add(createButtonBox(mainWindowToUse.getActions()), BorderLayout.AFTER_LAST_LINE);

		tree.getTree().addSelectionRow(0);
		mainWindowToUse.getProject().addCommandExecutedListener(this);
	}
}
