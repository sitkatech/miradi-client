/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.actions.*;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.planning.EmptyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.MainWindow;
import org.miradi.main.MiradiToolBar;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.diagram.doers.CreateFutureStatusDoer;
import org.miradi.views.planning.doers.*;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResourceDoer;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.workplan.WorkPlanBudgetCategoryManagementPanel;

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
		actionPlanManagementPanel = ActionPlanManagementPanel.createActionPlanPanel(getMainWindow());
		monitoringPlanManagementPanel = MonitoringPlanManagementPanel.createMonitoringPlanPanel(getMainWindow());
		objectsOnlyManagementPanel = ObjectsOnlyManagementPanel.createObjectsOnlyPanel(getMainWindow());
		customPlanManagementPanel = ConfigurablePlanningManagementPanel.createActionPlanPanel(getMainWindow());
		resourceManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new PlanningProjectResourceManagementConfiguration(getProject()));
		
		managementPanelMap = new HashMap<String, PlanningTreeManagementPanel>();

		addPlanningManagementTab(actionPlanManagementPanel);
		addPlanningManagementTab(monitoringPlanManagementPanel);
		addPlanningManagementTab(objectsOnlyManagementPanel);
		addPlanningManagementTab(customPlanManagementPanel);
		
		addNonScrollingTab(resourceManagementPanel);
	}

	private void addPlanningManagementTab(PlanningTreeManagementPanel managementPanel)
	{
		addNonScrollingTab(managementPanel);
		managementPanelMap.put(managementPanel.getPanelDescription(), managementPanel);
	}

	public RowColumnProvider getRowColumnProvider()
	{
		ObjectManagementPanel managementPanel = (ObjectManagementPanel)getCurrentTabContents();
		String panelDescriptionAsKey = managementPanel.getPanelDescription();
		if (getManagementPanelMap().containsKey(panelDescriptionAsKey))
			return getManagementPanelMap().get(panelDescriptionAsKey).getRowColumnProvider();
		
		return new EmptyRowColumnProvider();
	}
	
	@Override
	public void deleteTabs() throws Exception
	{
		actionPlanManagementPanel.dispose();
		actionPlanManagementPanel = null;
		
		monitoringPlanManagementPanel.dispose();
		monitoringPlanManagementPanel = null;
		
		objectsOnlyManagementPanel.dispose();
		objectsOnlyManagementPanel = null;
		
		customPlanManagementPanel.dispose();
		customPlanManagementPanel = null;
		
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
	public MiradiToolBar createToolBar()
	{
		return new PlanningToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{
		addDoerToMap(ActionTreeNodeUp.class, new TaskMoveUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TaskMoveDownDoer());
		
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
		addDoerToMap(ActionTreeCreateActivity.class, new CreateActivityNodeDoer());
		addDoerToMap(ActionTreeCreateMonitoringActivity.class, new CreateMonitoringActivityNodeDoer());
		addDoerToMap(ActionTreeMoveActivity.class, new TreeNodeMoveActivityDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new CreateMethodNodeDoer());
		addDoerToMap(ActionCreateTask.class, new CreateTaskDoer());
		addDoerToMap(ActionCreateSameLevelTask.class, new CreateSameLevelTaskDoer());
		addDoerToMap(ActionTreeCreateRelevancyActivity.class, new CreateRelevancyActivityDoer());
				
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResourceDoer());

		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningTreeNodeCreationMenuDoer());

		addDoerToMap(ActionTreeCreateIndicator.class, new TreeNodeCreateIndicatorDoer());
		addDoerToMap(ActionTreeCreateObjective.class, new TreeNodeCreateObjectiveDoer());

		addDoerToMap(ActionCreatePlanningViewConfigurationMenu.class, new CreatePlanningViewConfigurationMenuDoer());
		addDoerToMap(ActionCreatePlanningViewEmptyConfiguration.class, new CreatePlanningViewEmptyConfigurationDoer());
		addDoerToMap(ActionCreatePlanningViewPrefilledConfiguration.class, new CreatePlanningViewPrefilledConfigurationDoer());
		addDoerToMap(ActionDeletePlanningViewConfiguration.class, new DeletePlanningViewConfigurationDoer());
		addDoerToMap(ActionPlanningRowsEditor.class, new PlanningRowsEditorDoer());
		addDoerToMap(ActionPlanningColumnsEditor.class, new PlanningColumnsEditorDoer());
		addDoerToMap(ActionPlanningCustomizeDialogPopup.class, new PlanningCustomizeDialogPopupDoer());
		addDoerToMap(ActionCreateCustomFromCurrentTreeTableConfiguration.class, new CreateCustomFromCurrentTreeTableConfigurationDoer());
		addDoerToMap(ActionCreateIndicatorMeasurement.class, new CreateMeasurementDoer());
		addDoerToMap(ActionCreateFutureStatus.class, new CreateFutureStatusDoer());
	}
	
	private HashMap<String, PlanningTreeManagementPanel> getManagementPanelMap()
	{
		return managementPanelMap;
	}

	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}

	private PlanningTreeManagementPanel actionPlanManagementPanel;
	private PlanningTreeManagementPanel monitoringPlanManagementPanel;
	private PlanningTreeManagementPanel objectsOnlyManagementPanel;
	private PlanningTreeManagementPanel customPlanManagementPanel;
	private PlanningTreeManagementPanel resourceManagementPanel;
	
	private HashMap<String, PlanningTreeManagementPanel> managementPanelMap;
}
