/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.planning.legend;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.martus.swing.UiButton;
import org.miradi.actions.ActionCreatePlanningViewConfiguration;
import org.miradi.actions.ActionDeletePlanningViewConfiguration;
import org.miradi.actions.ActionRenamePlanningViewConfiguration;
import org.miradi.actions.EAMAction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.MainWindow;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.planning.PlanningView;

import com.jhlabs.awt.BasicGridLayout;

public class PlanningViewControlPanel extends ControlPanel implements CommandExecutedListener
{
	public PlanningViewControlPanel(MainWindow mainWindowToUse, PlanningTreeTable treeAsObjectPicker) throws Exception
	{
		mainWindow = mainWindowToUse;
		
		createCustomizationButtons();
		addLegendComponents(treeAsObjectPicker);
		
		ViewData viewData = getProject().getCurrentViewData();
		updateVisibility(viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE));
		getProject().addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
		rowsLegendPanel.dispose();
		columnsLegendPanel.dispose();
		planningCustomizationPanel.dispose();
	}
		
	private void addLegendComponents(PlanningTreeTable treeAsObjectPicker) throws Exception
	{
		planningCustomizationPanel = new PlanningViewCustomizationPanel(getProject());
		planningCustomizationPanel.add(new JLabel(""));
		planningCustomizationPanel.add(createCustomizationButtonPanel());		

		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow(), treeAsObjectPicker);
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		
		add(planningCustomizationPanel);
		add(rowsLegendPanel);
		add(columnsLegendPanel);
	}

	private void createCustomizationButtons()
	{
		createConfigurationButton = createLegendButton(mainWindow.getActions().get(ActionCreatePlanningViewConfiguration.class));
		renameConfigurationButton = createLegendButton(mainWindow.getActions().get(ActionRenamePlanningViewConfiguration.class));
		deleteConfigurationButton = createLegendButton(mainWindow.getActions().get(ActionDeletePlanningViewConfiguration.class));
	}
	
	private JPanel createCustomizationButtonPanel()
	{
		JPanel customizationButtonPanel = new JPanel(new BasicGridLayout(3, 1));
		customizationButtonPanel.setBackground(AppPreferences.getControlPanelBackgroundColor());
		
		customizationButtonPanel.add(createConfigurationButton);
		customizationButtonPanel.add(renameConfigurationButton);
		customizationButtonPanel.add(deleteConfigurationButton);
		
		return customizationButtonPanel; 
	}

	private void updateVisibility(String newStyleChoice)
	{
		boolean isVisible = newStyleChoice.equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
		renameConfigurationButton.setVisible(isVisible);
		deleteConfigurationButton.setVisible(isVisible);
		columnsLegendPanel.setVisible(isVisible);
	}
	
	private UiButton createLegendButton(EAMAction action)
	{
		PanelButton button = new PanelButton(action);
		return button;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		updateVisibility(setCommand.getDataValue());
	}

	private UiButton createConfigurationButton;
	private UiButton renameConfigurationButton;
	private UiButton deleteConfigurationButton; 
	private MainWindow mainWindow;
	private AbstractPlanningViewLegendPanel rowsLegendPanel;
	private PlanningViewCustomizationPanel planningCustomizationPanel;
	private PlanningViewColumsLegendPanel columnsLegendPanel;
}
