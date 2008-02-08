/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.legend;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitledBorder;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewSingleLevelQuestion;
import org.conservationmeasures.eam.views.planning.PlanningView;

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
			EAM.errorDialog(EAM.text("Error Occurred While in Planning View Leged Panel"));
		}
	}
	
	protected void rebuildPanel() throws Exception
	{
		removeAll();
		radioGroup = new PlanningViewRadioGroup(project);
		comboBoxes = new Hashtable();
		
		PlanningViewStrategicRadioButton strategicRadioButton = new PlanningViewStrategicRadioButton(project);
		radioGroup.addRadioButtonToGroup(strategicRadioButton);
		addRadioButtonWithLeftComponent(strategicRadioButton, new PanelTitleLabel(EAM.text("Strategic Plan")));
		
		PlanningViewMonitoringRadioButton monitoringRadioButton = new PlanningViewMonitoringRadioButton(project);
		radioGroup.addRadioButtonToGroup(monitoringRadioButton);
		addRadioButtonWithLeftComponent(monitoringRadioButton, new PanelTitleLabel(EAM.text("Monitoring Plan")));
		
		PlanningViewWorkPlanRadioButton workPlanRadioButton = new PlanningViewWorkPlanRadioButton(project);
		radioGroup.addRadioButtonToGroup(workPlanRadioButton);
		addRadioButtonWithLeftComponent(workPlanRadioButton, new PanelTitleLabel(EAM.text("Work Plan")));
		
		
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
	
	private void addRadioButtonWithLeftComponent(JRadioButton radioButton, Component leftComponent)
	{
		add(leftComponent);
		add(radioButton);
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
