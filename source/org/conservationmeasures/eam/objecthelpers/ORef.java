/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ORef
{
	public ORef(int objectTypeToUse, BaseId objectIdToUse)
	{
		objectType = objectTypeToUse;
		objectId = objectIdToUse;
	}
	
	public ORef(EnhancedJsonObject json)
	{
		if (! json.has(TAG_OBJECT_TYPE))
		{
			objectType = ObjectType.FAKE;
			objectId = null;
			return;
		}
			
		objectType = json.getInt(TAG_OBJECT_TYPE);
		objectId = json.getId(TAG_OBJECT_ID);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		if (objectId != null)
		{
			json.put(TAG_OBJECT_TYPE, objectType);
			json.putId(TAG_OBJECT_ID, objectId);
		}
		return json;
	}
	
	public BaseId getObjectId()
	{
		return objectId;
	}
	
	public int getObjectType()
	{
		return objectType;
	}
	
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof ORef))
			return false;
		
		ORef other = (ORef)rawOther;
		if (other.getObjectId().equals(objectId) && other.getObjectType() == objectType)
			return true;
		
		return false;
	}
	
	public int hashCode()
	{
		return toJson().hashCode();
	}
	
	public String toString()
	{
		if (objectId == null)
			return "";
		return toJson().toString();
	}
	
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
	
	private int objectType;
	private BaseId objectId;
	
}
