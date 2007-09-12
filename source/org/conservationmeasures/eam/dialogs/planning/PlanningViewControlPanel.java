/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewControlPanel extends JPanel
{
	public PlanningViewControlPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(new BasicGridLayout(2, 1));
		mainWindow = mainWindowToUse;
		addLegendComponents();
	}
	
	private void addLegendComponents() throws Exception
	{
		planningCustomizationPanel = new PlanningViewCustomizationPanel(getProject());
		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow());
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		
		add(rowsLegendPanel.createTitleBar(EAM.text("Control Bar")));
		add(planningCustomizationPanel);
		add(new UiButton(mainWindow.getActions().get(ActionCreatePlanningViewConfiguration.class)));
		add(new UiButton(mainWindow.getActions().get(ActionRenamePlanningViewConfiguration.class)));
		add(new UiButton(mainWindow.getActions().get(ActionDeletePlanningViewConfiguration.class)));		
		add(rowsLegendPanel);
		add(columnsLegendPanel);
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
