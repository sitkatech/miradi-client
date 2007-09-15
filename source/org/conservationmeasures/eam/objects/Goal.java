/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Goal extends Desire 
{
	public Goal(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
	}
	
	public Goal(BaseId idToUse)
	{
		super(idToUse);
	}
	
	public Goal(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public Goal(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
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
		return ObjectType.GOAL;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	// TODO: Consider combining with Objective.getUpstreamDownstreamIndicators
	public ORefList getUpstreamObjectives()
	{
		ORefList objectiveRefs = new ORefList();
		
		Factor[] upstreamFactors = getUpstreamFactors();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			IdList objectiveIds = upstreamFactors[i].getObjectives();
			for(int idIndex = 0; idIndex < objectiveIds.size(); ++idIndex)
			{
				BaseId objectiveId = objectiveIds.get(idIndex);
				if(objectiveId.isInvalid())
					continue;

				objectiveRefs.add(new ORef(Objective.getObjectType(), objectiveId));
			}
		}
		return objectiveRefs;
	}
	
	//FIXME: all OBJECT_NAME reference becaseu the are used in displayes shold be static methods that call EAM.text
	public static final String OBJECT_NAME = "Goal";
}
