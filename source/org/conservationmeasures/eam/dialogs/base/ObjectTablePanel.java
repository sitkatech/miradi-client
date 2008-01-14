/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MouseAdapterDoubleClickDelegator;

public class ObjectTablePanel extends ObjectCollectionPanel implements ListSelectionListener
{
	public ObjectTablePanel(Project projectToUse, ObjectTable tableToUse)
	{
		super(projectToUse, tableToUse);
		table = tableToUse;
		table.addListSelectionListener(this);
	}
	
	public BaseObject getSelectedObject()
	{
		BaseObject[] selected = table.getSelectedObjects();
		if(selected.length == 0)
			return null;
		return selected[0];
	}
	
	public void valueChanged(ListSelectionEvent event)
	{
		updatePropertiesPanel();
	}

	private void updatePropertiesPanel()
	{
		if (getPropertiesPanel()==null)
			return;
		try
		{
			ORef oref = new ORef(ObjectType.FAKE,BaseId.INVALID);
			int[] row = table.getSelectedRows();
			if (row.length == 1 )
			{
				BaseObject selectedObject = table.getObjectTableModel().getObjectFromRow(row[0]);
				oref = selectedObject.getRef();
				
			}
			getPropertiesPanel().setObjectRef(oref);
			getPropertiesPanel().setFocusOnFirstField();

		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public void setPropertiesPanel(AbstractObjectDataInputPanel panel)
	{
		super.setPropertiesPanel(panel);
		selectFirstRow();
	}

	protected void selectFirstRow()
	{
		table.scrollToAndSelectRow(0);
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

	public void addDoubleClickAction(EAMAction action ) 
	{
		table.addMouseListener(new MouseAdapterDoubleClickDelegator(action)); 
	}
	
	public ObjectTable getTable()
	{
		return table;
	}
	
	ObjectTable table;
}
