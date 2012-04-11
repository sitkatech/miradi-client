/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.utils.EnhancedJsonObject;

abstract public class Desire extends BaseObject
{
	public Desire(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schemaToUse)
	{
		super(objectManager, idToUse, schemaToUse);
	}

	public Desire(ObjectManager objectManager, BaseId idToUse, EnhancedJsonObject json, final BaseObjectSchema schemaToUse) throws Exception
	{
		super(objectManager, idToUse, json, schemaToUse);
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
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_PROGRESS_PERCENT_REFS))
			return true;
				
		return super.isRefList(tag);
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getProgressPercentRefs());
		
		return deepObjectRefsToCopy;
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
				indicatorRefs.add(new ORef(IndicatorSchema.getObjectType(), indicatorId));
			}
		}
		
		return indicatorRefs;
		
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

	protected String getRelevantActivityRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantActivityRefs();
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
		
		return createCommandsToEnsureProperRelevancy(strategyOrActivityRef, shouldBeRelevant);
	}
	
	public CommandVector createCommandsToEnsureStrategyOrActivityIsRelevant(ORef strategyOrActivityRef) throws Exception
	{
		boolean shouldBeRelevant = true;
		
		return createCommandsToEnsureProperRelevancy(strategyOrActivityRef, shouldBeRelevant);
	}

	private CommandVector createCommandsToEnsureProperRelevancy(ORef strategyOrActivityRef, boolean shouldBeRelevant) throws Exception
	{
		RelevancyOverrideSet relevancyOverrideSet = getStrategyActivityRelevancyOverrideSet();
		
		RelevancyOverride existingOverride = relevancyOverrideSet.find(strategyOrActivityRef);
		if (isAlreadyCorrectlyOverridden(existingOverride, shouldBeRelevant))
			return new CommandVector();
		
		boolean isCorrectDefaultRelevancy = isCorrectDefaultRelevancy(strategyOrActivityRef, shouldBeRelevant);
		if (isCorrectDefaultRelevancy && existingOverride == null)
			return new CommandVector();
		
		relevancyOverrideSet.remove(strategyOrActivityRef);
		if (!isCorrectDefaultRelevancy)
			relevancyOverrideSet.add(new RelevancyOverride(strategyOrActivityRef, shouldBeRelevant));
		
		CommandSetObjectData commandToEnsureProperRelevancy = new CommandSetObjectData(getRef(), TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrideSet.toString());
		return new CommandVector(commandToEnsureProperRelevancy);
	}

	private boolean isCorrectDefaultRelevancy(ORef strategyOrActivityRef, boolean shouldBeRelevant) throws Exception
	{
		return getDefaultRelevantStrategyRefs().contains(strategyOrActivityRef) == shouldBeRelevant;
	}

	private boolean isAlreadyCorrectlyOverridden(RelevancyOverride existingOverride, boolean shouldBeRelevant)
	{
		return (existingOverride != null) && (existingOverride.isOverride() == shouldBeRelevant);
	}

	public RelevancyOverrideSet getCalculatedRelevantIndicatorOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getIndicatorsOnSameFactor();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));	
	
		return relevantOverrides;
	}

	public RelevancyOverrideSet getCalculatedRelevantStrategyActivityOverrides(ORefList selectedStrategyAndActivityRefs) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getDefaultRelevantStrategyRefs();
		relevantOverrides.addAll(computeRelevancyOverrides(selectedStrategyAndActivityRefs, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, selectedStrategyAndActivityRefs , false));	
	
		return relevantOverrides;
	}

	private ORefList getDefaultRelevantStrategyRefs() throws Exception
	{
		return getDirectlyUpstreamNonDraftStrategies();
	}
	
	public RelevancyOverrideSet getStrategyActivityRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
	}
	
	public ORefList getRelevantIndicatorRefList() throws Exception
	{
		ORefSet relevantRefList = indicatorsOnSameFactorAsRefSet();
		RelevancyOverrideSet relevantOverrides = getRawRelevancyOverrideData(TAG_RELEVANT_INDICATOR_SET);
	
		return calculateRelevantRefList(relevantRefList, relevantOverrides);
	}

	public ORefList getRelevantStrategyAndActivityRefs() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getDefaultRelevantStrategyRefs());
		RelevancyOverrideSet relevantOverrides = getStrategyActivityRelevancyOverrideSet();
	
		return calculateRelevantRefList(relevantRefList, relevantOverrides);
	}
	
	public ORefList getRelevantStrategyRefs() throws Exception
	{
		return getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
	}

	public ORefList getRelevantActivityRefs() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getDefaultRelevantStrategyRefs());
		RelevancyOverrideSet relevantOverrides = getStrategyActivityRelevancyOverrideSet();
	
		return calculateRelevantRefList(relevantRefList, relevantOverrides).getFilteredBy(TaskSchema.getObjectType());
	}

	public static CommandVector buildRemoveObjectFromRelevancyListCommands(Project project, int typeWithRelevacnyOverrideSetList, String relevancyTag, ORef relevantObjectRefToRemove) throws Exception
	{
		CommandVector removeFromRelevancyListCommands = new CommandVector();
		ORefList objectRefsWithRelevancyOverrides = project.getPool(typeWithRelevacnyOverrideSetList).getORefList();
		for (int index = 0; index < objectRefsWithRelevancyOverrides.size(); ++index)
		{
			BaseObject objectWithRelevancyOverrides = BaseObject.find(project, objectRefsWithRelevancyOverrides.get(index));
			String relevancySetAsString = objectWithRelevancyOverrides.getData(relevancyTag);
			RelevancyOverrideSet relevancyOverrideSet = new RelevancyOverrideSet(relevancySetAsString);
			if (relevancyOverrideSet.contains(relevantObjectRefToRemove))
			{
				relevancyOverrideSet.remove(relevantObjectRefToRemove);
				CommandSetObjectData removeFromRelevancyListCommand = new CommandSetObjectData(objectWithRelevancyOverrides.getRef(), relevancyTag, relevancyOverrideSet.toString());
				removeFromRelevancyListCommands.add(removeFromRelevancyListCommand);
			}
		}
		
		return removeFromRelevancyListCommands;
	}
	
	public static ORefList findRelevantDesires(Project projectToUse, ORef strategyRef, final int desireType) throws Exception
	{
		ORefSet desireRefs = projectToUse.getPool(desireType).getRefSet();
		ORefList relevant = new ORefList();
		for(ORef desireRef: desireRefs)
		{
			Desire desire = Desire.findDesire(projectToUse, desireRef);
			if(desire.getRelevantStrategyRefs().contains(strategyRef))
				relevant.add(desire.getRef());
		}
		return relevant;
	}
	
	public static ORefList findAllRelevantDesires(Project projectToUse, ORef strategyOrActivityRef, final int desireType) throws Exception
	{
		ORefSet desireRefs = projectToUse.getPool(desireType).getRefSet();
		ORefList relevant = new ORefList();
		for(ORef desireRef: desireRefs)
		{
			Desire desire = Desire.findDesire(projectToUse, desireRef);
			if(desire.getRelevantStrategyAndActivityRefs().contains(strategyOrActivityRef))
				relevant.add(desire.getRef());
		}
		return relevant;
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
