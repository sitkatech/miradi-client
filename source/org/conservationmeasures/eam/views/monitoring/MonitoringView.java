/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;


import org.conservationmeasures.eam.dialogs.IndicatorPoolManagementPanel;
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
		indicatorManagementPanel = new IndicatorPoolManagementPanel(getProject(), getMainWindow(), getActions());
		addTab(EAM.text("Monitoring Plan"), monitoringPanel);
		addNoneScrollableTab(indicatorManagementPanel);
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
		wizardPanel = new MonitoringPlanWizardPanel(getMainWindow());
		return wizardPanel;
	}

	public IndicatorPoolManagementPanel getIndicatorManagementPanel()
	{
		return indicatorManagementPanel;
	}
	
	private void addMonitoringPlanDoersToMap()
	{
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}
	
	WizardPanel wizardPanel;
	MonitoringPanel monitoringPanel;
	IndicatorPoolManagementPanel indicatorManagementPanel;
}
