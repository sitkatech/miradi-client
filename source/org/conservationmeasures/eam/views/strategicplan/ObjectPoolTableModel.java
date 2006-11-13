/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.strategicplan;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectPoolTableModel extends AbstractTableModel
{
	public ObjectPoolTableModel(Project projectToUse, int listedItemType)
	{
		project = projectToUse;
		rowObjectType = listedItemType;
		rowObjectPool = projectToUse.getPool(rowObjectType).getIdList();
	}
	
	public int getColumnCount()
	{
		return 1;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, columnTag);
	}

	public int getRowCount()
	{
		return rowObjectPool.size();
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
			return rowObject.toString();
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
			BaseId rowObjectId = rowObjectPool.get(row);
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

	Project project;
	int rowObjectType;
	IdList rowObjectPool;
	String columnTag = "Goal";
}
