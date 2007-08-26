/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
import org.conservationmeasures.eam.views.treeViews.TreeTablePanel;

public class PlanningTreeTablePanel extends TreeTablePanel
{
	public PlanningTreeTablePanel(MainWindow mainWindowToUse, TreeTableWithStateSaving treeToUse)
	{
		super(mainWindowToUse, treeToUse, getButtonActions(), ObjectType.FAKE);
	}

	private static Class[] getButtonActions()
	{
		return new Class[] {};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
	}
}
