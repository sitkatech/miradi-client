/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.martus.swing.UiButton;

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
		rowsLegendPanel = new PlanningViewRowsLegendPanel(getMainWindow(), treeAsObjectPicker);
		columnsLegendPanel = new PlanningViewColumsLegendPanel(getMainWindow());
		
		add(planningCustomizationPanel);
		planningCustomizationPanel.add(createCustomizationButtonPanel());		
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
