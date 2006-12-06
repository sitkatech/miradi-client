/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
import org.martus.swing.UiScrollPane;

public class BudgetManagementPanel extends ModelessDialogPanel
{
	public BudgetManagementPanel(MainWindow mainWindow, Project project, BudgetPropertiesPanel propertiesPanelToUse, WorkPlanPanel treeTableComponentToUse)
	{
		propertiesPanel = propertiesPanelToUse;
		treeTableComponent = treeTableComponentToUse;
		add(treeTableComponent, BorderLayout.CENTER);
		
		treeTableComponent.setPropertiesPanel(propertiesPanel);
		add(new UiScrollPane(propertiesPanel), BorderLayout.AFTER_LAST_LINE);
	}

	public void dispose()
	{
		treeTableComponent.dispose();
		treeTableComponent = null;

		propertiesPanel.dispose();
		propertiesPanel = null;

		super.dispose();
	}

	public EAMObject getObject()
	{
		return treeTableComponent.getSelectedObject().getObject();
	}

	public String getPanelDescription()
	{
		return EAM.text("Budget management");
	}

	WorkPlanPanel treeTableComponent;
	BudgetPropertiesPanel propertiesPanel;
}
