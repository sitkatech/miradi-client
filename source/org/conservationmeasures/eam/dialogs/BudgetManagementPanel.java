/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTreeTablePanel;

public class BudgetManagementPanel extends VerticalSplitPanel
{
	public BudgetManagementPanel(MainWindow mainWindow, Project project, BudgetPropertiesPanel propertiesPanelToUse, BudgetTreeTablePanel treeTableComponentToUse)
	{
		super(mainWindow);
		propertiesPanel = propertiesPanelToUse;
		treeTableComponent = treeTableComponentToUse;
		treeTableComponent.setPropertiesPanel(propertiesPanel.getInputPanel());
		
		setComponentPreferredSize(propertiesPanel);
		setComponentPreferredSize(treeTableComponent);
		
		createVerticalSplitPane(treeTableComponent, propertiesPanel);
	}

	public void dispose()
	{
		// TODO: This should probably be disposed by whoever created it, not by this class
		treeTableComponent.dispose();
		treeTableComponent = null;

		propertiesPanel = null;

		super.dispose();
	}

	public EAMObject getObject()
	{
		return treeTableComponent.getSelectedObject().getObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Budget");
	}

	BudgetTreeTablePanel treeTableComponent;
	BudgetPropertiesPanel propertiesPanel;
}
