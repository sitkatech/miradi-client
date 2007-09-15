/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.ProjectChainObject;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()));
	}
	
	
	public Objective(BaseId id)
	{
		super(new ObjectiveId(id.asInt()));
	}
	
	public Objective(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ObjectiveId(idAsInt), json);
	}
	
	public Objective(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new ObjectiveId(idAsInt), json);
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
		return ObjectType.OBJECTIVE;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public ORefList getRelatedStrategies()
	{
		return objectManager.getStrategyRefsUpstreamOfObjective(getRef());
	}
	
	// TODO: Consider combining with Goal.getUpstreamObjectives
	public ORefList getUpstreamAndDownstreamIndicators()
	{
		ORefList indicatorRefs = new ORefList();
		
		Factor owner = getDirectOrIndirectOwningFactor();
		if(owner == null)
			return new ORefList();
		ProjectChainObject chainObject = new ProjectChainObject();
		chainObject.buildUpstreamDownstreamChain(owner);
		Factor[] upstreamDownstreamFactors = chainObject.getFactorsArray();
		for(int i = 0; i < upstreamDownstreamFactors.length; ++i)
		{
			IdList indicatorIds = upstreamDownstreamFactors[i].getDirectOrIndirectIndicators();
			for(int idIndex = 0; idIndex < indicatorIds.size(); ++idIndex)
			{
				BaseId indicatorId = indicatorIds.get(idIndex);
				if(indicatorId.isInvalid())
					continue;
				indicatorRefs.add(new ORef(Indicator.getObjectType(), indicatorId));
			}
		}
		
		return indicatorRefs;
		
	}

	public static final String OBJECT_NAME = "Objective";	
}
