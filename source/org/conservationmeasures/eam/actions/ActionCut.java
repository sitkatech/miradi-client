/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.main.MainWindowAction;

public class ActionCut extends MainWindowAction
{
	public ActionCut(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/cut.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Cut");
	}


	public void actionPerformed(ActionEvent arg0)
	{
		// TODO Auto-generated method stub

	}

}
