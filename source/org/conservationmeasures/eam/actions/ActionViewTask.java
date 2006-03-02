package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewTask extends MainWindowAction
{
	public ActionViewTask(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Task View");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Task View");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
