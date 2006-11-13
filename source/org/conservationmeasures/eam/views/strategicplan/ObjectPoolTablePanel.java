/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import java.awt.BorderLayout;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.DisposablePanel;
import org.conservationmeasures.eam.dialogs.ObjectDataInputPanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiScrollPane;

abstract public class ObjectPoolTablePanel extends DisposablePanel implements ListSelectionListener, CommandExecutedListener
{
	public ObjectPoolTablePanel(Project projectToUse, int objectTypeToUse, ObjectPoolTableModel model)
	{
		super(new BorderLayout());
		project = projectToUse;
		objectType = objectTypeToUse;
		table = new ObjectPoolTable(model);
		table.addListSelectionListener(this);
		add(new UiScrollPane(table), BorderLayout.CENTER);
		project.addCommandExecutedListener(this);
	}

	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}


	public Goal getSelectedObject()
	{
		EAMObject[] selected = table.getSelectedObjects();
		if(selected.length == 0)
			return null;
		return (Goal)selected[0];
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		try
		{
			BaseId selectedId = BaseId.INVALID;
			int row = table.getSelectedRow();
			if(row >= 0)
			{
				EAMObject selectedObject = table.getObjectPoolTableModel().getObjectFromRow(row);
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
			table.updateTableAfterCommand((CommandSetObjectData)event.getCommand());
	}

	public void commandUndone(CommandExecutedEvent event)
	{
		if(event.getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			table.updateTableAfterUndo((CommandSetObjectData)event.getCommand());
	}

	public void commandFailed(Command command, CommandFailedException e)
	{
	}
	
	Project project;
	int objectType;
	ObjectPoolTable table;
	ObjectDataInputPanel propertiesPanel;
}
