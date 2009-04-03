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

import org.miradi.actions.ActionAssignResource;
import org.miradi.actions.ActionCreateAccountingCode;
import org.miradi.actions.ActionCreateFundingSource;
import org.miradi.actions.ActionCreatePlanningViewConfiguration;
import org.miradi.actions.ActionCreateResource;
import org.miradi.actions.ActionDeleteAccountingCode;
import org.miradi.actions.ActionDeleteFundingSource;
import org.miradi.actions.ActionDeletePlanningViewConfiguration;
import org.miradi.actions.ActionDeletePlanningViewTreeNode;
import org.miradi.actions.ActionDeleteResource;
import org.miradi.actions.ActionImportAccountingCodes;
import org.miradi.actions.ActionPlanningColumnsEditor;
import org.miradi.actions.ActionPlanningCreationMenu;
import org.miradi.actions.ActionPlanningRowsEditor;
import org.miradi.actions.ActionRemoveAssignment;
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
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.accountingcode.AccountingCodePoolManagementPanel;
import org.miradi.dialogs.base.ObjectManagementPanel;
import org.miradi.dialogs.fundingsource.FundingSourcePoolManagementPanel;
import org.miradi.dialogs.planning.EmptyRowColumnProvider;
import org.miradi.dialogs.planning.PlanningTreeManagementPanel;
import org.miradi.dialogs.planning.RowColumnProvider;
import org.miradi.dialogs.resource.ResourcePoolManagementPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.PlanningViewConfiguration;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;
import org.miradi.views.planning.doers.AddAssignmentDoer;
import org.miradi.views.planning.doers.CreateAccountingCodeDoer;
import org.miradi.views.planning.doers.CreateFundingSourceDoer;
import org.miradi.views.planning.doers.CreatePlanningViewConfigurationDoer;
import org.miradi.views.planning.doers.DeleteAccountingCodeDoer;
import org.miradi.views.planning.doers.DeleteFundingSourceDoer;
import org.miradi.views.planning.doers.DeletePlanningViewConfigurationDoer;
import org.miradi.views.planning.doers.ImportAccountingCodesDoer;
import org.miradi.views.planning.doers.PlanningColumnsEditorDoer;
import org.miradi.views.planning.doers.PlanningCreationMenuDoer;
import org.miradi.views.planning.doers.PlanningRowsEditorDoer;
import org.miradi.views.planning.doers.RemoveAssignmentDoer;
import org.miradi.views.planning.doers.RenamePlanningViewConfigurationDoer;
import org.miradi.views.planning.doers.TreeNodeCreateActivityDoer;
import org.miradi.views.planning.doers.TreeNodeCreateIndicatorDoer;
import org.miradi.views.planning.doers.TreeNodeCreateMethodDoer;
import org.miradi.views.planning.doers.TreeNodeCreateObjectiveDoer;
import org.miradi.views.planning.doers.TreeNodeCreateTaskDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;
import org.miradi.views.planning.doers.TreeNodeDownDoer;
import org.miradi.views.planning.doers.TreeNodeShareActivityDoer;
import org.miradi.views.planning.doers.TreeNodeShareMethodDoer;
import org.miradi.views.planning.doers.TreeNodeUpDoer;
import org.miradi.views.umbrella.CreateResource;
import org.miradi.views.umbrella.DeleteResource;
import org.miradi.views.umbrella.UmbrellaView;

