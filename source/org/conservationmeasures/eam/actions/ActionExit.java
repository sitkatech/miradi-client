/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionExit extends MainWindow.Action
{
	public ActionExit(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Exit");
	}

	public void actionPerformed(ActionEvent event)
	{
		getMainWindow().exitNormally();
	}
}
