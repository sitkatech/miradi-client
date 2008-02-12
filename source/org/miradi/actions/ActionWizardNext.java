/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.actions;

import javax.swing.Icon;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionWizardNext extends ViewAction
{
	public ActionWizardNext(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), (Icon)null);
	}

	private static String getLabel()
	{
		return EAM.text("Action|Next >");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Go to the Next wizard step");
	}
	
}
