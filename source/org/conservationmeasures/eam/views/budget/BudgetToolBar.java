package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.actions.ActionViewBudget;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.EAMToolBar;

public class BudgetToolBar extends EAMToolBar
{
	public BudgetToolBar(Actions actions)
	{
		super(actions, ActionViewBudget.class);
	}

}
