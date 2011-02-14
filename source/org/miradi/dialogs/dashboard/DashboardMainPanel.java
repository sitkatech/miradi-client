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

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.main.MainWindow;

public class DashboardMainPanel extends DisposablePanel
{
	public DashboardMainPanel(MainWindow mainWindowToUse) throws Exception
	{
		super();
		
		mainWindow = mainWindowToUse;
		tabs = new PanelTabbedPane();
		createTabs();
		setInitialTab();
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
		conceptualizeDashboardTab = OpenStandardsConceptualizeDashboardTab.createLeftPanel(getMainWindow());
		planActionsAndMonitoringTab = OpenStandarsPlanActionsAndMonitoringTab.createLeftPanel(getMainWindow());
		implementActionsAndMonitoringTab = OpenStandardsImplementActionsAndMonitoringTab.createLeftPanel(getMainWindow());
		analyzeAdaptAndUseTab = OpenStandardsAnalyzeAdaptAndUseTab.createLeftPanel(getMainWindow());
		captureAndShareLearningTab = OpenStandardsCaptureAndShareLearningTab.createLeftPanel(getMainWindow());
	}

	private void addTabsToTabbedPanel()
	{
		addTab(conceptualizeDashboardTab);
		addTab(planActionsAndMonitoringTab);
		addTab(implementActionsAndMonitoringTab);
		addTab(analyzeAdaptAndUseTab);
		addTab(captureAndShareLearningTab);
	}
	
	private void setInitialTab()
	{
		currentTab = conceptualizeDashboardTab;
	}
	
	private void addTab(OpenStandardsDashboardTab tab)
	{
		tabs.addTab(tab.getPanelDescription(), tab);
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			currentTab.becomeInactive();
			OpenStandardsDashboardTab selectedTab = (OpenStandardsDashboardTab) tabs.getSelectedComponent();
			currentTab = selectedTab;
			currentTab.becomeActive();
		}
	}
	
	private JTabbedPane tabs;
	private MainWindow mainWindow;
	private DisposablePanel currentTab;
	private OpenStandardsDashboardTab conceptualizeDashboardTab;
	private OpenStandardsDashboardTab planActionsAndMonitoringTab;
	private OpenStandardsDashboardTab implementActionsAndMonitoringTab;
	private	OpenStandardsDashboardTab analyzeAdaptAndUseTab;
	private OpenStandardsDashboardTab captureAndShareLearningTab;
}
