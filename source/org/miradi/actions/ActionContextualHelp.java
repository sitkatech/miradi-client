/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import java.awt.event.ActionEvent;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionContextualHelp extends MainWindowAction
{
	public ActionContextualHelp(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|ContextSensitive|Help");
	}

	public void doAction(ActionEvent event) throws CommandFailedException
	{
		EAM.logWarning("Context-sensitive help action invoked!");
	}
}