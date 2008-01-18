/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.actions;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class ActionEditStrategyRelevancyList extends ObjectsAction
{
	public ActionEditStrategyRelevancyList(MainWindow mainWindow)
	{
		super(mainWindow, getLabel());
	}

	private static String getLabel()
	{
		return EAM.text("Action|Edit Strategies...");
	}

	public String getToolTipText()
	{
		return EAM.text("TT|Edit the list of strategies that are relevant to this objective");
	}
}
