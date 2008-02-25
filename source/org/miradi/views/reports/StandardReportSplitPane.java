/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import org.miradi.main.MainWindow;

public class StandardReportSplitPane extends ReportSplitPane
{
	public StandardReportSplitPane(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse, getUniqueSplitterName());
	}

	private static String getUniqueSplitterName()
	{
		return UNIQUE_SPLITTER_NAME;
	}
	
	protected ReportsViewControlBar getReportControlBar()
	{
		return new StandardReportsViewControlBar(this);
	}
	
	private static final String UNIQUE_SPLITTER_NAME = "StandardReportSplitPane";
}
