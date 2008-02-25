/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;


public class StandardReportsViewControlBar extends ReportsViewControlBar
{
	public StandardReportsViewControlBar(ReportSplitPane ownerToUse)
	{
		super(ownerToUse);
	}
	
	protected ReportSelectionTableModel getReportSelectionModel()
	{
		return new StandardReportsTableModel();
	}
}
