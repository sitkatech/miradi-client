/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import java.awt.Point;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
