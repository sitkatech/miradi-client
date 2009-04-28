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
package org.miradi.views.OperationalPlan;

import javax.swing.JToolBar;

import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionRemoveAssignment;
import org.miradi.actions.ActionTreeCreateActivity;
import org.miradi.actions.ActionTreeCreateActivityIconOnly;
import org.miradi.actions.ActionTreeCreateMethod;
import org.miradi.actions.ActionTreeCreateMethodIconOnly;
import org.miradi.actions.ActionTreeCreateTask;
import org.miradi.actions.ActionTreeCreateTaskIconOnly;
import org.miradi.actions.ActionTreeNodeDown;
import org.miradi.actions.ActionTreeNodeUp;
import org.miradi.actions.ActionTreeShareActivity;
import org.miradi.actions.ActionTreeShareMethod;
import org.miradi.dialogs.accountingcode.AccountingCodePoolManagementPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePoolManagementPanel;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.resource.ResourcePoolManagementPanel;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.CreateAccountingCodeDoer;
import org.miradi.views.planning.doers.CreateFundingSourceDoer;
import org.miradi.views.planning.doers.DeleteAccountingCodeDoer;
import org.miradi.views.planning.doers.DeleteFundingSourceDoer;
import org.miradi.views.planning.doers.ImportAccountingCodesDoer;
import org.miradi.views.planning.doers.PlanningTreeNodeCreationMenuDoer;
import org.miradi.views.planning.doers.TreeNodeCreateActivityDoer;
import org.miradi.views.planning.doers.TreeNodeCreateMethodDoer;
import org.miradi.views.planning.doers.TreeNodeCreateTaskDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResource;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.AddAssignmentDoer;
import org.miradi.views.umbrella.doers.RemoveAssignmentDoer;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareActivityDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareMethodDoer;

public class OperationalPlanView extends TabbedView
{
	public OperationalPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}
	
	@Override
	public void createTabs() throws Exception
	{
		workPlanManagementPanel = WorkPlanManagementPanel.createWorkPlanPanel(getMainWindow());
		resourceManagementPanel = new ResourcePoolManagementPanel(getMainWindow(), "");
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getMainWindow(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getMainWindow(), "");
		
		addNonScrollingTab(workPlanManagementPanel);
		addNonScrollingTab(resourceManagementPanel);
		addNonScrollingTab(accountingCodePoolManagementPanel);
		addNonScrollingTab(fundingSourcePoolManagementPanel);
	}
	
	@Override
	public void becomeActive() throws Exception
	{
		super.becomeActive();
	
		workPlanManagementPanel.becomeActive();
		resourceManagementPanel.becomeActive();
		accountingCodePoolManagementPanel.becomeActive();
		fundingSourcePoolManagementPanel.becomeActive();
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
		
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
	}

	@Override
	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.OPERATIONAL_PLAN_VIEW;
	}

	@Override
	public JToolBar createToolBar()
	{
		return new OperationalPlanToolBar(getActions());
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

		addDoerToMap(ActionAssignResource.class, new AddAssignmentDoer());
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());	
		
		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningTreeNodeCreationMenuDoer());
	}
	
	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}
	
	private PlanningTreeManagementPanel workPlanManagementPanel;
	private ResourcePoolManagementPanel resourceManagementPanel;
	private AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	private FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
}
