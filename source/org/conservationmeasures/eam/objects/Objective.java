/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSetData;
import org.conservationmeasures.eam.project.ObjectManager;
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
	
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS))
			return getIndicatorsOnSameFactor().toString();
	
		return super.getPseudoData(fieldTag);
	}
	
	public ORefList getUpstreamNonDraftStrategies(DiagramObject diagram)
	{
		ORefList nonDraftStrategyRefs = new ORefList();
		Factor[] upstreamFactors = getUpstreamFactors(diagram);
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			if(factor.isStrategy() && !factor.isStatusDraft())
				nonDraftStrategyRefs.add(factor.getRef());
		}

		return nonDraftStrategyRefs;
	}
	
	public ORefList getRelevantIndicatorRefs()
	{
		return getIndicatorsOnSameFactor();
	}
	
	public void clear()
	{
		super.clear();
		relevantIndicators = new RelevancyOverrideSetData();
		defaultRelevantIndicatorRefs = new ORefListData();
		
		addField(TAG_RELEVANT_INDICATOR_SET, relevantIndicators);
		addField(PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS, defaultRelevantIndicatorRefs);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String PSEUDO_DEFAULT_RELEVANT_INDICATOR_REFS = "PseudoDefaultRelevantIndicatorRefs";
	
	private RelevancyOverrideSetData relevantIndicators;
	private ORefListData defaultRelevantIndicatorRefs;
}
