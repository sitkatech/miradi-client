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

public class ActionCopy extends MainWindowAction
{
	public ActionCopy(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/copy.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Copy");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Copy the selection to the clipboard");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getCopyDoer().doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getCopyDoer().isAvailable();
	}
}
