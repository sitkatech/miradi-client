/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
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

public class PlanningCustomizationPanel extends JPanel
{
	public PlanningCustomizationPanel(Project projectToUse)
	{
		super();
		project = projectToUse;
		createLegendButtonPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}

	protected void createLegendButtonPanel()
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(3, 2));
		ButtonGroup buttonGroup = new ButtonGroup();
		StrategicButtonHandler strategicHandler = new StrategicButtonHandler();
		addRadioButton(jPanel, buttonGroup, strategicHandler, EAM.text("Strategic Plan"), STRATEGIC_VIEW);
		
		MonitoringButtonHandler monitoringHandler = new MonitoringButtonHandler();
		addRadioButton(jPanel, buttonGroup, monitoringHandler, EAM.text("Monitoring Plan"), MONITORING_VIEW);
		
		WorkPlanButtonHandler workPlanHandler = new WorkPlanButtonHandler();
		addRadioButton(jPanel, buttonGroup, workPlanHandler, EAM.text("Work Plan"), WORKPLAN_VIEW);
		
		add(jPanel);
	}

	private void addRadioButton(JPanel jPanel, ButtonGroup buttonGroup, ActionListener handler, String buttonName, String propertyName)
	{
		JRadioButton radioButton = new JRadioButton();
		buttonGroup.add(radioButton);
		
		radioButton.putClientProperty(CANNED_VIEW_NAME, propertyName);
		radioButton.addActionListener(handler);
		jPanel.add(new JLabel(buttonName));
		jPanel.add(radioButton);
	}
	
	private void saveData(String tag, CodeList listToHide)
	{
		try
		{
			ViewData data = project.getCurrentViewData();
			CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(data.getRef(), tag, listToHide.toString());
			project.executeCommand(setLegendSettingsCommand);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private void hideRows(CodeList rowsToShow)
	{
		CodeList masterCodeList = PlanningView.getMasterRowList();
		masterCodeList.subtract(rowsToShow);
		saveData(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES, masterCodeList);
	}
	
	public class StrategicButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			hideRows(getRowListToShow());
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
			hideRows(getRowListToShow());
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
			hideRows(getRowListToShow());
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
	
	private static final String CANNED_VIEW_NAME = "cannedViewName";
	
	private static final String STRATEGIC_VIEW = "StrategicView";
	private static final String MONITORING_VIEW = "MonitoringView";
	private static final String WORKPLAN_VIEW = "WorkPlanView";
}
