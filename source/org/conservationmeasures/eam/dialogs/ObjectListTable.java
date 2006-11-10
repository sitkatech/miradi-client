/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.text.ParseException;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiTable;

public class ObjectListTable extends UiTable implements ObjectPicker
{
	public ObjectListTable(ObjectListTableModel modelToUse)
	{
		super(modelToUse);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectListTableModel getObjectListTableModel()
	{
		return (ObjectListTableModel)getModel();
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
		return getObjectListTableModel().getObjectFromRow(row);
	}
	
	private int findRowObject(BaseId id)
	{
		return getObjectListTableModel().findRowObject(id);
	}

	public void addListSelectionListener(ListSelectionListener listener)
	{
		getSelectionModel().addListSelectionListener(listener);
	}
	
	
	void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		String oldData = cmd.getPreviousDataValue();
		String newData = cmd.getDataValue();
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
		updateTableIfRowWasAddedOrDeleted(cmd.getObjectType(), cmd.getObjectId(), cmd.getFieldTag(), oldData, newData);
	}
	
	void updateTableAfterUndo(CommandSetObjectData cmd)
	{
		String oldData = cmd.getDataValue();
		String newData = cmd.getPreviousDataValue();
		updateIfRowObjectWasModified(cmd.getObjectType(), cmd.getObjectId());
		updateTableIfRowWasAddedOrDeleted(cmd.getObjectType(), cmd.getObjectId(), cmd.getFieldTag(), oldData, newData);
	}
	
	void updateIfRowObjectWasModified(int type, BaseId id)
	{
		if(type != getObjectListTableModel().getRowObjectType())
			return;
		
		int row = findRowObject(id);
		if(row >= 0)
			getObjectListTableModel().fireTableRowsUpdated(row, row);
	}
	
	void updateTableIfRowWasAddedOrDeleted(int type, BaseId id, String tag, String oldData, String newData)
	{
		if(type != getObjectListTableModel().getContainingObjectType())
			return;
		
		if(!id.equals(getObjectListTableModel().getContainingObjectId()))
			return;
		
		if(!tag.equals(getObjectListTableModel().getFieldTag()))
			return;
		
		int desiredSelectionRow = getSelectedRow();
		try
		{
			IdList oldList = new IdList(oldData);
			IdList newList = new IdList(newData);
			if(newList.size() > oldList.size())
				desiredSelectionRow = newList.size() - 1;
		}
		catch(ParseException nothingWeCanDoAboutIt)
		{
			EAM.logException(nothingWeCanDoAboutIt);
		}

		getObjectListTableModel().fireTableDataChanged();
		desiredSelectionRow = Math.min(desiredSelectionRow, getRowCount() - 1);
		if(desiredSelectionRow >= 0)
			setRowSelectionInterval(desiredSelectionRow, desiredSelectionRow);
	}
	

}