/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.text.ParseException;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

abstract public class ObjectTableModel extends AbstractTableModel
{
	public ObjectTableModel(Project projectToUse, int listedItemType)
	{
		project = projectToUse;
		rowObjectType = listedItemType;
	}
	
	abstract public EAMObject getObjectFromRow(int row) throws RuntimeException;
	abstract public int findRowObject(BaseId id);
	abstract public String getColumnTag(int column);
	abstract public IdList getIdList() throws ParseException;
	
	// TODO: Actually implement this, probably by pulling the IdList up into this class,
	// and detecting when it gets modified externally (e.g. create/delete object)
	void setNewRowOrder(Integer[] existingRowIndexesInNewOrder)
	{
		EAM.logDebug("ObjectTableModel: Sort requested: ");
		for(int i = 0; i < existingRowIndexesInNewOrder.length; ++i)
			EAM.logDebug("   " + existingRowIndexesInNewOrder[i]);
	}

	public int getRowObjectType()
	{
		return rowObjectType;
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			EAMObject rowObject;
			rowObject = getObjectFromRow(row);
			return rowObject.getData(getColumnTag(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "(Error)";
		}
	}


	Project project;
	int rowObjectType;


}
