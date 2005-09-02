/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.actions;

import javax.swing.Icon;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.Doer;

abstract public class ViewAction extends MainWindowAction
{
	public ViewAction(MainWindow mainWindowToUse, String label)
	{
		super(mainWindowToUse, label);
	}

	public ViewAction(MainWindow mainWindowToUse, String label, String icon)
	{
		super(mainWindowToUse, label, icon);
	}

	public ViewAction(MainWindow mainWindowToUse, String label, Icon icon)
	{
		super(mainWindowToUse, label, icon);
	}

	Doer getDoer()
	{
		Doer doer = super.getDoer();
		doer.setView(mainWindow.getCurrentView());
		return doer;
	}

}
