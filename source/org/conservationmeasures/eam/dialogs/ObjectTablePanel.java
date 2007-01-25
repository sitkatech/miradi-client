/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;

import javax.swing.JPanel;
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

import com.jhlabs.awt.GridLayoutPlus;

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
		GridLayoutPlus layout = new GridLayoutPlus(0, 1);
		buttons = new JPanel(layout);
		add(buttons, BorderLayout.AFTER_LINE_ENDS);
		project.addCommandExecutedListener(this);
		setFocusCycleRoot(true);
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
			propertiesPanel.setFocusOnFirstField();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setPropertiesPanel(ObjectDataInputPanel panel)
	{
		propertiesPanel = panel;
		selectFirstRow();
	}

	private void selectFirstRow()
	{
		if(table.getRowCount()>0)
			table.setRowSelectionInterval(0, 0);
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
	JPanel buttons;

}
