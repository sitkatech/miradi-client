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
	public ObjectListTableModel(Project projectToUse, ORef containingRef, String idListFieldTag, int listedItemType, String[] columnTags)
	{
		this(projectToUse, containingRef.getObjectType(), containingRef.getObjectId(), idListFieldTag, listedItemType, columnTags);
	}
	
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String tableColumnTag)
	{
		this(projectToUse, objectType, objectId, idListFieldTag, listedItemType, new String[] {tableColumnTag});
	}
	
	public ObjectListTableModel(Project projectToUse, int objectType, BaseId objectId, 
			String idListFieldTag, int listedItemType, String[] tableColumnTags)
	{
		super(projectToUse, listedItemType, tableColumnTags);
		containingObjectType = objectType;
		containingObjectId = objectId;
		tagOfIdList = idListFieldTag;

		setRowObjectRefs(getLatestRefListFromProject());
	}
	
	public ORef getContainingRef()
	{
		return new ORef(getContainingObjectType(), getContainingObjectId());
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

	public BaseObject getContainingObject()
	{
		return project.findObject(containingObjectType, containingObjectId);
	}

	public ORefList getLatestRefListFromProject()
	{
		try
		{
			ObjectData objectData = getContainingObject().getField(tagOfIdList);
			if (objectData.isIdListData()) 
				return new ORefList(getRowObjectType(), new IdList(getContainingObject().getData(tagOfIdList)));
			
			return new ORefList(getContainingObject().getData(tagOfIdList));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new RuntimeException();
		}
	}
	
	private int containingObjectType;
	private BaseId containingObjectId;
	private String tagOfIdList;
}
