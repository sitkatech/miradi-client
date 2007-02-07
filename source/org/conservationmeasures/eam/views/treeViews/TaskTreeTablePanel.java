/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.treeViews;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.ids.BaseId;
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
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;
import org.conservationmeasures.eam.views.workplan.WorkPlanTaskNode;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

import com.jhlabs.awt.GridLayoutPlus;

public class TaskTreeTablePanel extends DisposablePanel  implements TreeSelectionListener, CommandExecutedListener
{
	public TaskTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		super(new BorderLayout());
		mainWindow = mainWindowToUse;
		project = projectToUse;
		tree = treeToUse;
		
		tree.getTree().addTreeSelectionListener(this);
		restoreTreeExpansionState();
		
		UiScrollPane uiScrollPane = new UiScrollPane(tree);
		add(uiScrollPane, BorderLayout.CENTER);
		
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		JPanel buttonBox = new JPanel(layout);
		add(buttonBox,BorderLayout.AFTER_LINE_ENDS);
		addButtonsToBox(buttonBox, mainWindow.getActions());

		tree.getTree().addSelectionRow(0);
		project.addCommandExecutedListener(this);
	}
	
	public TreeTableWithStateSaving getTree()
	{
		return tree;
	}

	protected void restoreTreeExpansionState() 
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
		project.removeCommandExecutedListener(this);
		tree.dispose();
		super.dispose();
	}

	public TreeTableNode getSelectedObject()
	{
		TreeTableNode selected = (TreeTableNode)tree.getTree().getLastSelectedPathComponent();
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
		setSelectedRow(row);
	}

	public Task getSelectedTask()
	{
		TreeTableNode selected = getSelectedObject();
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

	public TaskTreeTableModel getModel()
	{
		return model;
	}
	
	protected void addButtonsToBox(JPanel buttonBox, Actions actions)
	{
		addCreateButtonAndAddToBox(ActionTreeCreateActivity.class, buttonBox, actions);
		addCreateButtonAndAddToBox(ActionTreeCreateMethod.class, buttonBox, actions);
		addCreateButtonAndAddToBox(ActionTreeCreateTask.class, buttonBox, actions);
		addCreateButtonAndAddToBox(ActionDeleteWorkPlanNode.class, buttonBox, actions);
		addCreateButtonAndAddToBox(ActionTreeNodeUp.class, buttonBox, actions);
		addCreateButtonAndAddToBox(ActionTreeNodeDown.class, buttonBox, actions);
	}
	
	private void addCreateButtonAndAddToBox(Class actionClass, JPanel buttonBox, Actions actions)
	{
		UiButton button = createObjectsActionButton(actions.getObjectsAction(actionClass), tree);
		buttonBox.add(button);
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
		TreeTableNode selectedObject = getSelectedObject();
		if (selectedObject == null)
			return;
		if (propertiesPanel == null)
			return;
		
		BaseId baseId = BaseId.INVALID;
		if (selectedObject.getType() == ObjectType.TASK)
			baseId = getSelectedObject().getObjectReference().getObjectId();
		
		propertiesPanel.setObjectId(baseId);
		mainWindow.getActions().updateActionStates();
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		int currentSelectedRow = tree.getSelectedRow();
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
			model.dataWasChanged(cmd);
			if(TaskTreeTableModel.isTreeStructureChangingCommand(cmd))
				restoreTreeExpansionState();
			else
				repaint();
		}
		
		setSelectedRow(currentSelectedRow);
	}
	
	private void setSelectedRow(int currentSelectedRow)
	{
		if(currentSelectedRow < 0)
		{
			tree.clearSelection();
			return;
		}
		
		try
		{
			tree.setRowSelectionInterval(currentSelectedRow, currentSelectedRow);
		}
		catch (Exception e)
		{
			//TODO ingnoring for now.  precheck to ingore and only log real errors.
			EAM.logException(e);
		}
	}

	
	public void setPropertiesPanel(ObjectDataInputPanel propertiesPanelToUse)
	{
		propertiesPanel = propertiesPanelToUse;
	}

	Project project;
	MainWindow mainWindow;
	protected TreeTableWithStateSaving tree;
	protected TaskTreeTableModel model;
	ObjectDataInputPanel propertiesPanel;
}
