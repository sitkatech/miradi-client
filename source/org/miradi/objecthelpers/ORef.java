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
package org.miradi.objecthelpers;

import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.utils.EnhancedJsonObject;

public class ORef implements Comparable<ORef>
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
	
	public int compareTo(ORef otherRef)
	{
		return toString().compareTo(otherRef.toString());
	}
	
	public void ensureType(int type)
	{
		if (getObjectType() != type)
			throw new RuntimeException("wrong type: " + type + " for ref type: " + getObjectType());
	}
	
	public static ORef createInvalidWithType(int type)
	{
		return new ORef(type, BaseId.INVALID);
	}
	
	public static ORef INVALID = createInvalidWithType(ObjectType.FAKE);
	
	private static final String TAG_OBJECT_TYPE = "ObjectType";
	private static final String TAG_OBJECT_ID = "ObjectId";
	
	private int objectType;
	private BaseId objectId;
}
