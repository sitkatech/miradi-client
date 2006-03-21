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
		setLayout(new BorderLayout());
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

}

class ScheduleComponent extends JLabel
{
	public ScheduleComponent()
	{
		super(new ImageIcon("images/Calendar.jpg"));
	}
}