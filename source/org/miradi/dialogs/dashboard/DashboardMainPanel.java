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

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.project.Project;

public class DashboardMainPanel extends DisposablePanel
{
	public DashboardMainPanel(Project projectToUse) throws Exception
	{
		super();
		
		project = projectToUse;
		tabs = new PanelTabbedPane();
		tabs.addChangeListener(new TabChangeListener());
		createTabs();
		setInitialTab();
		addTabsToTabbedPane();
		add(tabs);
	}

	@Override
	public void dispose()
	{
		super.dispose();
		
		disposeTab(conceptualizeDashboardTab);
		disposeTab(planActionsAndMonitoringTab);
		disposeTab(actionsAndMonitoringTab);
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

	private AbstractObjectDataInputPanel getCurrentTab()
	{
		return (AbstractObjectDataInputPanel) tabs.getSelectedComponent();
	}
	
	private void disposeTab(ObjectDataInputPanel tab)
	{
		if (tab != null)
		{
			tab.dispose();
			tab = null;
		}
	}

	private void createTabs() throws Exception
	{
		conceptualizeDashboardTab = new ConceptualizeDashboardTabV2(getProject());
		planActionsAndMonitoringTab = new PlanActionsAndMonitoringTab(getProject());
		actionsAndMonitoringTab = new ImplementActionsAndMonitoringDashboardTab(getProject());
		analyzeAdaptAndUseTab = new AnalyzeAdaptAndUseTab(getProject());
		captureAndShareLearningTab = new CaptureAndShareLearningTab(getProject());
	}

	private void addTabsToTabbedPane()
	{
		addTab(conceptualizeDashboardTab);
		addTab(planActionsAndMonitoringTab);
		addTab(actionsAndMonitoringTab);
		addTab(analyzeAdaptAndUseTab);
		addTab(captureAndShareLearningTab);
	}
	
	private void setInitialTab()
	{
		currentTab = conceptualizeDashboardTab;
	}
	
	private void addTab(AbstractDashboardTab tab)
	{
		tabs.addTab(tab.getPanelDescription(), tab);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			currentTab.becomeInactive();
			AbstractObjectDataInputPanel selectedTab = (AbstractObjectDataInputPanel) tabs.getSelectedComponent();
			currentTab = selectedTab;
			currentTab.becomeActive();
		}
	}
	
	private JTabbedPane tabs;
	private Project project;
	private AbstractObjectDataInputPanel currentTab;
	private AbstractDashboardTab conceptualizeDashboardTab;
	private AbstractDashboardTab planActionsAndMonitoringTab;
	private AbstractDashboardTab actionsAndMonitoringTab;
	private AbstractDashboardTab analyzeAdaptAndUseTab;
	private AbstractDashboardTab captureAndShareLearningTab;
}
