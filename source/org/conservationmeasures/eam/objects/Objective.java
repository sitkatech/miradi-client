/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.ObjectiveId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ORefSet;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverride;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSet;
import org.conservationmeasures.eam.objecthelpers.RelevancyOverrideSetData;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
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
		if (fieldTag.equals(PSEUDO_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();
	
		if (fieldTag.equals(PSEUDO_RELEVANT_STRATEGY_REFS))
			return getRelevantStrategyRefsAsString();
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getRelevantIndicatorRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantIndicatorRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	private String getRelevantStrategyRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantStrategyRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public ORefList getUpstreamNonDraftStrategies()
	{
		ORefList nonDraftStrategyRefs = new ORefList();
		Factor[] upstreamFactors = getUpstreamFactors();
		for(int i = 0; i < upstreamFactors.length; ++i)
		{
			Factor factor = upstreamFactors[i];
			if(factor.isStrategy() && !factor.isStatusDraft())
				nonDraftStrategyRefs.add(factor.getRef());
		}

		return nonDraftStrategyRefs;
	}
	
	public RelevancyOverrideSet getCalculatedRelevantIndicatorOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getIndicatorsOnSameFactor();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));	
	
		return relevantOverrides;
	}
	
	public RelevancyOverrideSet getCalculatedRelevantStrategyrOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		//FIXME use updownstream strats
		ORefList defaultRelevantRefList = getIndicatorsOnSameFactor();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));	
	
		return relevantOverrides;
	}

	public RelevancyOverrideSet computeRelevancyOverrides(ORefList refList1, ORefList refList2, boolean relevancyValue)
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList overrideRefs = ORefList.subtract(refList1, refList2);
		for (int i = 0; i < overrideRefs.size(); ++i)
		{
			RelevancyOverride thisOverride = new RelevancyOverride(overrideRefs.get(i), relevancyValue);
			relevantOverrides.add(thisOverride);
		}
		
		return relevantOverrides;
	}
	
	public ORefList getRelevantIndicatorRefList() throws Exception
	{
		ORefSet relevantRefList = indicatorsOnSameFactorAsRefSet();
		RelevancyOverrideSet relevantOverrides = relevantIndicatorOverrides.getRawRelevancyOverrideSet();

		return calculateRefList(relevantRefList, relevantOverrides);
	}

	public ORefList getRelevantStrategyRefList() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getUpstreamNonDraftStrategies());
		RelevancyOverrideSet relevantOverrides = relevantStrategyOverrides.getRawRelevancyOverrideSet();

		return calculateRefList(relevantRefList, relevantOverrides);
	}
	
	private ORefList calculateRefList(ORefSet relevantRefList, RelevancyOverrideSet relevantOverrides)
	{
		for(RelevancyOverride override : relevantOverrides)
		{
			if (override.isOverride())
				relevantRefList.add(override.getRef());
			else
				relevantRefList.remove(override.getRef());
		}
		return new ORefList(relevantRefList);
	}
		
	public static Objective find(ObjectManager objectManager, ORef objectiveRef)
	{
		return (Objective) objectManager.findObject(objectiveRef);
	}
	
	public static Objective find(Project project, ORef objectiveRef)
	{
		return find(project.getObjectManager(), objectiveRef);
	}
	
	public void clear()
	{
		super.clear();
		relevantIndicatorOverrides = new RelevancyOverrideSetData();
		relevantStrategyOverrides = new RelevancyOverrideSetData();
		
		relevantIndicatorRefs = new PseudoORefListData(PSEUDO_RELEVANT_INDICATOR_REFS);
		relevantStrategyRefs = new PseudoORefListData(PSEUDO_RELEVANT_STRATEGY_REFS);
		
		addField(TAG_RELEVANT_INDICATOR_SET, relevantIndicatorOverrides);
		addField(TAG_RELEVANT_STRATEGY_SET, relevantStrategyOverrides);
		addField(PSEUDO_RELEVANT_INDICATOR_REFS, relevantIndicatorRefs);
		addField(PSEUDO_RELEVANT_STRATEGY_REFS, relevantStrategyRefs);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String TAG_RELEVANT_STRATEGY_SET = "RelevantStrategySet";
	public static final String PSEUDO_RELEVANT_INDICATOR_REFS = "PseudoDefaultRelevantIndicatorRefs";
	public static final String PSEUDO_RELEVANT_STRATEGY_REFS = "PseudoDefaultRelevantStrategyRefs";
	
	private RelevancyOverrideSetData relevantStrategyOverrides;
	private RelevancyOverrideSetData relevantIndicatorOverrides;
	private PseudoORefListData relevantIndicatorRefs;
	private PseudoORefListData relevantStrategyRefs;
}
