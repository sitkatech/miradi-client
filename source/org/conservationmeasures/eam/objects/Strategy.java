/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.RatingData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.ratings.RatingChoice;
import org.conservationmeasures.eam.ratings.StrategyCostQuestion;
import org.conservationmeasures.eam.ratings.StrategyDurationQuestion;
import org.conservationmeasures.eam.ratings.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.ratings.StrategyImpactQuestion;
import org.conservationmeasures.eam.ratings.StrategyRatingSummary;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Strategy extends Factor
{
	public Strategy(FactorId idToUse)
	{
		super(idToUse, Factor.TYPE_INTERVENTION);
		status = STATUS_REAL;
		clear();
	}
	
	public Strategy(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_INTERVENTION, json);
		status = json.optString(TAG_STATUS, STATUS_REAL);
	}

	public boolean isStrategy()
	{
		return true;
	}
	
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public boolean isStatusReal()
	{
		return !isStatusDraft();
	}
	
	public boolean isStatusDraft()
	{
		return STATUS_DRAFT.equals(status);
	}
	
	public void insertActivityId(BaseId activityId, int insertAt)
	{
		activityIds.insertAt(activityId, insertAt);
	}
	
	public void removeActivityId(BaseId activityId)
	{
		activityIds.removeId(activityId);
	}
	
	public IdList getActivityIds()
	{
		return activityIds.getIdList();
	}
	
	public String getData(String fieldTag)
	{
		if(TAG_STATUS.equals(fieldTag))
			return status;
		return super.getData(fieldTag);
	}

	public void setData(String fieldTag, String dataValue) throws Exception
	{
		if(TAG_STATUS.equals(fieldTag))
			status = dataValue;
		else
			super.setData(fieldTag, dataValue);
	}
	
	public RatingChoice getStrategyRating()
	{
		StrategyRatingSummary summary = new StrategyRatingSummary("");
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion("");
		StrategyDurationQuestion durationQuestion = new StrategyDurationQuestion("");
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion("");
		StrategyCostQuestion costQuestion = new StrategyCostQuestion("");
		
		RatingChoice impact = impactQuestion.findChoiceByCode(impactRating.get());
		RatingChoice duration = durationQuestion.findChoiceByCode(durationRating.get());
		RatingChoice feasibility = feasibilityQuestion.findChoiceByCode(feasibilityRating.get());
		RatingChoice cost = costQuestion.findChoiceByCode(costRating.get());
		RatingChoice result = summary.getResult(impact, duration, feasibility, cost);

		return result;
	}

	public EnhancedJsonObject toJson()
	{
		EnhancedJsonObject json = super.toJson();
		json.put(TAG_STATUS, status);
		return json;
	}
	
	void clear()
	{
		super.clear();
		activityIds = new IdListData();
		taxonomyCode = new StringData();
		impactRating = new RatingData();
		durationRating = new RatingData();
		feasibilityRating = new RatingData();
		costRating = new RatingData();
		
		addField(TAG_ACTIVITY_IDS, activityIds);
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_DURATION_RATING, durationRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_COST_RATING, costRating);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_DURATION_RATING = "DurationRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_COST_RATING = "CostRating";
	public static final String PSEUDO_TAG_RATING_SUMMARY = "PseudoTagRatingSummary";

	String status;
	IdListData activityIds;
	StringData taxonomyCode;
	RatingData impactRating;
	RatingData durationRating;
	RatingData feasibilityRating;
	RatingData costRating;
}
