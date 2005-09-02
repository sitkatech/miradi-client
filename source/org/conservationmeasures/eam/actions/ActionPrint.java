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

public class ActionPrint extends MainWindowAction 
{
	public ActionPrint(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/print.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Print");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Print the current view to a printer");
	}
	
	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getPrintDoer().doIt();
		getMainWindow().getActions().updateActionStates();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getPrintDoer().isAvailable();
	}

}
