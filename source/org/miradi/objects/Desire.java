/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.ChainWalker;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.TargetSet;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.ProgressPercentSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CommandVector;

abstract public class Desire extends BaseObject implements StrategyActivityRelevancyInterface
{
	public Desire(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schemaToUse)
	{
		super(objectManager, idToUse, schemaToUse);
	}

	@Override
	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = super.createCommandsToDeleteChildren();
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_PROGRESS_PERCENT_REFS));
		
		return commandsToDeleteChildren;
	}

	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return ProgressPercentSchema.getObjectType();
				
		return super.getAnnotationType(tag);
	}
	
	public ORefList getProgressPercentRefs()
	{
		return getSafeRefListData(TAG_PROGRESS_PERCENT_REFS);
	}
	
	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public String getFullText()
	{
		return getData(TAG_FULL_TEXT);
	}

	@Override
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}

	@Override
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
		
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
			return getRelevantActivityRefsAsString();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE))
			return getLatestProgressPercentComplete();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS))
			return getLatestProgressPercentDetails();
		
		return super.getPseudoData(fieldTag);
	}

	@Override
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

	private ORefList getDirectlyUpstreamNonDraftStrategies() throws Exception
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();
		
		return new ChainWalker().getDirectlyUpstreamNonDraftStrategies(owningFactor);
	}
	
	public CommandVector createCommandsToEnsureStrategyOrActivityIsIrrelevant(ORef strategyOrActivityRef) throws Exception
	{
		boolean shouldBeRelevant = false;
		
		return createCommandsToEnsureProperStrategyOrActivityRelevancy(strategyOrActivityRef, shouldBeRelevant);
	}
	
	public CommandVector createCommandsToEnsureStrategyOrActivityIsRelevant(ORef strategyOrActivityRef) throws Exception
	{
		boolean shouldBeRelevant = true;
		
		return createCommandsToEnsureProperStrategyOrActivityRelevancy(strategyOrActivityRef, shouldBeRelevant);
	}

	private CommandVector createCommandsToEnsureProperStrategyOrActivityRelevancy(ORef strategyOrActivityRef, boolean shouldBeRelevant) throws Exception
	{
		String relevancyOverridesTag = TAG_RELEVANT_STRATEGY_ACTIVITY_SET;
		return createCommandsToEnsureProperRelevancy(relevancyOverridesTag,
				strategyOrActivityRef, shouldBeRelevant);
	}

	public CommandVector createCommandsToEnsureIndicatorIsIrrelevant(ORef indicatorRef) throws Exception
	{
		boolean shouldBeRelevant = false;
		
		return createCommandsToEnsureProperIndicatorRelevancy(indicatorRef, shouldBeRelevant);
	}

	public CommandVector createCommandsToEnsureIndicatorIsRelevant(ORef indicatorRef) throws Exception
	{
		boolean shouldBeRelevant = true;
		
		return createCommandsToEnsureProperIndicatorRelevancy(indicatorRef, shouldBeRelevant);
	}

	private CommandVector createCommandsToEnsureProperIndicatorRelevancy(ORef indicatorRef, boolean shouldBeRelevant) throws Exception
	{
		String relevancyOverridesTag = TAG_RELEVANT_INDICATOR_SET;
		return createCommandsToEnsureProperRelevancy(relevancyOverridesTag,
				indicatorRef, shouldBeRelevant);
	}

	private CommandVector createCommandsToEnsureProperRelevancy(
			String relevancyOverridesTag, ORef ref, boolean shouldBeRelevant)
			throws Exception
	{
		RelevancyOverrideSet relevancyOverrideSet = null;
		if(relevancyOverridesTag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			relevancyOverrideSet = getStrategyActivityRelevancyOverrideSet();
		else if(relevancyOverridesTag.equals(TAG_RELEVANT_INDICATOR_SET))
			relevancyOverrideSet = getIndicatorRelevancyOverrideSet();
		else
			throw new RuntimeException("Unexpected relevancy request for: " + relevancyOverridesTag);
		
		RelevancyOverride existingOverride = relevancyOverrideSet.find(ref);
		if (isAlreadyCorrectlyOverridden(existingOverride, shouldBeRelevant))
			return new CommandVector();
		
		boolean isCorrectDefaultRelevancy = false;
		if(relevancyOverridesTag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			isCorrectDefaultRelevancy = isCorrectDefaultStrategyOrActivityRelevancy(ref, shouldBeRelevant);
		else if(relevancyOverridesTag.equals(TAG_RELEVANT_INDICATOR_SET))
			isCorrectDefaultRelevancy = isCorrectDefaultIndicatorRelevancy(ref, shouldBeRelevant);
		else
			throw new RuntimeException("Unexpected relevancy request for: " + relevancyOverridesTag);
		
		if (isCorrectDefaultRelevancy && existingOverride == null)
			return new CommandVector();
		
		relevancyOverrideSet.remove(ref);
		if (!isCorrectDefaultRelevancy)
			relevancyOverrideSet.add(new RelevancyOverride(ref, shouldBeRelevant));

		CommandSetObjectData commandToEnsureProperRelevancy = new CommandSetObjectData(getRef(), relevancyOverridesTag, relevancyOverrideSet.toString());
		return new CommandVector(commandToEnsureProperRelevancy);
	}

	private boolean isCorrectDefaultIndicatorRelevancy(ORef indicatorRef, boolean shouldBeRelevant) throws Exception
	{
		return getDefaultRelevantIndicatorRefs().contains(indicatorRef) == shouldBeRelevant;
	}

	private boolean isCorrectDefaultStrategyOrActivityRelevancy(ORef strategyOrActivityRef, boolean shouldBeRelevant) throws Exception
	{
		ORefList defaultRelevantStrategyRefs = getDefaultRelevantStrategyAndActivityRefs();
		boolean isRelevantByDefault = defaultRelevantStrategyRefs.contains(strategyOrActivityRef);
		return isRelevantByDefault == shouldBeRelevant;
	}

	private boolean isAlreadyCorrectlyOverridden(RelevancyOverride existingOverride, boolean shouldBeRelevant)
	{
		return (existingOverride != null) && (existingOverride.isOverride() == shouldBeRelevant);
	}

	@Override
	public String getRelevantStrategyActivitySetTag()
	{
		return TAG_RELEVANT_STRATEGY_ACTIVITY_SET;
	}

	public RelevancyOverrideSet getCalculatedRelevantStrategyActivityOverrides(ORefList selectedStrategyAndActivityRefs) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getDefaultRelevantStrategyAndActivityRefs();
		relevantOverrides.addAll(computeRelevancyOverrides(selectedStrategyAndActivityRefs, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, selectedStrategyAndActivityRefs , false));	
	
		return relevantOverrides;
	}

	private ORefList getDefaultRelevantStrategyAndActivityRefs() throws Exception
	{
        ORefList relevantRefList = new ORefList();

        ORefList relevantStrategyRefList = getDirectlyUpstreamNonDraftStrategies();

        for(ORef strategyRef : relevantStrategyRefList)
        {
            relevantRefList.add(strategyRef);
            Strategy strategy = Strategy.find(getProject(), strategyRef);
            relevantRefList.addAll(strategy.getActivityRefs());
        }

        return relevantRefList;
	}
	
	public RelevancyOverrideSet getStrategyActivityRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
	}

	@Override
	protected RelevancyOverrideSet getIndicatorRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_RELEVANT_INDICATOR_SET);
	}

	@Override
	public ORefList getIndicatorsOnSameFactor()
	{
		ORefList indicatorRefs = new ORefList();

		ORefList referrers = findAllObjectsThatReferToUs();
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
				indicatorRefs.add(new ORef(IndicatorSchema.getObjectType(), indicatorId));
			}
		}

		return indicatorRefs;
	}

	public ORefList getRelevantStrategyAndActivityRefs() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getDefaultRelevantStrategyAndActivityRefs());
		RelevancyOverrideSet relevantOverrides = getStrategyActivityRelevancyOverrideSet();
	
		return calculateRelevantRefList(relevantRefList, relevantOverrides);
	}
	
	public ORefList getRelevantStrategyRefs() throws Exception
	{
		return getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
	}

	public ORefList getRelevantActivityRefs() throws Exception
	{
        return getRelevantStrategyAndActivityRefs().getFilteredBy(TaskSchema.getObjectType());
	}

	public static ORefList findRelevantDesires(Project projectToUse, ORef strategyRef, final int desireType) throws Exception
	{
		return projectToUse.getRelevantDesiresCache().getRelevantDesiresForStrategy(strategyRef, desireType);
	}

	public static ORefList findAllRelevantDesires(Project projectToUse, ORef strategyOrActivityRef, final int desireType) throws Exception
	{
		return projectToUse.getRelevantDesiresCache().getAllRelevantDesiresForStrategyOrActivity(strategyOrActivityRef, desireType);
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
	
	public static boolean isDesire(ORef ref)
	{
		return isDesire(ref.getObjectType());
	}
	
	public static boolean isDesire(int objectType)
	{
		if (Objective.is(objectType))
			return true;
		
		return Goal.is(objectType);
	}

	public final static String TAG_SHORT_LABEL = "ShortLabel";
	public final static String TAG_FULL_TEXT = "FullText";
	public final static String TAG_COMMENTS = "Comments";
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";
	public static final String TAG_PROGRESS_PERCENT_REFS = "ProgressPrecentRefs";
	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
	public static final String PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS = "PseudoDesireRelevantStrategyRefs";
	public static final String PSEUDO_TAG_RELEVANT_ACTIVITY_REFS = "PseudoRelevantActivityRefs";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_COMPLETE = "PseudoLatestProgressPercentComplete";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_PERCENT_DETAILS = "PseudoLatestProgressPercentDetails";
	public final static String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public final static String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public final static String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
}
