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

import javax.swing.Icon;
import javax.swing.JToolBar;

import org.miradi.actions.ActionCollapseAllNodes;
import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionExpandAllNodes;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.dialogs.accountingcode.AccountingCodePoolManagementPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePoolManagementPanel;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.propertiesPanel.PlanningTreeMultiPropertiesPanel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTable;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTableModel;
import org.miradi.dialogs.planning.upperPanel.PlanningTreeTablePanel;
import org.miradi.dialogs.planning.upperPanel.WorkPlanTreeTableModel;
import org.miradi.dialogs.resource.ResourcePoolManagementPanel;
import org.miradi.icons.IconManager;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.CreateAccountingCodeDoer;
import org.miradi.views.planning.doers.CreateFundingSourceDoer;
import org.miradi.views.planning.doers.DeleteAccountingCodeDoer;
import org.miradi.views.planning.doers.DeleteFundingSourceDoer;
import org.miradi.views.planning.doers.ImportAccountingCodesDoer;
import org.miradi.views.planning.doers.PlanningCreationMenuDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResource;
import org.miradi.views.umbrella.UmbrellaView;

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
		workPlanManagementPanel = createWorkPlanPanel();
		resourceManagementPanel = new ResourcePoolManagementPanel(getMainWindow(), "");
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getMainWindow(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getMainWindow(), "");
		
		addNonScrollingTab(workPlanManagementPanel);
		addNonScrollingTab(resourceManagementPanel);
		addNonScrollingTab(accountingCodePoolManagementPanel);
		addNonScrollingTab(fundingSourcePoolManagementPanel);
	}
	
	private WorkPlanManagementPanel createWorkPlanPanel() throws Exception
	{
		PlanningTreeTableModel workPlanTreeTableModel = new WorkPlanTreeTableModel(getProject());
		
		Class[] buttonActions = new Class[] {
				ActionExpandAllNodes.class, 
				ActionCollapseAllNodes.class, 
				ActionPlanningCreationMenu.class,
				};
		
		PlanningTreeTablePanel workPlanTreeTablePanel = PlanningTreeTablePanel.createPlanningTreeTablePanel(getMainWindow(), workPlanTreeTableModel, buttonActions);
		PlanningTreeTable treeAsObjectPicker = (PlanningTreeTable)workPlanTreeTablePanel.getTree();
		PlanningTreeMultiPropertiesPanel workPlanPropertiesPanel = new PlanningTreeMultiPropertiesPanel(getMainWindow(), ORef.INVALID, treeAsObjectPicker);
		return new WorkPlanManagementPanel(getMainWindow(), workPlanTreeTablePanel, workPlanPropertiesPanel);
	}

	@Override
	public void becomeActive() throws Exception
	{
		super.becomeActive();
	
		workPlanManagementPanel.updateSplitterLocation();
		resourceManagementPanel.updateSplitterLocation();
		accountingCodePoolManagementPanel.updateSplitterLocation();
		fundingSourcePoolManagementPanel.updateSplitterLocation();
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
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());	
		
		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningCreationMenuDoer());
	}
	
	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}
	
	class WorkPlanManagementPanel extends PlanningTreeManagementPanel
	{
		public WorkPlanManagementPanel(MainWindow mainWindowToUse, PlanningTreeTablePanel planningTreeTablePanel, PlanningTreeMultiPropertiesPanel planningTreePropertiesPanel)	throws Exception
		{
			super(mainWindowToUse, planningTreeTablePanel, planningTreePropertiesPanel);
		}
		
		@Override
		public String getPanelDescription()
		{
			return panelDescription;
		}

		@Override
		public Icon getIcon()
		{
			return IconManager.getActivityIcon();
		}

		private final String panelDescription = EAM.text("Tab|Work Plan");
	}

	private PlanningTreeManagementPanel workPlanManagementPanel;
	private ResourcePoolManagementPanel resourceManagementPanel;
	private AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	private FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
}
