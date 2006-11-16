/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionImportTncCapProjectFile extends MainWindowAction
{
	public ActionImportTncCapProjectFile(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Project TNC CAP File");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Import to TNC CAP file");
	}

}
