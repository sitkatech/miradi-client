/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiTable;

public class ObjectTable extends UiTable implements ObjectPicker
{
	public ObjectTable(ObjectTableModel modelToUse)
	{
		super(modelToUse);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectTableModel getObjectTableModel()
	{
		return (ObjectTableModel)getModel();
	}
	
	public EAMObject[] getSelectedObjects()
	{
		int[] rows = getSelectedRows();
		EAMObject[] objects = new EAMObject[rows.length];
		for(int i = 0; i < objects.length; ++i)
			objects[i] = getObjectFromRow(rows[i]);
		return objects;
	}

	private EAMObject getObjectFromRow(int row)
	{
		return getObjectTableModel().getObjectFromRow(row);
	}
	
	int findRowObject(BaseId id)
	{
		return getObjectTableModel().findRowObject(id);
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateTableAfterUndo(CommandSetObjectData cmd)
	{
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateIfRowObjectWasModified(int type, BaseId id)
	{
		if(type != getObjectTableModel().getRowObjectType())
			return;
		
		int row = findRowObject(id);
		if(row >= 0)
			getObjectTableModel().fireTableRowsUpdated(row, row);
	}
	
	void updateTableAfterObjectCreated(ORef createdRef)
	{
		
	}
	
	void updateTableAfterObjectDeleted(ORef deletedRef)
	{
		
	}

}
