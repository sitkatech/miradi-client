package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardPanel;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTablePanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.workplan.CreateActivityDoer;
import org.conservationmeasures.eam.views.workplan.CreateMethodDoer;
import org.conservationmeasures.eam.views.workplan.CreateTaskDoer;
import org.conservationmeasures.eam.views.workplan.DeleteWorkPlanTreeNode;
import org.conservationmeasures.eam.views.workplan.TreeNodeDown;
import org.conservationmeasures.eam.views.workplan.TreeNodeUp;
import org.martus.swing.HtmlViewer;
import org.martus.swing.UiScrollPane;

public class BudgetView extends TabbedView
{

	public BudgetView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		addBudgetDoersToMap();
		setToolBar(new BudgetToolBar(mainWindowToUse.getActions()));
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
	}

	public void becomeInactive() throws Exception
	{
		super.becomeInactive();
	}

	public void createTabs() throws Exception
	{
		treeTableComponent = BudgetTreeTablePanel.createBudgetTreeTablePanel(getMainWindow(), getProject());
		
		budgetPropertiesPanel = new BudgetPropertiesPanel(getProject(), getMainWindow().getActions(), treeTableComponent);
		budgetManagmentPanel = new BudgetManagementPanel(getMainWindow(), getProject(), budgetPropertiesPanel, treeTableComponent);
		accountingCodePoolManagementPanel = new AccountingCodePoolManagementPanel(getProject(), getMainWindow().getActions(), "");
		fundingSourcePoolManagementPanel = new FundingSourcePoolManagementPanel(getProject(), getMainWindow().getActions(), "");
		resourceManagementPanel = new ResourcePoolManagementPanel(getProject(), getMainWindow().getActions(), "");
		
		addScrollableTab(budgetManagmentPanel);
		addTab(accountingCodePoolManagementPanel.getPanelDescription(),accountingCodePoolManagementPanel.getIcon(), accountingCodePoolManagementPanel);
		addTab(fundingSourcePoolManagementPanel.getPanelDescription(), fundingSourcePoolManagementPanel.getIcon(), fundingSourcePoolManagementPanel);
		addTab(EAM.text("Actuals - demo"), new UiScrollPane(getBudgetComponent()));
		addScrollableTab(resourceManagementPanel);
	}
	
	public void deleteTabs() throws Exception
	{
		fundingSourcePoolManagementPanel.dispose();
		fundingSourcePoolManagementPanel = null;
		
		accountingCodePoolManagementPanel.dispose();
		accountingCodePoolManagementPanel = null;
		
		budgetPropertiesPanel.dispose();
		budgetPropertiesPanel = null;
		
		budgetManagmentPanel.dispose();
		budgetManagmentPanel = null;
		
		resourceManagementPanel.dispose();
		resourceManagementPanel = null;
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
		
		addDoerToMap(ActionCreateFundingSource.class, new CreateFundingSourceDoer());
		addDoerToMap(ActionDeleteFundingSource.class, new DeleteFundingSourceDoer());
	}
	
	public WizardPanel createWizardPanel() throws Exception
	{
		return new BudgetWizardPanel(getMainWindow());
	}

	public TaskTreeTablePanel getTaskTreeTablePanel()
	{
		return treeTableComponent;
	}

	private HtmlViewer getBudgetComponent()
	{
		HtmlViewer htmlViewer= new HtmlViewer("",null);
		htmlViewer.setText("<p>Coming soon - ability to enter actual expenditures and to match " +
				"these up to budget line items and report on them by programmatic objectives and " +
				"activities, by accounting codes, or by funding sources.</p>");
		return htmlViewer;
	}
	
	BudgetTreeTablePanel treeTableComponent;
	BudgetPropertiesPanel budgetPropertiesPanel;
	BudgetManagementPanel budgetManagmentPanel;
	AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
	ResourcePoolManagementPanel resourceManagementPanel;
}

