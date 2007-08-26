/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.treeViews;

import javax.swing.event.TreeSelectionListener;

import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.objects.ViewData;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class TaskTreeTablePanel extends TreeTablePanel  implements TreeSelectionListener, CommandExecutedListener
{
	public TaskTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		super(mainWindowToUse, treeToUse, buttonActions);
	}

	//FIXME: Kevin: This code needs to be analyzed
	// to see if it really needs to rebuld its tree under all these conditions
	// Also, taskTreeTablePanel and TargetViabilityTreeTablePanel should have very similar 
	// CommandExecuted methods with very similar structures
	public void commandExecuted(CommandExecutedEvent event)
	{
		GenericTreeTableModel treeTableModel = getModel();
		int currentSelectedRow = getNextSelectionIndex(event);
		
		final boolean wereActivityNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(ObjectType.STRATEGY , Strategy.TAG_ACTIVITY_IDS);
		final boolean wereIndicatorNodesAddedOrRemoved = wereIndicatorNodesAddedOrRemoved(event);
		final boolean wereTaskNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(ObjectType.TASK , Task.TAG_SUBTASK_IDS);
		if(	wereActivityNodesAddedOrRemoved || 
				wereIndicatorNodesAddedOrRemoved ||
				wereTaskNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();
		}
		else if(event.isCreateObjectCommand() || 
				event.isDeleteObjectCommand() || 
				event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR , Factor.TAG_OBJECTIVE_IDS))
		{
			rebuildEntireTree();
		}
		else if(event.isSetDataCommand())
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			if(TaskTreeTableModel.isTreeStructureChangingCommand(cmd))
			{
				rebuildEntireTree();
			}
			else if(cmd.getFieldTag().equals(ViewData.TAG_CURRENT_EXPANSION_LIST))
			{
				restoreTreeExpansionState();
			}
			else
			{
				repaintObjectRow(cmd.getObjectORef());
			}
		}

		tree.getTree().setSelectionInterval(currentSelectedRow, currentSelectedRow);
	}

	private int getNextSelectionIndex(CommandExecutedEvent event)
	{
		if (! event.isDeleteObjectCommand())
			return tree.getSelectedRow();
		
		int selectedRow = tree.getSelectedRow();
		if (selectedRow >= 0)
			return selectedRow;
		
		return tree.getRowCount() - 1;
	}

	private boolean wereIndicatorNodesAddedOrRemoved(CommandExecutedEvent event)
	{
		if(event.isSetDataCommandWithThisTypeAndTag(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE , KeyEcologicalAttribute.TAG_INDICATOR_IDS))
			return true;
		if(event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR, Factor.TAG_INDICATOR_IDS))
			return true;
		
		return false;
	}

	
	//TODO: should pull up to super class
	private void rebuildEntireTree()
	{
		//TODO: Is it the right place/mechanism?
		//int currentSelectedRow = tree.getSelectedRow();
		model.rebuildEntireTree();
		restoreTreeExpansionState();
		
		//TODO: Is it the right place/mechanism?
		//tree.getSelectionModel().setSelectionInterval(currentSelectedRow, currentSelectedRow);
	}
	
	//TODO: should pull up to super class
	private void repaintObjectRow(ORef objectToRepaint)
	{
		model.repaintObjectRow(objectToRepaint);
	}
	

	static final Class[] buttonActions = new Class[] {
		ActionTreeCreateActivity.class, 
		ActionTreeCreateMethod.class,
		ActionTreeCreateTask.class,
		ActionDeleteWorkPlanNode.class,
		ActionTreeNodeUp.class,
		ActionTreeNodeDown.class,};
	
}
