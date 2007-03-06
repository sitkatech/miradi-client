/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.treeViews;

import org.conservationmeasures.eam.actions.ActionDeleteWorkPlanNode;
import org.conservationmeasures.eam.actions.ActionTreeCreateActivity;
import org.conservationmeasures.eam.actions.ActionTreeCreateMethod;
import org.conservationmeasures.eam.actions.ActionTreeCreateTask;
import org.conservationmeasures.eam.actions.ActionTreeNodeDown;
import org.conservationmeasures.eam.actions.ActionTreeNodeUp;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class TaskTreeTablePanel extends TreeTablePanel
{
	public TaskTreeTablePanel(MainWindow mainWindowToUse, Project projectToUse, TreeTableWithStateSaving treeToUse)
	{
		super( mainWindowToUse, projectToUse, treeToUse, buttonActions, ObjectType.TASK);
	}
	

	public void commandExecuted(CommandExecutedEvent event)
	{
		TaskTreeTableModel taskTreeTableModel = (TaskTreeTableModel)getModel();
		
		int currentSelectedRow = tree.getSelectedRow();
		if( isFactorCommand(event,  Strategy.TAG_ACTIVITY_IDS) ||
			isFactorCommand(event,  Task.TAG_SUBTASK_IDS))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			taskTreeTableModel.idListWasChanged(cmd.getObjectType(), cmd.getObjectId(), cmd.getDataValue());
			restoreTreeExpansionState();
		}
		else if(isCreateObjectCommand(event) || isDeleteObjectCommand(event) || isFactorCommand(event, Factor.TAG_OBJECTIVE_IDS))
		{
			taskTreeTableModel.objectiveWasModified();
			restoreTreeExpansionState();
		}
		else if(isSetDataCommand(event))
		{
			CommandSetObjectData cmd = (CommandSetObjectData)event.getCommand();
			taskTreeTableModel.dataWasChanged(cmd);
			if(TaskTreeTableModel.isTreeStructureChangingCommand(cmd))
				restoreTreeExpansionState();
			else
				repaint();
		}
		
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
