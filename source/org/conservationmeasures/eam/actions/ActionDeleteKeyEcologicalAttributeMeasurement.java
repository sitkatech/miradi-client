package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionDeleteKeyEcologicalAttributeMeasurement extends ObjectsAction
{
	public ActionDeleteKeyEcologicalAttributeMeasurement(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Delete Measurement");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Delete a Measurement");
	}
}
