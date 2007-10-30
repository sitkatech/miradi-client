/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.dialogs.planning.PlanningViewBudgetCalculator;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.NonDraftStrategySet;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.questions.TrendQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.StringMapData;

public class Indicator extends BaseObject
{
	public Indicator(ObjectManager objectManager, IndicatorId idToUse)
	{
		super(objectManager, idToUse);
		clear();
	}

	public Indicator(IndicatorId idToUse)
	{
		super(idToUse);
		clear();
	}
	public Indicator(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	public Indicator(int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(new BaseId(idAsInt), json);
	}
	
	public int getTaskCount()
	{
		return taskIds.size();
	}
	
	public BaseId getTaskId(int index)
	{
		return taskIds.get(index);
	}
	
	public IdList getTaskIdList()
	{
		return taskIds.getIdList().createClone();
	}
	
	public ORefList getTaskRefs()
	{
		return new ORefList(Task.getObjectType(), getTaskIdList());
	}
	
	public void addTaskId(BaseId taskId)
	{
		taskIds.add(taskId);
	}
	
	//TODO: several pseudo fields are shared between Indicator and Desires; this may indicate a need for a common super class
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE))
			return new StatusQuestion("").findChoiceByCode(indicatorThreshold.get()).getLabel();
			
		if(fieldTag.equals(PSEUDO_TAG_TARGETS))
			return getRelatedLabelsAsMultiLine(new TargetSet());
		
		if(fieldTag.equals(PSEUDO_TAG_DIRECT_THREATS))
			return getRelatedLabelsAsMultiLine(new DirectThreatSet());;
		
		if(fieldTag.equals(PSEUDO_TAG_STRATEGIES))
			return getRelatedLabelsAsMultiLine(new NonDraftStrategySet());
		
		if(fieldTag.equals(PSEUDO_TAG_FACTOR) || fieldTag.equals(LEGACY_PSEUDO_TAG_FACTOR))
			return getOwner().getLabel();
		
		if(fieldTag.equals(PSEUDO_TAG_METHODS))
			return getIndicatorMethodsSingleLine();
		
		if (fieldTag.equals(PSEUDO_TAG_RELATED_METHOD_OREF_LIST))
			return getMethods().toString();
		
		return super.getPseudoData(fieldTag);
	}
	
	public String getBudgetTotals()
	{
		try
		{
			return new PlanningViewBudgetCalculator(getProject()).getBudgetTotals(getRef());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.logWarning("Error occurred while calculating budget total for indicator");
			return "";
		}
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
	
	public ORefList getMethods()
	{
		return new ORefList(Task.getObjectType(), getTaskIdList());	
	}
	
	public ORefList getMeasurementRefs()
	{
		return measurementRefs.getORefList();
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_TASK_IDS))
			return Task.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_TASK_IDS))
			return true;
		
		return super.isIdListTag(tag);
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
		
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.TASK: 
				return true;
			default:
				return false;
		}
	}
	
	public ORefList getReferencedObjects(int objectType)
	{
		ORefList list = super.getReferencedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getTaskIdList()));
				break;
		}
		return list;
	}

	public String getShortLabel()
	{
		return getData(TAG_SHORT_LABEL);
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}
	
	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		priority = new ChoiceData();
		status = new ChoiceData();
		taskIds = new IdListData();
		indicatorThreshold = new StringMapData();
		measurementTrend= new ChoiceData();
		measurementStatus= new ChoiceData();
		measurementDate= new DateData();;
		measurementSummary= new StringData();
		measurementDetail= new StringData();
		measurementStatusConfidence = new ChoiceData();
		ratingSource= new ChoiceData();
		measurementRefs = new ORefListData();
		
		futureStatusRating = new ChoiceData();
		futureStatusDate = new DateData();;
		futureStatusSummary = new StringData();
		futureStatusDetail = new StringData();
		
		
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		multiLineDirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineStrategies = new PseudoStringData(PSEUDO_TAG_STRATEGIES);
		multiLineFactor = new PseudoStringData(PSEUDO_TAG_FACTOR);
		multiLineMethods = new PseudoStringData(PSEUDO_TAG_METHODS);
		indicatorThresholdLabel = new PseudoStringData(TAG_INDICATOR_THRESHOLD);
		priorityLabel = new PseudoQuestionData(new PriorityRatingQuestion(TAG_PRIORITY));
		statusLabel = new PseudoQuestionData(new IndicatorStatusRatingQuestion(TAG_STATUS));
		measurementTrendLabel = new PseudoQuestionData(new TrendQuestion(TAG_MEASUREMENT_TREND));
		measurementStatusLabel = new PseudoQuestionData(new StatusQuestion(TAG_MEASUREMENT_STATUS));
		ratingSourceLabel = new PseudoQuestionData(new RatingSourceQuestion(TAG_RATING_SOURCE));
		measurementStatusConfidenceLabel = new PseudoQuestionData(new StatusConfidenceQuestion(TAG_MEASUREMENT_STATUS_CONFIDENCE));
		
		futureStatusRatingLabel = new PseudoQuestionData(new StatusQuestion(TAG_FUTURE_STATUS_RATING));
		
		
		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_PRIORITY, priority);
		addField(TAG_STATUS, status);
		addField(TAG_TASK_IDS, taskIds);
		addField(TAG_INDICATOR_THRESHOLD, indicatorThreshold);
		addField(TAG_MEASUREMENT_TREND, measurementTrend);
		addField(TAG_MEASUREMENT_STATUS, measurementStatus);
		addField(TAG_MEASUREMENT_DATE, measurementDate);
		addField(TAG_MEASUREMENT_SUMMARY, measurementSummary);
		addField(TAG_MEASUREMENT_DETAIL, measurementDetail);
		addField(TAG_MEASUREMENT_STATUS_CONFIDENCE, measurementStatusConfidence);
		addField(TAG_RATING_SOURCE, ratingSource);
		addField(TAG_MEASUREMENT_REFS, measurementRefs);
		
		addField(TAG_FUTURE_STATUS_RATING, futureStatusRating);
		addField(TAG_FUTURE_STATUS_DATE, futureStatusDate);
		addField(TAG_FUTURE_STATUS_SUMMARY, futureStatusSummary);
		addField(TAG_FUTURE_STATUS_DETAIL, futureStatusDetail);
		
		addField(PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE, indicatorThresholdLabel);
		addField(PSEUDO_TAG_TARGETS, multiLineTargets);
		addField(PSEUDO_TAG_DIRECT_THREATS, multiLineDirectThreats);
		addField(PSEUDO_TAG_STRATEGIES, multiLineStrategies);
		addField(PSEUDO_TAG_FACTOR, multiLineFactor);
		addField(LEGACY_PSEUDO_TAG_FACTOR, multiLineFactor);
		addField(PSEUDO_TAG_METHODS, multiLineMethods);
		addField(PSEUDO_TAG_PRIORITY_VALUE, priorityLabel);
		addField(PSEUDO_TAG_STATUS_VALUE, statusLabel);
		addField(PSEUDO_TAG_MEASUREMENT_TREND_VALUE, measurementTrendLabel);
		addField(PSEUDO_TAG_MEASUREMENT_STATUS_VALUE, measurementStatusLabel);
		addField(PSEUDO_TAG_RATING_SOURCE_VALUE, ratingSourceLabel);
		addField(PSEUDO_TAG_MEASUREMENT_STATUS_CONFIDENCE_VALUE, measurementStatusConfidenceLabel);
		
		addField(PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE, futureStatusRatingLabel);
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_STATUS = "Status";
	public final static String TAG_TASK_IDS = "TaskIds";
	public static final String TAG_INDICATOR_THRESHOLD = "IndicatorThresholds";
	public static final String TAG_MEASUREMENT_TREND = "MeasurementTrend";
	public static final String TAG_MEASUREMENT_STATUS  = "MeasurementStatus";
	public static final String TAG_MEASUREMENT_DATE = "MeasurementDate";
	public static final String TAG_MEASUREMENT_SUMMARY = "MeasurementSummary";
	public static final String TAG_MEASUREMENT_DETAIL = "MeasurementDetail";
	public static final String TAG_MEASUREMENT_STATUS_CONFIDENCE = "MeasurementStatusConfidence";
	public static final String TAG_RATING_SOURCE = "RatingSource";
	public static final String TAG_MEASUREMENT_REFS = "MeasurementRefs";
	
	public static final String TAG_FUTURE_STATUS_RATING  = "FutureStatusRating";
	public static final String TAG_FUTURE_STATUS_DATE = "FutureStatusDate";
	public static final String TAG_FUTURE_STATUS_SUMMARY = "FutureStatusSummary";
	public static final String TAG_FUTURE_STATUS_DETAIL = "FutureStatusDetail";

	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	
	// NOTE: Can't change the following tags unless we recompile the jasper reports
	public static final String LEGACY_PSEUDO_TAG_FACTOR = "Factor";
	public static final String PSEUDO_TAG_TARGETS = "Targets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "DirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "Strategies";
	public static final String PSEUDO_TAG_METHODS = "Methods";
	public static final String PSEUDO_TAG_MEASUREMENT_STATUS_VALUE  = "MeasurementStatusValue";
	public static final String PSEUDO_TAG_INDICATOR_THRESHOLD_VALUE = "IndicatorThresholdValue";
	public static final String PSEUDO_TAG_RATING_SOURCE_VALUE = "RatingSourceValue";
	public static final String PSEUDO_TAG_PRIORITY_VALUE = "PriorityValue";
	public static final String PSEUDO_TAG_MEASUREMENT_TREND_VALUE = "MeasurementTrendValue";
	public static final String PSEUDO_TAG_MEASUREMENT_STATUS_CONFIDENCE_VALUE = "MeasurementStatusConfidenceValue";
	public static final String PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE  = "FutureStatusRatingValue";
	public static final String PSEUDO_TAG_STATUS_VALUE  = "StatusValue";
	
	public static final String PSEUDO_TAG_RELATED_METHOD_OREF_LIST = "PseudoTagRelatedMethodORefList";

	public static final String OBJECT_NAME = "Indicator";

	private StringData shortLabel;
	private ChoiceData priority;
	private ChoiceData status;
	private IdListData taskIds;
	private StringMapData indicatorThreshold;
	private ChoiceData measurementTrend;
	private ChoiceData measurementStatus;
	private DateData measurementDate;
	private StringData measurementSummary;
	private StringData measurementDetail;
	private ChoiceData measurementStatusConfidence;
	private ChoiceData ratingSource;
	private ORefListData measurementRefs;
	
	private ChoiceData futureStatusRating;
	private DateData futureStatusDate;
	private StringData futureStatusSummary;
	private StringData futureStatusDetail;
	
	private PseudoStringData multiLineTargets;
	private PseudoStringData multiLineDirectThreats;
	private PseudoStringData multiLineStrategies;
	private PseudoStringData multiLineFactor;
	private PseudoStringData multiLineMethods;
	private PseudoStringData indicatorThresholdLabel;
	
	private PseudoQuestionData priorityLabel;
	private PseudoQuestionData statusLabel;
	private PseudoQuestionData measurementTrendLabel;
	private PseudoQuestionData measurementStatusLabel;
	private PseudoQuestionData ratingSourceLabel;
	private PseudoQuestionData measurementStatusConfidenceLabel;
	
	private PseudoQuestionData futureStatusRatingLabel;
}
