/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import org.miradi.views.planning.PlanningView;

public class SwitchToPlanningViewDoer extends ViewSwitchDoer
{
	protected String getViewName()
	{
		return PlanningView.getViewName();
	}
}