/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.diagram.factortypes.FactorTypeThreatReductionResult;
import org.conservationmeasures.eam.ids.FactorId;
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
	
	public ThreatReductionResult(FactorId idToUse)
	{
		super(idToUse, new FactorTypeThreatReductionResult());
		clear();
	}
	
	public ThreatReductionResult(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_THREAT_REDUCTION_RESULT, json);
	}
	
	public ThreatReductionResult(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_THREAT_REDUCTION_RESULT, json);
	}

	public int getType()
	{
		return getObjectType();
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
				list.addAll(new ORefList(objectType, getObjectives()));
				break;
		}
		return list;
	}

	public static final String OBJECT_NAME = "Threat Reduction Result";
}
