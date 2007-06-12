/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyCostQuestion;
import org.conservationmeasures.eam.questions.StrategyDurationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;


public class Strategy extends Factor
{
	
	public Strategy(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_STRATEGY);
		clear();
	}
	
	public Strategy(FactorId idToUse)
	{
		super(idToUse, Factor.TYPE_STRATEGY);
		clear();
	}
	
	public Strategy(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_STRATEGY, json);
	}

	
	public Strategy(FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(idToUse, Factor.TYPE_STRATEGY, json);
	}
	
	public static boolean canOwnThisType(int type)
	{
		if (Factor.canOwnThisType(type))
			return true;
		
		switch(type)
		{
			case ObjectType.OBJECTIVE: 
				return true;
			case ObjectType.TASK: 
				return true;
			default:
				return false;
		}
	}
	
	public ORefList getResultsChains()
	{
		ORefList diagramObjects = new ORefList();
		ORefList diagramFactorList = findObjectsThatReferToUs(DiagramFactor.getObjectType());
		for (int i=0; i<diagramFactorList.size(); ++i)
		{
			DiagramFactor diagramFactor = (DiagramFactor) getProject().findObject(diagramFactorList.get(i));
			DiagramObject diagramObject = (DiagramObject)diagramFactor.getOwner();
			if (diagramObject!=null)
			{
				if (diagramObject.isResultsChain())
					diagramObjects.add(diagramObject.getRef());
			}
			{
				EAM.logError("getResultsChains:Owner not found for diagram factor:" +diagramFactor.getId());
			}
		}
		return diagramObjects;
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
		return STATUS_DRAFT.equals(status.get());
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
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_RATING_SUMMARY))
			return getStrategyRatingSummary();
		return super.getPseudoData(fieldTag);
	}

	private String getStrategyRatingSummary()
	{
		ChoiceItem rating = getStrategyRating();
		return rating.getCode();

	}
	
	public ChoiceItem getStrategyRating()
	{
		StrategyRatingSummaryQuestion summary = new StrategyRatingSummaryQuestion("");
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion("");
		StrategyDurationQuestion durationQuestion = new StrategyDurationQuestion("");
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion("");
		StrategyCostQuestion costQuestion = new StrategyCostQuestion("");
		
		ChoiceItem impact = impactQuestion.findChoiceByCode(impactRating.get());
		ChoiceItem duration = durationQuestion.findChoiceByCode(durationRating.get());
		ChoiceItem feasibility = feasibilityQuestion.findChoiceByCode(feasibilityRating.get());
		ChoiceItem cost = costQuestion.findChoiceByCode(costRating.get());
		ChoiceItem result = summary.getResult(impact, duration, feasibility, cost);

		return result;
	}

	public String toString()
	{
		return combineShortLabelAndLabel(shortLabel.toString(), label.toString());
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectives()));
				break;
			case ObjectType.TASK: 
				list.addAll(new ORefList(objectType, getActivityIds()));
				break;

		}
		return list;
	}

	public int getType()
	{
		return getObjectType();
	}
	
	public static int getObjectType()
	{
		return ObjectType.STRATEGY;
	}
	
	public String getShortLabel()
	{
		return shortLabel.toString();
	}
	
	void clear()
	{
		super.clear();
		status = new StringData();
		activityIds = new IdListData();
		shortLabel = new StringData();
	
		taxonomyCode = new StringData();
		impactRating = new ChoiceData();
		durationRating = new ChoiceData();
		feasibilityRating = new ChoiceData();
		costRating = new ChoiceData();
		tagRatingSummary = new PseudoStringData(PSEUDO_TAG_RATING_SUMMARY);
		

		taxonomyCodeLabel = new PseudoQuestionData(new StrategyClassificationQuestion(TAG_TAXONOMY_CODE));
		impactRatingLabel = new PseudoQuestionData(new StrategyImpactQuestion(TAG_IMPACT_RATING));
		durationRatingLabel = new PseudoQuestionData(new  StrategyDurationQuestion(TAG_DURATION_RATING));
		feasibilityRatingLabel = new PseudoQuestionData(new StrategyFeasibilityQuestion(TAG_FEASIBILITY_RATING));
		costRatingLabel = new PseudoQuestionData(new StrategyCostQuestion(TAG_COST_RATING));
		tagRatingSummaryLabel = new PseudoQuestionData(new StrategyRatingSummaryQuestion(PSEUDO_TAG_RATING_SUMMARY));


		
		addField(TAG_STATUS, status);
		addField(TAG_ACTIVITY_IDS, activityIds);
		addField(TAG_SHORT_LABEL, shortLabel);
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_DURATION_RATING, durationRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_COST_RATING, costRating);
		addField(PSEUDO_TAG_RATING_SUMMARY, tagRatingSummary);
		
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
		addField(PSEUDO_TAG_IMPACT_RATING_VALUE, impactRatingLabel);
		addField(PSEUDO_TAG_DURATION_RATING_VALUE, durationRatingLabel);
		addField(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, feasibilityRatingLabel);
		addField(PSEUDO_TAG_COST_RATING_VALUE, costRatingLabel);
		addField(PSEUDO_TAG_RATING_SUMMARY_VALUE, tagRatingSummaryLabel);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String     STATUS_DRAFT = "Draft";
	public static final String     STATUS_REAL = "Real";
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_DURATION_RATING = "DurationRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_COST_RATING = "CostRating";
	public static final String PSEUDO_TAG_RATING_SUMMARY = "RatingSummary";
	
	public static final String PSEUDO_TAG_TAXONOMY_CODE_VALUE = "TaxonomyCodeValue";
	public static final String PSEUDO_TAG_IMPACT_RATING_VALUE = "ImpactRatingValue";
	public static final String PSEUDO_TAG_DURATION_RATING_VALUE = "DurationRatingValue";
	public static final String PSEUDO_TAG_FEASIBILITY_RATING_VALUE = "FeasibilityRatingValue";
	public static final String PSEUDO_TAG_COST_RATING_VALUE = "CostRatingValue";
	public static final String PSEUDO_TAG_RATING_SUMMARY_VALUE = "RatingSummaryValue";

	
	public static final String OBJECT_NAME = "Strategy";
	
	StringData status;
	StringData shortLabel;
	IdListData activityIds;
	
	StringData taxonomyCode;
	ChoiceData impactRating;
	ChoiceData durationRating;
	ChoiceData feasibilityRating;
	ChoiceData costRating;
	PseudoStringData tagRatingSummary;
	
	PseudoQuestionData taxonomyCodeLabel;
	PseudoQuestionData impactRatingLabel;
	PseudoQuestionData durationRatingLabel;
	PseudoQuestionData feasibilityRatingLabel;
	PseudoQuestionData costRatingLabel;
	PseudoQuestionData tagRatingSummaryLabel;
}
