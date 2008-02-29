/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.AccountCodeIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateAccountingCode extends ObjectsAction
{
	public ActionCreateAccountingCode(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new AccountCodeIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create Accounting Code");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create an Accounting Code");
	}

}
