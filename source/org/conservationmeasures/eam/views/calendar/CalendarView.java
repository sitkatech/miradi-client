package org.conservationmeasures.eam.views.calendar;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
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
		return "Calendar";
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
		super(new ImageIcon("images/Calendar.png"));
	}
}