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
package org.miradi.views.workplan;

import javax.swing.JToolBar;

import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionFilterWorkPlanByProjectResource;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateMethodIconOnly;
import org.miradi.actions.ActionTreeCreateSubTask;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeCreateTaskIconOnly;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.actions.ActionWorkPlanBudgetColumnsEditor;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.CreateAccountingCodeDoer;
import org.miradi.views.planning.doers.CreateActivityNodeDoer;
import org.miradi.views.planning.doers.CreateFundingSourceDoer;
import org.miradi.views.planning.doers.CreateMethodNodeDoer;
import org.miradi.views.planning.doers.DeleteAccountingCodeDoer;
import org.miradi.views.planning.doers.DeleteFundingSourceDoer;
import org.miradi.views.planning.doers.ImportAccountingCodesDoer;
import org.miradi.views.planning.doers.PlanningTreeNodeCreationMenuDoer;
import org.miradi.views.planning.doers.TreeNodeCreateMethodDoer;
import org.miradi.views.planning.doers.TreeNodeCreateSubTaskDoer;
import org.miradi.views.planning.doers.TreeNodeCreateTaskDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.planning.doers.WorkPlanColumnsEditorDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResourceDoer;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareActivityDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareMethodDoer;
import org.miradi.views.workplan.doers.ProjectResourceWorkPlanFilterEditDoer;

public class WorkPlanView extends TabbedView
{
	public WorkPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}
	
	@Override
	public void createTabs() throws Exception
	{
		workPlanManagementPanel = WorkPlanManagementPanel.createWorkPlanPanel(getMainWindow());
		resourceManagementPanel = WorkPlanResourcesManagementPanel.createProjectResourcesPanel(getMainWindow());
		accountingCodePoolManagementPanel = WorkPlanAccountingCodeManagementPanel.createAccountingPanel(getMainWindow());
		fundingSourceManagementPanel = WorkPlanFundingSourceManagementPanel.createFundingSourcePanel(getMainWindow());
		
		addNonScrollingTab(workPlanManagementPanel);
		addNonScrollingTab(resourceManagementPanel);
		addNonScrollingTab(accountingCodePoolManagementPanel);
		addNonScrollingTab(fundingSourceManagementPanel);
	}
	
	@Override
	public void deleteTabs() throws Exception
	{
		workPlanManagementPanel.dispose();
		workPlanManagementPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		fundingSourceManagementPanel.dispose();
		fundingSourceManagementPanel = null;
		
		super.deleteTabs();
	}

	@Override
	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.WORK_PLAN_VIEW;
	}

	@Override
	public JToolBar createToolBar()
	{
		return new WorkPlanToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{				
		addDoerToMap(ActionTreeNodeUp.class, new TaskMoveUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TaskMoveDownDoer());
		
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
		addDoerToMap(ActionTreeCreateMethodIconOnly.class, new TreeNodeCreateMethodDoer());
		addDoerToMap(ActionTreeCreateTaskIconOnly.class, new TreeNodeCreateTaskDoer());
		addDoerToMap(ActionTreeCreateActivity.class, new CreateActivityNodeDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new CreateMethodNodeDoer());
		addDoerToMap(ActionTreeCreateTask.class, new TreeNodeCreateTaskDoer());
		addDoerToMap(ActionTreeCreateSubTask.class, new TreeNodeCreateSubTaskDoer());

		addDoerToMap(ActionTreeShareActivity.class, new TreeNodeShareActivityDoer());
		addDoerToMap(ActionTreeShareMethod.class, new TreeNodeShareMethodDoer());

		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResourceDoer());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());
		
		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningTreeNodeCreationMenuDoer());
		addDoerToMap(ActionWorkPlanBudgetColumnsEditor.class, new WorkPlanColumnsEditorDoer());
		addDoerToMap(ActionFilterWorkPlanByProjectResource.class, new ProjectResourceWorkPlanFilterEditDoer());
	}
	
	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}
	
	private PlanningTreeManagementPanel workPlanManagementPanel;
	private PlanningTreeManagementPanel resourceManagementPanel;
	private PlanningTreeManagementPanel accountingCodePoolManagementPanel;
	private PlanningTreeManagementPanel fundingSourceManagementPanel;
}
