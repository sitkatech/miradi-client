/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import java.awt.Point;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionPaste extends LocationAction
{
	public ActionPaste(MainWindow mainWindow)
	{
		this(mainWindow, new Point(0,0));
	}
	
	public ActionPaste(MainWindow mainWindow, Point startPointToUse)
	{
		super(mainWindow, getLabel(), "icons/paste.gif");
		setInvocationPoint(startPointToUse);	
	}

	private static String getLabel()
	{
		return EAM.text("Action|Paste");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Paste the clipboard");
	}
	
}
