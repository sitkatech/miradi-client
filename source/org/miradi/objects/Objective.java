/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.ids.BaseId;
import org.miradi.ids.ObjectiveId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.RelevancyOverrideSetData;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;


public class Objective extends Desire
{
	public Objective(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, new ObjectiveId(id.asInt()));
	}
	
	public Objective(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ObjectiveId(idAsInt), json);
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
		ORefList defaultRelevantRefList = getUpstreamNonDraftStrategies();
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
	
	public static boolean is(int nodeType)
	{
		return nodeType == getObjectType();
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}

	public void clear()
	{
		super.clear();
		relevantIndicatorOverrides = new RelevancyOverrideSetData(TAG_RELEVANT_INDICATOR_SET);
		relevantStrategyOverrides = new RelevancyOverrideSetData(TAG_RELEVANT_STRATEGY_SET);
		
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
	public static final String PSEUDO_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
	public static final String PSEUDO_RELEVANT_STRATEGY_REFS = "PseudoRelevantStrategyRefs";
	
	private RelevancyOverrideSetData relevantStrategyOverrides;
	private RelevancyOverrideSetData relevantIndicatorOverrides;
	private PseudoORefListData relevantIndicatorRefs;
	private PseudoORefListData relevantStrategyRefs;
}
