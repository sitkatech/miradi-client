package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionModifyGoal extends MainWindowAction
{
	public ActionModifyGoal(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Modify Goal");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Modify the selected Goal");
	}

}
