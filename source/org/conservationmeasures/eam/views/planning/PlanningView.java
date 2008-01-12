/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionAssignResource;
import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionCreatePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionImportAccountingCodes;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.ActionRenamePlanningViewConfiguration;
import org.conservationmeasures.eam.actions.ActionShareMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivityIconOnly;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethodIconOnly;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeCreateTaskIconOnly;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.accountingcode.AccountingCodePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.fundingsource.FundingSourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeManagementPanel;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTable;
import org.conservationmeasures.eam.dialogs.planning.PlanningTreeTablePanel;
import org.conservationmeasures.eam.dialogs.planning.legend.PlanningViewControlPanel;
import org.conservationmeasures.eam.dialogs.planning.propertiesPanel.PlanningTreePropertiesPanel;
import org.conservationmeasures.eam.dialogs.resource.ResourcePoolManagementPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.planning.doers.AddAssignmentDoer;
import org.conservationmeasures.eam.views.planning.doers.CreateAccountingCodeDoer;
import org.conservationmeasures.eam.views.planning.doers.CreateFundingSourceDoer;
import org.conservationmeasures.eam.views.planning.doers.CreatePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.DeleteAccountingCodeDoer;
import org.conservationmeasures.eam.views.planning.doers.DeleteFundingSourceDoer;
import org.conservationmeasures.eam.views.planning.doers.DeletePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.ImportAccountingCodesDoer;
import org.conservationmeasures.eam.views.planning.doers.RemoveAssignmentDoer;
import org.conservationmeasures.eam.views.planning.doers.RenamePlanningViewConfigurationDoer;
import org.conservationmeasures.eam.views.planning.doers.ShareMethodDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeCreateActivityDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeCreateMethodDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeCreateTaskDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeDeleteDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeDownDoer;
import org.conservationmeasures.eam.views.planning.doers.TreeNodeUpDoer;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;

public class PlanningView extends TabbedView
{
	public PlanningView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addPlanningViewDoersToMap();
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		planningManagementPanel.updateSplitterLocation();
		resourceManagementPanel.updateSplitterLocation();
		accountingCodePoolManagementPanel.updateSplitterLocation();
		fundingSourcePoolManagementPanel.updateSplitterLocation();
	}

	public void createTabs() throws Exception
	{
		PlanningTreeTablePanel planningTreeTablePanel = PlanningTreeTablePanel.createPlanningTreeTablePanel(getMainWindow());
		PlanningTreeTable treeAsObjectPicker = (PlanningTreeTable) planningTreeTablePanel.getTree();
		PlanningTreePropertiesPanel planningTreePropertiesPanel = new PlanningTreePropertiesPanel(getMainWindow(), ORef.INVALID, treeAsObjectPicker);
		planningManagementPanel = new PlanningTreeManagementPanel(getMainWindow(), planningTreeTablePanel, planningTreePropertiesPanel);
		resourceManagementPanel = new ResourcePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		
		controlPanel = new PlanningViewControlPanel(getMainWindow(), treeAsObjectPicker);
		FastScrollPane controlBarScrollPane = new FastScrollPane(controlPanel);
		controlBarScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		JPanel horizontalSplitPane = new JPanel(new BorderLayout());
		horizontalSplitPane.add(controlBarScrollPane, BorderLayout.BEFORE_LINE_BEGINS);
		horizontalSplitPane.add(planningManagementPanel, BorderLayout.CENTER);
		
		addTab(EAM.text("Planning"), horizontalSplitPane);
		addNonScrollableTab(resourceManagementPanel);
		addTab(accountingCodePoolManagementPanel.getPanelDescription(),accountingCodePoolManagementPanel.getIcon(), accountingCodePoolManagementPanel);
		addTab(fundingSourcePoolManagementPanel.getPanelDescription(), fundingSourcePoolManagementPanel.getIcon(), fundingSourcePoolManagementPanel);
	}

	public void deleteTabs() throws Exception
	{
		planningManagementPanel.dispose();
		planningManagementPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
		
		controlPanel.dispose();
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
		addDoerToMap(ActionShareMethod.class, new ShareMethodDoer());	
		addDoerToMap(ActionTreeCreateTask.class, new TreeNodeCreateTaskDoer());
				
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());

		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());

		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());		
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

	public static boolean isCustomizationStyle(ViewData viewData)
	{
		ORef planningViewConfigurationRef = ORef.createFromString(viewData.getData(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF));
		if (planningViewConfigurationRef.isInvalid())
			return false;
		
		return viewData.getData(ViewData.TAG_PLANNING_STYLE_CHOICE).equals(PlanningView.CUSTOMIZABLE_RADIO_CHOICE);
	}
	
	public static final String STRATEGIC_PLAN_RADIO_CHOICE = "StrategicPlanRadioChoice";
	public static final String MONITORING_PLAN_RADIO_CHOICE = "MonitoringPlanRadioChoice";
	public static final String WORKPLAN_PLAN_RADIO_CHOICE = "WorkPlanPlanRadioChoice";
	public static final String SINGLE_LEVEL_RADIO_CHOICE = "SingleLevelRadioChoice";
	public static final String CUSTOMIZABLE_RADIO_CHOICE = "CustomizableRadioChoice";
	
	public static final String SINGLE_LEVEL_COMBO = "SingleLevelCombo";
	public static final String CUSTOMIZABLE_COMBO = "CostomizableCombo";
	
	private PlanningViewControlPanel controlPanel;
	private PlanningTreeManagementPanel planningManagementPanel;
	private ResourcePoolManagementPanel resourceManagementPanel;
	private AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	private FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
}
