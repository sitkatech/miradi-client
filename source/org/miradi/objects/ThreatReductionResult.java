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
package org.miradi.objects;

import org.miradi.diagram.factortypes.FactorTypeThreatReductionResult;
import org.miradi.ids.FactorId;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.utils.EnhancedJsonObject;

public class ThreatReductionResult extends Factor
{
	public ThreatReductionResult(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, new FactorTypeThreatReductionResult());
		clear();
	}
	
	public ThreatReductionResult(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_THREAT_REDUCTION_RESULT, json);
	}
	
	public int getType()
	{
		return getObjectType();
	}
	
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.THREAT_REDUCTION_RESULT;
	}
	
	public boolean isThreatReductionResult()
	{
		return true;
	}
	
	public static boolean canOwnThisType(int type)
	{
		if (Factor.canOwnThisType(type))
			return true;
		
		switch(type)
		{
			case ObjectType.OBJECTIVE:
				return true;
				
			default:
				return false;
		}
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectiveIds()));
				break;
		}
		return list;
	}
	
	public String getRelatedDirectThreatRefAsString()
	{
		return relatedDirectThreat.get();
	}
	
	public ORef getRelatedThreatRef()
	{
		if (relatedDirectThreat.get().length() == 0)
			return ORef.INVALID;
		
		return ORef.createFromString(relatedDirectThreat.get());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	void clear()
	{
		super.clear();
		
		relatedDirectThreat = new StringData(TAG_RELATED_DIRECT_THREAT_REF);
		
		addField(TAG_RELATED_DIRECT_THREAT_REF, relatedDirectThreat);
	}

	public static final String TAG_RELATED_DIRECT_THREAT_REF = "RelatedDirectThreatRef";
	public static final String OBJECT_NAME = "ThreatReductionResult";
	
	StringData relatedDirectThreat;
}
