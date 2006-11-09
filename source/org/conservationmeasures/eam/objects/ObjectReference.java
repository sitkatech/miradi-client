/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

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
	
	private int objectType;
	private BaseId objectId;
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
}
