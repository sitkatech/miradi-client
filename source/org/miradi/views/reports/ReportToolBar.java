/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewReport;
import org.miradi.main.EAMToolBar;

public class ReportToolBar extends EAMToolBar
{
	public ReportToolBar(Actions actions)
	{
		super(actions, ActionViewReport.class);
	}
}