public class PlanningView extends TabbedView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}
	
	public void createTabs() throws Exception
	{
		planningManagementPanel = ConfigurablePlanningTreeManagementPanel.createConfigurablePlanningPanel(getMainWindow());
		strategicPlanManagementPanel = ActionPlanManagementPanel.createStrategicPlanPanel(getMainWindow());
		monitoringPlanManagementPanel = MonitoringPlanManagementPanel.createMonitoringPlanPanel(getMainWindow());
		objectsOnlyManagementPanel = ObjectsOnlyManagementPanel.createObjectsOnlyPanel(getMainWindow());
		
		resourceManagementPanel = new ResourcePoolManagementPanel(getMainWindow(), "");
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getMainWindow(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getMainWindow(), "");
		
		addNonScrollingTab(strategicPlanManagementPanel);
		addNonScrollingTab(monitoringPlanManagementPanel);
		addNonScrollingTab(objectsOnlyManagementPanel);
		addNonScrollingTab(planningManagementPanel);
		addNonScrollingTab(resourceManagementPanel);
		addNonScrollingTab(accountingCodePoolManagementPanel);
		addNonScrollingTab(fundingSourcePoolManagementPanel);
		
		loadManagementPanelMap();
	}

	private void loadManagementPanelMap()
	{
		managementPanelMap = new HashMap();
		
		managementPanelMap.put(planningManagementPanel.getPanelDescription(), planningManagementPanel);
		managementPanelMap.put(strategicPlanManagementPanel.getPanelDescription(), strategicPlanManagementPanel);
		managementPanelMap.put(monitoringPlanManagementPanel.getPanelDescription(), monitoringPlanManagementPanel);
		managementPanelMap.put(objectsOnlyManagementPanel.getPanelDescription(), objectsOnlyManagementPanel);
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
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		planningManagementPanel.updateSplitterLocation();
		strategicPlanManagementPanel.updateSplitterLocation();
		monitoringPlanManagementPanel.updateSplitterLocation();
		objectsOnlyManagementPanel.updateSplitterLocation();
		resourceManagementPanel.updateSplitterLocation();
		accountingCodePoolManagementPanel.updateSplitterLocation();
		fundingSourcePoolManagementPanel.updateSplitterLocation();
	}

	
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
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
	}

	public String cardName()
	{
		return getViewName();
	}

	static public String getViewName()
	{
		return Project.PLANNING_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new PlanningToolBar(getActions());
	}
	
	private void addPlanningViewDoersToMap()
	{
		addDoerToMap(ActionCreatePlanningViewConfiguration.class, new CreatePlanningViewConfigurationDoer());
		addDoerToMap(ActionDeletePlanningViewConfiguration.class, new DeletePlanningViewConfigurationDoer());
		addDoerToMap(ActionRenamePlanningViewConfiguration.class, new RenamePlanningViewConfigurationDoer());
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
		addDoerToMap(ActionAssignResource.class, new AddAssignmentDoer());
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUpDoer());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDownDoer());
		addDoerToMap(ActionDeletePlanningViewTreeNode.class, new TreeNodeDeleteDoer());
		addDoerToMap(ActionTreeCreateActivityIconOnly.class, new TreeNodeCreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethodIconOnly.class, new TreeNodeCreateMethodDoer());
		addDoerToMap(ActionTreeCreateTaskIconOnly.class, new TreeNodeCreateTaskDoer());
		addDoerToMap(ActionTreeCreateActivity.class, new TreeNodeCreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new TreeNodeCreateMethodDoer());	
		addDoerToMap(ActionTreeCreateTask.class, new TreeNodeCreateTaskDoer());
		addDoerToMap(ActionTreeShareActivity.class, new TreeNodeShareActivityDoer());
		addDoerToMap(ActionTreeShareMethod.class, new TreeNodeShareMethodDoer());
		addDoerToMap(ActionTreeCreateIndicator.class, new TreeNodeCreateIndicatorDoer());
		addDoerToMap(ActionTreeCreateObjective.class, new TreeNodeCreateObjectiveDoer());
				
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());	
		
		addDoerToMap(ActionPlanningCreationMenu.class, new PlanningCreationMenuDoer());
		addDoerToMap(ActionPlanningRowsEditor.class, new PlanningRowsEditorDoer());
		addDoerToMap(ActionPlanningColumnsEditor.class, new PlanningColumnsEditorDoer());
	}
	
	public static boolean isRowOrColumnChangingCommand(CommandExecutedEvent event)
	{
		if (! event.isSetDataCommand())
			return false;
	
		CommandSetObjectData setCommand = (CommandSetObjectData) event.getCommand();
		if(setCommand.getObjectType() == ViewData.getObjectType())
		{
			if(setCommand.getFieldTag().equals(ViewData.TAG_PLANNING_STYLE_CHOICE))
				return true;
			if(setCommand.getFieldTag().equals(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE))
				return true;
			if(setCommand.getFieldTag().equals(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
				return true;
		}
		
		if(setCommand.getObjectType() == PlanningViewConfiguration.getObjectType())
		{
			if(setCommand.getFieldTag().equals(PlanningViewConfiguration.TAG_COL_CONFIGURATION))
				return true;
			if(setCommand.getFieldTag().equals(PlanningViewConfiguration.TAG_ROW_CONFIGURATION))
				return true;
		}
		
		return false;
	}
	
	public HashMap<String, PlanningTreeManagementPanel> getManagementPanelMap()
	{
		return managementPanelMap;
	}

	public static boolean is(UmbrellaView view)
	{
		return view.cardName().equals(getViewName());
	}

	public static final String STRATEGIC_PLAN_RADIO_CHOICE = "StrategicPlanRadioChoice";
	public static final String MONITORING_PLAN_RADIO_CHOICE = "MonitoringPlanRadioChoice";
	public static final String WORKPLAN_PLAN_RADIO_CHOICE = "WorkPlanPlanRadioChoice";
	public static final String SINGLE_LEVEL_RADIO_CHOICE = "SingleLevelRadioChoice";
	public static final String CUSTOMIZABLE_RADIO_CHOICE = "CustomizableRadioChoice";
	
	public static final String SINGLE_LEVEL_COMBO = "SingleLevelCombo";
	public static final String CUSTOMIZABLE_COMBO = "CostomizableCombo";
	
	private PlanningTreeManagementPanel objectsOnlyManagementPanel;
	private PlanningTreeManagementPanel planningManagementPanel;
	private PlanningTreeManagementPanel strategicPlanManagementPanel;
	private PlanningTreeManagementPanel monitoringPlanManagementPanel;
	private ResourcePoolManagementPanel resourceManagementPanel;
	private AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	private FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
	
	private HashMap<String, PlanningTreeManagementPanel> managementPanelMap;
}
