/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import javax.swing.SwingUtilities;

import org.conservationmeasures.eam.actions.ActionDeletePlanningViewTreeNode;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
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
			
			if(isTaskMove(cmd))
				rebuildEntireTreeTable();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog("Error occurred: " + e.getMessage());
		}
	}
	
	private boolean isTaskMove(CommandSetObjectData cmd)
	{
		int type = cmd.getObjectType();
		String tag = cmd.getFieldTag();
		if(type == Task.getObjectType() && tag.equals(Task.TAG_SUBTASK_IDS))
			return true;
		if(type == Strategy.getObjectType() && tag.equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		if(type == Indicator.getObjectType() && tag.equals(Indicator.TAG_TASK_IDS))
			return true;
		return false;
	}

	private void rebuildEntireTreeTable() throws Exception
	{
		ORef selectedRef = ORef.INVALID;
		BaseObject[] selected = tree.getSelectedObjects();
		if(selected.length == 1)
			selectedRef = selected[0].getRef();

		// TODO: Perhaps possibly detect exactly what changed and 
		// only rebuild the columns or the rows rather than always doing both
		
		// NOTE: The following rebuild the columns but don't touch the tree
		getPlanningModel().rebuildCodeList();
		tree.rebuildTableCompletely();
		
		// NOTE: The following rebuild the tree but don't touch the columns
		getPlanningModel().rebuildEntireTree();
		restoreTreeExpansionState();

		selectObjectAfterSwingClearsItDueToTreeStructureChange(selectedRef);
	}

	private void selectObjectAfterSwingClearsItDueToTreeStructureChange(ORef selectedRef)
	{
		SwingUtilities.invokeLater(new Reselecter(tree, selectedRef));
	}
	
	class Reselecter implements Runnable
	{
		public Reselecter(TreeTableWithStateSaving treeTableToUse, ORef refToSelect)
		{
			treeTable = treeTableToUse;
			ref = refToSelect;
		}
		
		public void run()
		{
			treeTable.selectObject(ref);
		}
		
		TreeTableWithStateSaving treeTable;
		ORef ref;
	}

	private PlanningTreeModel getPlanningModel()
	{
		return (PlanningTreeModel)getModel();
	}
}
