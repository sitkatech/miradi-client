/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.monitoring.wizard.MonitoringPlanWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;

public class MonitoringView extends TabbedView
{
	public MonitoringView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MonitoringToolBar(mainWindowToUse.getActions()));

		addMonitoringPlanDoersToMap();
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.MONITORING_VIEW_NAME;
	}

	public void createTabs() throws Exception
	{
		monitoringPanel = new MonitoringPanel(getProject());
		indicatorManagementPanel = new IndicatorPoolTablePanel(this);
		addTab(EAM.text("Monitoring Plan"), monitoringPanel);
		addTab(EAM.text("Indicators"), indicatorManagementPanel);
	}

	public void deleteTabs() throws Exception
	{
		monitoringPanel = null;
		indicatorManagementPanel.dispose();
		indicatorManagementPanel = null;
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return new MonitoringPlanWizardPanel();
	}

	public IndicatorPoolTablePanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	private void addMonitoringPlanDoersToMap()
	{
	}
	
	MonitoringPanel monitoringPanel;
	IndicatorPoolTablePanel indicatorManagementPanel;
}
