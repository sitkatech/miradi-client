/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import org.conservationmeasures.eam.views.reports.ReportView;
import org.conservationmeasures.eam.views.umbrella.ViewSwitchDoer;

public class SwitchToReportViewDoer extends ViewSwitchDoer
{
	@Override
	protected String getViewName()
	{
		return ReportView.getViewName();
	}
}
