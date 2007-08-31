/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.Command;
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
		super();
		project = projectToUse;
		radioButtons = new Hashtable();
		createLegendButtonPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}

	protected void createLegendButtonPanel()
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(3, 2));
		ButtonGroup buttonGroup = new ButtonGroup();
		addRadioButton(jPanel, buttonGroup, new StrategicButtonHandler(), EAM.text("Strategic Plan"), STRATEGIC_PLAN);
		addRadioButton(jPanel, buttonGroup, new MonitoringButtonHandler(), EAM.text("Monitoring Plan"), MONITORING_PLAN);
		addRadioButton(jPanel, buttonGroup, new WorkPlanButtonHandler(), EAM.text("Work Plan"), WORKPLAN_PLAN);
		
		add(jPanel);
	}

	private void addRadioButton(JPanel jPanel, ButtonGroup buttonGroup, ActionListener handler, String buttonName, String propertyName)
	{
		JRadioButton radioButton = new JRadioButton();
		buttonGroup.add(radioButton);
		
		radioButton.putClientProperty(TAG_PREDEFINED_CONFIGURATION, propertyName);
		radioButton.addActionListener(handler);
		jPanel.add(new JLabel(buttonName));
		jPanel.add(radioButton);
		radioButtons.put(propertyName, radioButton);
		selectRadioButton(radioButton, propertyName);
	}
	
	private void selectRadioButton(JRadioButton radioButton, String propertyName)
	{
		try
		{
			ViewData viewData = project.getCurrentViewData();
			String selectedRadionName = viewData.getData(ViewData.TAG_PLANNING_RADIO_CHOICE);
			if (!propertyName.equals(selectedRadionName))
				return;
			
			radioButton.setSelected(true);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void saveData(String tag, CodeList listToHide, String radioName) throws Exception
	{
		project.executeCommand(new CommandBeginTransaction());
		try
		{
			ViewData viewData = project.getCurrentViewData();
			CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(viewData.getRef(), tag, listToHide.toString());
			project.executeCommand(setLegendSettingsCommand);
			
			CommandSetObjectData setRadioCommand = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_PLANNING_RADIO_CHOICE, radioName);
			project.executeCommand(setRadioCommand);
		}
		finally
		{
			project.executeCommand(new CommandEndTransaction());
		}
		
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		selectRadioButtonFromProjectSetting(event.getCommand());
	}
	
	private void selectRadioButtonFromProjectSetting(Command command)
	{
		if (! command.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			return;
		
		CommandSetObjectData setCommand = (CommandSetObjectData) command;
		if (! setCommand.getFieldTag().equals(ViewData.TAG_PLANNING_RADIO_CHOICE))
			return;
		
		updateRadioSelection(setCommand.getDataValue());
	}

	private void updateRadioSelection(String selectedProperty)
	{
		findRadionButton(selectedProperty).setSelected(true);
	}

	private JRadioButton findRadionButton(String property)
	{
		return (JRadioButton) radioButtons.get(property);
	}
	
	private void hideData(CodeList masterCodeList, CodeList rowsToShow, String dataTagToHide, String radioName)
	{
		try
		{
			masterCodeList.subtract(rowsToShow);
			saveData(dataTagToHide, masterCodeList, radioName);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public class StrategicButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			hideData(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, STRATEGIC_PLAN);
			hideData(PlanningView.getMasterColumnList(), getColumnListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, STRATEGIC_PLAN);
		}
		
		private CodeList getColumnListToShow()
		{
			CodeList listToShow = new CodeList();
			listToShow.add(Indicator.TAG_PRIORITY);
			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			
			return listToShow;
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
			hideData(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, MONITORING_PLAN);
			hideData(PlanningView.getMasterColumnList(), getColumnListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, STRATEGIC_PLAN);
		}
		
		private CodeList getColumnListToShow()
		{
			CodeList listToShow = new CodeList();

			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			listToShow.add(Indicator.PSEUDO_TAG_METHODS);
			listToShow.add(Indicator.PSEUDO_TAG_FACTOR);
			
			return listToShow;
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
			hideData(PlanningView.getMasterRowList(), getRowListToShow(), ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, WORKPLAN_PLAN);
			hideData(PlanningView.getMasterColumnList(), getColumnListToShow(), ViewData.TAG_PLANNING_HIDDEN_COL_TYPES, STRATEGIC_PLAN);
		}
		
		private CodeList getColumnListToShow()
		{
			CodeList listToShow = new CodeList();

			listToShow.add(Task.PSEUDO_TAG_ASSIGNED_RESOURCES_HTML);
			listToShow.add(Task.PSEUDO_TAG_COMBINED_EFFORT_DATES);
			listToShow.add(Task.PSEUDO_TAG_TASK_TOTAL);
			
			return listToShow;
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
	
	private Project project;
	private Hashtable<String, Component> radioButtons;
	private static final String TAG_PREDEFINED_CONFIGURATION = "PredefinedConfuration";
	
	private static final String STRATEGIC_PLAN = "StrategicPlan";
	private static final String MONITORING_PLAN = "MonitoringPlan";
	private static final String WORKPLAN_PLAN = "WorkPlanPlan";
}
