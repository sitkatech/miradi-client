package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionUpdateTabLabel extends MainWindowAction
{
	public ActionUpdateTabLabel(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Update Result Chain Label");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Update Result Chain Label");
	}

}
