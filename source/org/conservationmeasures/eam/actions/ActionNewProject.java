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
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class ActionNewProject extends MainWindowAction
{
	public ActionNewProject(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), "icons/new.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|New Project");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a new project");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getNewProjectDoer().doIt();
	}
	
	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getNewProjectDoer().isAvailable();
	}
}
