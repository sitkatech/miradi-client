package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewBudget extends MainWindowAction
{
	public ActionViewBudget(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Budget") + DEMO_INDICATOR;
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Budget View");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
