package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewMap extends MainWindowAction
{
	public ActionViewMap(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Map View");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Map View");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
