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
import org.miradi.objectdata.ORefListData;
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
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return ProgressPercent.getObjectType();
				
		return super.getAnnotationType(tag);
	}
	
	public boolean isRelevancyOverrideSet(String tag)
	{
		if (tag.equals(TAG_RELEVANT_INDICATOR_SET))
			return true;
		
		if (tag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		return false;
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return true;
				
		return super.isRefList(tag);
	}

	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();
	
		if (fieldTag.equals(PSEUDO_RELEVANT_STRATEGY_ACTIVITY_REFS))
			return getRelevantStrategyActivityRefsAsString();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return getLatestProgressPercentComplete();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS))
			return getLatestProgressPercentDetails();
		
		return super.getPseudoData(fieldTag);
	}
	
	
	private String getLatestProgressPercentDetails()
	{
		ProgressPercent latestProgressPercent = getLatestProgressPercent();
		if(latestProgressPercent == null)
			return "";
		return latestProgressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE_NOTES);
	}

	private String getLatestProgressPercentComplete()
	{
		ProgressPercent latestProgressPercent = getLatestProgressPercent();
		if(latestProgressPercent == null)
			return "";
		return latestProgressPercent.getData(ProgressPercent.TAG_PERCENT_COMPLETE);
	}
	
	private ProgressPercent getLatestProgressPercent()
	{
		return (ProgressPercent) getLatestObject(getObjectManager(), progressPercentRefs.getORefList(), ProgressPercent.TAG_DATE);
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
	
	private String getRelevantStrategyActivityRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantStrategyAndActivityRefs();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	private ORefList getDirectlyUpstreamNonDraftStrategies()
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();
		if(owningFactor == null)
			return new ORefList();
		
		ORefList nonDraftStrategyRefs = new ORefList();
		if (isNonDraftStrategy(owningFactor))
			nonDraftStrategyRefs.add(owningFactor.getRef());
		
		ORefList relatedFactorLinkRefs = owningFactor.findObjectsThatReferToUs(FactorLink.getObjectType());
		for (int index = 0; index < relatedFactorLinkRefs.size(); ++index)
		{
			FactorLink relatedFactorLink = FactorLink.find(getProject(), relatedFactorLinkRefs.get(index));
			Factor fromFactor = relatedFactorLink.getFromFactor();
			if(isNonDraftStrategy(fromFactor))
				nonDraftStrategyRefs.add(fromFactor.getRef());
		}
		
		return nonDraftStrategyRefs;
	}

	private boolean isNonDraftStrategy(Factor factor)
	{
		return factor.isStrategy() && !factor.isStatusDraft();
	}
	
	public RelevancyOverrideSet getCalculatedRelevantIndicatorOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getIndicatorsOnSameFactor();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));	
	
		return relevantOverrides;
	}
	
	public RelevancyOverrideSet getCalculatedRelevantStrategyActivityOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getDirectlyUpstreamNonDraftStrategies();
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
	
	public ORefSet getAllIndicatorRefsFromRelevancyOverrides() throws Exception
	{
		ORefSet rawRelevantIndicatorOverrideList = new ORefSet();
		RelevancyOverrideSet relevantOverrides = relevantIndicatorOverrides.getRawRelevancyOverrideSet();
		for(RelevancyOverride relevancyOverride : relevantOverrides)
		{
			rawRelevantIndicatorOverrideList.add(relevancyOverride.getRef());
		}
		
		return rawRelevantIndicatorOverrideList;
	}

	public ORefList getRelevantStrategyAndActivityRefs() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getDirectlyUpstreamNonDraftStrategies());
		RelevancyOverrideSet relevantOverrides = getStrategyActivityRelevancyOverrideSet();

		return calculateRefList(relevantRefList, relevantOverrides);
	}

	public RelevancyOverrideSet getStrategyActivityRelevancyOverrideSet()
	{
		return relevantStrategyActivityOverrides.getRawRelevancyOverrideSet();
	}
	
	private ORefList calculateRefList(ORefSet relevantRefList, RelevancyOverrideSet relevantOverrides)
	{
		for(RelevancyOverride override : relevantOverrides)
		{
			if (getProject().findObject(override.getRef()) == null)
				continue;
			
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
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
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
		relevantStrategyActivityOverrides = new RelevancyOverrideSetData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
		progressPercentRefs = new ORefListData(TAG_PROGRESS_PERCENT_REFS);
		
		relevantIndicatorRefs = new PseudoORefListData(PSEUDO_RELEVANT_INDICATOR_REFS);
		relevantStrategyRefs = new PseudoORefListData(PSEUDO_RELEVANT_STRATEGY_ACTIVITY_REFS);
		latestProgressPercentComplete = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE);
		latestProgressPercentDetails = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS);
		
		addField(TAG_RELEVANT_INDICATOR_SET, relevantIndicatorOverrides);
		addField(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantStrategyActivityOverrides);
		addField(TAG_PROGRESS_PERCENT_REFS, progressPercentRefs);
		
		addField(PSEUDO_RELEVANT_INDICATOR_REFS, relevantIndicatorRefs);
		addField(PSEUDO_RELEVANT_STRATEGY_ACTIVITY_REFS, relevantStrategyRefs);
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE, latestProgressPercentComplete);
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS, latestProgressPercentDetails);
	}
	
	public static final String OBJECT_NAME = "Objective";
	
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";
	public static final String TAG_PROGRESS_PERCENT_REFS = "ProgressPrecentRefs";
	
	public static final String PSEUDO_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
	public static final String PSEUDO_RELEVANT_STRATEGY_ACTIVITY_REFS = "PseudoRelevantStrategyRefs";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE = "PseudoLatestProgressPercentComplete";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS = "PseudoLatestProgressPercentDetails";
	
	private RelevancyOverrideSetData relevantStrategyActivityOverrides;
	private RelevancyOverrideSetData relevantIndicatorOverrides;
	private ORefListData progressPercentRefs;
	
	private PseudoORefListData relevantIndicatorRefs;
	private PseudoORefListData relevantStrategyRefs;
	private PseudoStringData latestProgressPercentComplete;
	private PseudoStringData latestProgressPercentDetails;
}
