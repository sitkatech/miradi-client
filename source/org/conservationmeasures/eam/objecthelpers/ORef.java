/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		return objectId.hashCode();
	}
	
	public String toString()
	{
		if (objectId == null)
			return "";
		return toJson().toString();
	}
	
	public static ORef INVALID = new ORef(ObjectType.FAKE, BaseId.INVALID);
	
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
	
	private int objectType;
	private BaseId objectId;
	
}
