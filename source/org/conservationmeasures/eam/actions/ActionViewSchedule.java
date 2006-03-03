package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewSchedule extends MainWindowAction
{
	public ActionViewSchedule(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Schedule View");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Schedule View");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
