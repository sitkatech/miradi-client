/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.objectdata.ChoiceData;
import org.miradi.objectdata.IdListData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.StrategyFeasibilityQuestion;
import org.miradi.questions.StrategyImpactQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.StrategyTaxonomyQuestion;
import org.miradi.utils.DateRange;
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

	
	public static boolean canOwnThisType(int type)
	{
		if (Factor.canOwnThisType(type))
			return true;
		
		switch(type)
		{
			case ObjectType.OBJECTIVE: 
				return true;
			default:
				return false;
		}
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
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_RATING_SUMMARY))
			return getStrategyRatingSummary();
		
		if (fieldTag.equals(PSEUDO_TAG_TAXONOMY_CODE_VALUE))
			return new StrategyClassificationQuestion().findChoiceByCode(taxonomyCode.get()).getLabel();
		
		return super.getPseudoData(fieldTag);
	}
	
	@Override
	public ProgressReport getLatestProgressReport()
	{
		ProgressReport progressReport = (ProgressReport) getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
		return progressReport;
	}
	
	public ORefList getProgressReportRefs()
	{
		return progressReportRefs.getORefList();
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

	public String toString()
	{
		return combineShortLabelAndLabel(getShortLabel().toString(), label.toString());
	}
	
	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getActivityRefs());
		deepObjectRefsToCopy.addAll(getProgressReportRefs());
		
		return deepObjectRefsToCopy;
	}
	
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
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return Task.getObjectType();
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return ProgressReport.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	public boolean isRefList(String tag)
	{
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
		return ObjectType.STRATEGY;
	}
	
	public double getBudgetCostRollup(DateRange dateRangeToUse) throws Exception
	{
		return getTasksBudgetCostRollUp(dateRangeToUse, getActivityRefs());
	}
	
	public DateRange getWhenRollup() throws Exception
	{
		return combineSubtaskEffortListDateRanges(getActivityRefs());
	}
	
	public ORefSet getWhoRollup() throws Exception
	{
		return getAllResources(getActivityRefs());
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
	
	void clear()
	{
		super.clear();
		status = new StringData(TAG_STATUS);
		activityIds = new IdListData(TAG_ACTIVITY_IDS, Task.getObjectType());
	
		taxonomyCode = new ChoiceData(TAG_TAXONOMY_CODE, getQuestion(StrategyTaxonomyQuestion.class));
		impactRating = new ChoiceData(TAG_IMPACT_RATING, getQuestion(StrategyImpactQuestion.class));
		feasibilityRating = new ChoiceData(TAG_FEASIBILITY_RATING, getQuestion(StrategyFeasibilityQuestion.class));
		progressReportRefs = new ORefListData(TAG_PROGRESS_REPORT_REFS);
		legacyTncStrategyRanking = new StringData(TAG_LEGACY_TNC_STRATEGY_RANKING);
	
		tagRatingSummary = new PseudoStringData(PSEUDO_TAG_RATING_SUMMARY);
		impactRatingLabel = new PseudoQuestionData(PSEUDO_TAG_IMPACT_RATING_VALUE, new StrategyImpactQuestion());
		feasibilityRatingLabel = new PseudoQuestionData(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, new StrategyFeasibilityQuestion());
		tagRatingSummaryLabel = new PseudoQuestionData(PSEUDO_TAG_RATING_SUMMARY_VALUE, new StrategyRatingSummaryQuestion());
		
		addField(TAG_STATUS, status);
		addField(TAG_ACTIVITY_IDS, activityIds);
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		addField(TAG_LEGACY_TNC_STRATEGY_RANKING, legacyTncStrategyRanking);
		
		addField(PSEUDO_TAG_RATING_SUMMARY, tagRatingSummary);
		addField(PSEUDO_TAG_IMPACT_RATING_VALUE, impactRatingLabel);
		addField(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, feasibilityRatingLabel);
		addField(PSEUDO_TAG_RATING_SUMMARY_VALUE, tagRatingSummaryLabel);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
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
	private ORefListData progressReportRefs;
	private StringData legacyTncStrategyRanking;
	
	private PseudoStringData tagRatingSummary;
	private PseudoQuestionData impactRatingLabel;
	private PseudoQuestionData feasibilityRatingLabel;
	private PseudoQuestionData tagRatingSummaryLabel;
}
