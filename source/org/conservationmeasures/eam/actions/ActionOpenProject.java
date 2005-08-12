/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import java.awt.event.ActionEvent;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.UmbrellaView;

public class ActionOpenProject extends MainWindowAction
{
	public ActionOpenProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/open.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Open Project");
	}


	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.doOpenProject(mainWindow);
	}
	
	public String getToolTipText()
	{
		return EAM.text("TT|Open an existing project");
	}

	public boolean shouldBeEnabled()
	{
		return true;
	}
}
