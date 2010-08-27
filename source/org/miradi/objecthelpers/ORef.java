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

import org.miradi.ids.AccountingCodeId;
import org.miradi.ids.BaseId;
import org.miradi.ids.DiagramContentsId;
import org.miradi.ids.DiagramFactorId;
import org.miradi.ids.DiagramLinkId;
import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.ids.FundingSourceId;
import org.miradi.ids.GoalId;
import org.miradi.ids.IndicatorId;
import org.miradi.ids.KeyEcologicalAttributeId;
import org.miradi.ids.ObjectId;
import org.miradi.ids.ProjectResourceId;
import org.miradi.ids.ResourceAssignmentId;
import org.miradi.ids.TaskId;
import org.miradi.ids.ViewDataId;
import org.miradi.main.EAM;
import org.miradi.objects.Factor;
import org.miradi.utils.EnhancedJsonObject;

public class ORef implements Comparable<ORef>
{
	public ORef(int objectTypeToUse, BaseId objectIdToUse)
	{
		setTypeAndId(objectTypeToUse, objectIdToUse);
	}

	public ORef(EnhancedJsonObject json)
	{
		if (! json.has(TAG_OBJECT_TYPE))
		{
			objectType = INVALID.getObjectType();
			objectId = INVALID.getObjectId();
			return;
		}
			
		setTypeAndId(json.getInt(TAG_OBJECT_TYPE), json.getId(TAG_OBJECT_ID));
	}
	
	private void setTypeAndId(int objectTypeToUse, BaseId objectIdToUse)
	{
		Class<? extends BaseId> incomingClass = objectIdToUse.getClass();
		Class<BaseId> baseClass = BaseId.class;
		if(incomingClass.equals(baseClass))
			objectIdToUse = createTypedId(objectTypeToUse, objectIdToUse);
		objectType = objectTypeToUse;
		objectId = objectIdToUse;
	}
	
	private BaseId createTypedId(int objectTypeToUse, BaseId objectIdToUse)
	{
		int idToUse = objectIdToUse.asInt();
		switch(objectTypeToUse)
		{
			case ObjectType.CONCEPTUAL_MODEL_DIAGRAM:
				return new DiagramContentsId(idToUse);
			case ObjectType.DIAGRAM_FACTOR:
				return new DiagramFactorId(idToUse);
			case ObjectType.DIAGRAM_LINK:
				return new DiagramLinkId(idToUse);
			case ObjectType.ACCOUNTING_CODE:
				return new AccountingCodeId(idToUse);
			case ObjectType.FACTOR_LINK:
				return new FactorLinkId(idToUse);
			case ObjectType.FUNDING_SOURCE:
				return new FundingSourceId(idToUse);
			case ObjectType.GOAL:
				return new GoalId(idToUse);
			case ObjectType.INDICATOR:
				return new IndicatorId(idToUse);
			case ObjectType.KEY_ECOLOGICAL_ATTRIBUTE:
				return new KeyEcologicalAttributeId(idToUse);
			case ObjectType.OBJECTIVE:
				return new ObjectId(idToUse);
			case ObjectType.PROJECT_RESOURCE:
				return new ProjectResourceId(idToUse);
			case ObjectType.RESOURCE_ASSIGNMENT:
				return new ResourceAssignmentId(idToUse);
			case ObjectType.TASK:
				return new TaskId(idToUse);
			case ObjectType.VIEW_DATA:
				return new ViewDataId(idToUse);
		}
		
		if(Factor.isFactor(objectTypeToUse))
			return new FactorId(idToUse);
		
		return objectIdToUse;
	}

	public ORef(ORef ref)
	{
		this(ref.getObjectType(), ref.getObjectId());
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
	
	public boolean isValid()
	{
		return !isInvalid();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if (! (rawOther instanceof ORef))
			return false;
		
		ORef other = (ORef)rawOther;
		if (other.getObjectId().equals(objectId) && other.getObjectType() == objectType)
			return true;
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return objectId.hashCode();
	}
	
	@Override
	public String toString()
	{
		if (isInvalid())
			return "";
		return toJson().toString();
	}
	
	public int compareTo(ORef otherRef)
	{
		return toString().compareTo(otherRef.toString());
	}
	
	public void ensureExactType(int type)
	{
		if (getObjectType() != type)
			throw new RuntimeException("wrong type: " + type + " for ref type: " + getObjectType());
	}
	
	public void ensureTypeIfValid(int type)
	{
		if (isValid())
			ensureExactType(type);
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
