/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;

public class BudgetTreeTablePanel extends TaskTreeTablePanel
{
	public static BudgetTreeTablePanel createBudgetTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse) throws Exception
	{
		BudgetTreeTableModel model = new BudgetTreeTableModel(projectToUse);
		BudgetTreeTable tree = new BudgetTreeTable(projectToUse, model);
		return new BudgetTreeTablePanel(mainWindowToUse, projectToUse,tree, model);
	}
	
	private BudgetTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, BudgetTreeTable treeToUse, BudgetTreeTableModel modelToUse)
	{
		super(mainWindowToUse, projectToUse, treeToUse);
		model = modelToUse;
	}
}
