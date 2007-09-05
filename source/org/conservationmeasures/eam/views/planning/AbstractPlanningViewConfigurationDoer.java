/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.planning;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.views.ViewDoer;

abstract public class AbstractPlanningViewConfigurationDoer extends ViewDoer
{
	public boolean isAvailable()
	{
		if (!isPlanningView())
			return false;
		
		if (!isValidConfigurationChoice())
			return false;
			
		return true;
	}
	
	private boolean isValidConfigurationChoice()
	{
		try
		{
			ViewData viewData = getProject().getCurrentViewData();
			if (PlanningView.isCustomizationStyle(viewData))
				return true;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return false;
		}
		
		return false;
	}
}
