/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.PlanningViewCustomizationQuestion;
import org.conservationmeasures.eam.questions.PlanningViewSingleLevelQuestion;
import org.conservationmeasures.eam.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningViewCustomizationPanel extends JPanel implements CommandExecutedListener
{
	public PlanningViewCustomizationPanel(Project projectToUse)
	{
		super(new GridLayoutPlus(3, 2));
		project = projectToUse;
		project.addCommandExecutedListener(this);
		
		rebuildCustomizationPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
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
		addRadioButtonWithLeftComponent(strategicRadioButton, new JLabel(EAM.text("Strategic Plan")));
		
		PlanningViewMonitoringRadioButton monitoringRadioButton = new PlanningViewMonitoringRadioButton(project);
		radioGroup.addRadioButtonToGroup(monitoringRadioButton);
		addRadioButtonWithLeftComponent(monitoringRadioButton, new JLabel(EAM.text("Monitoring Plan")));
		
		PlanningViewWorkPlanRadioButton workPlanRadioButton = new PlanningViewWorkPlanRadioButton(project);
		radioGroup.addRadioButtonToGroup(workPlanRadioButton);
		addRadioButtonWithLeftComponent(workPlanRadioButton, new JLabel(EAM.text("Work Plan")));
		
		
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
		radioGroup.selectAppropriateRadioButton();
		selectAppropriateSingleLevelComboBoxItem();
		selectAppropriateConfiguredComboBoxItem();
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
		return;
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
		if (! shouldRebuild(event))
			return;
	
		PlanningViewCustomizationComboBox comboBox = (PlanningViewCustomizationComboBox) comboBoxes.get(PlanningView.CUSTOMIZABLE_COMBO);
		comboBox.removeAllItems();
		ChoiceItem[] choices = new PlanningViewCustomizationQuestion(project).getChoices();
		for (int i = 0; i< choices.length; ++i)
		{
			comboBox.addItem(choices[i]);
		}
		
		selectAppropriateConfiguredComboBoxItem();
	}
	
	private boolean shouldRebuild(CommandExecutedEvent event)
	{
		if (event.isCreateObjectCommand())
			return true;
		
		if (event.isDeleteObjectCommand())
			return true;
		
		return false;
	}
	
	private void setComboBoxSelection(String comboName, String itemProperty)
	{		
		JComboBox comboBox = (JComboBox) comboBoxes.get(comboName);
		ChoiceItem currentSelection = (ChoiceItem)comboBox.getSelectedItem();
		PlanningViewSingleLevelQuestion question = new PlanningViewSingleLevelQuestion();
		ChoiceItem choiceItemToSelect = question.findChoiceByCode(itemProperty);
		if (currentSelection.equals(choiceItemToSelect))
			return;
		
		comboBox.setSelectedItem(choiceItemToSelect);
	}

	private void setCustomizationComboBoxSelection(ORef refToSelect)
	{
		JComboBox comboBox = (JComboBox) comboBoxes.get(PlanningView.CUSTOMIZABLE_COMBO);
		if(refToSelect.isInvalid())
		{
			comboBox.setSelectedIndex(0);
			return;
		}
		
		PlanningViewConfiguration configuration = (PlanningViewConfiguration) project.findObject(refToSelect);
		ChoiceItem choiceToSelect = new ChoiceItem(configuration.getRef().toString(), configuration.getLabel());
		comboBox.setSelectedItem(choiceToSelect);
	}

	private Project project;
	private PlanningViewRadioGroup radioGroup;
	private PlanningViewSingleLevelComboBox singleLevelCombo;
	private PlanningViewCustomizationComboBox customizationComboBox;
	private Hashtable<String, Component> comboBoxes;
}
