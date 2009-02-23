/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.base;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;

public class ObjectTablePanel extends ObjectCollectionPanel implements ListSelectionListener
{
	public ObjectTablePanel(MainWindow mainWindowToUse, ObjectTable tableToUse)
	{
		super(mainWindowToUse, tableToUse);
		table = tableToUse;
		table.addListSelectionListener(this);

	}
	
	@Override
	public void dispose()
	{
		super.dispose();
		getTable().dispose();
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
			ORefList selectedRefs = new ORefList();
			ORefList[] selectedHierarchies = table.getSelectedHierarchies();
			if (selectedHierarchies.length == 1)
				selectedRefs = selectedHierarchies[0];

			getPropertiesPanel().setObjectRefs(selectedRefs);
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
			getPropertiesPanel().setFocusOnFirstField();
		}
		else if(event.getCommandName().equals(CommandDeleteObject.COMMAND_NAME))
		{
			CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
			table.updateTableAfterObjectDeleted(new ORef(cmd.getObjectType(), cmd.getObjectId()));
		}
	}

	public ObjectTable getTable()
	{
		return table;
	}
	
	ObjectTable table;
}
