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

		rowObjectIds = getLatestIdListFromProject();
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

	private EAMObject getContainingObject()
	{
		return project.findObject(containingObjectType, containingObjectId);
	}

	public IdList getLatestIdListFromProject()
	{
		try
		{
			return new IdList(getContainingObject().getData(tagOfIdList));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException();
		}
	}
	
	int containingObjectType;
	BaseId containingObjectId;
	String tagOfIdList;
	String[] columnTags;
}
