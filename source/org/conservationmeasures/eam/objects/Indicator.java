/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.IndicatorId;
import org.conservationmeasures.eam.objectdata.DateData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;
import org.conservationmeasures.eam.utils.StringMapData;

public class Indicator extends BaseObject
{
	public Indicator(IndicatorId idToUse)
	{
		super(idToUse);
		clear();
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
	
	public void addTaskId(BaseId taskId)
	{
		taskIds.add(taskId);
	}
	
	public IdList getGoalIds()
	{
		return goalIds.getIdList();
	}

	void clear()
	{
		super.clear();
		shortLabel = new StringData();
		priority = new ChoiceData();
		status = new ChoiceData();
		taskIds = new IdListData();
		goalIds = new IdListData();
		indicatorThreshold = new StringMapData();
		measurementTrend= new ChoiceData();
		measurementStatus= new ChoiceData();
		measurementDate= new DateData();;
		measurementSummary= new StringData();
		measurementDetail= new StringData();
		measurementStatusConfidence = new StringData();
		ratingSource= new ChoiceData();

		addField(TAG_SHORT_LABEL, shortLabel);
		addField(TAG_PRIORITY, priority);
		addField(TAG_STATUS, status);
		addField(TAG_TASK_IDS, taskIds);
		addField(TAG_GOAL_IDS, goalIds);
		addField(TAG_INDICATOR_THRESHOLDS, indicatorThreshold);
		addField(TAG_MEASUREMENT_TREND, measurementTrend);
		addField(TAG_MEASUREMENT_STATUS, measurementStatus);
		addField(TAG_MEASUREMENT_DATE, measurementDate);
		addField(TAG_MEASUREMENT_SUMMARY, measurementSummary);
		addField(TAG_MEASUREMENT_DETAIL, measurementDetail);
		addField(TAG_MEASUREMENT_STATUS_CONFIDENCE, measurementStatusConfidence);
		addField(TAG_RATING_SOURCE, ratingSource);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public static int getObjectType()
	{
		return ObjectType.INDICATOR;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		switch(type)
		{
			case ObjectType.GOAL: 
				return true;
			case ObjectType.TASK: 
				return true;
			default:
				return false;
		}
	}
	
	
	public static boolean canReferToThisType(int type)
	{
		return false;
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

	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_PRIORITY = "Priority";
	public static final String TAG_STATUS = "Status";
	public final static String TAG_TASK_IDS = "TaskIds";
	public final static String TAG_GOAL_IDS = "GoalIds";
	public static final String TAG_INDICATOR_THRESHOLDS = "IndicatorThresholds";
	public static final String TAG_MEASUREMENT_TREND = "MeasurementTrend";
	public static final String TAG_MEASUREMENT_STATUS  = "MeasurementStatus";
	public static final String TAG_MEASUREMENT_DATE = "MeasurementDate";
	public static final String TAG_MEASUREMENT_SUMMARY = "MeasurementSummary";
	public static final String TAG_MEASUREMENT_DETAIL = "MeasurementDetail";
	public static final String TAG_MEASUREMENT_STATUS_CONFIDENCE = "MeasurementStatusConfidence";
	public static final String TAG_RATING_SOURCE = "RatingSource";
	
	public static final String PSEUDO_TAG_FACTOR = "PseudoTagFactor";
	public static final String PSEUDO_TAG_TARGETS = "PseudoTagTargets";
	public static final String PSEUDO_TAG_DIRECT_THREATS = "PseudoTagDirectThreats";
	public static final String PSEUDO_TAG_STRATEGIES = "PseudoTagStrategies";
	public static final String PSEUDO_TAG_METHODS = "PseudoTagMethods";

	
	public static final String OBJECT_NAME = "Indicator";

	StringData shortLabel;
	ChoiceData priority;
	ChoiceData status;
	IdListData taskIds;
	StringMapData indicatorThreshold;
	ChoiceData measurementTrend;
	ChoiceData measurementStatus;
	DateData measurementDate;
	StringData measurementSummary;
	StringData measurementDetail;
	StringData measurementStatusConfidence;
	ChoiceData ratingSource;
	IdListData goalIds;
}
