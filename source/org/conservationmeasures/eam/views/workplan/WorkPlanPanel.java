/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.workplan;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionInsertActivity;
import org.conservationmeasures.eam.actions.ActionModifyActivity;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ActivityInsertionPoint;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class WorkPlanPanel extends DisposablePanel implements TreeSelectionListener, CommandExecutedListener
{
	public WorkPlanPanel(MainWindow mainWindowToUse, Project projectToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		model = new WorkPlanTreeTableModel(projectToUse);
		tree = new WorkPlanTreeTable(mainWindow.getProject(), model);
		tree.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		tree.getTree().setShowsRootHandles(true);
		tree.getTree().addTreeSelectionListener(this);
		restoreTreeExpansionState();
		
		UiScrollPane uiScrollPane = new UiScrollPane(tree);
		add(uiScrollPane, BorderLayout.CENTER);
		add(createButtonBox(mainWindow.getActions()), BorderLayout.AFTER_LAST_LINE);

		tree.getTree().addSelectionRow(0);
		mainWindow.getProject().addCommandExecutedListener(this);
	}

	private void restoreTreeExpansionState() 
	{
		try
		{
			tree.restoreTreeState();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error restoring tree state"));
		}
	}

	public void dispose()
	{
		mainWindow.getProject().removeCommandExecutedListener(this);
		super.dispose();
	}

	public WorkPlanTreeTableNode getSelectedObject()
	{
		WorkPlanTreeTableNode selected = (WorkPlanTreeTableNode)tree.getTree().getLastSelectedPathComponent();
		return selected;
	}

	public void selectObject(EAMObject objectToSelect)
	{
		TreePath found = model.findObject(model.getPathToRoot(), objectToSelect.getType(), objectToSelect.getId());
		if(found == null)
			return;
		tree.getTree().expandPath(found.getParentPath());
		EAM.logDebug("" + found + ": " + tree.getTree().getRowForPath(found));
		TreePath parentPath = found.getParentPath();
		EAM.logDebug("" + parentPath + ": " + tree.getTree().getRowForPath(parentPath));
		parentPath = parentPath.getParentPath();
		EAM.logDebug("" + parentPath + ": " + tree.getTree().getRowForPath(parentPath));
		int row = tree.getTree().getRowForPath(found);
		EAM.logDebug("" + row);
		if(row < 0)
		{
			EAM.logWarning("StrategicPlanPanel.selectObject failed: row -1");
			return;
		}
		tree.clearSelection();
		tree.setRowSelectionInterval(row, row);
	}

	public Task getSelectedTask()
	{
		WorkPlanTreeTableNode selected = getSelectedObject();
		if(selected == null)
			return null;
		if(selected.getType() != ObjectType.TASK)
			return null;

		return ((WorkPlanTaskNode)selected).getActivity();
	}

	public ActivityInsertionPoint getActivityInsertionPoint()
	{
		TreePath path = tree.getTree().getSelectionPath();
		return new ActivityInsertionPoint(path, 0);
	}

	public WorkPlanTreeTableModel getModel()
	{
		return model;
	}

	public Strategy getParentIntervention(Task activity)
	{
		return model.getParentIntervention(activity);
	}
	
	private Box createButtonBox(Actions actions)
	{
		Box buttonBox = Box.createHorizontalBox();
		UiButton addButton = new UiButton(actions.get(ActionInsertActivity.class));
		UiButton editButton = new UiButton(actions.get(ActionModifyActivity.class));
		UiButton deleteButton = createObjectsActionButton(actions.getObjectsAction(ActionDeleteWorkPlanNode.class), tree);
		UiButton upButton = new UiButton(actions.get(ActionTreeNodeUp.class));
		UiButton downButton = new UiButton(actions.get(ActionTreeNodeDown.class));
		buttonBox.add(addButton);
		buttonBox.add(editButton);
		buttonBox.add(deleteButton);
		buttonBox.add(upButton);
		buttonBox.add(downButton);

		return buttonBox;
	}

	boolean isSetDataCommand(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		return(rawCommand.getCommandName().equals(CommandSetObjectData.COMMAND_NAME));
	}

	boolean isChangeActivitiesListCommand(CommandExecutedEvent event)
	{
		if(!isSetDataCommand(event))
			return false;

		Command rawCommand = event.getCommand();
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		if(cmd.getObjectType() == ObjectType.FACTOR && cmd.getFieldTag().equals(Strategy.TAG_ACTIVITY_IDS))
			return true;
		return false;
	}

	boolean isChangeSubtaskListCommand(CommandExecutedEvent event)
	{
		if(!isSetDataCommand(event))
			return false;

		Command rawCommand = event.getCommand();
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		if(cmd.getObjectType() == ObjectType.TASK && cmd.getFieldTag().equals(Task.TAG_SUBTASK_IDS))
			return true;
		return false;
	}

	boolean isChangeObjectiveListCommand(CommandExecutedEvent event)
	{
		if(!isSetDataCommand(event))
			return false;

		Command rawCommand = event.getCommand();
		CommandSetObjectData cmd = (CommandSetObjectData)rawCommand;
		if(cmd.getObjectType() == ObjectType.FACTOR && cmd.getFieldTag().equals(Factor.TAG_OBJECTIVE_IDS))
		{
			return true;
		}
		return false;
	}

	boolean isCreateObjectCommand(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		return (rawCommand.getCommandName().equals(CommandCreateObject.COMMAND_NAME));
	}

	boolean isDeleteObjectCommand(CommandExecutedEvent event)
	{
		Command rawCommand = event.getCommand();
		return (rawCommand.getCommandName().equals(CommandDeleteObject.COMMAND_NAME));
	}

	public void valueChanged(TreeSelectionEvent e)
	{
		mainWindow.getActions().updateActionStates();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if(isChangeActivitiesListCommand(event) || isChangeSubtaskListCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			model.idListWasChanged(cmd.getObjectType(), cmd.getObjectId(), cmd.getDataValue());
			restoreTreeExpansionState();
		}
		else if(isCreateObjectCommand(event) || isDeleteObjectCommand(event) || isChangeObjectiveListCommand(event))
		{
			model.objectiveWasModified();
			restoreTreeExpansionState();
		}
		else if(isSetDataCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			if(cmd.getObjectType() != ObjectType.VIEW_DATA)
				model.dataWasChanged(cmd.getObjectType(), cmd.getObjectId());
			restoreTreeExpansionState();
		}
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		if(isChangeActivitiesListCommand(event) || isChangeSubtaskListCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			model.idListWasChanged(cmd.getObjectType(), cmd.getObjectId(), cmd.getPreviousDataValue());
			restoreTreeExpansionState();
		}
		else if(isCreateObjectCommand(event) || isDeleteObjectCommand(event) || isChangeObjectiveListCommand(event))
		{
			model.objectiveWasModified();
			restoreTreeExpansionState();
		}
		else if(isSetDataCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			model.dataWasChanged(cmd.getObjectType(), cmd.getObjectId());
			restoreTreeExpansionState();
		}
	}

	MainWindow mainWindow;
	WorkPlanTreeTable tree;
	WorkPlanTreeTableModel model;
}
