/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.ChainWalker;
import org.miradi.ids.IdList;
import org.miradi.ids.IndicatorId;
import org.miradi.main.EAM;
import org.miradi.objectdata.CodeToUserStringMapData;
import org.miradi.objecthelpers.*;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.*;
import org.miradi.utils.CommandVector;

public class Indicator extends BaseObject implements StrategyActivityRelevancyInterface
{
	public Indicator(final ObjectManager objectManager, final IndicatorId idToUse)
	{
		this(objectManager, idToUse, createSchema(objectManager));
	}

	protected Indicator(ObjectManager objectManager, IndicatorId idToUse, IndicatorSchema indicatorSchema)
	{
		super(objectManager, idToUse, indicatorSchema);
	}

	public static IndicatorSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static IndicatorSchema createSchema(ObjectManager objectManager)
	{
		return (IndicatorSchema) objectManager.getSchemas().get(ObjectType.INDICATOR);
	}

	public IdList getMethodIds()
	{
		return getSafeIdListData(TAG_METHOD_IDS);
	}

	public CodeToUserStringMapData getThresholdsMap()
	{
		return (CodeToUserStringMapData)getField(TAG_THRESHOLDS_MAP);
	}
	
	public CodeToUserStringMap getThresholdDetailsMap()
	{
		return getCodeToUserStringMapData(TAG_THRESHOLD_DETAILS_MAP);
	}
	
	@Override
	protected CommandVector createCommandsToDereferenceObject() throws Exception
	{
		CommandVector commandsToDereferences = super.createCommandsToDereferenceObject();
		commandsToDereferences.addAll(buildRemoveIndicatorFromRelevancyListCommands(getRef()));
		
		return commandsToDereferences;
	}

	private CommandVector buildRemoveIndicatorFromRelevancyListCommands(ORef relevantIndicatorRefToRemove) throws Exception
	{
		CommandVector removeFromRelevancyListCommands = new CommandVector();
		removeFromRelevancyListCommands.addAll(Desire.buildRemoveObjectFromRelevancyListCommands(getProject(), ObjectiveSchema.getObjectType(), Objective.TAG_RELEVANT_INDICATOR_SET, relevantIndicatorRefToRemove));
		removeFromRelevancyListCommands.addAll(Desire.buildRemoveObjectFromRelevancyListCommands(getProject(), GoalSchema.getObjectType(), Goal.TAG_RELEVANT_INDICATOR_SET, relevantIndicatorRefToRemove));

		return removeFromRelevancyListCommands;
	}
		
