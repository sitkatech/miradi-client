/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import java.awt.BorderLayout;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;

public class ReportView extends TabbedView
{
	public ReportView(MainWindow mainWindowToUse)
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
		reportSplitPane.restoreSavedLocation();
	}
	
	public void createTabs() throws Exception
	{
		reportSplitPane = new ReportSplitPane(getMainWindow());

		addTab(EAM.text("Reports"), reportSplitPane);
	}

	public void deleteTabs() throws Exception
	{
		// lightweight tabs...nothing to dispose yet
	}
	
	private ReportSplitPane reportSplitPane;
}
