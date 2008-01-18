/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.views.summary.SummaryView;

public class SwitchToSummaryViewDoer extends ViewSwitchDoer
{
	String getViewName()
	{
		return SummaryView.getViewName();
	}
}
