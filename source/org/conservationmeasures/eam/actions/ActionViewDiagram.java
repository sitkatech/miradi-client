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

public class ActionViewDiagram extends MainWindowAction
{
	public ActionViewDiagram(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Diagram View");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Switch to the Diagram View");
	}
	
	public String toString()
	{
		return getLabel();
	}

	public void doAction(UmbrellaView view, ActionEvent event) throws CommandFailedException
	{
		view.getViewDiagram().doIt();
	}

	public boolean shouldBeEnabled(UmbrellaView view)
	{
		return view.getViewDiagram().isAvailable();
	}

}
