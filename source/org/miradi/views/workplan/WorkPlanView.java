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


import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionCreateCategoryOne;
import org.miradi.actions.ActionCreateCategoryTwo;
import org.miradi.actions.ActionCreateChildTask;
import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionCreateSameLevelTask;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionDeleteCategoryOne;
import org.miradi.actions.ActionDeleteCategoryTwo;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionEditAnalysisRows;
import org.miradi.actions.ActionFilterWorkPlanByProjectResource;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.actions.ActionWorkPlanBudgetCustomizeTableEditor;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.main.MainWindow;
import org.miradi.main.MiradiToolBar;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.CreateAccountingCodeDoer;
import org.miradi.views.planning.doers.CreateActivityNodeDoer;
import org.miradi.views.planning.doers.CreateChildTaskDoer;
import org.miradi.views.planning.doers.CreateFundingSourceDoer;
import org.miradi.views.planning.doers.CreateMethodNodeDoer;
import org.miradi.views.planning.doers.CreateSameLevelTaskDoer;
import org.miradi.views.planning.doers.DeleteAccountingCodeDoer;
import org.miradi.views.planning.doers.DeleteFundingSourceDoer;
import org.miradi.views.planning.doers.ImportAccountingCodesDoer;
import org.miradi.views.planning.doers.PlanningTreeNodeCreationMenuDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.planning.doers.WorkPlanCustomizeTableEditorDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResourceDoer;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareActivityDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareMethodDoer;
import org.miradi.views.workplan.doers.CreateCategoryOneDoer;
import org.miradi.views.workplan.doers.CreateCategoryTwoDoer;
import org.miradi.views.workplan.doers.DeleteCategoryOneDoer;
import org.miradi.views.workplan.doers.DeleteCategoryTwoDoer;
import org.miradi.views.workplan.doers.EditAnalysisRowsDoer;
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
		rollupReportsManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new AnalysisManagementConfiguration(getProject()));
		resourceManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new ProjectResourceManagementConfiguration(getProject()));
		accountingCodePoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new AccountingCodeManagementConfiguration(getProject()));
		fundingSourcePoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new FundingSourceManagementConfiguration(getProject())); 
		categoryOnePoolMangementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new BudgetCategoryOneManagementConfiguration(getProject()));
		categoryTwoPoolMangementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new BudgetCategoryTwoManagementConfiguration(getProject()));
		
		addNonScrollingTab(workPlanManagementPanel);
		addNonScrollingTab(rollupReportsManagementPanel);
		addNonScrollingTab(resourceManagementPanel);
		addNonScrollingTab(accountingCodePoolManagementPanel);
		addNonScrollingTab(fundingSourcePoolManagementPanel);
		addNonScrollingTab(categoryOnePoolMangementPanel);
		addNonScrollingTab(categoryTwoPoolMangementPanel);
	}
	
	@Override
	public void deleteTabs() throws Exception
	{
		workPlanManagementPanel.dispose();
		workPlanManagementPanel = null;
		
		rollupReportsManagementPanel.dispose();
		rollupReportsManagementPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
		
		categoryOnePoolMangementPanel.dispose();
		categoryOnePoolMangementPanel = null;
		
		categoryTwoPoolMangementPanel.dispose();
		categoryTwoPoolMangementPanel = null;
		
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
	public MiradiToolBar createToolBar()
	{
		return new WorkPlanToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{				
		addDoerToMap(ActionTreeNodeUp.class, new TaskMoveUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TaskMoveDownDoer());
		
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
		addDoerToMap(ActionTreeCreateActivity.class, new CreateActivityNodeDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new CreateMethodNodeDoer());
		addDoerToMap(ActionCreateChildTask.class, new CreateChildTaskDoer());
		addDoerToMap(ActionCreateSameLevelTask.class, new CreateSameLevelTaskDoer());

		addDoerToMap(ActionTreeShareActivity.class, new TreeNodeShareActivityDoer());
		addDoerToMap(ActionTreeShareMethod.class, new TreeNodeShareMethodDoer());

		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResourceDoer());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());
		
		addDoerToMap(ActionCreateCategoryOne.class, new CreateCategoryOneDoer());
		addDoerToMap(ActionDeleteCategoryOne.class, new DeleteCategoryOneDoer());		
		
		addDoerToMap(ActionCreateCategoryTwo.class, new CreateCategoryTwoDoer());
		addDoerToMap(ActionDeleteCategoryTwo.class, new DeleteCategoryTwoDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());
		
		addDoerToMap(ActionEditAnalysisRows.class, new EditAnalysisRowsDoer());
		
		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningTreeNodeCreationMenuDoer());
		addDoerToMap(ActionWorkPlanBudgetCustomizeTableEditor.class, new WorkPlanCustomizeTableEditorDoer());
		addDoerToMap(ActionFilterWorkPlanByProjectResource.class, new ProjectResourceWorkPlanFilterEditDoer());
	}
	
	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}
	
	private PlanningTreeManagementPanel workPlanManagementPanel;
	private PlanningTreeManagementPanel rollupReportsManagementPanel;
	private PlanningTreeManagementPanel resourceManagementPanel;
	private PlanningTreeManagementPanel accountingCodePoolManagementPanel;
	private PlanningTreeManagementPanel fundingSourcePoolManagementPanel;
	private PlanningTreeManagementPanel categoryOnePoolMangementPanel;
	private PlanningTreeManagementPanel categoryTwoPoolMangementPanel;
}
