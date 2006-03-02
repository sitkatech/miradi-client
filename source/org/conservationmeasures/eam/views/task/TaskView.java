package org.conservationmeasures.eam.views.task;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class TaskView extends UmbrellaView
{
	public TaskView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new TaskToolBar(mainWindowToUse.getActions()));
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Task";
	}
}
