/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objecthelpers;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class ObjectReference
{
	public ObjectReference(int objectTypeToUse, BaseId objectIdToUse)
	{
		objectType = objectTypeToUse;
		objectId = objectIdToUse;
	}
	
	public ObjectReference(EnhancedJsonObject json)
	{
		objectType = json.getInt(TAG_OBJECT_TYPE);
		objectId = json.getId(TAG_OBJECT_ID);
	}
	
	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = new EnhancedJsonObject();
		json.put(TAG_OBJECT_TYPE, objectType);
		json.putId(TAG_OBJECT_ID, objectId);
		
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
		if (! (rawOther instanceof ObjectReference))
			return false;
		
		ObjectReference other = (ObjectReference)rawOther;
		if (other.getObjectId() == objectId && other.getObjectType() == objectType)
			return true;
		
		return false;
	}
	
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
	
	private int objectType;
	private BaseId objectId;
	
}
