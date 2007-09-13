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
import org.conservationmeasures.eam.objects.PlanningViewConfiguration;
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
		try
		{
			if(forcesTableRebuild(cmd))
				rebuildAfterColumnChange();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
	}
	
	private void rebuildAfterColumnChange() throws Exception
	{
		getPlanningModel().rebuildCodeList();
		tree.rebuildTableCompletely();
	}

// TODO: This is a possible alternative to rebuildAfterColumnChange
// If that is not working, try this. If that is working, delete this
//	private void rebuildAfterRowChange()
//	{
//		getPlanningModel().rebuildEntireTree();
//		restoreTreeExpansionState();
//	}

	private boolean forcesTableRebuild(CommandSetObjectData cmd)
	{
		if(cmd.getObjectType() == ViewData.getObjectType())
		{
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_STYLE_CHOICE))
				return true;
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_SINGLE_LEVEL_CHOICE))
				return true;
			if(cmd.getFieldTag().equals(ViewData.TAG_PLANNING_CUSTOM_PLAN_REF))
				return true;
		}
		
		if(cmd.getObjectType() == PlanningViewConfiguration.getObjectType())
		{
			if(cmd.getFieldTag().equals(PlanningViewConfiguration.TAG_COL_CONFIGURATION))
				return true;
			if(cmd.getFieldTag().equals(PlanningViewConfiguration.TAG_ROW_CONFIGURATION))
				return true;
		}
		
		return false;
	}

	private PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getModel();
	}
}
