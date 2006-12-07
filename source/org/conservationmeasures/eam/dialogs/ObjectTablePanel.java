/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MouseAdapterDoubleClickDelegator;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class ObjectTablePanel extends DisposablePanel implements ListSelectionListener, CommandExecutedListener
{
	public ObjectTablePanel(Project projectToUse, int objectTypeToUse, ObjectTable tableToUse)
	{
		super(new BorderLayout());
		project = projectToUse;
		objectType = objectTypeToUse;
		table = tableToUse;
		table.addListSelectionListener(this);
		add(new UiScrollPane(table), BorderLayout.CENTER);

		buttons = Box.createVerticalBox();
		add(buttons, BorderLayout.AFTER_LINE_ENDS);

		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}

	public EAMObject getSelectedObject()
	{
		EAMObject[] selected = table.getSelectedObjects();
		if(selected.length == 0)
			return null;
		return selected[0];
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		if (propertiesPanel==null)
			return;
		try
		{
			BaseId selectedId = BaseId.INVALID;
			int[] row = table.getSelectedRows();
			if (row.length == 1 )
			{
				EAMObject selectedObject = table.getObjectTableModel().getObjectFromRow(row[0]);
				selectedId = selectedObject.getId();
			}
			propertiesPanel.setObjectId(selectedId);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setPropertiesPanel(ObjectDataInputPanel panel)
	{
		propertiesPanel = panel;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		if(event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
		{
			table.updateTableAfterCommand((CommandSetObjectData)event.getCommand());
		}
		else if(event.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
		{
			CommandCreateObject cmd = (CommandCreateObject)event.getCommand();
			table.updateTableAfterObjectCreated(new ORef(cmd.getObjectType(), cmd.getCreatedId()));
		}
		else if(event.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
		{
			CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
			table.updateTableAfterObjectDeleted(new ORef(cmd.getObjectType(), cmd.getObjectId()));
		}
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		if(event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			table.updateTableAfterUndo((CommandSetObjectData)event.getCommand());
		if(event.getCommandName().equals(CommandCreateObject.COMMAND_NAME))
		{
			CommandCreateObject cmd = (CommandCreateObject)event.getCommand();
			table.updateTableAfterObjectDeleted(new ORef(cmd.getObjectType(), cmd.getCreatedId()));
		}
		if(event.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
		{
			CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
			table.updateTableAfterObjectCreated(new ORef(cmd.getObjectType(), cmd.getObjectId()));
		}
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	public void addButton(ObjectsAction action)
	{
		addButton(createObjectsActionButton(action, table));
	}
	
	public void addButton(UiButton button)
	{
		buttons.add(button);
	}
	
	public void addDoubleClickAction(EAMAction action ) 
	{
		table.addMouseListener(new MouseAdapterDoubleClickDelegator(action)); 
	}
	
	public ObjectTable getTable()
	{
		return table;
	}
	
	Project project;
	int objectType;
	ObjectTable table;
	ObjectDataInputPanel propertiesPanel;
	Box buttons;

}
