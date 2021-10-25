/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import org.miradi.objecthelpers.*;
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


public class Strategy extends Factor
{
	public Strategy(ObjectManager objectManager, FactorId idToUse)
	{
		super(objectManager, idToUse, createSchema(objectManager));
	}

	public static StrategySchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static StrategySchema createSchema(ObjectManager objectManager)
	{
		return (StrategySchema) objectManager.getSchemas().get(ObjectType.STRATEGY);
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

	@Override
	public boolean canHaveOutputs()
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
		return getSafeIdListData(TAG_ACTIVITY_IDS);
	}
	
	public ORefList getActivityRefs()
	{
		return new ORefList(TaskSchema.getObjectType(), getActivityIds());
	}
	
	public ORefList getMonitoringActivityRefs()
	{
		Vector<Task> monitoringActivities = getMonitoringActivities();
		return new ORefList(monitoringActivities);
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
	
	public Vector<Task> getMonitoringActivities()
	{
		Vector<Task> activities = new Vector<Task>();
		ORefList activityRefs = getActivityRefs();
		for (int index = 0; index < activityRefs.size(); ++index)
		{
			Task activity = Task.find(getProject(), activityRefs.get(index));
			if (activity.isMonitoringActivity())
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
			return getLabelsAsMultiline(getActivityRefs());

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS))
			return getRelevantObjectivesRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_GOAL_REFS))
			return getRelevantGoalRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();

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

	@Override
	public ORefList getIndicatorsOnSameFactor()
	{
		return getOnlyDirectIndicatorRefs();
	}

	@Override
	public ORefList getRelevantIndicatorRefList() throws Exception
	{
		return getRelevantIndicatorRefs();
	}

	protected String getRelevantIndicatorRefsAsString()
	{
		try
		{
			return getRelevantIndicatorRefs().toString();
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

	public ORefList getRelevantIndicatorRefs() throws Exception
	{
		ORefSet relevantIndicators = new ORefSet(AbstractTreeRebuilder.findRelevantIndicators(getProject(), getRef()));
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();

		return calculateRelevantRefList(relevantIndicators, relevantOverrides);
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
	protected ORefList getNonOwnedObjectsToDeepCopy(ORefList deepCopiedFactorRefs)
	{
		ORefList deepObjectRefsToCopy = super.getNonOwnedObjectsToDeepCopy(deepCopiedFactorRefs);
		deepObjectRefsToCopy.addAll(getActivityRefs());
		
		return deepObjectRefsToCopy;
	}
	
	@Override
	public CommandVector createCommandsToDeleteChildren() throws Exception
	{
		CommandVector commandsToDeleteChildren  = super.createCommandsToDeleteChildren();
		commandsToDeleteChildren.addAll(createCommandsToDeleteBudgetChildren());
		commandsToDeleteChildren.addAll(createCommandsToDeleteRefs(TAG_OUTPUT_REFS));
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
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_ACTIVITY_IDS))
			return TaskSchema.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	@Override
	public ORefList getChildTaskRefs()
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
	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";

	public static final String OBJECT_NAME_DRAFT = "Draft" + StrategySchema.OBJECT_NAME;
}
