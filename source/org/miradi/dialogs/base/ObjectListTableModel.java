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

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.ObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

public class ObjectListTableModel extends ObjectTableModel
{
	public ObjectListTableModel(Project projectToUse, ORef containingRefToUse, String listFieldTag, int listedItemType, String[] columnTags)
	{
		this(projectToUse, listFieldTag, listedItemType, columnTags);
		
		containingRef = containingRefToUse;

		setRowObjectRefs(getLatestRefListFromProject());
	}

	public ObjectListTableModel(Project projectToUse, String idListFieldTag, int listedItemType, String[] columnTags)
	{
		super(projectToUse, listedItemType, columnTags);
		
		tagOfList = idListFieldTag;
	}
	
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String tableColumnTag)
	{
		this(projectToUse, new ORef(objectType, objectId), idListFieldTag, listedItemType, new String[] {tableColumnTag});
	}
	
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String[] tableColumnTags)
	{
		this(projectToUse, new ORef(objectType, objectId), idListFieldTag, listedItemType, tableColumnTags);
	}
	
	public ORef getContainingRef()
	{
		return new ORef(getContainingObjectType(), getContainingObjectId());
	}
	
	public int getContainingObjectType()
	{
		return containingRef.getObjectType();
	}
	
	public BaseId getContainingObjectId()
	{
		return containingRef.getObjectId();
	}
	
	public String getFieldTag()
	{
		return tagOfList;
	}

	public BaseObject getContainingObject()
	{
		return project.findObject(containingRef);
	}

	public ORefList getLatestRefListFromProject()
	{
		try
		{
			if(containingRef.isInvalid())
				return new ORefList();
				
			ObjectData objectData = getContainingObject().getField(tagOfList);
			String containedDataAsString = getContainingObject().getData(tagOfList);
			if (objectData.isIdListData()) 
				return new ORefList(getRowObjectType(), new IdList(getRowObjectType(), containedDataAsString));
			
			return new ORefList(containedDataAsString);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException(e);
		}
	}
	
	private String tagOfList;
	private ORef containingRef;
}
