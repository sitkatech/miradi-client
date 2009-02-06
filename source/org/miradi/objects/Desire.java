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

import java.util.Arrays;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.RelevancyOverrideSetData;
import org.miradi.objecthelpers.TargetSet;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

abstract public class Desire extends BaseObject
{
	public Desire(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public Desire(ObjectManager objectManager, BaseId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json);
	}

	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return ProgressPercent.getObjectType();
				
		return super.getAnnotationType(tag);
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return true;
				
		return super.isRefList(tag);
	}
	
	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getProgressPercentRefs());
		
		return deepObjectRefsToCopy;
	}

	private ORefList getProgressPercentRefs()
	{
		return progressPercentRefs.getORefList();
	}
	
	abstract public int getType();

	public String getShortLabel()
	{
		return shortLabel.get();
	}
	
	public String getFullText()
	{
		return fullText.get();
	}

	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}

	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getRelatedLabelsAsMultiLine(new TargetSet());
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getRelatedLabelsAsMultiLine(new DirectThreatSet());
		
		if(fieldTag.equals(PSEUDO_TAG_FACTOR))
			return getOwner().getLabel();
		
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();
	
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS))
			return getRelevantStrategyActivityRefsAsString();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return getLatestProgressPercentComplete();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS))
			return getLatestProgressPercentDetails();
		
		return super.getPseudoData(fieldTag);
	}
	
	public ORefSet indicatorsOnSameFactorAsRefSet()
	{
		ORefSet indicatorsOnSameFactor = new ORefSet();
		ORef[] indicators = getIndicatorsOnSameFactor().toArray();
		indicatorsOnSameFactor.addAll(Arrays.asList(indicators));
		
		return indicatorsOnSameFactor;
	}
	
	public ORefList getIndicatorsOnSameFactor()
	{
		ORefList indicatorRefs = new ORefList();
		
		ORefList referrers = findObjectsThatReferToUs();
		for(int i = 0; i < referrers.size(); ++i)
		{
			ORef thisRef = referrers.get(i);
			if(!Factor.isFactor(thisRef))
				continue;
			
			Factor factor = Factor.findFactor(getObjectManager(), thisRef);
			IdList indicatorIds = factor.getDirectOrIndirectIndicators();
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

	public boolean isRelevancyOverrideSet(String tag)
	{
		if (tag.equals(TAG_RELEVANT_INDICATOR_SET))
			return true;
		
		if (tag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;
		
		return false;
	}

	protected String getRelevantIndicatorRefsAsString()
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

	protected String getRelevantStrategyActivityRefsAsString()
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
	
	boolean isNonDraftStrategy(Factor factor)
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

	public RelevancyOverrideSet computeRelevancyOverrides(ORefList refList1, ORefList refList2,	boolean relevancyValue)
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
		return (ProgressPercent) getLatestObject(getObjectManager(), getProgressPercentRefs(), ProgressPercent.TAG_DATE);
	}
	
	public static Desire findDesire(ObjectManager objectManager, ORef desireRef)
	{
		return (Desire) objectManager.findObject(desireRef);
	}
	
	public static Desire findDesire(Project project, ORef desireRef)
	{
		return findDesire(project.getObjectManager(), desireRef);
	}
	
	public static boolean isDesire(int objectType)
	{
		if (Objective.is(objectType))
			return true;
		
		return Goal.is(objectType);
	}

	void clear()
	{
		super.clear();

		shortLabel = new StringData(TAG_SHORT_LABEL);
		fullText = new StringData(TAG_FULL_TEXT);
		comments = new StringData(TAG_COMMENTS);
		relevantIndicatorOverrides = new RelevancyOverrideSetData(TAG_RELEVANT_INDICATOR_SET);
		relevantStrategyActivityOverrides = new RelevancyOverrideSetData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
		progressPercentRefs = new ORefListData(TAG_PROGRESS_PERCENT_REFS);
		
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		multiLineDirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineFactor = new PseudoStringData(PSEUDO_TAG_FACTOR);
		relevantIndicatorRefs = new PseudoORefListData(PSEUDO_TAG_RELEVANT_INDICATOR_REFS);
		relevantStrategyRefs = new PseudoORefListData(PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS);
		latestProgressPercentComplete = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE);
		latestProgressPercentDetails = new PseudoStringData(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS);
		
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_FULL_TEXT, fullText);
		addField(TAG_COMMENTS, comments);
		addField(TAG_RELEVANT_INDICATOR_SET, relevantIndicatorOverrides);
		addField(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantStrategyActivityOverrides);
		addField(TAG_PROGRESS_PERCENT_REFS, progressPercentRefs);
	
		addField(PSEUDO_TAG_TARGETS, multiLineTargets);
		addField(PSEUDO_TAG_DIRECT_THREATS, multiLineDirectThreats);
		addField(PSEUDO_TAG_FACTOR, multiLineFactor);
		addField(PSEUDO_TAG_RELEVANT_INDICATOR_REFS, relevantIndicatorRefs);
		addField(PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS, relevantStrategyRefs);
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE, latestProgressPercentComplete);
		addField(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS, latestProgressPercentDetails);
	}
		
	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";
	public final static String TAG_COMMENTS = "Comments";	
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";
	public static final String TAG_PROGRESS_PERCENT_REFS = "ProgressPrecentRefs";
	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
	public static final String PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS = "PseudoRelevantStrategyRefs";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE = "PseudoLatestProgressPercentComplete";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS = "PseudoLatestProgressPercentDetails";
	public final static String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public final static String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public final static String PSEUDO_TAG_FACTOR = "PseudoTagFactor";

	public static final String OBJECT_NAME = "Desire";

	private StringData shortLabel;
	private StringData fullText;
	private StringData comments;
	private RelevancyOverrideSetData relevantStrategyActivityOverrides;
	private RelevancyOverrideSetData relevantIndicatorOverrides;
	private ORefListData progressPercentRefs;
	private PseudoStringData multiLineTargets;
	private PseudoStringData multiLineDirectThreats;
	private PseudoStringData multiLineFactor;
	private PseudoORefListData relevantIndicatorRefs;
	private PseudoORefListData relevantStrategyRefs;
	private PseudoStringData latestProgressPercentComplete;
	private PseudoStringData latestProgressPercentDetails;
}
