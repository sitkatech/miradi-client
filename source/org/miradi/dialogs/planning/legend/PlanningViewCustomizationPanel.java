/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitledBorder;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Goal;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.PlanningViewSingleLevelQuestion;
import org.miradi.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewCustomizationPanel extends JPanel implements CommandExecutedListener
{
	public PlanningViewCustomizationPanel(Project projectToUse)
	{
		super(new GridLayoutPlus(3, 2));
		project = projectToUse;

		setBackground(AppPreferences.getControlPanelBackgroundColor());
		rebuildCustomizationPanel();
		setBorder(new PanelTitledBorder(EAM.text("Planning Views")));
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void rebuildCustomizationPanel()
	{
		try
		{
			rebuildPanel();
			selectDefaults();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error Occurred While in Planning View Legend Panel"));
		}
	}
	
	protected void rebuildPanel() throws Exception
	{
		removeAll();
		radioGroup = new PlanningViewRadioGroup(project);
		comboBoxes = new Hashtable();
		
		PlanningViewStrategicRadioButton strategicRadioButton = new PlanningViewStrategicRadioButton(project);
		radioGroup.addRadioButtonToGroup(strategicRadioButton);
		PanelButton strategicPlanButton = new PanelButton(EAM.text("Strategic Plan"));
		strategicPlanButton.addActionListener(strategicRadioButton);
		addRadioButtonWithLeftComponent(strategicRadioButton, strategicPlanButton);
		
		PlanningViewMonitoringRadioButton monitoringRadioButton = new PlanningViewMonitoringRadioButton(project);
		radioGroup.addRadioButtonToGroup(monitoringRadioButton);
		PanelButton monitoringPlanButton = new PanelButton(EAM.text("Monitoring Plan"));
		monitoringPlanButton.addActionListener(monitoringRadioButton);
		addRadioButtonWithLeftComponent(monitoringRadioButton, monitoringPlanButton);
		
		PlanningViewWorkPlanRadioButton workPlanRadioButton = new PlanningViewWorkPlanRadioButton(project);
		radioGroup.addRadioButtonToGroup(workPlanRadioButton);
		PanelButton workPlanButton = new PanelButton(EAM.text("Work Plan (BETA)"));
		workPlanButton.addActionListener(workPlanRadioButton);
		addRadioButtonWithLeftComponent(workPlanRadioButton, workPlanButton);
		
		
		singleLevelCombo = new PlanningViewSingleLevelComboBox(project);
		PlanningViewSingleLevelRadioButton singleLevelRadioButton = new PlanningViewSingleLevelRadioButton(project, singleLevelCombo);
		radioGroup.addRadioButtonToGroup(singleLevelRadioButton);
		comboBoxes.put(singleLevelCombo.getPropertyName(), singleLevelCombo);
		addRadioButtonWithLeftComponent(singleLevelRadioButton, singleLevelCombo);

		customizationComboBox = new PlanningViewCustomizationComboBox(project);
		PlanningViewCustomizationRadioButton customizationRadioButton = new PlanningViewCustomizationRadioButton(project, customizationComboBox);
		comboBoxes.put(customizationComboBox.getPropertyName(), customizationComboBox);
		radioGroup.addRadioButtonToGroup(customizationRadioButton);
		addRadioButtonWithLeftComponent(customizationRadioButton, customizationComboBox);
	}
	
	private void selectDefaults() throws Exception
	{
		selectAppropriateSingleLevelComboBoxItem();
		selectAppropriateConfiguredComboBoxItem();
		radioGroup.selectAppropriateRadioButton();
	}
	
	private void addRadioButtonWithLeftComponent(JRadioButton radioButton, Component otherComponent)
	{
		add(radioButton);
		add(otherComponent);
	}
	
	private void selectAppropriateSingleLevelComboBoxItem() throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		String preconfiguredChoice = getCurrentSingleLevelChoice(viewData);
		setComboBoxSelection(PlanningView.SINGLE_LEVEL_COMBO, preconfiguredChoice);
	}
	
	private void selectAppropriateConfiguredComboBoxItem() throws Exception
	{
		ViewData viewData = project.getCurrentViewData();
		ORef configurationChoiceRef = getCurrentConfigurationComboBoxChoice(viewData);
		setCustomizationComboBoxSelection(configurationChoiceRef);
	}

	private ORef getCurrentConfigurationComboBoxChoice(ViewData viewData)
	{	
		String preconfiguredChoice = viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		boolean shouldReturnDefault = preconfiguredChoice.trim().equals("");
		if (shouldReturnDefault)
			return ORef.INVALID;

		return ORef.createFromString(preconfiguredChoice);
	}
	
	private String getCurrentSingleLevelChoice(ViewData viewData)
	{	
		String singleLevelChoice = viewData.getData(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE);
		boolean shouldReturnDefault = singleLevelChoice.trim().equals("");
		if (shouldReturnDefault)
			return Goal.OBJECT_NAME;

		return singleLevelChoice;
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			refillCustomizationComboBox(event);
			updateRadioButtonSelection(event);
			setCustomizationComboSelection(event);
			setSingleLevelComboSelection(event, ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE, PlanningView.SINGLE_LEVEL_COMBO);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unexpected Error Occurred"));
		}
	}
	
	private void updateRadioButtonSelection(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		radioGroup.updateRadioSelection(setCommand.getDataValue());
	}
	
	private void setCustomizationComboSelection(CommandExecutedEvent event) throws Exception
	{
		boolean isLabelChange = event.isSetDataCommandWithThisTypeAndTag(PlanningViewConfiguration.getObjectType(), PlanningViewConfiguration.TAG_LABEL);
		boolean isSelectionChange = event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF);
		if (! (isLabelChange || isSelectionChange))
			return;
		
		selectAppropriateConfiguredComboBoxItem();
	}
	
	private void setSingleLevelComboSelection(CommandExecutedEvent event, String choice, String comboPropertyName)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), choice))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		String property = setCommand.getDataValue();
		setComboBoxSelection(comboPropertyName, property);
	}

	private void refillCustomizationComboBox(CommandExecutedEvent event) throws Exception
	{
		PlanningViewCustomizationComboBox comboBox = (PlanningViewCustomizationComboBox) comboBoxes.get(PlanningView.CUSTOMIZABLE_COMBO);

		if (event.isDeleteObjectCommand() || event.isCreateObjectCommand())
		{
			comboBox.syncContentsWithProject();
			selectAppropriateConfiguredComboBoxItem();
		}
		
		if(event.isSetDataCommandWithThisTypeAndTag(PlanningViewConfiguration.getObjectType(), BaseObject.TAG_LABEL))
			comboBox.repaint();
	}
	
	private void setComboBoxSelection(String comboName, String itemProperty)
	{		
		PlanningViewComboBox comboBox = (PlanningViewComboBox) comboBoxes.get(comboName);
		ChoiceItem currentSelection = (ChoiceItem)comboBox.getSelectedItem();
		PlanningViewSingleLevelQuestion question = new PlanningViewSingleLevelQuestion();
		ChoiceItem choiceItemToSelect = question.findChoiceByCode(itemProperty);
		if (currentSelection.equals(choiceItemToSelect))
			return;
		
		comboBox.setSelectedItemWithoutFiring(choiceItemToSelect);
	}

	private void setCustomizationComboBoxSelection(ORef refToSelect)
	{
		PlanningViewComboBox comboBox = (PlanningViewComboBox) comboBoxes.get(PlanningView.CUSTOMIZABLE_COMBO);
		if(refToSelect.isInvalid())
		{
			comboBox.setSelectedIndex(0);
			return;
		}
		
		PlanningViewConfiguration configuration = (PlanningViewConfiguration) project.findObject(refToSelect);
		ChoiceItem choiceToSelect = new ChoiceItem(configuration.getRef().toString(), configuration.getLabel());
		comboBox.setSelectedItemWithoutFiring(choiceToSelect);
	}

	private Project project;
	private PlanningViewRadioGroup radioGroup;
	private PlanningViewSingleLevelComboBox singleLevelCombo;
	private PlanningViewCustomizationComboBox customizationComboBox;
	private Hashtable<String, Component> comboBoxes;
}
