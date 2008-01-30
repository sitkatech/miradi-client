/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeThreatReductionResult;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

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
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_OBJECTIVE_IDS);
		return set;
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
				list.addAll(new ORefList(objectType, getObjectives()));
				break;
		}
		return list;
	}
	
	public String getRelatedDirectThreatRefAsString()
	{
		return relatedDirectThreat.get();
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
