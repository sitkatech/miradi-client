/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.actions.ActionCreateIndicator;
import org.conservationmeasures.eam.actions.ActionDeleteIndicator;
import org.conservationmeasures.eam.actions.ActionModifyIndicator;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.umbrella.CreateIndicator;
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
		indicatorManagementPanel = new IndicatorManagementPanel(this);
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
	
	MonitoringPanel monitoringPanel;
	IndicatorManagementPanel indicatorManagementPanel;
}
