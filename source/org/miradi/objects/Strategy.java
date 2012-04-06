/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.planning.upperPanel.rebuilder.AbstractTreeRebuilder;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.StrategyStatusQuestion;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.CommandVector;
import org.miradi.utils.EnhancedJsonObject;


public class Strategy extends Factor
{
	public Strategy(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, new StrategySchema());
	}
	
	public Strategy(ObjectManager objectManager, FactorId idToUse, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, idToUse, json, new StrategySchema());
	}

	
	public ORefList getResultsChains()
	{
		
		ORefList diagramRefs = new ORefList();
		ORefList resultsChainRefs = getProject().getPool(ResultsChainDiagramSchema.getObjectType()).getORefList();
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
		return getData(TAG_STATUS).equals(StrategyStatusQuestion.STATUS_DRAFT_CODE);
	}
	
	public IdList getActivityIds()
	{
		return getIdListData(TAG_ACTIVITY_IDS);
	}
	
	public ORefList getActivityRefs()
	{
		return new ORefList(TaskSchema.getObjectType(), getActivityIds());
	}
	
	public Vector<Task> getActivities()
	{
		Vector<Task> activities = new Vector<Task>();
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
		
		if (fieldTag.equals(PSEUDO_TAG_ACTIVITIES))
			return getBaseObjectLabelsOnASingleLine(getActivityRefs());

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS))
			return getRelevantObjectivesRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_GOAL_REFS))
			return getRelevantGoalRefsAsString();
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getRelevantObjectivesRefsAsString()
	{
		try
		{
			return getRelevantObjectiveRefs().toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	private String getRelevantGoalRefsAsString()
	{
		try
		{
			return getRelevantGoalRefs().toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public ORefList getRelevantObjectiveRefs() throws Exception
	{
		ORefSet relevantObjectives = new ORefSet(Desire.findAllRelevantDesires(getProject(), getRef(), ObjectiveSchema.getObjectType()));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		return calculateRelevantRefList(relevantObjectives, relevantOverrides);
	}
	
	public ORefList getRelevantGoalRefs() throws Exception
	{
		ORefSet relevantGoals = new ORefSet(AbstractTreeRebuilder.findRelevantGoals(getProject(), getRef()));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
	
		return calculateRelevantRefList(relevantGoals, relevantOverrides);
	}
	
	public String getTaxonomyCode()
	{
		return getData(TAG_TAXONOMY_CODE);
	}
	
	public String getStrategyRatingSummary()
	{
		ChoiceItem rating = getStrategyRating();
		return rating.getCode();
	}
	
	public ChoiceItem getStrategyRating()
	{
		ChoiceItem impact = getChoiceItemData(TAG_IMPACT_RATING);
		ChoiceItem feasibility = getChoiceItemData(TAG_FEASIBILITY_RATING);

		StrategyRatingSummaryQuestion summary = new StrategyRatingSummaryQuestion();
		ChoiceItem result = summary.getResult(impact, feasibility);

		return result;
	}
	
	@Override
	public String toString()
	{
		return combineShortLabelAndLabel(getShortLabel(), getLabel());
	}
	
	@Override
	public ORefList getAllObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getActivityRefs());
		
		return deepObjectRefsToCopy;
	}
	
	@Override
	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = super.createCommandsToDeleteChildren();
		commandsToDeleteChildren.addAll(createCommandsToDeleteBudgetChildren());
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_PROGRESS_REPORT_REFS));
		
		return commandsToDeleteChildren;
	}
	
	@Override
	protected CommandVector createCommandsToDereferenceObject() throws Exception
	{
		CommandVector commandsToDereferences = super.createCommandsToDereferenceObject();
		commandsToDereferences.addAll(buildRemoveFromRelevancyListCommands(getRef()));
		
		return commandsToDereferences;
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
			case ObjectType.RESOURCE_ASSIGNMENT: 
				list.addAll(getResourceAssignmentRefs());
				break;
			case ObjectType.EXPENSE_ASSIGNMENT:
				list.addAll(getExpenseAssignmentRefs());
				break;
			case ObjectType.PROGRESS_REPORT:
				list.addAll(getRefListData(TAG_PROGRESS_REPORT_REFS));
				break;
		}
		return list;
	}
	
	@Override
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return TaskSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public boolean isIdListTag(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return true;
		
		if (tag.equals(TAG_RESOURCE_ASSIGNMENT_IDS))
			return true;
		
		return super.isIdListTag(tag);
	}
	
	@Override
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_EXPENSE_ASSIGNMENT_REFS))
			return true;
		
		if (tag.equals(TAG_PROGRESS_REPORT_REFS))
			return true;
		
		return super.isRefList(tag);
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
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
		return objectType == StrategySchema.getObjectType();
	}
	
	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_STATUS = "Status";
	
	public static final String TAG_TAXONOMY_CODE = "TaxonomyCode";
	public static final String TAG_IMPACT_RATING = "ImpactRating";
	public static final String TAG_FEASIBILITY_RATING = "FeasibilityRating";
	public static final String TAG_LEGACY_TNC_STRATEGY_RANKING = "LegacyTncStrategyRanking";
	
	public static final String PSEUDO_TAG_RATING_SUMMARY = "RatingSummary";
	public static final String PSEUDO_TAG_IMPACT_RATING_VALUE = "ImpactRatingValue";
	public static final String PSEUDO_TAG_FEASIBILITY_RATING_VALUE = "FeasibilityRatingValue";
	public static final String PSEUDO_TAG_RATING_SUMMARY_VALUE = "RatingSummaryValue";
	public static final String PSEUDO_TAG_ACTIVITIES = "PseudoTagActivities";
	public static final String PSEUDO_TAG_RELEVANT_GOAL_REFS = "PseudoStrategyRelevantGoalRefs";
	public static final String PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS = "PseudoStrategyRelevantObjectiveRefs";
	
	public static final String OBJECT_NAME_DRAFT = "Draft" + StrategySchema.OBJECT_NAME;
	
}
