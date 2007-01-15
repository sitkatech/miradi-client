/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionExportZippedProjectFile extends MainWindowAction
{
	public ActionExportZippedProjectFile(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Miradi Zip File and Reports");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Miradi Zip File and Reports");
	}

}
