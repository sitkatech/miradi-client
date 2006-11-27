/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;

public class ObjectPoolTableModel extends ObjectTableModel
{
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, String[] columnTagsToUse)
	{
		this(projectToUse, listedItemType, projectToUse.getPool(listedItemType).getIdList(), columnTagsToUse);
	}
	
	public ObjectPoolTableModel(Project projectToUse, int listedItemType, IdList listToUse, String[] columnTagsToUse)
	{
		super(projectToUse, listedItemType);
		rowObjectIds = listToUse;
		columnTags = columnTagsToUse;
	}
	
	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnTag(int column)
	{
		return columnTags[column];
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, getColumnTag(column));
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

	public IdList getLatestIdListFromProject()
	{
		return project.getPool(getRowObjectType()).getIdList();
	}
	
	String[] columnTags;
}
