/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.ViewData;
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
		return new Class[] {};
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(!event.isSetDataCommand())
			return;
		
		CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
		if(cmd.getObjectType() != ViewData.getObjectType())
			return;
		
		rowConfigurationChanged(cmd);
		columnConfigurationChanged(cmd);
	}

	private void columnConfigurationChanged(CommandSetObjectData cmd)
	{
		if(! cmd.getFieldTag().equals(ViewData.TAG_PLANNING_HIDDEN_COL_TYPES))
			return;
		
		try
		{
			getPlanningModel().rebuildCodeList();	
			tree.tableChanged(null);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private void rowConfigurationChanged(CommandSetObjectData cmd)
	{
		if(! cmd.getFieldTag().equals(ViewData.TAG_PLANNING_HIDDEN_ROW_TYPES))
			return;
		
		rebuildEntireTree();
	}
	
	private void rebuildEntireTree()
	{
		getPlanningModel().rebuildEntireTree();
		restoreTreeExpansionState();
	}
	
	private PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getModel();
	}
}
