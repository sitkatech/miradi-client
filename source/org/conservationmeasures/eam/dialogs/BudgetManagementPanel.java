/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTreeTablePanel;

public class BudgetManagementPanel extends ModelessDialogPanel
{
	public BudgetManagementPanel(MainWindow mainWindow, Project project, BudgetPropertiesPanel propertiesPanelToUse, BudgetTreeTablePanel treeTableComponentToUse)
	{
		propertiesPanel = propertiesPanelToUse;
		treeTableComponent = treeTableComponentToUse;

		addSplitPane();
	}

	private void addSplitPane()
	{
		JScrollPane treeComponentScroll = new JScrollPane(treeTableComponent);
		JScrollPane propertiesScroll = new JScrollPane(propertiesPanel);
		
		treeTableComponent.setPropertiesPanel(propertiesPanel.getInputPanel());
		JSplitPane splitter = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitter.setOneTouchExpandable(true);
		splitter.setDividerSize(15);
		splitter.setResizeWeight(.5);
		splitter.setTopComponent(treeComponentScroll);
		splitter.setBottomComponent(propertiesScroll);
		splitter.setDividerLocation(0.5);
		add(splitter, BorderLayout.CENTER);
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

	BudgetTreeTablePanel treeTableComponent;
	BudgetPropertiesPanel propertiesPanel;
}
