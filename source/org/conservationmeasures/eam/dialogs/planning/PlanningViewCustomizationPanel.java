/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
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
		
		rebuildLegendPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		customizationComboBox.dispose();
		singleLevelCombo.dispose();
	}
	
	protected void rebuildLegendPanel()
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
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		PlanningViewStrategicRadioButton strategicRadioButton = new PlanningViewStrategicRadioButton(project);
		radioGroup.addRadioButtonToGroup(buttonGroup, strategicRadioButton);
		addLabeledRadioButton(strategicRadioButton, EAM.text("Strategic Plan"));
		
		PlanningViewMonitoringRadioButton monitoringRadioButton = new PlanningViewMonitoringRadioButton(project);
		radioGroup.addRadioButtonToGroup(buttonGroup, monitoringRadioButton);
		addLabeledRadioButton(monitoringRadioButton, EAM.text("Monitoring Plan"));
		
		PlanningViewWorkPlanRadioButton workPlanRadioButton = new PlanningViewWorkPlanRadioButton(project);
		radioGroup.addRadioButtonToGroup(buttonGroup, workPlanRadioButton);
		addLabeledRadioButton(workPlanRadioButton, EAM.text("Work Plan"));
		
		
		singleLevelCombo = new PlanningViewSingleLevelComboBox(project);
		PlanningViewSingleLevelRadioButton singleLevelRadioButton = new PlanningViewSingleLevelRadioButton(project, singleLevelCombo);
		radioGroup.addRadioButtonToGroup(buttonGroup, singleLevelRadioButton);
		radioGroup.addComboBoxToHashMap(singleLevelCombo);
		addDropDownRadioButton(singleLevelRadioButton, singleLevelCombo);
		//TODO remvoe commented code
		//SingleLevelComboRadioButtonHandler preConfiguredButtonHandler = new SingleLevelComboRadioButtonHandler(PlanningView.SINGLE_LEVEL_RADIO_CHOICE, PlanningView.SINGLE_LEVEL_COMBO);
		//Object[] singleLevelChoiceItems = PlanningViewSingleLevelQuestion.getSingleLevelChoices();
		//SingleLevelComboBoxHandler comboHandler = new SingleLevelComboBoxHandler();
		//JComboBox cannedComboBox = createComboBox(singleLevelChoiceItems, comboHandler, PlanningView.SINGLE_LEVEL_COMBO);
		

		customizationComboBox = new PlanningViewCustomizationComboBox(project);
		PlanningViewCustomizationRadioButton customizationRadioButton = new PlanningViewCustomizationRadioButton(project, customizationComboBox);
		radioGroup.addComboBoxToHashMap(customizationComboBox);
		radioGroup.addRadioButtonToGroup(buttonGroup, customizationRadioButton);
		addDropDownRadioButton(customizationRadioButton, customizationComboBox);
		//TODO remvoe commented code
		//CustomizableComboRadioButtonHandler configuredButtonHandler = new CustomizableComboRadioButtonHandler();
		//PlanningViewConfiguration[] allConfigurations = getConfigurableChoices();
		//CustomizableComboBoxHandler configurableComboHandler = new CustomizableComboBoxHandler();
		//JComboBox configurableComboBox = createComboBox(allConfigurations, configurableComboHandler, PlanningView.CUSTOMIZABLE_COMBO);
	}
	
	private void selectDefaults() throws Exception
	{
		radioGroup.selectAppropriateRadioButton();
		selectAppropriateSingleLevelComboBoxItem();
		selectAppropriateConfiguredComboBoxItem();
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
		selectRadioButtonFromProjectSetting(event);
	}
	
	private void selectRadioButtonFromProjectSetting(CommandExecutedEvent event)
	{
		possiblyRebuild(event);
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_STYLE_CHOICE))
		{
			CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
			radioGroup.updateRadioSelection(setCommand.getDataValue());
			return;
		}
		
		if (event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
		{
			radioGroup.updateRadioSelection(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
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
		JComboBox comboBox = radioGroup.findComboBox(PlanningView.SINGLE_LEVEL_COMBO);
		PlanningViewSingleLevelQuestion question = new PlanningViewSingleLevelQuestion();
		ChoiceItem choiceItemToSelect = question.findChoiceByCode(property);
		comboBox.setSelectedItem(choiceItemToSelect);
	}

	private void selectConfigurationComboButton(ORef refToSelect)
	{
		JComboBox comboBox = radioGroup.findComboBox(PlanningView.CUSTOMIZABLE_COMBO);
		if (refToSelect.isInvalid())
			return;
		
		ChoiceItem choiceItem = (ChoiceItem) comboBox.getSelectedItem();
		ORef currentRef = ORef.createFromString(choiceItem.getCode());
		if (currentRef.equals(refToSelect))
			return;
		
		ChoiceItem choiceToSelect = getConfigurationToSelect(refToSelect);
		comboBox.setSelectedItem(choiceToSelect);
	}

	private ChoiceItem getConfigurationToSelect(ORef refToSelect)
	{
		if (refToSelect.getObjectId().isInvalid())
			return PlanningViewCustomizationQuestion.createDefaultInvalidConfigurationObject(project);
		
		PlanningViewConfiguration configuration = (PlanningViewConfiguration) project.findObject(refToSelect);
		return new ChoiceItem(configuration.getRef().toString(), configuration.getLabel());
	}

	
//	TODO these methods exist in radioGroup as well, but they use a different hashmap
//	public JComboBox findComboBox(String property)
//	{
//		return (JComboBox) findComponent(property);
//	}
//	
//	private Component findComponent(String property)
//	{
//		return configurationComponents.get(property);
//	}
	
//	private void saveConfiguration(String tag, String newValue) throws Exception
//	{
//		//FIXME planning - look into this code, test it, was getting nested transaction exceptions
//		ViewData viewData = project.getCurrentViewData();
//		String existingValue = viewData.getData(tag);
//		if (existingValue.equals(newValue))
//			return;
//
//		CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
//		project.executeCommand(setComboItem);
//	}
	
//	private void saveCodeList(CodeList codeListToSave, String dataTagToHide, String radioName)
//	{
//		try
//		{	
//			saveConfiguration(ViewData.TAG_PLANNING_STYLE_CHOICE, radioName);
//			saveConfiguration(dataTagToHide, codeListToSave.toString());
//		}
//		catch (Exception e)
//		{
//			EAM.logException(e);
//		}
//	}
	
//	private void saveVisibleRowList(CodeList masterCodeList, CodeList rowsToShow, String dataTagToHide, String radioName)
//	{
//		masterCodeList.subtract(rowsToShow);
//		saveCodeList(masterCodeList, dataTagToHide, radioName);
//	}
//	
//	private void saveVisibleColumnList(CodeList masterCodeList, CodeList columnsToShow, String dataTagToHide, String radioName)
//	{
//		masterCodeList.subtract(columnsToShow);
//		saveCodeList(masterCodeList, dataTagToHide, radioName);
//	}
	
//TODO planning - remove commented code	
//	private PlanningViewConfiguration[] getConfigurableChoices()
//	{
//		PlanningViewConfiguration invalidConfiguration = createDefaultInvalidConfigurationObject();
//		PlanningViewConfigurationPool configurationPool = (PlanningViewConfigurationPool) project.getPool(PlanningViewConfiguration.getObjectType());
//		PlanningViewConfiguration[] allConfigurations = configurationPool.getAllConfigurations();
//		
//		Vector allConfigurationsWithFirstInvalid = new Vector();
//		allConfigurationsWithFirstInvalid.add(invalidConfiguration);
//		for (int i = 0; i < allConfigurations.length; ++i)
//		{
//			allConfigurationsWithFirstInvalid.add(allConfigurations[i]);
//		}
//	 
//		return (PlanningViewConfiguration[]) allConfigurationsWithFirstInvalid.toArray(new PlanningViewConfiguration[0]);
//	}

//	private PlanningViewConfiguration createDefaultInvalidConfigurationObject()
//	{
//		PlanningViewConfiguration invalidConfiguration = new PlanningViewConfiguration(project.getObjectManager(), BaseId.INVALID);
//		try
//		{
//			invalidConfiguration.setLabel("--Customize--");
//		}
//		catch (Exception e)
//		{
//			EAM.logException(e);
//		}
//		
//		return invalidConfiguration;
//	}
	
//	public class SingleLevelComboBoxHandler implements ActionListener
//	{
//		public void actionPerformed(ActionEvent e)
//		{
//			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.SINGLE_LEVEL_RADIO_CHOICE);
//			saveVisibleColumnList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.SINGLE_LEVEL_RADIO_CHOICE);
//
//			JComboBox comboBox = (JComboBox) e.getSource();
//			ChoiceItem choiceItem = (ChoiceItem) comboBox.getSelectedItem();
//			saveComboSelection(choiceItem);
//		}
//		
//		private void saveComboSelection(ChoiceItem choiceItem)
//		{
//			try
//			{
//				saveConfiguration(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE, choiceItem.getCode());
//			}
//			catch (Exception e)
//			{
//				EAM.logException(e);
//			}
//		}
//		
//		private CodeList getRowListToShow()
//		{
//			CodeList listToShow = new CodeList();
//			JComboBox comboBox = radioGroup.findComboBox(PlanningView.SINGLE_LEVEL_COMBO);
//			ChoiceItem choiceItem = (ChoiceItem) comboBox.getSelectedItem();
//			listToShow.add(choiceItem.getCode());
//			
//			return listToShow;
//		}		
//	}
	
//	public class CustomizableComboBoxHandler implements ActionListener
//	{
//		public void actionPerformed(ActionEvent e)
//		{
//			saveSelectedConfiguration((JComboBox) e.getSource());
//		}
//
//		private void saveSelectedConfiguration(JComboBox comboBox)
//		{
//			PlanningViewConfiguration configuration = (PlanningViewConfiguration) comboBox.getSelectedItem();
//			if (configuration == null)
//				return;
//			
//			CodeList rowConfiguration = configuration.getRowConfiguration();
//			saveCodeList(rowConfiguration, ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
//			
//			CodeList colConfiguration = configuration.getColumnConfiguration();
//			saveCodeList(colConfiguration, ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
//			
//			saveComboSelection(configuration.getRef());
//		}
//		
//		private void saveComboSelection(ORef ref)
//		{
//			try
//			{
//				saveConfiguration(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF, ref.toString());
//			}
//			catch (Exception e)
//			{
//				EAM.logException(e);
//			}
//		}
//	}
	
//	public class CustomizableComboRadioButtonHandler implements ActionListener
//	{
//		public void actionPerformed(ActionEvent e)
//		{			
//			saveCurrentConfiguration();
//		}
//
//		private void saveCurrentConfiguration()
//		{
//			JComboBox comboBox = findComboBox(PlanningView.CUSTOMIZABLE_COMBO);
//			PlanningViewConfiguration configuration = (PlanningViewConfiguration) comboBox.getSelectedItem();
//			CodeList rowList = configuration.getRowConfiguration();
//			saveCodeList(rowList, ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
//			
//			CodeList columnList = configuration.getColumnConfiguration();
//			saveCodeList(columnList, ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
//		}
//	}
//	
//	public class SingleLevelComboRadioButtonHandler implements ActionListener
//	{
//		public SingleLevelComboRadioButtonHandler(String dropDownRadioTagToUse, String planningComboTagToUse)
//		{
//			dropDownRadioTag = dropDownRadioTagToUse;
//			planningComboTag = planningComboTagToUse;
//		}
//		
//		public void actionPerformed(ActionEvent e)
//		{			
//			saveVisibleRowList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, dropDownRadioTag);
//			saveVisibleColumnList(PlanningView.getMasterColumnList(), getColumListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, dropDownRadioTag);
//		}
//
//		private CodeList getRowListToShow()
//		{
//			CodeList listToShow = new CodeList();
//			String propertyName = getSelectedItemProperty();
//			listToShow.add(propertyName);
//			
//			return listToShow;
//		}
//		
//		private CodeList getColumListToShow()
//		{
//			String propertyName = getSelectedItemProperty();
//			if (propertyName.equals(Goal.OBJECT_NAME))
//				return PlanningView.getGoalColumns();
//
//			if (propertyName.equals(Objective.OBJECT_NAME))
//				return PlanningView.getObjectiveColumns();
//			
//			if (propertyName.equals(Strategy.OBJECT_NAME))
//				return PlanningView.getStrategyColumns();
//			
//			if (propertyName.equals(Task.ACTIVITY_NAME))
//				return PlanningView.getActivityColumns();
//
//			if (propertyName.equals(Indicator.OBJECT_NAME))
//				return PlanningView.getIndicatorColumns();
//
//			if (propertyName.equals(Task.METHOD_NAME))
//				return PlanningView.getMethodColumns();
//
//			if (propertyName.equals(Task.OBJECT_NAME))
//				return PlanningView.getTaskColumns();
//			
//			return new CodeList();
//		}
//
//		private String getSelectedItemProperty()
//		{
//			JComboBox comboBox = findComboBox(planningComboTag);
//			ChoiceItem choiceItem = (ChoiceItem) comboBox.getSelectedItem();
//			return choiceItem.getCode();
//		}
//		
//		private String dropDownRadioTag;
//		private String planningComboTag;
//	}
		
	private Project project;
	private PlanningViewRadioGroup radioGroup;
	private PlanningViewSingleLevelComboBox singleLevelCombo;
	private PlanningViewCustomizationComboBox customizationComboBox;
}
