package org.conservationmeasures.eam.views.calendar;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class CalendarView extends UmbrellaView
{

	public CalendarView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new CalendarToolBar(mainWindowToUse.getActions()));
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
