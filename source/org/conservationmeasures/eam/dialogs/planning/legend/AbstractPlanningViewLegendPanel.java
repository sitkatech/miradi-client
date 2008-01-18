/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitledBorder;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.umbrella.LegendPanel;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

abstract public class AbstractPlanningViewLegendPanel extends LegendPanel implements ActionListener, CommandExecutedListener
{
	public AbstractPlanningViewLegendPanel(MainWindow mainWindowToUse, PlanningTreeTable treeAsObjectPicker)
	{
		super(mainWindowToUse.getProject());
		mainWindow = mainWindowToUse;
		project = mainWindow.getProject();
		picker = treeAsObjectPicker;

		createCheckboxes(getMasterListToCreateCheckBoxesFrom());
		setBorder(new EmptyBorder(5,5,5,5));
		add(createLegendButtonPanel(mainWindow.getActions()));	
		setMinimumSize(new Dimension(0,0));
		updateEnabledStateFromProject();
		setBorder(new PanelTitledBorder(getBorderTitle()));
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void updateCheckboxes(CodeList visibleTypes)
	{
		setAllCheckboxes(false);
		for (int i = 0; i < visibleTypes.size(); ++i)
		{
			String visibleType = visibleTypes.get(i);
			JCheckBox checkBox = findCheckBox(visibleType);
			// FIXME: This avoided an exception on Kevin's machine...
			// verify that it is legit and not just a bandaid covering 
			// up a real bug!
			//Verified that it wasnt a real but, but might still be effecting legacy projects.  
			if(checkBox == null)
				continue;
			
			checkBox.setSelected(true);
		}
	}
	
	private void updateEnabledStateFromProject()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String currentChoice = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
			updateEnableState(currentChoice);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void updateEnableState(String choice)
	{
		disableAllCheckBoxes(shouldEnableAll(choice));
	}
	
	public boolean shouldEnableAll(String choice)
	{
		if (choice.equals(PlanningView.STRATEGIC_PLAN_RADIO_CHOICE))
			return false;
		
		if (choice.equals(PlanningView.MONITORING_PLAN_RADIO_CHOICE))
			return false;
		
		if (choice.equals(PlanningView.WORKPLAN_PLAN_RADIO_CHOICE))
			return false;
		
		if (choice.equals(PlanningView.SINGLE_LEVEL_RADIO_CHOICE))
			return false;
		
		return true;
	}

	public void actionPerformed(ActionEvent event)
	{	
		try
		{
			saveSettingsAsTransction();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occured While Saving Setting"));
		}
	}

	private void saveSettingsAsTransction() throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			saveSettingsToProject(null);	
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}
	
	public CodeList getLegendSettings()
	{
		CodeList shownTypes = new CodeList();
		Object[] keys = checkBoxes.keySet().toArray();
		for (int i = 0; i < keys.length; ++i)
		{
			JCheckBox checkBox = findCheckBox(keys[i]);
			if (checkBox.isSelected())
				shownTypes.add(keys[i].toString());
		}

		return shownTypes;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			updateCheckBoxes(event);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error: " + e.getMessage());
		}
	}
	
	void updateCheckBoxes(CommandExecutedEvent event) throws Exception
	{
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
			updateEnableState(((CommandSetObjectData) event.getCommand()).getDataValue());

		if(PlanningView.isRowOrColumnChangingCommand(event))
			updateCheckBoxesFromProjectSettings();
	}
	
	protected void saveSettingsToProject(String tag)
	{
		// NOTE: We don't invoke super here because we don't want to save our data to ViewData
		savePlanningConfigurationData();
	}
	
	private void savePlanningConfigurationData()
	{
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			if (! PlanningView.isCustomizationStyle(viewData))
				return;
			
			String listAsString = getLegendSettings().toString();
			ORef configurationRef = viewData.getORef(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
			CommandSetObjectData setRowListCommand = new CommandSetObjectData(configurationRef, getConfigurationTypeTag(), listAsString);
			getProject().executeCommand(setRowListCommand);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void updateCheckBoxesFromProjectSettings() throws Exception
	{
		updateCheckboxes(getVisibleTypes());
	}

	ViewData getViewData() throws Exception
	{
		return project.getViewData(PlanningView.getViewName());
	}
	
	abstract protected String getConfigurationTypeTag();
	abstract protected CodeList getMasterListToCreateCheckBoxesFrom();	
	abstract protected JComponent createLegendButtonPanel(Actions actions);
	abstract protected String getBorderTitle();
	abstract protected CodeList getVisibleTypes() throws Exception;
	
	MainWindow mainWindow;
	Project project;
	JCheckBox objectiveCheckBox;
	ObjectPicker picker;
}
