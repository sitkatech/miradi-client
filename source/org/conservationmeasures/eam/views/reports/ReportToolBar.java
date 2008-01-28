/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewReport;
import org.conservationmeasures.eam.main.EAMToolBar;

public class ReportToolBar extends EAMToolBar
{
	public ReportToolBar(Actions actions)
	{
		super(actions, ActionViewReport.class);
	}
}
