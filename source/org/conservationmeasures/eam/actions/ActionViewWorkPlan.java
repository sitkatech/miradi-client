package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewWorkPlan extends MainWindowAction
{
	public ActionViewWorkPlan(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Workplan") + DEMO_INDICATOR;
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Workplan View");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
