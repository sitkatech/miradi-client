/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.treeViews.TreeTablePanel;

public class PlanningTreeTablePanel extends TreeTablePanel
{
	public static PlanningTreeTablePanel createPlanningTreeTablePenel(MainWindow mainWindowToUse)
	{
		PlanningTreeRoot planningTreeRoot = new PlanningTreeRoot(mainWindowToUse.getProject(), mainWindowToUse.getProject().getMetadata().getRef());
		PlanningTreeModel model = new PlanningTreeModel(mainWindowToUse.getProject(), planningTreeRoot);
		PlanningTreeTable treeTable = new PlanningTreeTable(mainWindowToUse.getProject(), model);	

		return new PlanningTreeTablePanel(mainWindowToUse, treeTable, model);
	}
	
	private PlanningTreeTablePanel(MainWindow mainWindowToUse, PlanningTreeTable treeToUse, PlanningTreeModel modelToUse)
	{
		super(mainWindowToUse, treeToUse, getButtonActions());
		model = modelToUse;
	}
	
	private static Class[] getButtonActions()
	{
		return new Class[] {};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
	}
}
