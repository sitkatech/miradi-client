/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.planning.doers;

import org.miradi.main.EAM;
import org.miradi.objects.ViewData;
import org.miradi.views.ViewDoer;
import org.miradi.views.planning.PlanningView;

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
