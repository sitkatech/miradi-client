/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import java.awt.BorderLayout;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionModifyIndicator;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.CreateIndicator;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MonitoringView extends UmbrellaView
{
	public MonitoringView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MonitoringToolBar(mainWindowToUse.getActions()));

		tabs = new JTabbedPane();
		add(tabs, BorderLayout.CENTER);
		tabs.addChangeListener(new TabChangeListener());
		
		addMonitoringPlanDoersToMap();
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Monitoring ";
	}

	public void becomeActive() throws Exception
	{
		int mostRecentTabIndex = currentTab;
		
		tabs.removeAll();

		monitoringPanel = new MonitoringPanel(getProject());
		indicatorManagementPanel = new IndicatorManagementPanel(this);
		tabs.add(EAM.text("Monitoring Plan"), monitoringPanel);
		tabs.add(EAM.text("Indicators"), indicatorManagementPanel);
		
		tabs.setSelectedIndex(mostRecentTabIndex);
	}

	public void becomeInactive() throws Exception
	{
		monitoringPanel = null;
		indicatorManagementPanel = null;
	}

	public IndicatorManagementPanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	private void addMonitoringPlanDoersToMap()
	{
		addDoerToMap(ActionCreateIndicator.class, new CreateIndicator());
		addDoerToMap(ActionModifyIndicator.class, new ModifyIndicator());
		addDoerToMap(ActionDeleteIndicator.class, new DeleteIndicator());

		
	}
	
	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent e)
		{
			currentTab = tabs.getSelectedIndex();
			closeActivePropertiesDialog();
		}
		
	}

	JTabbedPane tabs;
	int currentTab;
	
	MonitoringPanel monitoringPanel;
	IndicatorManagementPanel indicatorManagementPanel;
}
