/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.schedule;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.views.schedule.wizard.ScheduleWizardPanel;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

public class ScheduleView extends TabbedView
{
	public ScheduleView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ScheduleToolBar(mainWindowToUse.getActions()));
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.SCHEDULE_VIEW_NAME;
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
		return new ScheduleWizardPanel(getMainWindow());
	}
	
}

class ScheduleComponent extends JLabel
{
	public ScheduleComponent(String file)
	{
		super(new ResourceImageIcon(file));
	}
}