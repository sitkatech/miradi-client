/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionViewDiagram extends MainWindowAction
{
	public ActionViewDiagram(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Diagram");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Conceptual Model Diagram");
	}
	
	public String toString()
	{
		return getLabel();
	}

}
