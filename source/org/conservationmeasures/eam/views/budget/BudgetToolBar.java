package org.conservationmeasures.eam.views.budget;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewBudget;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class BudgetToolBar extends JToolBar
{
	public BudgetToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewBudget.class));
	}

}
