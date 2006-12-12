/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionImportAccountingCodes extends ObjectsAction
{
	public ActionImportAccountingCodes(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Import Accounting Codes");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Import accounting codes");
	}

}
