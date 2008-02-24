/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import org.miradi.views.reports.ReportsView;
import org.miradi.views.umbrella.ViewSwitchDoer;

public class SwitchToReportViewDoer extends ViewSwitchDoer
{
	@Override
	protected String getViewName()
	{
		return ReportsView.getViewName();
	}
}
