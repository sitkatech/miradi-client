/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.actions;

import org.miradi.icons.ConceptualModelIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ActionShowConceptualModel extends ViewAction
{
	public ActionShowConceptualModel(MainWindow mainWindow)
	{
		super(mainWindow, getLabel(), new ConceptualModelIcon());
	}
	
	private static String getLabel()
	{
		return EAM.text("Action|Show Conceptual Model");
	}

	public String getToolTipText()
	{
		return EAM.text("Action|Show Conceptual Model");
	}
}
