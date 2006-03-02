package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class BudgetView extends UmbrellaView
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
		return "Budget";
	}

}