	@Override
	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = super.createCommandsToDeleteChildren();
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_PROGRESS_REPORT_REFS));
		commandsToDeleteChildren.addAll(createCommandsToDeleteMethods());
		commandsToDeleteChildren.addAll(createCommandsToDeleteMeasurements());
		commandsToDeleteChildren.addAll(createCommandsToDeleteFutureStatuses());
		
		return commandsToDeleteChildren;
	}

	private CommandVector createCommandsToDeleteFutureStatuses() throws Exception
	{
		return createCommandsToDeleteAnnotation(getFutureStatusRefs());
	}
	
	private CommandVector createCommandsToDeleteMeasurements() throws Exception
	{
		return createCommandsToDeleteAnnotation(getMeasurementRefs());
	}
	
	private CommandVector createCommandsToDeleteMethods() throws Exception
	{		
		return createCommandsToDeleteAnnotation(getMethodRefs());
	}
	
	private CommandVector createCommandsToDeleteAnnotation(ORefList annotationRefs) throws Exception
	{
		CommandVector commandsToDeleteAnnotation = new CommandVector();
		for (int index = 0; index < annotationRefs.size(); ++index)
		{
			BaseObject annotationToDelete = BaseObject.find(getProject(), annotationRefs.get(index));
			ORefList referrers = annotationToDelete.findObjectsThatReferToUs(IndicatorSchema.getObjectType());
			if (referrers.size() == 1)
				commandsToDeleteAnnotation.addAll(annotationToDelete.createCommandsToDeleteChildrenAndObject());
		}
		
		return commandsToDeleteAnnotation;
	}

	//TODO: several pseudo fields are shared between Indicator and Desires; this may indicate a need for a common super class
	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getRelatedLabelsAsMultiLine(new TargetSet());
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getRelatedLabelsAsMultiLine(new DirectThreatSet());
		
		if(fieldTag.equals(PSEUDO_TAG_STRATEGIES))
			return getRelatedLabelsAsMultiLine(new NonDraftStrategySet());

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_ACTIVITY_REFS))
			return getRelevantActivityRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS))
			return getRelevantStrategyActivityRefsAsString();

		if(fieldTag.equals(PSEUDO_TAG_FACTOR))
			return getSafeLabel(getDirectOrIndirectOwningFactor());
		
		if(fieldTag.equals(PSEUDO_TAG_METHODS))
			return getIndicatorMethodsSingleLine();
		
		if (fieldTag.equals(PSEUDO_TAG_RELATED_METHOD_OREF_LIST))
			return getMethodRefs().toString();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_MEASUREMENT_REF))
			return getLatestMeasurementRef().toString();
		
		if(fieldTag.equals(PSEUDO_TAG_STATUS_VALUE))
			return getCurrentStatus();
		
		return super.getPseudoData(fieldTag);
	}

	public boolean isActive()
	{
		ORef ownerRef = getOwnerRef();
		if(KeyEcologicalAttribute.is(ownerRef))
			return isOwningKeyEcologicalAttributeActive(ownerRef);
		
		if(AbstractTarget.isAbstractTarget(ownerRef))
			return isOwningTargetInSimpleMode(ownerRef);
		
		return true;
	}

	private boolean isOwningKeyEcologicalAttributeActive(ORef keaRef)
	{
		KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getObjectManager(), keaRef);
		return kea.isActive();
	}

	private boolean isOwningTargetInSimpleMode(ORef targetRef)
	{
		AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
		return !target.isViabilityModeTNC();
	}

	public String getCurrentStatus()
	{
		ORef measurementRef = getLatestMeasurementRef();
		if(measurementRef == null || measurementRef.isInvalid())
			return "";
		
		Measurement measurement = (Measurement)getProject().findObject(measurementRef);
		String statusCode = measurement.getData(Measurement.TAG_STATUS);
		return statusCode;
	}

	private String getIndicatorMethodsSingleLine()
	{
		return getLabelsAsMultiline(getMethodRefs());
	}

	public ORefList getMethodRefs()
	{
		return new ORefList(MethodSchema.getObjectType(), getMethodIds());
	}

	public ORef getLatestMeasurementRef()
	{
		return getSafeLatestObject(getMeasurementRefs(), Measurement.TAG_DATE);
	}

	public ORef getLatestFutureStatusRef()
	{
		return getSafeLatestObject(getFutureStatusRefs(), FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
	}
	
	private ORef getSafeLatestObject(final ORefList refs, final String dateTag)
	{
		BaseObject latestObject = getLatestObject(getObjectManager(), refs, dateTag);
		if (latestObject == null)
			return ORef.INVALID;
		
		return latestObject.getRef();
	}
	
	public ORefList getMeasurementRefs()
	{
		return getSafeRefListData(TAG_MEASUREMENT_REFS);
	}
	
	public ORefList getFutureStatusRefs()
	{
		return getSafeRefListData(TAG_FUTURE_STATUS_REFS);
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_METHOD_IDS))
			return MethodSchema.getObjectType();
		
		if (tag.equals(TAG_MEASUREMENT_REFS))
			return MeasurementSchema.getObjectType();
		
		if (tag.equals(TAG_FUTURE_STATUS_REFS))
			return FutureStatusSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {
			StrategySchema.getObjectType(),
			CauseSchema.getObjectType(),
			IntermediateResultSchema.getObjectType(),
			ThreatReductionResultSchema.getObjectType(),
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			KeyEcologicalAttributeSchema.getObjectType(),
            BiophysicalFactorSchema.getObjectType(),
            BiophysicalResultSchema.getObjectType(),
		};
	}

	@Override
	public boolean isRelevancyOverrideSet(String tag)
	{
		if (tag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			return true;

		return false;
	}

	@Override
	public String getRelevantStrategyActivitySetTag()
	{
		return TAG_RELEVANT_STRATEGY_ACTIVITY_SET;
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

	public ORefList getRelevantStrategyAndActivityRefs() throws Exception
	{
		ORefSet relevantRefList = new ORefSet(getDefaultRelevantStrategyAndActivityRefs());
		RelevancyOverrideSet relevantOverrides = getStrategyActivityRelevancyOverrideSet();

		return calculateRelevantRefList(relevantRefList, relevantOverrides);
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

	private ORefList getDirectlyUpstreamNonDraftStrategies() throws Exception
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();

		return new ChainWalker().getDirectlyUpstreamNonDraftStrategies(owningFactor);
	}

	public RelevancyOverrideSet getStrategyActivityRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
	}

	public RelevancyOverrideSet getCalculatedRelevantStrategyActivityOverrides(ORefList selectedStrategyAndActivityRefs) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = getDefaultRelevantStrategyAndActivityRefs();
		relevantOverrides.addAll(computeRelevancyOverrides(selectedStrategyAndActivityRefs, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, selectedStrategyAndActivityRefs , false));

		return relevantOverrides;
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
		return createCommandsToEnsureProperRelevancy(relevancyOverridesTag, strategyOrActivityRef, shouldBeRelevant);
	}

	private CommandVector createCommandsToEnsureProperRelevancy(String relevancyOverridesTag, ORef ref, boolean shouldBeRelevant) throws Exception
	{
		RelevancyOverrideSet relevancyOverrideSet = null;
		if(relevancyOverridesTag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			relevancyOverrideSet = getStrategyActivityRelevancyOverrideSet();
		else
			throw new RuntimeException("Unexpected relevancy request for: " + relevancyOverridesTag);

		RelevancyOverride existingOverride = relevancyOverrideSet.find(ref);
		if (isAlreadyCorrectlyOverridden(existingOverride, shouldBeRelevant))
			return new CommandVector();

		boolean isCorrectDefaultRelevancy = false;
		if(relevancyOverridesTag.equals(TAG_RELEVANT_STRATEGY_ACTIVITY_SET))
			isCorrectDefaultRelevancy = isCorrectDefaultStrategyOrActivityRelevancy(ref, shouldBeRelevant);
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

	private boolean isAlreadyCorrectlyOverridden(RelevancyOverride existingOverride, boolean shouldBeRelevant)
	{
		return (existingOverride != null) && (existingOverride.isOverride() == shouldBeRelevant);
	}

	private boolean isCorrectDefaultStrategyOrActivityRelevancy(ORef strategyOrActivityRef, boolean shouldBeRelevant) throws Exception
	{
		ORefList defaultRelevantStrategyRefs = getDefaultRelevantStrategyAndActivityRefs();
		boolean isRelevantByDefault = defaultRelevantStrategyRefs.contains(strategyOrActivityRef);
		return isRelevantByDefault == shouldBeRelevant;
	}

	public ORefList getRelevantStrategyRefs() throws Exception
	{
		return getRelevantStrategyAndActivityRefs().getFilteredBy(StrategySchema.getObjectType());
	}

	public static ORefList findRelevantIndicators(Project projectToUse, ORef strategyRef) throws Exception
	{
		return projectToUse.getRelevantIndicatorsCache().getRelevantIndicatorsForStrategy(strategyRef);
	}

	public static ORefList findAllRelevantIndicators(Project projectToUse, ORef strategyOrActivityRef) throws Exception
	{
		return projectToUse.getRelevantIndicatorsCache().getAllRelevantIndicatorsForStrategyOrActivity(strategyOrActivityRef);
	}

	public ORefList getRelevantActivityRefs() throws Exception
	{
		return getRelevantStrategyAndActivityRefs().getFilteredBy(TaskSchema.getObjectType());
	}

	@Override
	protected ORefList getNonOwnedObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getNonOwnedObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getMethodRefs());
		
		return deepObjectRefsToCopy;
	}

	@Override
	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	@Override
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}
	
	public boolean isViabilityIndicator()
	{
		ORefList keaReferrerRefs = findObjectsThatReferToUs(KeyEcologicalAttributeSchema.getObjectType());
		return keaReferrerRefs.size() > 0;
	}
	
	public static boolean is(BaseObject object)
	{
		if(object == null)
			return false;
		return is(object.getRef());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == IndicatorSchema.getObjectType();
	}
	
	public static Indicator find(ObjectManager objectManager, ORef indicatorRef)
	{
		return (Indicator) objectManager.findObject(indicatorRef);
	}
	
	public static Indicator find(Project project, ORef indicatorRef)
	{
		return find(project.getObjectManager(), indicatorRef);
	}
	
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";

	public final static String TAG_METHOD_IDS = "MethodIds";
	public static final String TAG_THRESHOLDS_MAP = "IndicatorThresholds";
	public static final String TAG_THRESHOLD_DETAILS_MAP = "ThresholdDetails";
	public static final String TAG_RATING_SOURCE = "RatingSource";
	public static final String TAG_MEASUREMENT_REFS = "MeasurementRefs";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_VIABILITY_RATINGS_COMMENTS = "ViabilityRatingsComment";
	public static final String TAG_FUTURE_STATUS_REFS = "FutureStatusRefs";
	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";
	public static final String TAG_UNIT = "Unit";

	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	
	// NOTE: Can't change the following tags unless we recompile the jasper reports
	public static final String PSEUDO_TAG_TARGETS = "Targets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "DirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "Strategies";
	public static final String PSEUDO_TAG_METHODS = "Methods";
	public static final String PSEUDO_TAG_RATING_SOURCE_VALUE = "RatingSourceValue";
	public static final String PSEUDO_TAG_PRIORITY_VALUE = "PriorityValue";
	public static final String PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE  = "FutureStatusRatingValue";
	public static final String PSEUDO_TAG_STATUS_VALUE  = "StatusValue";
	public static final String PSEUDO_TAG_LATEST_MEASUREMENT_REF = "LatestMeasurementRef";
	public static final String PSEUDO_TAG_RELEVANT_ACTIVITY_REFS = "PseudoRelevantActivityRefs";
	public static final String PSEUDO_TAG_RELEVANT_STRATEGY_ACTIVITY_REFS = "PseudoIndicatorRelevantStrategyRefs";

	public static final String PSEUDO_TAG_RELATED_METHOD_OREF_LIST = "PseudoTagRelatedMethodORefList";

	public static final String META_COLUMN_TAG = "IndicatorMetaColumnTag";
}
