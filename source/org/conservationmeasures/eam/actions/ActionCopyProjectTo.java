/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionCopyProjectTo extends MainWindowAction
{
	public ActionCopyProjectTo(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel());
	}

	public static String getLabel()
	{
		return EAM.text("Action|Save Copy As...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a copy of this project under a different name");
	}

}
