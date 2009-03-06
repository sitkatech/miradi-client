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

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.ids.IndicatorId;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.DateData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.DirectThreatSet;
import org.miradi.objecthelpers.NonDraftStrategySet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.TargetSet;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.IndicatorStatusRatingQuestion;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.StringMapData;

public class Indicator extends BaseObject
{
	public Indicator(ObjectManager objectManager, IndicatorId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public Indicator(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public IdList getTaskIdList()
	{
		return taskIds.getIdList().createClone();
	}
	
	public ORefList getTaskRefs()
	{
		return new ORefList(Task.getObjectType(), getTaskIdList());
	}
	
	public StringMapData getThreshold()
	{
		return indicatorThreshold;
	}
	
	//TODO: several pseudo fields are shared between Indicator and Desires; this may indicate a need for a common super class
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE))
			return new StatusQuestion().findChoiceByCode(indicatorThreshold.get()).getLabel();
			
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getRelatedLabelsAsMultiLine(new TargetSet());
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getRelatedLabelsAsMultiLine(new DirectThreatSet());
		
		if(fieldTag.equals(PSEUDO_TAG_STRATEGIES))
			return getRelatedLabelsAsMultiLine(new NonDraftStrategySet());
		
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
		StringBuffer result = new StringBuffer();
		IdList methodIds = getTaskIdList();
		for(int i = 0; i < methodIds.size(); ++i)
		{
			if(i > 0)
				result.append("; ");
			
			BaseId methodId = methodIds.get(i);
			BaseObject method = objectManager.findObject(ObjectType.TASK, methodId);
			result.append(method.getData(Task.TAG_LABEL));
		}
		
