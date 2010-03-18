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

import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.utils.EnhancedJsonObject;


public class Strategy extends Factor
{
	public Strategy(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, Factor.TYPE_STRATEGY);
		clear();
	}
	
	public Strategy(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, Factor.TYPE_STRATEGY, json);
	}

	
	public ORefList getResultsChains()
	{
		
		ORefList diagramRefs = new ORefList();
		ORefList resultsChainRefs = getProject().getPool(ResultsChainDiagram.getObjectType()).getORefList();
		for(int diagramIndex = 0; diagramIndex < resultsChainRefs.size(); ++diagramIndex)
		{
			ORef diagramRef = resultsChainRefs.get(diagramIndex);
			ResultsChainDiagram diagram = (ResultsChainDiagram)getProject().findObject(diagramRef);
			ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
			for(int diagramFactorIndex = 0; diagramFactorIndex < diagramFactorRefs.size(); ++diagramFactorIndex)
			{
				DiagramFactor diagramFactor = (DiagramFactor)getProject().findObject(diagramFactorRefs.get(diagramFactorIndex));
				if(diagramFactor.getWrappedORef().equals(getRef()))
					diagramRefs.add(diagramRef);
			}
		}
		
		return diagramRefs;
	}

	
	@Override
	public boolean isStrategy()
	{
		return true;
	}
	
	@Override
	public boolean canHaveObjectives()
	{
		return true;
	}
	
	public boolean isStatusReal()
	{
		return !isStatusDraft();
	}
	
	@Override
	public boolean isStatusDraft()
	{
		return STATUS_DRAFT.equals(status.get());
	}
	
	public void addActivity(ORef activityRef)
	{
		activityIds.add(activityRef.getObjectId());
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
	
	public ORefList getActivityRefs()
	{
		return new ORefList(Task.getObjectType(), getActivityIds());
	}
	
	public Vector<Task> getActivities()
	{
		Vector<Task> activities = new Vector();
		ORefList activityRefs = getActivityRefs();
		for (int index = 0; index < activityRefs.size(); ++index)
		{
			Task activity = Task.find(getProject(), activityRefs.get(index));
			activities.add(activity);
		}
		
		return activities;
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_RATING_SUMMARY))
			return getStrategyRatingSummary();
		
		if (fieldTag.equals(PSEUDO_TAG_TAXONOMY_CODE_VALUE))
			return new StrategyClassificationQuestion().findChoiceByCode(getTaxonomyCode()).getLabel();
		
		return super.getPseudoData(fieldTag);
	}

	public String getTaxonomyCode()
	{
		return taxonomyCode.get();
	}
	
	public String getStrategyRatingSummary()
	{
		ChoiceItem rating = getStrategyRating();
		return rating.getCode();
	}
	
	public ChoiceItem getStrategyRating()
	{
		StrategyRatingSummaryQuestion summary = new StrategyRatingSummaryQuestion();
		StrategyImpactQuestion impactQuestion = new StrategyImpactQuestion();
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion();
		
		ChoiceItem impact = impactQuestion.findChoiceByCode(impactRating.get());
		ChoiceItem feasibility = feasibilityQuestion.findChoiceByCode(feasibilityRating.get());
		ChoiceItem result = summary.getResult(impact, feasibility);

		return result;
	}
	
	@Override
	public Vector<Command> createCommandsToDeleteChildren() throws Exception
	{
		return createCommandsToDeleteBudgetChildren();
	}

	@Override
	public String toString()
	{
		return combineShortLabelAndLabel(getShortLabel().toString(), label.toString());
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getActivityRefs());
		
		return deepObjectRefsToCopy;
	}
	
	@Override
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectiveIds()));
				break;
		}
		return list;
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return Task.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return new int[] {};
	}
	
	public static int getObjectType()
	{
		return ObjectType.STRATEGY;
	}
	
	@Override
	public ORefList getSubTaskRefs()
	{
		return getActivityRefs();
	}
	
	public static Strategy find(ObjectManager objectManager, ORef strategyRef)
	{
		return (Strategy) objectManager.findObject(strategyRef);
	}
	
	public static Strategy find(Project project, ORef strategyRef)
	{
		return find(project.getObjectManager(), strategyRef);
	}
	
	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	@Override
	void clear()
	{
		super.clear();
		status = new StringData(TAG_STATUS);
		activityIds = new IdListData(TAG_ACTIVITY_IDS, Task.getObjectType());
	
		taxonomyCode = new ChoiceData(TAG_TAXONOMY_CODE, getQuestion(StrategyTaxonomyQuestion.class));
		impactRating = new ChoiceData(TAG_IMPACT_RATING, getQuestion(StrategyImpactQuestion.class));
		feasibilityRating = new ChoiceData(TAG_FEASIBILITY_RATING, getQuestion(StrategyFeasibilityQuestion.class));
		legacyTncStrategyRanking = new StringData(TAG_LEGACY_TNC_STRATEGY_RANKING);
	
		tagRatingSummary = new PseudoStringData(PSEUDO_TAG_RATING_SUMMARY);
		impactRatingLabel = new PseudoQuestionData(PSEUDO_TAG_IMPACT_RATING_VALUE, new StrategyImpactQuestion());
		feasibilityRatingLabel = new PseudoQuestionData(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, new StrategyFeasibilityQuestion());
		tagRatingSummaryLabel = new PseudoQuestionData(PSEUDO_TAG_RATING_SUMMARY_VALUE, new StrategyRatingSummaryQuestion());
		taxonomyCodeLabel = new PseudoQuestionData(PSEUDO_TAG_TAXONOMY_CODE_VALUE, new StrategyClassificationQuestion());
		
		addField(TAG_STATUS, status);
		addField(TAG_ACTIVITY_IDS, activityIds);
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_LEGACY_TNC_STRATEGY_RANKING, legacyTncStrategyRanking);
		
		addField(PSEUDO_TAG_RATING_SUMMARY, tagRatingSummary);
		addField(PSEUDO_TAG_IMPACT_RATING_VALUE, impactRatingLabel);
		addField(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, feasibilityRatingLabel);
		addField(PSEUDO_TAG_RATING_SUMMARY_VALUE, tagRatingSummaryLabel);
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_LEGACY_TNC_STRATEGY_RANKING = "LegacyTncStrategyRanking";
	
	public static final String PSEUDO_TAG_RATING_SUMMARY = "RatingSummary";
	public static final String PSEUDO_TAG_IMPACT_RATING_VALUE = "ImpactRatingValue";
	public static final String PSEUDO_TAG_FEASIBILITY_RATING_VALUE = "FeasibilityRatingValue";
	public static final String PSEUDO_TAG_RATING_SUMMARY_VALUE = "RatingSummaryValue";
	
	public static final String OBJECT_NAME = "Strategy";
	public static final String OBJECT_NAME_DRAFT = "Draft" + Strategy.OBJECT_NAME;
	
	private StringData status;
	private IdListData activityIds;
	
	private ChoiceData taxonomyCode;
	private ChoiceData impactRating;
	private ChoiceData feasibilityRating;
	private StringData legacyTncStrategyRanking;
	
	private PseudoStringData tagRatingSummary;
	private PseudoQuestionData impactRatingLabel;
	private PseudoQuestionData feasibilityRatingLabel;
	private PseudoQuestionData tagRatingSummaryLabel;
	private PseudoQuestionData taxonomyCodeLabel;
}
