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

import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTabbedPane;
import org.miradi.project.Project;

public class DashboardMainPanel extends PanelTabbedPane
{
	public DashboardMainPanel(Project projectToUse) throws Exception
	{
		super();
		
		project = projectToUse;
		createTabs();
		addTabs();
	}
	
	public void dispose()
	{
		disposeTab(conceptualizeDashboardTab);
	}
	
	public void becomeActive()
	{
		getCurrentTab().becomeActive();
	}
	
	public void becomeInactive()
	{
		getCurrentTab().becomeInactive();
	}

	private AbstractObjectDataInputPanel getCurrentTab()
	{
		return (AbstractObjectDataInputPanel) getSelectedComponent();
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
	}

	private void addTabs()
	{
		addTab(conceptualizeDashboardTab.getPanelDescription(), conceptualizeDashboardTab);
		addTab(planActionsAndMonitoringTab.getPanelDescription(), planActionsAndMonitoringTab);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private Project project;
	private ConceptualizeDashboardTabV2 conceptualizeDashboardTab;
	private PlanActionsAndMonitoringTab planActionsAndMonitoringTab;
}
