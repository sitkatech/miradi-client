package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewStrategicPlan extends MainWindowAction
{
	public ActionViewStrategicPlan(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Strategic Plan") + DEMO_INDICATOR;
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Strategic Plan View");
	}
	
	public String toString()
	{
		return getLabel();
	}
}
