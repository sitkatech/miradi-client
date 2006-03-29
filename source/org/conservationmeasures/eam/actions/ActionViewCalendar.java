package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewCalendar extends MainWindowAction
{
	public ActionViewCalendar(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Calendar");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Calendar");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
