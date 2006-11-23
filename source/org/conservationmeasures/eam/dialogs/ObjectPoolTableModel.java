/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectPoolTableModel extends ObjectTableModel
{
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, String columnTagToUse)
	{
		this(projectToUse, listedItemType, projectToUse.getPool(listedItemType).getIdList(), columnTagToUse);
	}
	
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, IdList listToUse, String columnTagToUse)
	{
		super(projectToUse, listedItemType);
		rowObjectIds = listToUse;
		columnTag = columnTagToUse;
	}
	
	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnTag(int column)
	{
		return columnTag;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, getColumnTag(column));
	}

	public int getRowCount()
	{
		return rowObjectIds.size();
	}
	
	public int getRowObjectType()
	{
		return rowObjectType;
	}
	
	public Object getValueAt(int row, int column)
	{
		try
		{
			return project.getObjectData(getRowObjectType(), getObjectFromRow(row).getId(), getColumnTag(column));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "(Error)";
		}
	}

	public EAMObject getObjectFromRow(int row) throws RuntimeException
	{
		try
		{
			BaseId rowObjectId = rowObjectIds.get(row);
			EAMObject rowObject = project.findObject(rowObjectType, rowObjectId);
			return rowObject;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException("TeamModel.getObjectFromRow error");
		}
	}
	
	public int findRowObject(BaseId id)
	{
		for(int row = 0; row < getRowCount(); ++row)
		{
			if(getObjectFromRow(row).getId().equals(id))
				return row;
		}
		
		return -1;
	}

	public void rowsWereAddedOrRemoved()
	{
		rowObjectIds = project.getPool(getRowObjectType()).getIdList();
		fireTableDataChanged();
	}
	
	IdList rowObjectIds;
	String columnTag;
}
