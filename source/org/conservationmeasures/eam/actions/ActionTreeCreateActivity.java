/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.icons.ActivityIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionTreeCreateActivity extends ObjectsAction
{
	public ActionTreeCreateActivity(MainWindow mainWindowToUse)
	{
		this(mainWindowToUse, getLabel());
	}

	public ActionTreeCreateActivity(MainWindow mainWindowToUse, String label)
	{
		super(mainWindowToUse, label, new ActivityIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Activity");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create an Activity for the selected Strategy");
	}
	
}
