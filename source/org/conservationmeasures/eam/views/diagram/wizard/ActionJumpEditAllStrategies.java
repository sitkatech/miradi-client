package org.conservationmeasures.eam.views.diagram.wizard;

import org.conservationmeasures.eam.actions.MainWindowAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionJumpEditAllStrategies extends MainWindowAction
{
	public ActionJumpEditAllStrategies(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	private static String getLabel()
	{
		return EAM.text("Edit All Strategies"); 
	}


}
