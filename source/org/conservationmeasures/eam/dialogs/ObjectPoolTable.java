/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;

public class ObjectPoolTable extends ObjectTable
{
	public ObjectPoolTable(ObjectPoolTableModel modelToUse)
	{
		super(modelToUse);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectPoolTableModel getObjectPoolTableModel()
	{
		return (ObjectPoolTableModel)getModel();
	}
	
	void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		super.updateTableAfterCommand(cmd);
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateTableAfterUndo(CommandSetObjectData cmd)
	{
		super.updateTableAfterUndo(cmd);
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
	}
	
	void updateIfRowObjectWasModified(int type, BaseId id)
	{
		if(type != getObjectPoolTableModel().getRowObjectType())
			return;
		
		int row = findRowObject(id);
		if(row >= 0)
			getObjectPoolTableModel().fireTableRowsUpdated(row, row);
	}
	
	

}
