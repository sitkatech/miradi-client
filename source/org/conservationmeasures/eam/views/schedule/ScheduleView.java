/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.schedule;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

public class ScheduleView extends TabbedView
{
	public ScheduleView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
		wizardPanel = new WizardPanel(mainWindowToUse, this);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.SCHEDULE_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new ScheduleToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		UiScrollPane calendarPanel = new UiScrollPane(new ScheduleComponent("images/Calendar.png"));
		UiScrollPane ganttPanel = new UiScrollPane(new ScheduleComponent("images/Tasks.png"));

		addTab(EAM.text("Calendar"),calendarPanel);
		addTab(EAM.text("Tasks"),ganttPanel);
	}


	public void deleteTabs() throws Exception
	{
	}

	
	public WizardPanel createWizardPanel() throws Exception
	{
		return wizardPanel;
	}
}

class ScheduleComponent extends JLabel
{
	public ScheduleComponent(String file)
	{
		super(new ResourceImageIcon(file));
	}
}