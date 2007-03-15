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
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class TaskTreeTablePanel extends TreeTablePanel  implements TreeSelectionListener, CommandExecutedListener
{
	public TaskTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		super(mainWindowToUse, treeToUse, buttonActions, ObjectType.TASK);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		GenericTreeTableModel treeTableModel = getModel();
		
		final boolean whereActivityNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(ObjectType.FACTOR , Strategy.TAG_ACTIVITY_IDS);
		final boolean whereIndicatorNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(ObjectType.KEY_ECOLOGICAL_ATTRIBUTE , KeyEcologicalAttribute.TAG_INDICATOR_IDS);
		final boolean whereTaskNodesAddedOrRemoved = event.isSetDataCommandWithThisTypeAndTag(ObjectType.TASK , Task.TAG_SUBTASK_IDS);
		if(	whereActivityNodesAddedOrRemoved || 
				whereIndicatorNodesAddedOrRemoved ||
				whereTaskNodesAddedOrRemoved)
		{
			treeTableModel.rebuildEntireTree();
			restoreTreeExpansionState();
			int objectType = ((CommandSetObjectData)event.getCommand()).getObjectType();
			IdList newIdList = event.extractNewlyAddedIds();
			for (int i=0; i<newIdList.size(); ++i)
			{
				int annoationObjectType = ObjectType.INDICATOR;
				if (whereActivityNodesAddedOrRemoved || whereTaskNodesAddedOrRemoved )
					annoationObjectType = ObjectType.TASK;
				expandAndSelectObject(annoationObjectType, newIdList.get(i));
			}
			if (newIdList.size()==0)
			{
				//TODO: right now we only go to the root of the node being deleted....not the sibling
				CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
				expandAndSelectObject(objectType, cmd.getObjectId());
			}
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
			else
				repaint();
		}
	}

	
	//TODO: should pull up to super class
	private void rebuildEntireTree()
	{
		int currentSelectedRow = tree.getSelectedRow();
		model.rebuildEntireTree();
		restoreTreeExpansionState();
		setSelectedRow(currentSelectedRow);
	}
	

	static final Class[] buttonActions = new Class[] {
		ActionTreeCreateActivity.class, 
		ActionTreeCreateMethod.class,
		ActionTreeCreateTask.class,
		ActionDeleteWorkPlanNode.class,
		ActionTreeNodeUp.class,
		ActionTreeNodeDown.class,};
	
}
