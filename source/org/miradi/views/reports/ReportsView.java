/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.reports;

import java.awt.BorderLayout;

import javax.swing.JToolBar;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.views.TabbedView;

public class ReportsView extends TabbedView
{
	public ReportsView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.REPORT_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new ReportToolBar(getActions());
	}

	@Override
	public void becomeActive() throws Exception
	{
		super.becomeActive();
		standardReportSplitPane.restoreSavedLocation();
		customReportSplitPane.restoreSavedLocation();
	}
	
	public void createTabs() throws Exception
	{
		standardReportSplitPane = new StandardReportSplitPane(getMainWindow());
		customReportSplitPane = new CustomReportSplitPane(getMainWindow());

		addTab(EAM.text("Standard Reports"), standardReportSplitPane);
		addTab(EAM.text("Custom Reports"), customReportSplitPane);
	}

	public void deleteTabs() throws Exception
	{
		// lightweight tabs...nothing to dispose yet
	}
	
	@Override
	public void prepareForTabSwitch()
	{
		super.prepareForTabSwitch();
		
		standardReportSplitPane.clear();
		customReportSplitPane.clear();
	}
	
	private StandardReportSplitPane standardReportSplitPane;
	private CustomReportSplitPane customReportSplitPane;
}
