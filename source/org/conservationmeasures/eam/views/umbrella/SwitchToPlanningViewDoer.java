/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.views.planning.PlanningView;

public class SwitchToPlanningViewDoer extends ViewSwitchDoer
{
	String getViewName()
	{
		return PlanningView.getViewName();
	}
}
