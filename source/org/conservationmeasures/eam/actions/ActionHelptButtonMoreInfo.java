/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionHelptButtonMoreInfo extends MainWindowAction
{
	public ActionHelptButtonMoreInfo(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(),"images/info.png");
	}

	private static String getLabel()
	{
		return EAM.text("Action|More Info");
	}

}