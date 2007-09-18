/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewControlPanel extends JPanel
{
	public PlanningViewControlPanel(MainWindow mainWindowToUse, PlanningTreeTable treeAsObjectPicker) throws Exception
	{
		super(new BasicGridLayout(2, 1));
		mainWindow = mainWindowToUse;
		setBackground(AppPreferences.CONTROL_PANEL_BACKGROUND);

		addLegendComponents(treeAsObjectPicker);
	}
	
	private void addLegendComponents(PlanningTreeTable treeAsObjectPicker) throws Exception
	{
		planningCustomizationPanel = new PlanningViewCustomizationPanel(getProject());
		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow(), treeAsObjectPicker);
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		
		add(rowsLegendPanel.createTitleBar(EAM.text("Control Bar")));
		add(planningCustomizationPanel);
		planningCustomizationPanel.add(createCustomizationButtonPanel());		
		add(rowsLegendPanel);
		add(columnsLegendPanel);
	}

	private JPanel createCustomizationButtonPanel()
	{
		JPanel customizationButtonPanel = new JPanel(new BasicGridLayout(3, 0));
		customizationButtonPanel.add(createLegendButton(mainWindow.getActions().get(ActionCreatePlanningViewConfiguration.class)));
		customizationButtonPanel.add(createLegendButton(mainWindow.getActions().get(ActionRenamePlanningViewConfiguration.class)));
		customizationButtonPanel.add(createLegendButton(mainWindow.getActions().get(ActionDeletePlanningViewConfiguration.class)));
		
		return customizationButtonPanel;
	}
	
	private UiButton createLegendButton(EAMAction action)
	{
		UiButton button = new UiButton(action);
		return button;
	}
	
	public void dispose()
	{
		rowsLegendPanel.dispose();
		columnsLegendPanel.dispose();
		planningCustomizationPanel.dispose();
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}

	private MainWindow mainWindow;
	private AbstractPlanningViewLegendPanel rowsLegendPanel;
	private PlanningViewCustomizationPanel planningCustomizationPanel;
	private PlanningViewColumsLegendPanel columnsLegendPanel;
}
