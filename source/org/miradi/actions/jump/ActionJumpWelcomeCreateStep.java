/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions.jump;

import org.miradi.actions.MainWindowAction;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionJumpWelcomeCreateStep extends MainWindowAction
{
	public ActionJumpWelcomeCreateStep(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}
	
	@Override
	public String getToolTipText()
	{
		return EAM.text("Create a new Miradi project"); 
	}
	
	@Override
	public boolean isAvailableWithoutProject()
	{
		return true;
	}
	
	private static String getLabel()
	{
		return EAM.text("Create..."); 
	}


}
