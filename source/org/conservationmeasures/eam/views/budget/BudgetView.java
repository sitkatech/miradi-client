package org.conservationmeasures.eam.views.budget;

import javax.swing.JLabel;

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
		budgetPanel = new BudgetPanel();
		addTab(EAM.text("Budget"), budgetPanel);
		addTab(EAM.text("Reporting"), new UiScrollPane(new BudgetComponent()));
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		return new BudgetWizardPanel();
	}

	public void deleteTabs() throws Exception
	{
		budgetPanel.dispose();
		budgetPanel = null;
	}
	
	BudgetPanel budgetPanel;
}

class BudgetComponent extends JLabel
{
	public BudgetComponent()
	{
		super(new ResourceImageIcon("images/Budget.png"));
	}
}