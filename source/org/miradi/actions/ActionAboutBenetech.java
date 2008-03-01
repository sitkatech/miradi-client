/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiResourceImageIcon;

public class ActionAboutBenetech extends MainWindowAction
{
	public ActionAboutBenetech(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new MiradiResourceImageIcon("icons/benetech16.png"));
	}

	private static String getLabel()
	{
		return EAM.text("Action|About Benetech");
	}

}
