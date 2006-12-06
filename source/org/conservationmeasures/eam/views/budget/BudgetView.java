package org.conservationmeasures.eam.views.budget;

import javax.swing.JLabel;

import org.conservationmeasures.eam.actions.ActionAddAssignment;
import org.conservationmeasures.eam.actions.ActionRemoveAssignment;
import org.conservationmeasures.eam.dialogs.BudgetManagementPanel;
import org.conservationmeasures.eam.dialogs.BudgetPropertiesPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.budget.wizard.BudgetWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.conservationmeasures.eam.views.workplan.WorkPlanPanel;
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
	
		treeTableComponent = new WorkPlanPanel(getMainWindow(), getProject());
		budgetPropertiesPanel = new BudgetPropertiesPanel(getProject(), getMainWindow().getActions(), treeTableComponent);
		budgetManagmentPanel = new BudgetManagementPanel(getMainWindow(), getProject(), budgetPropertiesPanel, treeTableComponent);
		addTab(budgetManagmentPanel.getPanelDescription(), budgetManagmentPanel);
		addTab(EAM.text("Reporting"), new UiScrollPane(new BudgetComponent()));
	}
	
	public void deleteTabs() throws Exception
	{
		budgetPropertiesPanel.dispose();
		budgetPropertiesPanel = null;
		
		budgetManagmentPanel.dispose();
		budgetManagmentPanel = null;
	}
	
	private void addBudgetDoersToMap()
	{
		addDoerToMap(ActionAddAssignment.class, new AddAssignmentDoer());
		addDoerToMap(ActionRemoveAssignment.class, new RemoveAssignmentDoer());
	}
	
	public WizardPanel createWizardPanel() throws Exception
	{
		return new BudgetWizardPanel();
	}
	
	BudgetPropertiesPanel budgetPropertiesPanel;
	BudgetManagementPanel budgetManagmentPanel;
	WorkPlanPanel treeTableComponent;
}

class BudgetComponent extends JLabel
{
	public BudgetComponent()
	{
		super(new ResourceImageIcon("images/Budget.png"));
	}
}