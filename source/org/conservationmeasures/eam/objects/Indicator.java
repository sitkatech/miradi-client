/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.DirectThreatSet;
import org.conservationmeasures.eam.objecthelpers.NonDraftStrategySet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objecthelpers.TargetSet;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.ProgressReportStatusQuestion;
import org.conservationmeasures.eam.questions.RatingSourceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;
import org.conservationmeasures.eam.utils.DateRange;
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
		
		if(fieldTag.equals(PSEUDO_TAG_FACTOR) || fieldTag.equals(LEGACY_PSEUDO_TAG_FACTOR))
			return getOwner().getLabel();
		
		if(fieldTag.equals(PSEUDO_TAG_METHODS))
			return getIndicatorMethodsSingleLine();
		
		if (fieldTag.equals(PSEUDO_TAG_RELATED_METHOD_OREF_LIST))
			return getMethods().toString();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_MEASUREMENT_REF))
			return getLatestMeasurementRef().toString();
		
		if(fieldTag.equals(PSEUDO_TAG_STATUS_VALUE))
			return getCurrentStatus();
		
		if (fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return getLatestProgressReportDate();
		
		return super.getPseudoData(fieldTag);
	}
	
	String getCurrentStatus()
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
	
	public ORefList getMethods()
	{
		return new ORefList(Task.getObjectType(), getTaskIdList());	
	}
	
	public static BaseObject getLatestObject(ObjectManager objectManagerToUse, ORefList objectRefs, String dateTag)
	{
		BaseObject latestObjectToFind = null; 
		for (int i = 0; i < objectRefs.size(); ++i)
		{
			BaseObject thisObject = objectManagerToUse.findObject(objectRefs.get(i));
			if (i == 0)
				latestObjectToFind = thisObject;
			
			String thisDateAsString = thisObject.getData(dateTag);
			String latestDateAsString = latestObjectToFind.getData(dateTag);
			if (thisDateAsString.compareTo(latestDateAsString) > 0)
			{
				latestObjectToFind = thisObject;
			}
		}
		
		return latestObjectToFind;		
	}
	
	public ORef getLatestMeasurementRef()
	{
		BaseObject latestObject = getLatestObject(getObjectManager(), getMeasurementRefs(), Measurement.TAG_DATE);
		if (latestObject == null)
			return ORef.INVALID;
		
		return latestObject.getRef();
	}
	
	private String  getLatestProgressReportDate()
	{
		ProgressReport progressReport = (ProgressReport) getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
		if (progressReport == null)
			return "";
		
		return progressReport.getProgressStatusChoice().getCode();
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
		
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.TASK: 
				return true;
			case ObjectType.MEASUREMENT:
				return true;
			case ObjectType.PROGRESS_REPORT:
				return true;
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set set = super.getReferencedObjectTags();
		set.add(TAG_TASK_IDS);
		set.add(TAG_MEASUREMENT_REFS);
		set.add(TAG_PROGRESS_REPORT_REFS);
			
		return set;
	}
	
	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getMethods());
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
		double total = 0.0;
		ORefList methodRefs = getTaskRefs();
		for(int i = 0; i < methodRefs.size(); ++i)
		{
			Task method = Task.find(getProject(), methodRefs.get(i));
			total += method.getProportionalBudgetCost(dateRangeToUse);
		}

		return total;
	}
	
	public String toString()
	{
		if(getId().isInvalid())
			return "(None)";
		return combineShortLabelAndLabel(shortLabel.toString(), getLabel());
	}
	
	public static Indicator find(Project project, ORef indicatorRef)
	{
		return (Indicator)project.findObject(indicatorRef);
	}

	void clear()
	{
		super.clear();
		shortLabel = new StringData(TAG_SHORT_LABEL);
		priority = new ChoiceData(TAG_PRIORITY);
		status = new ChoiceData(TAG_STATUS);
		taskIds = new IdListData(TAG_TASK_IDS, Task.getObjectType());
		indicatorThreshold = new StringMapData(TAG_INDICATOR_THRESHOLD);
		ratingSource= new ChoiceData(TAG_RATING_SOURCE);
		measurementRefs = new ORefListData(TAG_MEASUREMENT_REFS);
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		detail = new StringData(TAG_DETAIL);
		comment = new StringData(TAG_COMMENT);
		viabilityRatingsComment = new StringData(TAG_VIABILITY_RATINGS_COMMENT);
		
		futureStatusRating = new ChoiceData(TAG_FUTURE_STATUS_RATING);
		futureStatusDate = new DateData(TAG_FUTURE_STATUS_DATE);
		futureStatusSummary = new StringData(TAG_FUTURE_STATUS_SUMMARY);
		futureStatusDetail = new StringData(TAG_FUTURE_STATUS_DETAIL);
		futureStatusComment = new StringData(TAG_FUTURE_STATUS_COMMENT);
		
		multiLineTargets = new PseudoStringData(PSEUDO_TAG_TARGETS);
		multiLineDirectThreats = new PseudoStringData(PSEUDO_TAG_DIRECT_THREATS);
		multiLineStrategies = new PseudoStringData(PSEUDO_TAG_STRATEGIES);
		multiLineFactor = new PseudoStringData(PSEUDO_TAG_FACTOR);
		multiLineMethods = new PseudoStringData(PSEUDO_TAG_METHODS);
		indicatorThresholdLabel = new PseudoStringData(TAG_INDICATOR_THRESHOLD);
		priorityLabel = new PseudoQuestionData(TAG_PRIORITY, new PriorityRatingQuestion());
		statusLabel = new PseudoQuestionData(TAG_STATUS, new IndicatorStatusRatingQuestion());
		ratingSourceLabel = new PseudoQuestionData(TAG_RATING_SOURCE, new RatingSourceQuestion());
		latestMeasurement = new PseudoQuestionData(TAG_INDICATOR_THRESHOLD, new StatusQuestion());
		latestProgressReport = new PseudoQuestionData(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, new ProgressReportStatusQuestion());
		
		futureStatusRatingLabel = new PseudoQuestionData(TAG_FUTURE_STATUS_RATING, new StatusQuestion());
		
		
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
		addField(LEGACY_PSEUDO_TAG_FACTOR, multiLineFactor);
		addField(PSEUDO_TAG_METHODS, multiLineMethods);
		addField(PSEUDO_TAG_PRIORITY_VALUE, priorityLabel);
		addField(PSEUDO_TAG_STATUS_VALUE, statusLabel);
		addField(PSEUDO_TAG_RATING_SOURCE_VALUE, ratingSourceLabel);
		
		addField(PSEUDO_TAG_FUTURE_STATUS_RATING_VALUE, futureStatusRatingLabel);
		addField(PSEUDO_TAG_LATEST_MEASUREMENT_REF, latestMeasurement);
		addField(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, latestProgressReport);
	}

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_STATUS = "Status";
	public final static String TAG_TASK_IDS = "TaskIds";
	public static final String TAG_INDICATOR_THRESHOLD = "IndicatorThresholds";
	public static final String TAG_RATING_SOURCE = "RatingSource";
	public static final String TAG_MEASUREMENT_REFS = "MeasurementRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_VIABILITY_RATINGS_COMMENT = "ViabilityRatingsComment";

	public static final String TAG_FUTURE_STATUS_RATING  = "FutureStatusRating";
	public static final String TAG_FUTURE_STATUS_DATE = "FutureStatusDate";
	public static final String TAG_FUTURE_STATUS_SUMMARY = "FutureStatusSummary";
	public static final String TAG_FUTURE_STATUS_DETAIL = "FutureStatusDetail";
	public static final String TAG_FUTURE_STATUS_COMMENT = "FutureStatusComment";

	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	
	// NOTE: Can't change the following tags unless we recompile the jasper reports
	public static final String LEGACY_PSEUDO_TAG_FACTOR = "Factor";
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
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE = "PseudoLatestProgressReportCode";
	
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
	private PseudoQuestionData latestProgressReport;
}
