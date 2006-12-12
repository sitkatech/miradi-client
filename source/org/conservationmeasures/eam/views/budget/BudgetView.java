package org.conservationmeasures.eam.views.budget;

import javax.swing.JLabel;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionCreateAccountingCode;
import org.conservationmeasures.eam.actions.ActionCreateFundingSource;
import org.conservationmeasures.eam.actions.ActionDeleteAccountingCode;
import org.conservationmeasures.eam.actions.ActionDeleteFundingSource;
import org.conservationmeasures.eam.actions.ActionImportAccountingCodes;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.dialogs.AccountingCodePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.BudgetManagementPanel;
import org.conservationmeasures.eam.dialogs.BudgetPropertiesPanel;
import org.conservationmeasures.eam.dialogs.FundingSourcePoolManagementPanel;
import org.conservationmeasures.eam.dialogs.ModelessDialogPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.ResourceImageIcon;
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
		
		addScrollableTab(budgetManagmentPanel);
		addTab(accountingCodePoolManagementPanel.getPanelDescription(),accountingCodePoolManagementPanel.getIcon(), accountingCodePoolManagementPanel);
		addTab(fundingSourcePoolManagementPanel.getPanelDescription(), fundingSourcePoolManagementPanel.getIcon(), fundingSourcePoolManagementPanel);
		addTab(EAM.text("Reporting"), new UiScrollPane(new BudgetComponent()));
	}
	
	private void addScrollableTab(ModelessDialogPanel panel)
	{
		JScrollPane scrollPane = new JScrollPane(panel);
		addTab(panel.getPanelDescription(), scrollPane);
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
	}
	
	private void addBudgetDoersToMap()
	{
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
		return new BudgetWizardPanel();
	}
	
	BudgetTreeTablePanel treeTableComponent;
	
	BudgetPropertiesPanel budgetPropertiesPanel;
	BudgetManagementPanel budgetManagmentPanel;

	AccountingCodePoolManagementPanel accountingCodePoolManagementPanel;
	
	FundingSourcePoolManagementPanel fundingSourcePoolManagementPanel;
}

class BudgetComponent extends JLabel
{
	public BudgetComponent()
	{
		super(new ResourceImageIcon("images/Budget.png"));
	}
}