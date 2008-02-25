/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import org.miradi.main.EAM;

public class CustomReportsTableModel extends ReportSelectionTableModel
{
	@Override
	public String getColumnName(int column)
	{
		return EAM.text("Custom Reports");
	}
	
	protected Report[] getAvailableReports()
	{
		return new Report[] {
				new Report("Rare: Executive Summary", "/reports/RareReport.jasper"),
		};
	}
	

}
