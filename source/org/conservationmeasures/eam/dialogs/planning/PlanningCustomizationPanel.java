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

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
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
		project = projectToUse;
		configurationComponents = new Hashtable<String, Component>();
		project.addCommandExecutedListener(this);
		createLegendButtonPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
	}
	
	protected void createLegendButtonPanel()
	{
		JPanel panel = new JPanel(new GridLayoutPlus(3, 2));
		ButtonGroup buttonGroup = new ButtonGroup();
		
		JRadioButton stratRadio = createRadioButton(buttonGroup, new StrategicButtonHandler(), PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(panel, stratRadio, EAM.text("Strategic Plan"));
		
		JRadioButton monRadio = createRadioButton(buttonGroup, new MonitoringButtonHandler(), PlanningView.MONITORING_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(panel, monRadio, EAM.text("Monitoring Plan"));
		
		JRadioButton workRadio = createRadioButton(buttonGroup, new WorkPlanButtonHandler(), PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
		addLabeledRadioButton(panel, workRadio, EAM.text("Work Plan"));
		
		PreConfiguredButtonHandler preConfiguredButtonHandler = new PreConfiguredButtonHandler();
		JRadioButton singleObjectRadio = createRadioButton(buttonGroup, preConfiguredButtonHandler, PlanningView.PRE_CONFIGURED_RADIO_CHOICE);
		Object[] singleObjectComboBox = getPreConfiguredButtonNames().values().toArray();
		JComboBox cannedComboBox = createComboBox(singleObjectComboBox, preConfiguredButtonHandler, PlanningView.PRE_CONFIGURED_COMBO);
		addDropDownRadioButton(panel, singleObjectRadio, cannedComboBox);
		
		add(panel);
		selectAppropriateRadioButton();
		selectAppropriateCheckBoxItem();
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
		
		radioButton.putClientProperty(TAG_PREDEFINED_CONFIGURATION, propertyName);
		radioButton.addActionListener(handler);
		configurationComponents.put(propertyName, radioButton);
		
		return radioButton;
	}
	
	private void addDropDownRadioButton(JPanel panel, JRadioButton radioButton, JComboBox comboBox)
	{
		panel.add(comboBox);
		panel.add(radioButton);
	}
	
	private void addLabeledRadioButton(JPanel panel, JRadioButton radioButton, String buttonName)
	{
		panel.add(new JLabel(buttonName));
		panel.add(radioButton);
	}
	
	private void selectAppropriateCheckBoxItem()
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_CONFIGERATION_CHOICE);
			if (! selectedRadioName.equals(PlanningView.PRE_CONFIGURED_RADIO_CHOICE))
				return;
			
			String preconfiguredChoice = viewData.getData(ViewData.TAG_PLANNING_SINGLE_TYPE_CHOICE);
			selectComboButton(preconfiguredChoice);
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
			String selectedRadioName = getChoice(viewData);
			JRadioButton radioButton = findRadioButton(selectedRadioName);
			radioButton.setSelected(true);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private String getChoice(ViewData viewData)
	{
		String selectedRadioName = viewData.getData(ViewData.TAG_PLANNING_CONFIGERATION_CHOICE);
		if (selectedRadioName.trim().equals(""))
			return PlanningView.STRATEGIC_PLAN_RADIO_CHOICE;

		return selectedRadioName;
	}

	private void saveConfigeration(String tag, String newValue) throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ViewData viewData = project.getCurrentViewData();
			CommandSetObjectData setComboItem = new CommandSetObjectData(viewData.getRef(), tag, newValue);
			project.executeCommand(setComboItem);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}		
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		selectRadioButtonFromProjectSetting(event);
		selectCombBoxFromProjectSetting(event);
	}
	
	private void selectCombBoxFromProjectSetting(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_SINGLE_TYPE_CHOICE))
			return;

		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		String property = setCommand.getDataValue();
		selectComboButton(property);
	}

	private void selectComboButton(String property)
	{
		JComboBox comboBox = findComboBox(PlanningView.PRE_CONFIGURED_COMBO);
		HashMap preConfiguredHashMap = getPreConfiguredButtonNames();
		ComboBoxButton buttonToSelect = (ComboBoxButton) preConfiguredHashMap.get(property);
		comboBox.setSelectedItem(buttonToSelect);
	}

	private void selectRadioButtonFromProjectSetting(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommandWithThisTypeAndTag(ViewData.getObjectType(), ViewData.TAG_PLANNING_CONFIGERATION_CHOICE))
			return;
	
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		updateRadioSelection(setCommand.getDataValue());
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
	
	//TODO planning - Try to simplify the method signature and improve the name
	private void saveCodeList(CodeList masterCodeList, CodeList rowsToShow, String dataTagToHide, String radioName)
	{
		try
		{
			masterCodeList.subtract(rowsToShow);
			saveConfigeration(ViewData.TAG_PLANNING_CONFIGERATION_CHOICE, radioName);
			saveConfigeration(dataTagToHide, masterCodeList.toString());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public HashMap getPreConfiguredButtonNames()
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
	
	public class StrategicButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveCodeList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
			saveCodeList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.STRATEGIC_PLAN_RADIO_CHOICE);
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Goal.OBJECT_NAME);
			listToShow.add(Objective.OBJECT_NAME);
			listToShow.add(Strategy.OBJECT_NAME);
			
			return listToShow;
		}
	}
	
	public class MonitoringButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			saveCodeList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.MONITORING_PLAN_RADIO_CHOICE);
			saveCodeList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.MONITORING_PLAN_RADIO_CHOICE);
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Goal.OBJECT_NAME);
			listToShow.add(Objective.OBJECT_NAME);
			listToShow.add(Indicator.OBJECT_NAME);
			
			return listToShow;
		}
	}
	
	public class WorkPlanButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			saveCodeList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
			saveCodeList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.WORKPLAN_PLAN_RADIO_CHOICE);
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
	}
	
	public class PreConfiguredButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{			
			saveCodeList(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, PlanningView.PRE_CONFIGURED_RADIO_CHOICE);
			saveCodeList(PlanningView.getMasterColumnList(), new CodeList(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, PlanningView.PRE_CONFIGURED_RADIO_CHOICE);
			
			JComboBox checkBox = (JComboBox) e.getSource();
			ComboBoxButton comboChoice = (ComboBoxButton) checkBox.getSelectedItem();
			saveComboSelection(comboChoice);
		}

		private void saveComboSelection(ComboBoxButton comboChoice)
		{
			try
			{
				saveConfigeration(ViewData.TAG_PLANNING_SINGLE_TYPE_CHOICE, comboChoice.getPropertyName());
			}
			catch (Exception e)
			{
				EAM.logException(e);
			}
		}
		
		private CodeList getRowListToShow()
		{
			CodeList listToShow = new CodeList();
			JComboBox comboBox = findComboBox(PlanningView.PRE_CONFIGURED_COMBO);
			ComboBoxButton comboButton = (ComboBoxButton) comboBox.getSelectedItem();
			listToShow.add(comboButton.getPropertyName());
			
			return listToShow;
		}		
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
	private static final String TAG_PREDEFINED_CONFIGURATION = "PredefinedConfuration";
}
