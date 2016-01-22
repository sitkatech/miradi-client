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
package org.miradi.views.workplan;


import org.miradi.actions.*;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.planning.EmptyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.main.*;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.schemas.ProjectMetadataSchema;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.*;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResourceDoer;
import org.miradi.views.umbrella.UmbrellaView;
import org.miradi.views.umbrella.doers.TaskMoveDownDoer;
import org.miradi.views.umbrella.doers.TaskMoveUpDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareActivityDoer;
import org.miradi.views.umbrella.doers.TreeNodeShareMethodDoer;
import org.miradi.views.workplan.doers.*;

import java.util.HashMap;

public class WorkPlanView extends TabbedView implements CommandExecutedListener
{
	public WorkPlanView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}

	@Override
	public void createTabs() throws Exception
	{
		ProjectMetadata projectMetadata = getProject().getMetadata();

		if (projectMetadata.shouldDisplaySharedWorkPlan())
			sharedWorkPlanManagementPanel = SharedWorkPlanManagementPanel.createWorkPlanPanel(getMainWindow());

		if (projectMetadata.shouldDisplayLegacyWorkPlan())
			workPlanManagementPanel = WorkPlanManagementPanel.createWorkPlanPanel(getMainWindow());

		settingsPanel = new WorkPlanSettingsPanel(getMainWindow(), getProject().getMetadata().getRef());
		rollupReportsManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new AnalysisManagementConfiguration(getProject()));
		resourceManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new ProjectResourceManagementConfiguration(getProject()));
		accountingCodePoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new AccountingCodeManagementConfiguration(getProject()));
		fundingSourcePoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new FundingSourceManagementConfiguration(getProject())); 
		categoryOnePoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new BudgetCategoryOneManagementConfiguration(getProject()));
		categoryTwoPoolManagementPanel = WorkPlanBudgetCategoryManagementPanel.createManagementPanel(getMainWindow(), new BudgetCategoryTwoManagementConfiguration(getProject()));

		managementPanelMap = new HashMap<String, PlanningTreeManagementPanel>();

		if (projectMetadata.shouldDisplaySharedWorkPlan())
			addPlanningManagementTab(sharedWorkPlanManagementPanel);

		if (projectMetadata.shouldDisplayLegacyWorkPlan())
			addPlanningManagementTab(workPlanManagementPanel);

		addNonScrollingTab(settingsPanel);

		addPlanningManagementTab(rollupReportsManagementPanel);
		addPlanningManagementTab(resourceManagementPanel);
		addPlanningManagementTab(accountingCodePoolManagementPanel);
		addPlanningManagementTab(fundingSourcePoolManagementPanel);
		addPlanningManagementTab(categoryOnePoolManagementPanel);
		addPlanningManagementTab(categoryTwoPoolManagementPanel);
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
	public void commandExecuted(CommandExecutedEvent event)
	{
		super.commandExecuted(event);

		try
		{
			if(event.isSetDataCommandWithThisTypeAndTag(ProjectMetadataSchema.getObjectType(), ProjectMetadata.TAG_WORKPLAN_DISPLAY_MODE))
			{
				ProjectMetadata projectMetadata = getProject().getMetadata();
				MiradiTabContentsPanelInterface currentTab = getCurrentTabPanel();
				String newTabName = currentTab.getClass().getSimpleName();

				if(currentTab instanceof SharedWorkPlanManagementPanel && !projectMetadata.shouldDisplaySharedWorkPlan())
					newTabName = WorkPlanManagementPanel.class.getSimpleName();

				if(currentTab instanceof WorkPlanManagementPanel && !projectMetadata.shouldDisplayLegacyWorkPlan())
					newTabName = SharedWorkPlanManagementPanel.class.getSimpleName();

				this.refresh();

				int settingsTabIndex = getTabIndex(newTabName);
				getProject().executeAsSideEffect(getViewData().createTabChangeCommand(settingsTabIndex));
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	@Override
	public void deleteTabs() throws Exception
	{
		if (sharedWorkPlanManagementPanel != null)
		{
			sharedWorkPlanManagementPanel.dispose();
			sharedWorkPlanManagementPanel = null;
		}

		if (workPlanManagementPanel != null)
		{
			workPlanManagementPanel.dispose();
			workPlanManagementPanel = null;
		}

		settingsPanel.dispose();
		settingsPanel = null;

		rollupReportsManagementPanel.dispose();
		rollupReportsManagementPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
		
		categoryOnePoolManagementPanel.dispose();
		categoryOnePoolManagementPanel = null;
		
		categoryTwoPoolManagementPanel.dispose();
		categoryTwoPoolManagementPanel = null;
		
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
		addDoerToMap(ActionSharedWorkPlanningCreationMenu.class, new SharedWorkPlanningTreeNodeCreationMenuDoer());
		addDoerToMap(ActionWorkPlanBudgetCustomizeTableEditor.class, new WorkPlanCustomizeTableEditorDoer());
		addDoerToMap(ActionSharedWorkPlanBudgetCustomizeTableEditor.class, new SharedWorkPlanCustomizeTableEditorDoer());
		addDoerToMap(ActionFilterWorkPlanByProjectResource.class, new ProjectResourceWorkPlanFilterEditDoer());
	}
	
	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}

	private HashMap<String, PlanningTreeManagementPanel> getManagementPanelMap()
	{
		return managementPanelMap;
	}

	private PlanningTreeManagementPanel sharedWorkPlanManagementPanel;
	private PlanningTreeManagementPanel workPlanManagementPanel;
	private WorkPlanSettingsPanel settingsPanel;
	private PlanningTreeManagementPanel rollupReportsManagementPanel;
	private PlanningTreeManagementPanel resourceManagementPanel;
	private PlanningTreeManagementPanel accountingCodePoolManagementPanel;
	private PlanningTreeManagementPanel fundingSourcePoolManagementPanel;
	private PlanningTreeManagementPanel categoryOnePoolManagementPanel;
	private PlanningTreeManagementPanel categoryTwoPoolManagementPanel;

	private HashMap<String, PlanningTreeManagementPanel> managementPanelMap;
}
