/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionCreateKeyEcologicalAttribute extends ObjectsAction
{
	public ActionCreateKeyEcologicalAttribute(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse, getLabel(), new KeyEcologicalAttributeIcon());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Manage|Create KEA");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Create a Key Ecological Attribute");
	}


}
