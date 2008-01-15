/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.ProgressReportStatusQuestion;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
import org.conservationmeasures.eam.questions.StrategyFeasibilityQuestion;
import org.conservationmeasures.eam.questions.StrategyImpactQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.utils.DateRange;
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
			default:
				return false;
		}
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
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_ACTIVITY_IDS);
		set.add(TAG_OBJECTIVE_IDS);
		set.add(TAG_PROGRESS_REPORT_REFS);
		return set;
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
	
	public String getPseudoData(String fieldTag)
	{
		if(fieldTag.equals(PSEUDO_TAG_RATING_SUMMARY))
			return getStrategyRatingSummary();
		
		if(fieldTag.equals(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return getLatestProgressReportDate();
		
		return super.getPseudoData(fieldTag);
	}

	private String  getLatestProgressReportDate()
	{
		ProgressReport progressReprot = (ProgressReport) Indicator.getLatestObject(getObjectManager(), getProgressReportRefs(), ProgressReport.TAG_PROGRESS_DATE);
		if (progressReprot == null)
			return "";
		
		return progressReprot.getProgressStatusChoice().getCode();
	}
	
	public ORefList getProgressReportRefs()
	{
		return progressReportRefs.getORefList();
	}
	
	public ORefList getActivities()
	{
		return new ORefList(Task.getObjectType(), getActivityIds());
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
		StrategyFeasibilityQuestion feasibilityQuestion = new StrategyFeasibilityQuestion("");
		
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
		deepObjectRefsToCopy.addAll(getActivities());
		deepObjectRefsToCopy.addAll(getProgressReportRefs());
		
		return deepObjectRefsToCopy;
	}
	
	public ORefList getOwnedObjects(int objectType)
	{
		ORefList list = super.getOwnedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.OBJECTIVE: 
				list.addAll(new ORefList(objectType, getObjectives()));
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
		double total = 0.0;
		ORefList activityRefs = getActivityRefs();
		for(int i = 0; i < activityRefs.size(); ++i)
		{
			Task activity = Task.find(getProject(), activityRefs.get(i));
			total += activity.getProportionalBudgetCost(dateRangeToUse);
		}

		return total;
	}
	
	public static boolean is(ORef ref)
	{
		return ref.getObjectType() == getObjectType();
	}
	
	void clear()
	{
		super.clear();
		status = new StringData();
		activityIds = new IdListData(Task.getObjectType());
	
		taxonomyCode = new StringData();
		impactRating = new ChoiceData();
		feasibilityRating = new ChoiceData();
		progressReportRefs = new ORefListData();
	
		tagRatingSummary = new PseudoStringData(PSEUDO_TAG_RATING_SUMMARY);
		taxonomyCodeLabel = new PseudoQuestionData(new StrategyClassificationQuestion(TAG_TAXONOMY_CODE));
		impactRatingLabel = new PseudoQuestionData(new StrategyImpactQuestion(TAG_IMPACT_RATING));
		feasibilityRatingLabel = new PseudoQuestionData(new StrategyFeasibilityQuestion(TAG_FEASIBILITY_RATING));
		tagRatingSummaryLabel = new PseudoQuestionData(new StrategyRatingSummaryQuestion(PSEUDO_TAG_RATING_SUMMARY));
		latestProgressReport = new PseudoQuestionData(new ProgressReportStatusQuestion(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE));
		
		addField(TAG_STATUS, status);
		addField(TAG_ACTIVITY_IDS, activityIds);
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_PROGRESS_REPORT_REFS, progressReportRefs);
		
		addField(PSEUDO_TAG_RATING_SUMMARY, tagRatingSummary);
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
		addField(PSEUDO_TAG_IMPACT_RATING_VALUE, impactRatingLabel);
		addField(PSEUDO_TAG_FEASIBILITY_RATING_VALUE, feasibilityRatingLabel);
		addField(PSEUDO_TAG_RATING_SUMMARY_VALUE, tagRatingSummaryLabel);
		addField(PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE, latestProgressReport);
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	public static final String STATUS_DRAFT = "Draft";
	public static final String STATUS_REAL = "Real";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public static final String PSEUDO_TAG_RATING_SUMMARY = "RatingSummary";
	public static final String PSEUDO_TAG_TAXONOMY_CODE_VALUE = "TaxonomyCodeValue";
	public static final String PSEUDO_TAG_IMPACT_RATING_VALUE = "ImpactRatingValue";
	public static final String PSEUDO_TAG_FEASIBILITY_RATING_VALUE = "FeasibilityRatingValue";
	public static final String PSEUDO_TAG_RATING_SUMMARY_VALUE = "RatingSummaryValue";
	public static final String PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE = "PseudoLatestProgressReportDate";
	
	public static final String OBJECT_NAME = "Strategy";
	public static final String OBJECT_NAME_DRAFT = "Draft" + Strategy.OBJECT_NAME;
	
	private StringData status;
	private IdListData activityIds;
	
	private StringData taxonomyCode;
	private ChoiceData impactRating;
	private ChoiceData feasibilityRating;
	private ORefListData progressReportRefs;
	private PseudoStringData tagRatingSummary;
	private PseudoQuestionData taxonomyCodeLabel;
	private PseudoQuestionData impactRatingLabel;
	private PseudoQuestionData feasibilityRatingLabel;
	private PseudoQuestionData tagRatingSummaryLabel;
	private PseudoQuestionData latestProgressReport;
}