		return result.toString();
	}
	
	public ORefList getMethodRefs()
	{
		return new ORefList(Task.getObjectType(), getTaskIdList());
	}
	
	public ORef getLatestMeasurementRef()
	{
		BaseObject latestObject = getLatestObject(getObjectManager(), getMeasurementRefs(), Measurement.TAG_DATE);
		if (latestObject == null)
			return ORef.INVALID;
		
		return latestObject.getRef();
	}
	
	@Override
	public ProgressReport getLatestProgressReport()
	{
		ProgressReport progressReport = (ProgressReport) getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
		return progressReport;
	}
	
	public ORefList getMeasurementRefs()
	{
		return measurementRefs.getORefList();
	}
	
	public ORefList getProgressReportRefs()
	{
		return progressReportRefs.getORefList();
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_TASK_IDS))
			return Task.getObjectType();
		
		if (tag.equals(TAG_MEASUREMENT_REFS))
			return Measurement.getObjectType();
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return ProgressReport.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_TASK_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_MEASUREMENT_REFS))
			return true;
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return true;
		
		return super.isRefList(tag);
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
		return ObjectType.INDICATOR;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public String getFutureStatusRating()
	{
		return futureStatusRating.get();
	}
	
	public String getFutureStatusSummary()
	{
		return futureStatusSummary.get();
	}
		
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getMethodRefs());
		deepObjectRefsToCopy.addAll(getMeasurementRefs());
		deepObjectRefsToCopy.addAll(getProgressReportRefs());
		
		return deepObjectRefsToCopy;
	}

	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public double getBudgetCostRollup(DateRange dateRangeToUse) throws Exception
	{
		return getTasksBudgetCostRollUp(dateRangeToUse, getMethodRefs());
	}
	
	public DateRange getWhenRollup() throws Exception
	{
		return combineSubtaskEffortListDateRanges(getMethodRefs());
	}
	
	public ORefSet getWhoRollup() throws Exception
	{
		return getAllResources(getMethodRefs());
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}
	
	public boolean isViabilityIndicator()
	{
		ORefList keaReferrerRefs = findObjectsThatReferToUs(KeyEcologicalAttribute.getObjectType());
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
		return objectType == getObjectType();
	}
	
	public static Indicator find(ObjectManager objectManager, ORef indicatorRef)
	{
		return (Indicator) objectManager.findObject(indicatorRef);
	}
	
	public static Indicator find(Project project, ORef indicatorRef)
	{
		return find(project.getObjectManager(), indicatorRef);
	}
	
	void clear()
	{
		super.clear();
		shortLabel = new StringData(TAG_SHORT_LABEL);
		priority = new ChoiceData(TAG_PRIORITY, getQuestion(PriorityRatingQuestion.class));
		status = new ChoiceData(TAG_STATUS, getQuestion(IndicatorStatusRatingQuestion.class));
		taskIds = new IdListData(TAG_TASK_IDS, Task.getObjectType());
		indicatorThreshold = new StringMapData(TAG_INDICATOR_THRESHOLD);
		ratingSource= new ChoiceData(TAG_RATING_SOURCE, getQuestion(RatingSourceQuestion.class));
		measurementRefs = new ORefListData(TAG_MEASUREMENT_REFS);
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		detail = new StringData(TAG_DETAIL);
		comment = new StringData(TAG_COMMENT);
		viabilityRatingsComment = new StringData(TAG_VIABILITY_RATINGS_COMMENT);
	    thresholdDetails = new StringMapData(TAG_THRESHOLD_DETAILS);

		futureStatusRating = new ChoiceData(TAG_FUTURE_STATUS_RATING, getQuestion(StatusQuestion.class));
		futureStatusDate = new DateData(TAG_FUTURE_STATUS_DATE);
		futureStatusSummary = new StringData(TAG_FUTURE_STATUS_SUMMARY);
		futureStatusDetail = new StringData(TAG_FUTURE_STATUS_DETAIL);
		futureStatusComment = new StringData(TAG_FUTURE_STATUS_COMMENT);
		
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		multiLineDirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineStrategies = new PseudoStringData(PSEUDO_TAG_STRATEGIES);
		multiLineFactor = new PseudoStringData(PSEUDO_TAG_FACTOR);
		multiLineMethods = new PseudoStringData(PSEUDO_TAG_METHODS);
		indicatorThresholdLabel = new PseudoStringData(PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE);
		priorityLabel = new PseudoQuestionData(PSEUDO_TAG_PRIORITY_VALUE, new PriorityRatingQuestion());
		statusLabel = new PseudoQuestionData(PSEUDO_TAG_STATUS_VALUE, new IndicatorStatusRatingQuestion());
		ratingSourceLabel = new PseudoQuestionData(PSEUDO_TAG_RATING_SOURCE_VALUE, new RatingSourceQuestion());
		latestMeasurement = new PseudoQuestionData(PSEUDO_TAG_LATEST_MEASUREMENT_REF, new StatusQuestion());
		
		futureStatusRatingLabel = new PseudoQuestionData(PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE, new StatusQuestion());
		
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_PRIORITY, priority);
		addField(TAG_STATUS, status);
		addField(TAG_TASK_IDS, taskIds);
		addField(TAG_INDICATOR_THRESHOLD, indicatorThreshold);
		addField(TAG_RATING_SOURCE, ratingSource);
		addField(TAG_MEASUREMENT_REFS, measurementRefs);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		addField(TAG_DETAIL, detail);
		addField(TAG_COMMENT, comment);
		addField(TAG_VIABILITY_RATINGS_COMMENT, viabilityRatingsComment);
		addField(TAG_THRESHOLD_DETAILS, thresholdDetails); 
		
		addField(TAG_FUTURE_STATUS_RATING, futureStatusRating);
		addField(TAG_FUTURE_STATUS_DATE, futureStatusDate);
		addField(TAG_FUTURE_STATUS_SUMMARY, futureStatusSummary);
		addField(TAG_FUTURE_STATUS_DETAIL, futureStatusDetail);
		addField(TAG_FUTURE_STATUS_COMMENT, futureStatusComment);
		
		addField(PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE, indicatorThresholdLabel);
		addField(PSEUDO_TAG_TARGETS, multiLineTargets);
		addField(PSEUDO_TAG_DIRECT_THREATS, multiLineDirectThreats);
		addField(PSEUDO_TAG_STRATEGIES, multiLineStrategies);
		addField(PSEUDO_TAG_FACTOR, multiLineFactor);
		addField(PSEUDO_TAG_METHODS, multiLineMethods);
		addField(PSEUDO_TAG_PRIORITY_VALUE, priorityLabel);
		addField(PSEUDO_TAG_STATUS_VALUE, statusLabel);
		addField(PSEUDO_TAG_RATING_SOURCE_VALUE, ratingSourceLabel);
		
		addField(PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE, futureStatusRatingLabel);
		addField(PSEUDO_TAG_LATEST_MEASUREMENT_REF, latestMeasurement);
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";
	
	//FIXME: The following is not actually stored on disk for Indicator any more
	// See Strategy, Measurement, and overall Project Viability which use the same string
	public static final String TAG_STATUS = "Status";
	
	public final static String TAG_TASK_IDS = "TaskIds";
	public static final String TAG_INDICATOR_THRESHOLD = "IndicatorThresholds";
	public static final String TAG_RATING_SOURCE = "RatingSource";
	public static final String TAG_MEASUREMENT_REFS = "MeasurementRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_VIABILITY_RATINGS_COMMENT = "ViabilityRatingsComment";
	public static final String TAG_THRESHOLD_DETAILS = "ThresholdDetails"; 

	public static final String TAG_FUTURE_STATUS_RATING  = "FutureStatusRating";
	public static final String TAG_FUTURE_STATUS_DATE = "FutureStatusDate";
	public static final String TAG_FUTURE_STATUS_SUMMARY = "FutureStatusSummary";
	public static final String TAG_FUTURE_STATUS_DETAIL = "FutureStatusDetail";
	public static final String TAG_FUTURE_STATUS_COMMENT = "FutureStatusComment";

	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	
	// NOTE: Can't change the following tags unless we recompile the jasper reports
	public static final String PSEUDO_TAG_TARGETS = "Targets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "DirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "Strategies";
	public static final String PSEUDO_TAG_METHODS = "Methods";
	public static final String PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE = "IndicatorThresholdValue";
	public static final String PSEUDO_TAG_RATING_SOURCE_VALUE = "RatingSourceValue";
	public static final String PSEUDO_TAG_PRIORITY_VALUE = "PriorityValue";
	public static final String PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE  = "FutureStatusRatingValue";
	public static final String PSEUDO_TAG_STATUS_VALUE  = "StatusValue";
	public static final String PSEUDO_TAG_LATEST_MEASUREMENT_REF = "LatestMeasurementRef";
	
	public static final String PSEUDO_TAG_RELATED_METHOD_OREF_LIST = "PseudoTagRelatedMethodORefList";

	public static final String META_COLUMN_TAG = "IndicatorMetaColumnTag"; 
	public static final String OBJECT_NAME = "Indicator";

	private StringData shortLabel;
	private ChoiceData priority;
	private ChoiceData status;
	private IdListData taskIds;
	private StringMapData indicatorThreshold;
	private ChoiceData ratingSource;
	private ORefListData measurementRefs;
	private ORefListData progressReportRefs;
	private StringData detail;
	private StringData comment;
	private StringData viabilityRatingsComment;
	private StringMapData thresholdDetails;

	private ChoiceData futureStatusRating;
	private DateData futureStatusDate;
	private StringData futureStatusSummary;
	private StringData futureStatusDetail;
	private StringData futureStatusComment;
	
	private PseudoStringData multiLineTargets;
	private PseudoStringData multiLineDirectThreats;
	private PseudoStringData multiLineStrategies;
	private PseudoStringData multiLineFactor;
	private PseudoStringData multiLineMethods;
	private PseudoStringData indicatorThresholdLabel;
	
	private PseudoQuestionData priorityLabel;
	private PseudoQuestionData statusLabel;
	private PseudoQuestionData ratingSourceLabel;
	
	private PseudoQuestionData futureStatusRatingLabel;
	private PseudoQuestionData latestMeasurement;
}
