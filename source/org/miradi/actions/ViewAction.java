/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.main.MainWindow;
import org.miradi.views.Doer;

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

	public Doer getDoer()
	{
		Doer doer = super.getDoer();
		if(doer != null)
			doer.setView(mainWindow.getCurrentView());
		return doer;
	}

}
