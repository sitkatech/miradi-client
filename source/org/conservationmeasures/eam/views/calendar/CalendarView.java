package org.conservationmeasures.eam.views.calendar;

import java.awt.BorderLayout;

import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.ResourceImageIcon;
import org.martus.swing.UiScrollPane;

public class CalendarView extends UmbrellaView
{

	public CalendarView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new CalendarToolBar(mainWindowToUse.getActions()));
		add(createScreenShotLabel(), BorderLayout.BEFORE_FIRST_LINE);
		add(new UiScrollPane(new ScheduleComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.CALENDAR_VIEW_NAME;
	}

	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}

}

class ScheduleComponent extends JLabel
{
	public ScheduleComponent()
	{
		super(new ResourceImageIcon("images/Calendar.png"));
	}
}