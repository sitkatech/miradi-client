/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objecthelpers;

import java.io.IOException;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.martus.util.UnicodeWriter;

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
			objectType = INVALID.getObjectType();
			objectId = INVALID.getObjectId();
			return;
		}
			
		objectType = json.getInt(TAG_OBJECT_TYPE);
		objectId = json.getId(TAG_OBJECT_ID);
	}
	
	public static ORef createFromString(String orefAsJsonString)
	{
		try
		{
			return new ORef(new EnhancedJsonObject(orefAsJsonString));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return INVALID;
		}
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
	
	public boolean isInvalid()
	{
		if(getObjectId() == null)
			return true;
		
		return getObjectId().isInvalid();
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
		if (isInvalid())
			return "";
		return toJson().toString();
	}
	
	public void toXml(UnicodeWriter out) throws IOException
	{
		out.write(toXmlString());
	}

	public String toXmlString()
	{
		return getObjectType() + "." + getObjectId();
	}
	
	public static ORef INVALID = new ORef(ObjectType.FAKE, BaseId.INVALID);
	
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
	
	private int objectType;
	private BaseId objectId;
}
