/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;


import javax.swing.JToolBar;

import org.conservationmeasures.eam.dialogs.IndicatorPoolManagementPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.wizard.WizardPanel;

public class MonitoringView extends TabbedView
{
	public MonitoringView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
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

	public JToolBar createToolBar()
	{
		return new MonitoringToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		monitoringPanel = new MonitoringPanel(getProject());
		indicatorManagementPanel = new IndicatorPoolManagementPanel(getProject(), getMainWindow(), getActions());
		addTab(EAM.text("Monitoring Plan"), monitoringPanel);
		addNonScrollableTab(indicatorManagementPanel);
	}
	
	public void deleteTabs() throws Exception
	{
		monitoringPanel.dispose();
		monitoringPanel = null;
		indicatorManagementPanel.dispose();
		indicatorManagementPanel = null;
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}

	public IndicatorPoolManagementPanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	private void addMonitoringPlanDoersToMap()
	{
	}
	
	public void becomeActive() throws Exception
	{
		super.becomeActive();

		indicatorManagementPanel.updateSplitterLocation();
	}

	MonitoringPanel monitoringPanel;
	IndicatorPoolManagementPanel indicatorManagementPanel;
}
