/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.reports;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.martus.swing.UiScrollPane;

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

	public void createTabs() throws Exception
	{
		UiScrollPane labelPanel = new UiScrollPane(new JLabel("not implemented yet"));

		addTab(EAM.text("Reports"),labelPanel);
	}

	public void deleteTabs() throws Exception
	{
		// lightweight tabs...nothing to dispose yet
	}	
}
