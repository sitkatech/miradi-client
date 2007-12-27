/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.text.DecimalFormat;
import java.util.Set;

import org.conservationmeasures.eam.dialogs.planning.PlanningViewBudgetCalculator;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.ChoiceData;
import org.conservationmeasures.eam.objectdata.IdListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.questions.BudgetCostModeQuestion;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StrategyClassificationQuestion;
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
	
	public boolean isBudgetOverrideMode()
	{
		BudgetCostModeQuestion question = new BudgetCostModeQuestion(TAG_BUDGET_COST_MODE);
		ChoiceItem choice = question.findChoiceByCode(budgetCostMode.get());
		
		return choice.getCode().equals(BudgetCostModeQuestion.OVERRIDE_MODE_CODE);
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
		
		return super.getPseudoData(fieldTag);
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
		
		return super.getAnnotationType(tag);
	}

	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
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
		return ObjectType.STRATEGY;
	}
	
	private double getBudgetCostOverrideValue() throws Exception
	{
		String override = budgetCostOverride.get();
		if (override.length() == 0)
			return 0;
		
		return Double.parseDouble(override);
	}
		
	public String getBudgetCostAsString()
	{
		try
		{
			if (isBudgetOverrideMode())
				return formateResults(getBudgetCostOverrideValue());
			
			return new PlanningViewBudgetCalculator(getProject()).getBudgetTotals(getRef());
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.logWarning("Error occurred while calculating budget total for strategy");
			return "";
		}
	}
	
	private String formateResults(double cost)
	{
		DecimalFormat formater = objectManager.getProject().getCurrencyFormatter();
		return formater.format(cost);
	}

	void clear()
	{
		super.clear();
		status = new StringData();
		activityIds = new IdListData(Task.getObjectType());
	
		taxonomyCode = new StringData();
		impactRating = new ChoiceData();
		feasibilityRating = new ChoiceData();
		budgetCostOverride = new StringData();
		budgetCostMode = new ChoiceData();
	
		tagRatingSummary = new PseudoStringData(PSEUDO_TAG_RATING_SUMMARY);
		taxonomyCodeLabel = new PseudoQuestionData(new StrategyClassificationQuestion(TAG_TAXONOMY_CODE));
		impactRatingLabel = new PseudoQuestionData(new StrategyImpactQuestion(TAG_IMPACT_RATING));
		feasibilityRatingLabel = new PseudoQuestionData(new StrategyFeasibilityQuestion(TAG_FEASIBILITY_RATING));
		tagRatingSummaryLabel = new PseudoQuestionData(new StrategyRatingSummaryQuestion(PSEUDO_TAG_RATING_SUMMARY));
		
		addField(TAG_STATUS, status);
		addField(TAG_ACTIVITY_IDS, activityIds);
		
		addField(TAG_TAXONOMY_CODE, taxonomyCode);
		addField(TAG_IMPACT_RATING, impactRating);
		addField(TAG_FEASIBILITY_RATING, feasibilityRating);
		addField(TAG_BUDGET_COST_OVERRIDE, budgetCostOverride);
		addField(TAG_BUDGET_COST_MODE, budgetCostMode);
		
		addField(PSEUDO_TAG_RATING_SUMMARY, tagRatingSummary);
		addField(PSEUDO_TAG_TAXONOMY_CODE_VALUE, taxonomyCodeLabel);
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
	public final static String TAG_BUDGET_COST_OVERRIDE = "BudgetCostOverride";
	public final static String TAG_BUDGET_COST_MODE = "BudgetCostMode";
	
	public static final String PSEUDO_TAG_RATING_SUMMARY = "RatingSummary";
	public static final String PSEUDO_TAG_TAXONOMY_CODE_VALUE = "TaxonomyCodeValue";
	public static final String PSEUDO_TAG_IMPACT_RATING_VALUE = "ImpactRatingValue";
	public static final String PSEUDO_TAG_FEASIBILITY_RATING_VALUE = "FeasibilityRatingValue";
	public static final String PSEUDO_TAG_RATING_SUMMARY_VALUE = "RatingSummaryValue";
	
	public static final String OBJECT_NAME = "Strategy";
	public static final String OBJECT_NAME_DRAFT = "Draft" + Strategy.OBJECT_NAME;
	
	private StringData status;
	private IdListData activityIds;
	
	private StringData taxonomyCode;
	private ChoiceData impactRating;
	private ChoiceData feasibilityRating;
	private StringData budgetCostOverride;
	private ChoiceData budgetCostMode;
	
	private PseudoStringData tagRatingSummary;
	private PseudoQuestionData taxonomyCodeLabel;
	private PseudoQuestionData impactRatingLabel;
	private PseudoQuestionData feasibilityRatingLabel;
	private PseudoQuestionData tagRatingSummaryLabel;
}
