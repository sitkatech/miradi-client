package org.conservationmeasures.eam.views.schedule;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ScheduleView extends UmbrellaView
{

	public ScheduleView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ScheduleToolBar(mainWindowToUse.getActions()));
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Schedule";
	}

}
