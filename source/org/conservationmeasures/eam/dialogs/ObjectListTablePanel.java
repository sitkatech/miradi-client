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

import org.conservationmeasures.eam.actions.MainWindowAction;
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
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

abstract public class ObjectListTablePanel extends DisposablePanel implements ListSelectionListener, CommandExecutedListener
{
	public ObjectListTablePanel(Project projectToUse, int objectTypeToUse, ObjectListTableModel model, MainWindowAction createAction, ObjectsAction deleteAction)
	{
		super(new BorderLayout());
		project = projectToUse;
		objectType = objectTypeToUse;
		table = new ObjectListTable(model);
		table.addListSelectionListener(this);
		add(new UiScrollPane(table), BorderLayout.CENTER);

		Box buttons = Box.createVerticalBox();
		buttons.add(new UiButton(createAction));
		buttons.add(new ObjectsActionButton(deleteAction, table));
		add(buttons, BorderLayout.AFTER_LINE_ENDS);
		
		project.addCommandExecutedListener(this);
	}

	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}


	public Indicator getSelectedObject()
	{
		EAMObject[] selected = table.getSelectedObjects();
		if(selected.length == 0)
			return null;
		return (Indicator)selected[0];
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			BaseId selectedId = BaseId.INVALID;
			int row = table.getSelectedRow();
			if(row >= 0)
			{
				EAMObject selectedObject = table.getObjectListTableModel().getObjectFromRow(row);
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
		updateTableIfObjectsChanged(event);
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		updateTableIfObjectsChanged(event);
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	void updateTableIfObjectsChanged(CommandExecutedEvent e)
	{
		Command rawCommand = e.getCommand();
		String commandName = rawCommand.getCommandName();
		if(commandName.equals(CommandCreateObject.COMMAND_NAME))
			objectWasCreated((CommandCreateObject)rawCommand);
		else if(commandName.equals(CommandDeleteObject.COMMAND_NAME))
			objectWasDeleted((CommandDeleteObject)rawCommand);
		else if(commandName.equals(CommandSetObjectData.COMMAND_NAME))
			objectWasModified((CommandSetObjectData)rawCommand);
	}
	
	void objectWasCreated(CommandCreateObject cmd)
	{
		table.getObjectListTableModel().fireTableDataChanged();
	}
	
	void objectWasDeleted(CommandDeleteObject cmd)
	{
		table.getObjectListTableModel().fireTableDataChanged();
	}
	
	void objectWasModified(CommandSetObjectData cmd)
	{
		table.getObjectListTableModel().fireTableDataChanged();
	}

	Project project;
	int objectType;
	ObjectListTable table;
	ObjectDataInputPanel propertiesPanel;
}
