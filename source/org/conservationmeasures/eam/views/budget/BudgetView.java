/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.awt.Component;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionExportBudgetTableTree;
import org.conservationmeasures.eam.actions.ActionImportAccountingCodes;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.dialogs.AccountingCodePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.BudgetManagementPanel;
import org.conservationmeasures.eam.dialogs.BudgetPropertiesPanel;
import org.conservationmeasures.eam.dialogs.FundingSourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ResourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.HtmlFormViewer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;
import org.conservationmeasures.eam.views.umbrella.CreateResource;
import org.conservationmeasures.eam.views.umbrella.DeleteResource;
import org.conservationmeasures.eam.views.workplan.CreateActivityDoer;
import org.conservationmeasures.eam.views.workplan.CreateMethodDoer;
import org.conservationmeasures.eam.views.workplan.CreateTaskDoer;
import org.conservationmeasures.eam.views.workplan.DeleteWorkPlanTreeNode;
import org.conservationmeasures.eam.views.workplan.TreeNodeDown;
import org.conservationmeasures.eam.views.workplan.TreeNodeUp;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.martus.swing.UiScrollPane;

public class BudgetView extends TabbedView
{

	public BudgetView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addBudgetDoersToMap();
		wizardPanel = new WizardPanel(mainWindowToUse, this);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.BUDGET_VIEW_NAME;
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();

		budgetManagmentPanel.updateSplitterLocation();
		accountingCodePoolManagementPanel.updateSplitterLocation();
		fundingSourcePoolManagementPanel.updateSplitterLocation();
		resourceManagementPanel.updateSplitterLocation();
		
		getMainWindow().setStatusBarIfDataExistsOutOfRange();
	}

	public JToolBar createToolBar()
	{
		return new BudgetToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		treeTableComponent = BudgetTreeTablePanel.createBudgetTreeTablePanel(getMainWindow(), getProject());
		
		budgetPropertiesPanel = new BudgetPropertiesPanel(getProject(), getMainWindow().getActions(), treeTableComponent.getTree());
		budgetManagmentPanel = new BudgetManagementPanel(getMainWindow(), getProject(), budgetPropertiesPanel, treeTableComponent);
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		resourceManagementPanel = new ResourcePoolManagementPanel(getProject(), getMainWindow(), getMainWindow().getActions(), "");
		
		addNonScrollableTab(budgetManagmentPanel);
		addTab(accountingCodePoolManagementPanel.getPanelDescription(),accountingCodePoolManagementPanel.getIcon(), accountingCodePoolManagementPanel);
		addTab(fundingSourcePoolManagementPanel.getPanelDescription(), fundingSourcePoolManagementPanel.getIcon(), fundingSourcePoolManagementPanel);
		addNonScrollableTab(resourceManagementPanel);
		addTab(EAM.text("Actuals - demo"), new UiScrollPane(getBudgetComponent()));
	}
	
	public void deleteTabs() throws Exception
	{
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		treeTableComponent.dispose();
		treeTableComponent = null;
		
		budgetPropertiesPanel.dispose();
		budgetPropertiesPanel = null;
		
		budgetManagmentPanel.dispose();
		budgetManagmentPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
	}
	
	public void jump(Class stepMarker) throws Exception
	{
		wizardPanel.jump(stepMarker);
	}
	
	private void addBudgetDoersToMap()
	{
		addDoerToMap(ActionTreeCreateActivity.class, new CreateActivityDoer());
		addDoerToMap(ActionTreeCreateMethod.class, new CreateMethodDoer());
		addDoerToMap(ActionTreeCreateTask.class, new CreateTaskDoer());
		addDoerToMap(ActionDeleteWorkPlanNode.class, new DeleteWorkPlanTreeNode());
		addDoerToMap(ActionTreeNodeUp.class, new TreeNodeUp());
		addDoerToMap(ActionTreeNodeDown.class, new TreeNodeDown());

		addDoerToMap(ActionAddAssignment.class, new AddAssignmentDoer());
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
		
		addDoerToMap(ActionCreateAccountingCode.class, new CreateAccountingCodeDoer());
		addDoerToMap(ActionDeleteAccountingCode.class, new DeleteAccountingCodeDoer());
		addDoerToMap(ActionImportAccountingCodes.class, new ImportAccountingCodesDoer());
		addDoerToMap(ActionExportBudgetTableTree.class, new ExportBudgetTreeTableDoer());
		
		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());
		
		addDoerToMap(ActionCreateResource.class, new CreateResource());
		addDoerToMap(ActionDeleteResource.class, new DeleteResource());
	}
	
	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}

	public TaskTreeTablePanel getTaskTreeTablePanel()
	{
		return treeTableComponent;
	}

	private Component getBudgetComponent()
	{
		HtmlFormViewer htmlViewer= new WizardHtmlViewer(getMainWindow(),null);
		htmlViewer.setText(
				"<h2>Coming soon</h2>" +
				"<STRONG>Tracking of Actual Expenses</STRONG> -&nbsp;" +
				"Tools to enter actual expenditures and to match these up to budget line items " +
				"and report on them by programmatic objectives and activities, " +
				"by accounting codes, or by funding sources, " +
				"using a Quicken or Quickbooks style interface.");
		return htmlViewer;
	}
	
	BudgetTreeTablePanel treeTableComponent;
	BudgetPropertiesPanel budgetPropertiesPanel;
	BudgetManagementPanel budgetManagmentPanel;
	AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
	ResourcePoolManagementPanel resourceManagementPanel;
}

