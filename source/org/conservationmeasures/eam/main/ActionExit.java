/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.main;

import java.awt.event.ActionEvent;

public class ActionExit extends MainWindow.Action
{
	public ActionExit(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	public void actionPerformed(ActionEvent event)
	{
		getMainWindow().exitNormally();
	}

	private static String getLabel()
	{
		return EAM.text("Action|Exit");
	}
}
