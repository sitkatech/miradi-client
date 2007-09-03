/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;

import com.jhlabs.awt.BasicGridLayout;

abstract public class AbstractPlanningViewLegendPanel extends LegendPanel implements ActionListener, CommandExecutedListener
{
	public AbstractPlanningViewLegendPanel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse.getProject(), new BasicGridLayout(0, 1));
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();

		project.addCommandExecutedListener(this);
		createCheckBoxes();
		setBorder(new EmptyBorder(5,5,5,5));
		add(createLegendButtonPanel(mainWindow.getActions()));	
		selectAllCheckBoxes();
		setMinimumSize(new Dimension(0,0));
		updateCheckBoxesFromProjectSettings();
		updateEnabledStateFromProject();
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void createCheckBoxes()
	{	
		CodeList masterList = getMasterListToCreateCheckBoxesFrom();
		for (int i = 0; i < masterList.size(); ++i)
		{
			createCheckBox(masterList.get(i));
		}
	}
	
	public void updateCheckBoxesFromProjectSettings()
	{
		CodeList hiddenTypes = getLegendSettings(getViewDataHiddenTypesTag());
		selectAllCheckBoxes();
		for (int i = 0; i < hiddenTypes.size(); ++i)
		{
			String hiddenType = hiddenTypes.get(i);
			JCheckBox checkBox = findCheckBox(hiddenType);
			checkBox.setSelected(false);
		}
	}
	
	private void updateEnabledStateFromProject()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String currentChoice = viewData.getData(ViewData.TAG_PLANNING_CONFIGURATION_CHOICE);
			updateEnableStatus(currentChoice);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateEnableStatus(String choice)
	{
		if (shouldDisableAll(choice))
			disableAllCheckBoxes();
	}
	
	public boolean shouldDisableAll(String choice)
	{
		if (choice.equals(PlanningView.STRATEGIC_PLAN_RADIO_CHOICE))
			return true;
		
		if (choice.equals(PlanningView.MONITORING_PLAN_RADIO_CHOICE))
			return true;
		
		if (! choice.equals(PlanningView.WORKPLAN_PLAN_RADIO_CHOICE))
			return true;
		
		return false;
	}

	public void actionPerformed(ActionEvent event)
	{	
		saveSettingsToProject(getViewDataHiddenTypesTag());
	}

	public CodeList getLegendSettings()
	{
		CodeList hiddenTypes = new CodeList();
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			JCheckBox checkBox = findCheckBox(keys[i]);
			if (checkBox.isSelected())
				continue;
			
			hiddenTypes.add(keys[i].toString());
		}

		return hiddenTypes;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		updateCheckBoxes(event.getCommand());
	}
	
	private void updateCheckBoxes(Command command)
	{
		if (! command.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) command;
		if ( setCommand.getFieldTag().equals(getViewDataHiddenTypesTag()))
			updateCheckBoxesFromProjectSettings();
		
		if (setCommand.getFieldTag().equals(ViewData.TAG_PLANNING_CONFIGURATION_CHOICE))
			updateEnableStatus(setCommand.getDataValue());
	}

	abstract protected CodeList getMasterListToCreateCheckBoxesFrom();	
	abstract protected JPanel createLegendButtonPanel(Actions actions);
	abstract protected String getViewDataHiddenTypesTag();
	
	MainWindow mainWindow;
	Project project;
	JCheckBox objectiveCheckBox;
}
