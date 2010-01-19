/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.planning;


import java.util.HashMap;

import javax.swing.JToolBar;

import org.miradi.actions.ActionCreatePlanningViewConfigurationMenuDoer;
import org.miradi.actions.ActionCreatePlanningViewEmptyConfiguration;
import org.miradi.actions.ActionCreatePlanningViewPrefilledConfiguration;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeletePlanningViewConfiguration;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionPlanningColumnsEditor;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionPlanningRowsEditor;
import org.miradi.actions.ActionRenamePlanningViewConfiguration;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateActivityIconOnly;
import org.miradi.actions.ActionTreeCreateIndicator;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateMethodIconOnly;
import org.miradi.actions.ActionTreeCreateObjective;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeCreateTaskIconOnly;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.planning.EmptyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.CreatePlanningViewConfigurationMenuDoer;
import org.miradi.views.planning.doers.CreatePlanningViewEmptyConfigurationDoer;
import org.miradi.views.planning.doers.CreatePlanningViewPrefilledConfigurationDoer;
import org.miradi.views.planning.doers.DeletePlanningViewConfigurationDoer;
import org.miradi.views.planning.doers.PlanningColumnsEditorDoer;
import org.miradi.views.planning.doers.PlanningRowsEditorDoer;
import org.miradi.views.planning.doers.PlanningTreeNodeCreationMenuDoer;
import org.miradi.views.planning.doers.RenamePlanningViewConfigurationDoer;
import org.miradi.views.planning.doers.TreeNodeCreateActivityDoer;
import org.miradi.views.planning.doers.TreeNodeCreateIndicatorDoer;
import org.miradi.views.planning.doers.TreeNodeCreateMethodDoer;
import org.miradi.views.planning.doers.TreeNodeCreateObjectiveDoer;
import org.miradi.views.planning.doers.TreeNodeCreateTaskDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResource;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareActivityDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareMethodDoer;

public class PlanningView extends TabbedView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}
	
	@Override
	public void createTabs() throws Exception
	{
		planningManagementPanel = ConfigurablePlanningTreeManagementPanel.createConfigurablePlanningPanel(getMainWindow());
		strategicPlanManagementPanel = ActionPlanManagementPanel.createStrategicPlanPanel(getMainWindow());
		monitoringPlanManagementPanel = MonitoringPlanManagementPanel.createMonitoringPlanPanel(getMainWindow());
		objectsOnlyManagementPanel = ObjectsOnlyManagementPanel.createObjectsOnlyPanel(getMainWindow());
		resourceManagementPanel = StrategicPlanResourcesManagementPanel.createProjectResourcesPanelWithoutBudgetColumns(getMainWindow());
		
		managementPanelMap = new HashMap();

		addPlanningManagementTab(strategicPlanManagementPanel);
		addPlanningManagementTab(monitoringPlanManagementPanel);
		addPlanningManagementTab(objectsOnlyManagementPanel);
		addPlanningManagementTab(planningManagementPanel);
		
		addNonScrollingTab(resourceManagementPanel);
	}

	private void addPlanningManagementTab(PlanningTreeManagementPanel managementPanel)
	{
		addNonScrollingTab(managementPanel);
		managementPanelMap.put(managementPanel.getPanelDescription(), managementPanel);
	}

	public RowColumnProvider getRowColumnProvider()
	{
		ObjectManagementPanel managmentPanel = (ObjectManagementPanel)getCurrentTabContents();
		String panelDescriptionAsKey = managmentPanel.getPanelDescription();
		if (getManagementPanelMap().containsKey(panelDescriptionAsKey))
			return getManagementPanelMap().get(panelDescriptionAsKey).getRowColumnProvider();
		
		return new EmptyRowColumnProvider();
	}
	
	@Override
	public void deleteTabs() throws Exception
	{
		planningManagementPanel.dispose();
		planningManagementPanel = null;
		
		strategicPlanManagementPanel.dispose();
		strategicPlanManagementPanel = null;
		
		monitoringPlanManagementPanel.dispose();
		monitoringPlanManagementPanel = null;
		
		objectsOnlyManagementPanel.dispose();
		objectsOnlyManagementPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		
		super.deleteTabs();
	}

	@Override
	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.PLANNING_VIEW_NAME;
	}

	@Override
	public JToolBar createToolBar()
	{
		return new PlanningToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{
		addDoerToMap(ActionTreeNodeUp.class, new TaskMoveUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TaskMoveDownDoer());
		
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
		addDoerToMap(ActionTreeCreateActivityIconOnly.class, new TreeNodeCreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethodIconOnly.class, new TreeNodeCreateMethodDoer());
		addDoerToMap(ActionTreeCreateTaskIconOnly.class, new TreeNodeCreateTaskDoer());
		addDoerToMap(ActionTreeCreateActivity.class, new TreeNodeCreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new TreeNodeCreateMethodDoer());	
		addDoerToMap(ActionTreeCreateTask.class, new TreeNodeCreateTaskDoer());
				
		addDoerToMap(ActionTreeShareActivity.class, new TreeNodeShareActivityDoer());
		addDoerToMap(ActionTreeShareMethod.class, new TreeNodeShareMethodDoer());

		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());

		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningTreeNodeCreationMenuDoer());

		addDoerToMap(ActionTreeCreateIndicator.class, new TreeNodeCreateIndicatorDoer());
		addDoerToMap(ActionTreeCreateObjective.class, new TreeNodeCreateObjectiveDoer());

		addDoerToMap(ActionCreatePlanningViewConfigurationMenuDoer.class, new CreatePlanningViewConfigurationMenuDoer());
		addDoerToMap(ActionCreatePlanningViewEmptyConfiguration.class, new CreatePlanningViewEmptyConfigurationDoer());
		addDoerToMap(ActionCreatePlanningViewPrefilledConfiguration.class, new CreatePlanningViewPrefilledConfigurationDoer());
		addDoerToMap(ActionDeletePlanningViewConfiguration.class, new DeletePlanningViewConfigurationDoer());
		addDoerToMap(ActionRenamePlanningViewConfiguration.class, new RenamePlanningViewConfigurationDoer());
		addDoerToMap(ActionPlanningRowsEditor.class, new PlanningRowsEditorDoer());
		addDoerToMap(ActionPlanningColumnsEditor.class, new PlanningColumnsEditorDoer());
	}
	
	public HashMap<String, PlanningTreeManagementPanel> getManagementPanelMap()
	{
		return managementPanelMap;
	}

	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}

	private PlanningTreeManagementPanel objectsOnlyManagementPanel;
	private PlanningTreeManagementPanel planningManagementPanel;
	private PlanningTreeManagementPanel strategicPlanManagementPanel;
	private PlanningTreeManagementPanel monitoringPlanManagementPanel;
	private PlanningTreeManagementPanel resourceManagementPanel;
	
	private HashMap<String, PlanningTreeManagementPanel> managementPanelMap;
}
