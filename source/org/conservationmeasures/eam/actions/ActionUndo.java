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

public class ActionUndo extends MainWindowAction
{
	public ActionUndo(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), "icons/undo.gif");
	}

	private static String getLabel()
	{
		return EAM.text("Action|Undo");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Undo last action");
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getUndoDoer().doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getUndoDoer().isAvailable();
	}
	
}
