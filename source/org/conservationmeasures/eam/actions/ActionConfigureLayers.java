/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionConfigureLayers extends MainWindowAction
{
	public ActionConfigureLayers(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "images/layers.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|View|Layer...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Hide or show portions of the diagram");
	}
}
