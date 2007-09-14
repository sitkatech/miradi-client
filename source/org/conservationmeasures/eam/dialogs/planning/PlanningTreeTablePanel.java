/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.planning.PlanningView;
import org.conservationmeasures.eam.views.treeViews.TreeTablePanel;

public class PlanningTreeTablePanel extends TreeTablePanel
{
	public static PlanningTreeTablePanel createPlanningTreeTablePanel(MainWindow mainWindowToUse) throws Exception
	{ 
		PlanningTreeModel model = new PlanningTreeModel(mainWindowToUse.getProject());
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
		return new Class[] {
			ActionDeletePlanningViewTreeNode.class,
			ActionTreeNodeUp.class,
			ActionTreeNodeDown.class,
		};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(!event.isSetDataCommand())
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		try
		{
			if(PlanningView.isRowOrColumnChangingCommand(cmd))
				rebuildEntireTreeTable();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
	}
	
	private void rebuildEntireTreeTable() throws Exception
	{
		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
		
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().rebuildCodeList();
		tree.rebuildTableCompletely();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		restoreTreeExpansionState();
	}

	private PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getModel();
	}
}
