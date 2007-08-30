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

import org.conservationmeasures.eam.main.EAM;

import com.jhlabs.awt.GridLayoutPlus;

public class PlanningCustomizationPanel extends JPanel
{
	public PlanningCustomizationPanel()
	{
		super();
		createLegendButtonPanel();
		setBorder(BorderFactory.createTitledBorder(EAM.text("Standard Views")));
	}

	protected void createLegendButtonPanel()
	{
		JPanel jPanel = new JPanel(new GridLayoutPlus(3, 2));
		ButtonGroup buttonGroup = new ButtonGroup();
		StratigicButtonHandler strategicHandler = new StratigicButtonHandler();
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
	
//	private CodeList getCustomizedStrategicRowCodeList()
//	{
//		CodeList strategicRows = new CodeList();
//		
//		
//		return strategicRows;
//		
//	}
	
	public class StratigicButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}	
	}
	
	public class MonitoringButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}	
	}
	
	public class WorkPlanButtonHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}		
	}
	
	private static final String CANNED_VIEW_NAME = "cannedViewName";
	
	private static final String STRATEGIC_VIEW = "StratigicView";
	private static final String MONITORING_VIEW = "MonitoringView";
	private static final String WORKPLAN_VIEW = "WorkPlanView";
}
