package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteGoal extends MainWindowAction
{
	public ActionDeleteGoal(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Goal");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete the selected Goal");
	}

}
