/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.text.ParseException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectListTableModel extends ObjectTableModel
{
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String tableColumnTag)
	{
		this(projectToUse, objectType, objectId, idListFieldTag, listedItemType, new String[] {tableColumnTag});
	}
	
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String[] tableColumnTags)
	{
		super(projectToUse, listedItemType);
		containingObjectType = objectType;
		containingObjectId = objectId;
		tagOfIdList = idListFieldTag;
		columnTags = tableColumnTags;
	}
	
	public EAMObject getObjectFromRow(int row) throws RuntimeException
	{
		try
		{
			BaseId rowObjectId = getIdList().get(row);
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

	public String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(rowObjectType, getColumnTag(column));
	}

	public int getRowCount()
	{
		try
		{
			return getIdList().size();
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			throw new RuntimeException("Parse error reading IdList " + tagOfIdList);
		}
	}
	
	public int getContainingObjectType()
	{
		return containingObjectType;
	}
	
	public BaseId getContainingObjectId()
	{
		return containingObjectId;
	}
	
	public String getFieldTag()
	{
		return tagOfIdList;
	}

	private IdList getIdList() throws ParseException
	{
		return new IdList(getContainingObject().getData(tagOfIdList));
	}

	private EAMObject getContainingObject()
	{
		return project.findObject(containingObjectType, containingObjectId);
	}

	int containingObjectType;
	BaseId containingObjectId;
	String tagOfIdList;
	String[] columnTags;
}
