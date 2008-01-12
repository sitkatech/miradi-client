/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.base;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ObjectData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectListTableModel extends ObjectTableModel
{
	public ObjectListTableModel(Project projectToUse, ORef containingRefToUse, String idListFieldTag, int listedItemType, String[] columnTags)
	{
		super(projectToUse, listedItemType, columnTags);
		containingRef = containingRefToUse;
		tagOfList = idListFieldTag;

		setRowObjectRefs(getLatestRefListFromProject());
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
