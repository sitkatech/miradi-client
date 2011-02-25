/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogs.dashboard;

import java.util.HashMap;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;

public class DashboardMainPanel extends DisposablePanel
{
	public DashboardMainPanel(MainWindow mainWindowToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		codeToTabMap = new HashMap<String, OpenStandardsDashboardTab>();
		tabs = new PanelTabbedPane();
		createTabs();
		addTabsToTabbedPanel();
		add(tabs);
		tabs.addChangeListener(new TabChangeListener());
	}

	@Override
	public void dispose()
	{
		super.dispose();
		
		disposeTab(conceptualizeDashboardTab);
		disposeTab(planActionsAndMonitoringTab);
		disposeTab(implementActionsAndMonitoringTab);
		disposeTab(analyzeAdaptAndUseTab);
		disposeTab(captureAndShareLearningTab);
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		selectTabComponent();
		getCurrentTab().becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		getCurrentTab().becomeInactive();
		
		super.becomeInactive();
	}

	private DisposablePanel getCurrentTab()
	{
		return (DisposablePanel) tabs.getSelectedComponent();
	}
	
	private void disposeTab(DisposablePanel tab)
	{
		if (tab != null)
		{
			tab.dispose();
			tab = null;
		}
	}

	private void createTabs() throws Exception
	{
		conceptualizeDashboardTab = OpenStandardsConceptualizeDashboardTab.createPanel(getMainWindow());
		planActionsAndMonitoringTab = OpenStandarsPlanActionsAndMonitoringTab.createPanel(getMainWindow());
		implementActionsAndMonitoringTab = OpenStandardsImplementActionsAndMonitoringTab.createPanel(getMainWindow());
		analyzeAdaptAndUseTab = OpenStandardsAnalyzeAdaptAndUseTab.createLeftPanel(getMainWindow());
		captureAndShareLearningTab = OpenStandardsCaptureAndShareLearningTab.createLeftPanel(getMainWindow());
	}

	private void addTabsToTabbedPanel()
	{
		ignoreTabChanges = true;
		try
		{
			addTab(conceptualizeDashboardTab);
			addTab(planActionsAndMonitoringTab);
			addTab(implementActionsAndMonitoringTab);
			addTab(analyzeAdaptAndUseTab);
			addTab(captureAndShareLearningTab);
		}
		finally 
		{
			ignoreTabChanges = false;
		}
	}
	
	private void selectTabComponent()
	{
		ignoreTabChanges = true;
		try
		{
			ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
			Dashboard dashboard = Dashboard.find(getProject(), dashboardRef);
			String dashboardTabCode = dashboard.getData(Dashboard.TAG_CURRENT_DASHBOARD_TAB);
			currentTab = codeToTabMap.get(dashboardTabCode);		
			if (currentTab == null)
				currentTab = conceptualizeDashboardTab;

			tabs.setSelectedComponent(currentTab);
		}
		finally 
		{
			ignoreTabChanges = false;
		}
	}
	
	private void addTab(OpenStandardsDashboardTab tab)
	{
		tabs.addTab(tab.getPanelDescription(), tab);
		codeToTabMap.put(tab.getTabCode(), tab);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private Project getProject()
	{
		return getMainWindow().getProject();
	}
	
	private class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			if(ignoreTabChanges)
				return;

			currentTab.becomeInactive();
			OpenStandardsDashboardTab selectedTab = (OpenStandardsDashboardTab) tabs.getSelectedComponent();
			currentTab = selectedTab;
			currentTab.becomeActive();

			saveTab(selectedTab);
		}

		private void saveTab(OpenStandardsDashboardTab selectedTab)
		{
			try
			{
				ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
				CommandSetObjectData setTabCommand = new CommandSetObjectData(dashboardRef, Dashboard.TAG_CURRENT_DASHBOARD_TAB, selectedTab.getTabCode());
				getProject().executeCommand(setTabCommand);
			}
			catch (Exception e)
			{
				EAM.logException(e);
				EAM.unexpectedErrorDialog(e);
			}
		}
	}
	
	private JTabbedPane tabs;
	private MainWindow mainWindow;
	private DisposablePanel currentTab;
	private boolean ignoreTabChanges;
	private HashMap<String, OpenStandardsDashboardTab> codeToTabMap; 
	private OpenStandardsDashboardTab conceptualizeDashboardTab;
	private OpenStandardsDashboardTab planActionsAndMonitoringTab;
	private OpenStandardsDashboardTab implementActionsAndMonitoringTab;
	private	OpenStandardsDashboardTab analyzeAdaptAndUseTab;
	private OpenStandardsDashboardTab captureAndShareLearningTab;
}
