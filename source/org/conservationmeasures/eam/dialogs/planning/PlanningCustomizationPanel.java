/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.PlanningViewConfigurationPool;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.PlanningView;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningCustomizationPanel extends JPanel implements CommandExecutedListener
{
	public PlanningCustomizationPanel(Project projectToUse)
	{
		super(new GridLayoutPlus(3, 2));
		project = projectToUse;
		configurationComponents = new Hashtable<String, Component>();
		project.addCommandExecutedListener(this);
		
		rebuildLegendPanel();
		selectDefaults();

		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void rebuildLegendPanel()
	{
		removeAll();
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton stratRadio = createRadioButton(buttonGroup, new StrategicButtonHandler(), PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(stratRadio, EAM.text("Strategic Plan"));
		
		JRadioButton monRadio = createRadioButton(buttonGroup, new MonitoringButtonHandler(), PlanningView.MONITORING_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(monRadio, EAM.text("Monitoring Plan"));
		
		JRadioButton workRadio = createRadioButton(buttonGroup, new WorkPlanButtonHandler(), PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(workRadio, EAM.text("Work Plan"));
		
		SingleLevelComboRadioButtonHandler preConfiguredButtonHandler = new SingleLevelComboRadioButtonHandler(PlanningView.SINGLE_LEVEL_RADIO_CHOICE, PlanningView.SINGLE_LEVEL_COMBO);
		JRadioButton singleObjectRadio = createRadioButton(buttonGroup, preConfiguredButtonHandler, PlanningView.SINGLE_LEVEL_RADIO_CHOICE);
		Object[] singleObjectComboBox = getPreConfiguredButtons().values().toArray();
		SingleLevelComboBoxHandler comboHandler = new SingleLevelComboBoxHandler();
		JComboBox cannedComboBox = createComboBox(singleObjectComboBox, comboHandler, PlanningView.SINGLE_LEVEL_COMBO);
		addDropDownRadioButton(singleObjectRadio, cannedComboBox);
		
		CustomizableComboRadioButtonHandler configuredButtonHandler = new CustomizableComboRadioButtonHandler();
		JRadioButton configurableRadioButton = createRadioButton(buttonGroup, configuredButtonHandler, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
		PlanningViewConfiguration[] allConfigurations = getConfigurableChoices();
		CustomizableComboBoxHandler configurableComboHandler = new CustomizableComboBoxHandler();
		JComboBox configurableComboBox = createComboBox(allConfigurations, configurableComboHandler, PlanningView.CUSTOMIZABLE_COMBO);
		addDropDownRadioButton(configurableRadioButton, configurableComboBox);
	}
	
	private void selectDefaults()
	{
		selectAppropriateRadioButton();
		selectAppropriateSingleLevelComboBoxItem();
		selectAppropriateConfiguredComboBoxItem();
	}
	
	private JComboBox createComboBox(Object[] preconfiguredItems, ActionListener handler, String propertyName)
	{
		JComboBox comboBox = new JComboBox(preconfiguredItems);
		comboBox.addActionListener(handler);
		configurationComponents.put(propertyName, comboBox);
		
		return comboBox;
	}
	
	private JRadioButton createRadioButton(ButtonGroup buttonGroup, ActionListener handler, String propertyName)
	{
		JRadioButton radioButton = new JRadioButton();
		buttonGroup.add(radioButton);
		
		radioButton.addActionListener(handler);
		configurationComponents.put(propertyName, radioButton);
		
		return radioButton;
	}
	
	private void addDropDownRadioButton(JRadioButton radioButton, JComboBox comboBox)
	{
		add(comboBox);
		add(radioButton);
	}
	
	private void addLabeledRadioButton(JRadioButton radioButton, String buttonName)
	{
		add(new JLabel(buttonName));
		add(radioButton);
	}
	
	private void selectAppropriateSingleLevelComboBoxItem()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
			if (! selectedRadioName.equals(PlanningView.SINGLE_LEVEL_RADIO_CHOICE))
				return;

			String preconfiguredChoice = getCurrentSingleLevelChoice(viewData);
			selectSingleLevelComboButton(preconfiguredChoice);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void selectAppropriateConfiguredComboBoxItem()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
			if (! selectedRadioName.equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE))
				return;
			
			ORef configurationChoiceRef = getCurrentConfigurationComboBoxChoice(viewData);
			selectConfigurationComboButton(configurationChoiceRef);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void selectAppropriateRadioButton()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = getCurrentRadioChoice(viewData);
			JRadioButton radioButton = findRadioButton(selectedRadioName);
			radioButton.setSelected(true);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private String getCurrentRadioChoice(ViewData viewData)
	{
		String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE);
		boolean shouldReturnDefault = selectedRadioName.trim().equals("");
		if (shouldReturnDefault)
			return PlanningView.STRATEGIC_PLAN_RADIO_CHOICE;

		return selectedRadioName;
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
	
	private void saveConfiguration(String tag, String newValue) throws Exception
	{
		//FIXME planning - look into this code, test it, was getting nested transaction exceptions
		ViewData viewData = project.getCurrentViewData();
		String existingValue = viewData.getData(tag);
		if (existingValue.equals(newValue))
			return;

		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
		project.executeCommand(setComboItem);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		selectRadioButtonFromProjectSetting(event);
	}
	
	private void selectRadioButtonFromProjectSetting(CommandExecutedEvent event)
	{
		possiblyRebuild(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			updateRadioSelection(setCommand.getDataValue());
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
		{
			updateRadioSelection(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			String property = setCommand.getDataValue();
			selectSingleLevelComboButton(property);
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(PlanningViewConfiguration.getObjectType(), PlanningViewConfiguration.TAG_LABEL))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			ORef ref = setCommand.getObjectORef();
			selectConfigurationComboButton(ref);
			return;
		}
	}

	private void possiblyRebuild(CommandExecutedEvent event)
	{
		if (! (event.isCreateObjectCommand() || event.isDeleteObjectCommand()))
			return;
		
		rebuildLegendPanel();
		validate();
	}
	
	private void selectSingleLevelComboButton(String property)
	{
		JComboBox comboBox = findComboBox(PlanningView.SINGLE_LEVEL_COMBO);
		HashMap preConfiguredHashMap = getPreConfiguredButtons();
		ComboBoxButton buttonToSelect = (ComboBoxButton) preConfiguredHashMap.get(property);
		comboBox.setSelectedItem(buttonToSelect);
	}
	
	private void selectConfigurationComboButton(ORef refToSelect)
	{
		JComboBox comboBox = findComboBox(PlanningView.CUSTOMIZABLE_COMBO);
		if (refToSelect.isInvalid())
			return;
		
		PlanningViewConfiguration selectedConfiguration = (PlanningViewConfiguration) comboBox.getSelectedItem();
		if (selectedConfiguration.getRef().equals(refToSelect))
			return;
		
		PlanningViewConfiguration configuration = getConfigurationToSelect(refToSelect);
		comboBox.setSelectedItem(configuration);
	}

	private PlanningViewConfiguration getConfigurationToSelect(ORef refToSelect)
	{
		if (refToSelect.getObjectId().isInvalid())
			return createDefaultInvalidConfigurationObject();
		
		return (PlanningViewConfiguration) project.findObject(refToSelect);
	}

	private void updateRadioSelection(String selectedProperty)
	{
		findRadioButton(selectedProperty).setSelected(true);
	}

	private JRadioButton findRadioButton(String property)
	{
		return (JRadioButton) findComponent(property);
	}
	
	private JComboBox findComboBox(String property)
	{
		return (JComboBox) findComponent(property);
	}
	
	private Component findComponent(String property)
	{
		return configurationComponents.get(property);
	}
	
	private void saveCodeList(CodeList codeListToSave, String dataTagToHide, String radioName)
	{
		try
		{	
			saveConfiguration(ViewData.TAG_PLANNING_STYLE_CHOICE, radioName);
			saveConfiguration(dataTagToHide, codeListToSave.toString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void saveVisibleRowList(CodeList masterCodeList, CodeList rowsToShow, String dataTagToHide, String radioName)
	{
		masterCodeList.subtract(rowsToShow);
		saveCodeList(masterCodeList, dataTagToHide, radioName);
	}
	
	private void saveVisibleColumnList(CodeList masterCodeList, CodeList columnsToShow, String dataTagToHide, String radioName)
	{
		masterCodeList.subtract(columnsToShow);
		saveCodeList(masterCodeList, dataTagToHide, radioName);
	}
	
	public HashMap getPreConfiguredButtons()
	{
		HashMap hashMap = new HashMap();
		hashMap.put(Goal.OBJECT_NAME, new ComboBoxButton(Goal.OBJECT_NAME, EAM.text("Goals Only"))); 
		hashMap.put(Objective.OBJECT_NAME, new ComboBoxButton(Objective.OBJECT_NAME, EAM.text("Objectives Only"))); 
		hashMap.put(Strategy.OBJECT_NAME, new ComboBoxButton(Strategy.OBJECT_NAME, EAM.text("Strategies Only"))); 
		hashMap.put(Task.ACTIVITY_NAME, new ComboBoxButton(Task.ACTIVITY_NAME, EAM.text("Actions Only")));
		hashMap.put(Indicator.OBJECT_NAME, new ComboBoxButton(Indicator.OBJECT_NAME, EAM.text("Indicators Only")));
		hashMap.put(Task.METHOD_NAME, new ComboBoxButton(Task.METHOD_NAME, EAM.text("Methods Only")));
		hashMap.put(Task.OBJECT_NAME, new ComboBoxButton(Task.OBJECT_NAME, EAM.text("Tasks Only")));
		
		return hashMap;
	}
	
	private PlanningViewConfiguration[] getConfigurableChoices()
	{
		PlanningViewConfiguration invalidConfiguration = createDefaultInvalidConfigurationObject();
		PlanningViewConfigurationPool configurationPool = (PlanningViewConfigurationPool) project.getPool(PlanningViewConfiguration.getObjectType());
		PlanningViewConfiguration[] allConfigurations = configurationPool.getAllConfigurations();
		
		Vector allConfigurationsWithFirstInvalid = new Vector();
		allConfigurationsWithFirstInvalid.add(invalidConfiguration);
		for (int i = 0; i < allConfigurations.length; ++i)
		{
			allConfigurationsWithFirstInvalid.add(allConfigurations[i]);
		}
	 
		return (PlanningViewConfiguration[]) allConfigurationsWithFirstInvalid.toArray(new PlanningViewConfiguration[0]);
	}

	private PlanningViewConfiguration createDefaultInvalidConfigurationObject()
	{
		PlanningViewConfiguration invalidConfiguration = new PlanningViewConfiguration(project.getObjectManager(), BaseId.INVALID);
		try
		{
			invalidConfiguration.setLabel("--Customize--");
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		return invalidConfiguration;
	}
	
	public class StrategicButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
			saveVisibleColumnList(PlanningView.getMasterColumnList(), getColumListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Goal.OBJECT_NAME);
			listToShow.add(Objective.OBJECT_NAME);
			listToShow.add(Strategy.OBJECT_NAME);
			
			return listToShow;
		}
		
		private CodeList getColumListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Indicator.PSEUDO_TAG_STATUS_VALUE);			
			listToShow.add(Indicator.TAG_PRIORITY);
			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			
			return listToShow;
		}
	}
	
	public class MonitoringButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.MONITORING_PLAN_RADIO_CHOICE);
			saveVisibleColumnList(PlanningView.getMasterColumnList(), getColumListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.MONITORING_PLAN_RADIO_CHOICE);
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Goal.OBJECT_NAME);
			listToShow.add(Objective.OBJECT_NAME);
			listToShow.add(Indicator.OBJECT_NAME);
			
			return listToShow;
		}
		
		private CodeList getColumListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Indicator.PSEUDO_TAG_STATUS_VALUE);			
			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			listToShow.add(Indicator.PSEUDO_TAG_FACTOR);
			listToShow.add(Indicator.PSEUDO_TAG_METHODS);
			
			return listToShow;
		}
	}
	
	public class WorkPlanButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
			saveVisibleColumnList(PlanningView.getMasterColumnList(), getColumListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Strategy.OBJECT_NAME);
			listToShow.add(Task.ACTIVITY_NAME);
			listToShow.add(Indicator.OBJECT_NAME);
			listToShow.add(Task.METHOD_NAME);
			listToShow.add(Task.OBJECT_NAME);
			
			return listToShow;
		}
		
		private CodeList getColumListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			
			return listToShow;
		}
	}
	
	public class SingleLevelComboBoxHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.SINGLE_LEVEL_RADIO_CHOICE);
			saveVisibleColumnList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.SINGLE_LEVEL_RADIO_CHOICE);

			JComboBox comboBox = (JComboBox) e.getSource();
			ComboBoxButton comboChoice = (ComboBoxButton) comboBox.getSelectedItem();
			saveComboSelection(comboChoice);
		}
		
		private void saveComboSelection(ComboBoxButton comboChoice)
		{
			try
			{
				saveConfiguration(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE, comboChoice.getPropertyName());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			JComboBox comboBox = findComboBox(PlanningView.SINGLE_LEVEL_COMBO);
			ComboBoxButton comboButton = (ComboBoxButton) comboBox.getSelectedItem();
			listToShow.add(comboButton.getPropertyName());
			
			return listToShow;
		}		
	}
	
	public class CustomizableComboBoxHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveSelectedConfiguration((JComboBox) e.getSource());
		}

		private void saveSelectedConfiguration(JComboBox comboBox)
		{
			PlanningViewConfiguration configuration = (PlanningViewConfiguration) comboBox.getSelectedItem();
			if (configuration == null)
				return;
			
			CodeList rowConfiguration = configuration.getRowConfiguration();
			saveCodeList(rowConfiguration, ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
			
			CodeList colConfiguration = configuration.getColumnConfiguration();
			saveCodeList(colConfiguration, ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
			
			saveComboSelection(configuration.getRef());
		}
		
		private void saveComboSelection(ORef ref)
		{
			try
			{
				saveConfiguration(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, ref.toString());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
	}
	
	public class CustomizableComboRadioButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			saveCurrentConfiguration();
		}

		private void saveCurrentConfiguration()
		{
			JComboBox comboBox = findComboBox(PlanningView.CUSTOMIZABLE_COMBO);
			PlanningViewConfiguration configuration = (PlanningViewConfiguration) comboBox.getSelectedItem();
			CodeList rowList = configuration.getRowConfiguration();
			saveCodeList(rowList, ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
			
			CodeList columnList = configuration.getColumnConfiguration();
			saveCodeList(columnList, ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
		}
	}
	
	public class SingleLevelComboRadioButtonHandler implements ActionListener
	{
		public SingleLevelComboRadioButtonHandler(String dropDownRadioTagToUse, String planningComboTagToUse)
		{
			dropDownRadioTag = dropDownRadioTagToUse;
			planningComboTag = planningComboTagToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{			
			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, dropDownRadioTag);
			saveVisibleColumnList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, dropDownRadioTag);
		}

		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			JComboBox comboBox = findComboBox(planningComboTag);
			ComboBoxButton comboButton = (ComboBoxButton) comboBox.getSelectedItem();
			listToShow.add(comboButton.getPropertyName());
			
			return listToShow;
		}	
		
		private String dropDownRadioTag;
		private String planningComboTag;
	}
		
	public class ComboBoxButton
	{
		public ComboBoxButton(String propertyNameToUse, String buttonNameToUse)
		{
			propertyName = propertyNameToUse;
			buttonName = buttonNameToUse;
		}
		
		public String getPropertyName()
		{
			return propertyName;
		}
		
		public String getButtonName()
		{
			return buttonName;
		}
		
		public String toString()
		{
			return buttonName;
		}
		
		public boolean equals(Object other)
		{
			if ( !(other instanceof ComboBoxButton))
					return false;
			
			ComboBoxButton otherComboButton = (ComboBoxButton) other;
			if (! buttonName.equals(otherComboButton.getButtonName()))
				return false;
			
			if (! propertyName.equals(otherComboButton.getPropertyName()))
				return false;
			
			return true;
		}
		
		private String propertyName;
		private String buttonName;
	}

	private Project project;
	private Hashtable<String, Component> configurationComponents;
}
