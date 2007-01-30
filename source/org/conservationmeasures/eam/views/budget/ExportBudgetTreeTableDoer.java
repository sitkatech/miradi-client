/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.views.MainWindowDoer;

public class ExportBudgetTreeTableDoer extends MainWindowDoer
{

	public boolean isAvailable()
	{
		String currentViewName = getMainWindow().getCurrentView().cardName();
		String budgetViewName = BudgetView.getViewName();
		
		if (currentViewName.equals(budgetViewName))
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
		//TODO needs implementation.  DoIt
	}
	
	

}
