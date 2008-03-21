/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
		standardReportSplitPane.restoreSavedLocation();
		customReportSplitPane.restoreSavedLocation();
	}
	
	private StandardReportSplitPane standardReportSplitPane;
	private CustomReportSplitPane customReportSplitPane;
}
