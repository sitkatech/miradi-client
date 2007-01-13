package org.conservationmeasures.eam.actions.jump;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpLinkDirectThreatsToTargets extends MainWindowAction
{
	public ActionJumpLinkDirectThreatsToTargets(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	static String getLabel()
	{
		return EAM.text("Link Direct Threats to Targets");
	}

}
