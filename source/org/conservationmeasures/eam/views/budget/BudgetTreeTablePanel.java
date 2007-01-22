/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
