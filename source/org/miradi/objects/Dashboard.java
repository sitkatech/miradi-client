/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.util.Collection;
import java.util.HashSet;
import java.util.Vector;

import org.miradi.diagram.ThreatTargetChainWalker;
import org.miradi.dialogs.dashboard.DashboardRowDefinition;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.StringChoiceMap;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.OpenStandardsProgressQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DoubleUtilities;
import org.miradi.utils.EnhancedJsonObject;

public class Dashboard extends BaseObject
{
	public Dashboard(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		
		clear();
	}
		
	public Dashboard(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.DASHBOARD;
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		try
		{
			if (fieldTag.equals(PSEUDO_TEAM_MEMBER_COUNT))
				return getObjectPoolCountAsString(ProjectResource.getObjectType());

			if (fieldTag.equals(PSEUDO_PROJECT_SCOPE_WORD_COUNT))
				return getProjectScopeWordCount();

			if (fieldTag.equals(PSEUDO_TARGET_COUNT))
				return getObjectPoolCountAsString(Target.getObjectType());

			if (fieldTag.equals(PSEUDO_HUMAN_WELFARE_TARGET_COUNT))
				return getObjectPoolCountAsString(HumanWelfareTarget.getObjectType());

			if (fieldTag.equals(PSEUDO_TARGET_WITH_KEA_COUNT))
				return getTargetWithKeaCount();

			if (fieldTag.equals(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT))
				return getTargetWithSimpleViabilityCount();

			if (fieldTag.equals(PSEUDO_THREAT_COUNT))
				return getThreatCount();

			if (fieldTag.equals(PSEUDO_THREAT_WITH_TAXONOMY_COUNT))
				return getThreatWithTaxonomyCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_COUNT))
				return getThreatTargetLinkCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT))
				return getThreatTargetLinkWithRatingCount();
			
			if (fieldTag.equals(PSEUDO_TARGETS_WITH_GOALS_COUNT))
				return getTargetWithGoalCount();
			
			if (fieldTag.equals(PSEUDO_GOAL_COUNT))
				return getObjectPoolCountAsString(Goal.getObjectType());
			
			if (fieldTag.equals(PSEUDO_DRAFT_STRATEGY_COUNT))
				return getDraftStrategyCount();
			
			if (fieldTag.equals(PSEUDO_RANKED_DRAFT_STRATEGY_COUNT))
				return getRankedDraftStrategyCount();
			
			if (fieldTag.equals(PSEUDO_STRATEGY_COUNT))
				return getStrategyCount();
			
			if (fieldTag.equals(PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT))
				return getStrategyWithTaxonomyCount();
			
			if (fieldTag.equals(PSEUDO_RESULTS_CHAIN_COUNT))
				return getObjectPoolCountAsString(ResultsChainDiagram.getObjectType());
			
			if (fieldTag.equals(PSEUDO_OBJECTIVE_COUNT))
				return getObjectPoolCountAsString(Objective.getObjectType());
			
			if (fieldTag.equals(PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT))
				return getResultsChainWithObjectiveCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE))
				return getRelevantObjectivesRelevantToStrategiesPercentage();
			
			if (fieldTag.equals(PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT))
				return getIrrelenvatStratiesToObjectivesCount();
			
			if (fieldTag.equals(PSEUDO_KEA_INDICATORS_COUNT))
				return getKeaIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_FACTOR_INDICATORS_COUNT))
				return getFactorIndicatorCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE))
				return getObjectiveRelevantToIndicatorsPercentage();
			
			if (fieldTag.equals(PSEUDO_PROJECT_PLANNING_START_DATE))
				return getProject().getProjectCalendar().getPlanningStartDate();
			
			if (fieldTag.equals(PSEUDO_PROJECT_PLANNING_END_DATE))
				return getProject().getProjectCalendar().getPlanningEndDate();
			
			if (fieldTag.equals(PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT))
				return getStrategiesWithActivitiesCount();
			
			if (fieldTag.equals(PSEUDO_ACTIVITIES_COUNT))
				return getActivitiesCount();
			
			if (fieldTag.equals(PSEUDO_ACTIVITIES_AND_TASKS_COUNT))
				return getSpecifiedTaskTypeAndTasksCount(Task.ACTIVITY_NAME);
			
			if (fieldTag.equals(PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT))
				return getSpecifiedTaskTypeAndTasksWithAssignmentsCount(Task.ACTIVITY_NAME);
			
			if (fieldTag.equals(PSEUDO_INDICATORS_WITH_METHODS_COUNT))
				return getIndicatorsWithMethodsCount();
			
			if (fieldTag.equals(PSEUDO_METHODS_COUNT))
				return getMethodsCount();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_COUNT))
				return getObjectPoolCountAsString(Indicator.getObjectType());
			
			if (fieldTag.equals(PSEUDO_METHODS_AND_TASKS_COUNT))
				return getSpecifiedTaskTypeAndTasksCount(Task.METHOD_NAME);
			
			if (fieldTag.equals(PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT))
				return getSpecifiedTaskTypeAndTasksWithAssignmentsCount(Task.METHOD_NAME);
			
			if (fieldTag.equals(PSEUDO_WORK_PLAN_START_DATE))
				return getProject().getMetadata().getWorkPlanStartDateAsString();
			
			if (fieldTag.equals(PSEUDO_WORK_PLAN_END_DATE))
				return getProject().getMetadata().getWorkPlanEndDate();
			
			if (fieldTag.equals(PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS))
				return getTotalProjectResourcesCost();
			
			if (fieldTag.equals(PSEUDO_TOTAL_PROJECT_EXPENSES))
				return getTotalProjectExpenses();
			
			if (fieldTag.equals(PSEUDO_PROJECT_BUDGET))
				return getProjectBudget();
			
			if (fieldTag.equals(PSEUDO_CURRENCY_SYMBOL))
				return getCurrencySymbol();
			
			if (fieldTag.equals(PSEUDO_BUDGET_SECURED_PERCENT))
				return getTotalBudgetSecuredPercent();
			
			if (fieldTag.equals(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT))
				return getStrategiesAndActivitiesWithProgressReportCount();
			
			if (fieldTag.equals(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT))
				return getStrategiesAndActivitiesWithProgressReportPercent();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT))
				return getIndicatorsAndMethodsWithProgressReportCount();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT))
				return getIndicatorAndMethodsWithProgressReportPerncet();
			
			if (fieldTag.equals(PSEUDO_EFFECTIVE_STATUS_MAP))
				return getEffectiveStatusMap();
				
			return super.getPseudoData(fieldTag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error Retrieving Data");
		}
	}
	
	private String getEffectiveStatusMap() throws Exception
	{
		StringChoiceMap map = new StringChoiceMap();
		CodeList allThirdLevelCodes = getDashboardRowDefinitionManager().getThirdLevelCodes();
		for (int index = 0; index < allThirdLevelCodes.size(); ++index)
		{
			String thirdLevelCode = allThirdLevelCodes.get(index);
			Vector<DashboardRowDefinition> rowDefinitions = getDashboardRowDefinitionManager().getRowDefinitions(thirdLevelCode);
			map.add(thirdLevelCode, computeStatusCodeFromStatistics(rowDefinitions));
		}
		
		return map.toString();
	}

	public DashboardRowDefinitionManager getDashboardRowDefinitionManager()
	{
		if (rowDefinitionManager == null)
			rowDefinitionManager = new DashboardRowDefinitionManager();
		
		return rowDefinitionManager;
	}

	private String computeStatusCodeFromStatistics(Vector<DashboardRowDefinition> rowDefinitions)
	{
		Vector<String> pseudoValues = new Vector<String>();
		for (DashboardRowDefinition rowDefinition: rowDefinitions)
		{
			Vector<String> pseudoTags = rowDefinition.getPseudoTags();
			for (String pseudoTag: pseudoTags)
			{
				String pseudoDataValue = getPseudoData(pseudoTag);
				pseudoValues.add(pseudoDataValue);
			}
		}
		
		return getStatusCode(pseudoValues);
	}
	
	private String getStatusCode(Collection<String> rawDataValues)
	{
		if (rawDataValues.isEmpty())
			return OpenStandardsProgressQuestion.NOT_SPECIFIED_CODE;
		
		int valuesWithDataCount = 0;
		for (String rawData : rawDataValues)
		{
			if (rawData.length() > 0 && !rawData.equals("0"))
				++valuesWithDataCount;
		}
		
		if (valuesWithDataCount == 0)
			return OpenStandardsProgressQuestion.NOT_STARTED_CODE;
			
		if (valuesWithDataCount < rawDataValues.size())
			return OpenStandardsProgressQuestion.NOT_STARTED_CODE;
			
		return OpenStandardsProgressQuestion.IN_PROGRESS_CODE;
	}
	
	private String getIndicatorAndMethodsWithProgressReportPerncet() throws Exception
	{
		int strategiesAndActiviesWithProgressReports = getIndicatorsAndMethodsWithProgressReports().size();
		int allStrategiesAndActivitiesCount = getAllIndicatorsAndMethods().size();
		
		return calculatePercentage(strategiesAndActiviesWithProgressReports, allStrategiesAndActivitiesCount);
	}

	private String getIndicatorsAndMethodsWithProgressReportCount() throws Exception
	{
		return Integer.toString(getIndicatorsAndMethodsWithProgressReports().size());
	}
	
	private Vector<BaseObject> getAllIndicatorsAndMethods()
	{
		Vector<BaseObject> baseObjects = new Vector<BaseObject>();
		baseObjects.addAll(getProject().getIndicatorPool().getAllObjects());
		baseObjects.addAll(getProject().getTaskPool().getAllMethods());
		
		return baseObjects;
	}
	
	private Vector<BaseObject> getIndicatorsAndMethodsWithProgressReports() throws Exception
	{
		return getBaseObjectsWithProgressReports(getAllIndicatorsAndMethods());
	}
	
	private String getStrategiesAndActivitiesWithProgressReportPercent() throws Exception
	{
		int strategiesAndActiviesWithProgressReports = getStrategiesAndActiviesWithProgressReports().size();
		int allStrategiesAndActivitiesCount = getAllStrategiesAndActivities().size();
		
		return calculatePercentage(strategiesAndActiviesWithProgressReports, allStrategiesAndActivitiesCount);
	}
	
	private Vector<BaseObject> getAllStrategiesAndActivities()
	{
		Vector<BaseObject> baseObjects = new Vector<BaseObject>();
		baseObjects.addAll(getProject().getStrategyPool().getAllObjects());
		baseObjects.addAll(getProject().getTaskPool().getAllActivities());
		
		return baseObjects;
	}

	private Vector<BaseObject> getStrategiesAndActiviesWithProgressReports() throws Exception
	{
		return getBaseObjectsWithProgressReports(getAllStrategiesAndActivities());
	}
	
	private String getStrategiesAndActivitiesWithProgressReportCount() throws Exception
	{
		return Integer.toString(getStrategiesAndActiviesWithProgressReports().size());
	}

	private Vector<BaseObject> getBaseObjectsWithProgressReports(Vector<BaseObject> baseObjects) throws Exception
	{
		Vector<BaseObject> objectsWithProgressReports = new Vector<BaseObject>();
		for (BaseObject baseObject : baseObjects)
		{
			if (baseObject.getRefList(BaseObject.TAG_PROGRESS_REPORT_REFS).hasRefs())
				objectsWithProgressReports.add(baseObject);
		}
		
		return objectsWithProgressReports;
	}

	private String getTotalBudgetSecuredPercent()
	{
		return getProject().getMetadata().getData(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
	}

	private String getTotalProjectResourcesCost() throws Exception
	{
		return calculateTotalProjectCost().getTotalWorkUnits().toString();
	}

	private String getTotalProjectExpenses() throws Exception
	{
		return calculateTotalProjectCost().getTotalExpense().toString();
	}

	private TimePeriodCosts calculateTotalProjectCost() throws Exception
	{
		return getProject().getProjectTotalCalculator().calculateProjectTotals().calculateTotalBudgetCost();
	}

	private String getProjectBudget()
	{
		return getProject().getMetadata().getData(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
	}

	private String getCurrencySymbol()
	{
		return "\\" + getProject().getMetadata().getData(ProjectMetadata.TAG_CURRENCY_SYMBOL);
	}

	private String getMethodsCount()
	{
		int count = getProject().getTaskPool().getAllMethods().size();
		return Integer.toString(count);
	}

	private String getIndicatorsWithMethodsCount()
	{
		ORefSet indicatorRefs = getProject().getIndicatorPool().getRefSet();
		ORefSet indicatorsWithMethods = new ORefSet();
		for (ORef indicatorRef : indicatorRefs)
		{
			Indicator indicator = Indicator.find(getProject(), indicatorRef);
			if (indicator.getMethodRefs().hasRefs())
				indicatorsWithMethods.add(indicatorRef);
		}
		
		return Integer.toString(indicatorsWithMethods.size());
	}

	private String getSpecifiedTaskTypeAndTasksWithAssignmentsCount(String taskObjectTypeName) throws Exception
	{
		Vector<Task> specifiedTaskAndTasks = getSpecifiedTaskTypeAndTasks(taskObjectTypeName);
		HashSet<Task> tasksWithAssignments = new HashSet<Task>();
		for (Task task : specifiedTaskAndTasks)
		{
			if (task.getRefList(Task.TAG_EXPENSE_ASSIGNMENT_REFS).hasRefs())
				tasksWithAssignments.add(task);
			
			if (task.getRefList(Task.TAG_RESOURCE_ASSIGNMENT_IDS).hasRefs())
				tasksWithAssignments.add(task);
		}
		
		return Integer.toString(tasksWithAssignments.size());
	}

	protected Vector<Task> getSpecifiedTaskTypeAndTasks(String taskObjectTypeName)
	{
		Vector<Task> specifiedTaskAndTasks = new Vector<Task>();
		specifiedTaskAndTasks.addAll(getProject().getTaskPool().getAllTasks());
		specifiedTaskAndTasks.addAll(getProject().getTaskPool().getTasks(taskObjectTypeName));
		
		return specifiedTaskAndTasks;
	}

	private String getSpecifiedTaskTypeAndTasksCount(String taskObjectTypeName)
	{
		int count = getSpecifiedTaskTypeAndTasks(taskObjectTypeName).size();
		
		return Integer.toString(count);
	}

	private String getActivitiesCount()
	{
		int count = getProject().getTaskPool().getAllActivities().size();
		
		return Integer.toString(count);
	}

	private String getStrategiesWithActivitiesCount()
	{
		ORefSet strategyRefs = getProject().getStrategyPool().getRefSet();
		ORefSet strategyWithActivities = new ORefSet();
		for(ORef strategyRef : strategyRefs)
		{
			Strategy strategy = Strategy.find(getProject(), strategyRef);
			if (strategy.getActivityRefs().hasRefs())
				strategyWithActivities.add(strategyRef);
		}
		
		return Integer.toString(strategyWithActivities.size());
	}

	private String getObjectiveRelevantToIndicatorsPercentage() throws Exception
	{
		ORefSet objectiveRefs = getProject().getObjectivePool().getRefSet();
		ORefSet objectivesRelevantToIndictors = new ORefSet();
		for(ORef objectiveRef : objectiveRefs)
		{
			Objective objective = Objective.find(getProject(), objectiveRef);
			if (objective.getRelevantIndicatorRefList().hasRefs())
				objectivesRelevantToIndictors.add(objectiveRef);
		}
		
		return calculateRelevantPercentage(objectivesRelevantToIndictors, objectiveRefs);
	}

	private String getFactorIndicatorCount()
	{
		ORefSet keaIndicatorRefs = getKeaIndicatorRefs();
		ORefSet allIndicatorRefs = getProject().getIndicatorPool().getRefSet();
		allIndicatorRefs.removeAll(keaIndicatorRefs);
		
		return Integer.toString(allIndicatorRefs.size());
	}

	private String getKeaIndicatorsCount()
	{
		ORefSet keaIndicatorRefs = getKeaIndicatorRefs();
		
		return Integer.toString(keaIndicatorRefs.size());
	}

	protected ORefSet getKeaIndicatorRefs()
	{
		ORefSet keaIndicatorRefs = new ORefSet();
		KeyEcologicalAttribute[] keas = getProject().getKeyEcologicalAttributePool().getAllKeyEcologicalAttribute();
		for (int index = 0; index < keas.length; ++index)
		{
			KeyEcologicalAttribute kea = keas[index];
			keaIndicatorRefs.addAllRefs(kea.getIndicatorRefs());
		}
		return keaIndicatorRefs;
	}

	private String getIrrelenvatStratiesToObjectivesCount() throws Exception
	{
		Vector<Strategy> strategies = getProject().getStrategyPool().getNonDraftStrategiesAsVector();
		Vector<Strategy> irrelevantStrategiesToObjectives = new Vector<Strategy>();
		ORefSet strategiesRelevantToObjectives = getStrategiesRelevantToObjectives();
		for (Strategy strategy : strategies)
		{	
			if (strategy.getObjectiveRefs().isEmpty() && !strategiesRelevantToObjectives.contains(strategy.getRef()))
				irrelevantStrategiesToObjectives.add(strategy);
		}
		
		return Integer.toString(irrelevantStrategiesToObjectives.size());
	}

	private String getRelevantObjectivesRelevantToStrategiesPercentage() throws Exception
	{
		ORefSet objectiveRefs = getProject().getObjectivePool().getRefSet();
		ORefSet objectivesRelevantToStrategies = new ORefSet();
		for(ORef objectiveRef : objectiveRefs)
		{
			Objective objective = Objective.find(getProject(), objectiveRef);
			if (objective.getRelevantStrategyRefs().hasRefs())
				objectivesRelevantToStrategies.add(objectiveRef);
		}
		
		return calculateRelevantPercentage(objectivesRelevantToStrategies, objectiveRefs);
	}

	private String calculateRelevantPercentage(ORefSet objectivesRelevantToStrategies, ORefSet objectiveRefs)
	{
		return calculatePercentage(objectivesRelevantToStrategies.size(), objectiveRefs.size());
	}

	private String calculatePercentage(int numinator, int denominator)
	{
		double percentage = ((double)numinator / (double)denominator) * 100;
		if (Double.isNaN(percentage))
			return "";

		return DoubleUtilities.toStringForHumans(percentage);
	}
	
	private ORefSet getStrategiesRelevantToObjectives() throws Exception
	{
		ORefSet objectiveRefs = getProject().getObjectivePool().getRefSet();
		ORefSet stratiesReleventToObjectives = new ORefSet();
		for(ORef objectiveRef : objectiveRefs)
		{
			Objective objective = Objective.find(getProject(), objectiveRef);
			stratiesReleventToObjectives.addAllRefs(objective.getRelevantStrategyRefs());
		}
		
		return stratiesReleventToObjectives;
	}

	private String getResultsChainWithObjectiveCount()
	{
		ORefSet resultsChainRefs = getProject().getResultsChainDiagramPool().getRefSet();
		ORefSet resultsChainsWithObjectives = new ORefSet();
		for(ORef resultsChainRef : resultsChainRefs)
		{
			ResultsChainDiagram resultsChain = ResultsChainDiagram.find(getProject(), resultsChainRef);
			if (resultsChain.getAllObjectiveRefs().hasRefs())
				resultsChainsWithObjectives.add(resultsChainRef);
		}
		
		return Integer.toString(resultsChainsWithObjectives.size());
	}

	private String getStrategyWithTaxonomyCount()
	{
		Vector<Strategy> strategies = getProject().getStrategyPool().getNonDraftStrategiesAsVector();
		HashSet<Strategy> strategiesWithTaxonomyCode = new HashSet<Strategy>();
		for(Strategy strategy : strategies)
		{
			if (strategy.getTaxonomyCode().length() > 0)
				strategiesWithTaxonomyCode.add(strategy);
		}
		
		return Integer.toString(strategiesWithTaxonomyCode.size());
	}

	private String getStrategyCount()
	{
		int count = getProject().getStrategyPool().getNonDraftStrategyRefs().size();
		return Integer.toString(count);
	}

	private String getRankedDraftStrategyCount()
	{
		Vector<Strategy> draftStrategies = getProject().getStrategyPool().getDraftStrategiesAsVector();
		HashSet<Strategy> strategiesWithRanking = new HashSet<Strategy>();
		for(Strategy draftStrategy : draftStrategies)
		{
			if (!draftStrategy.getStrategyRatingSummary().equals(StrategyRatingSummaryQuestion.UNKNOWN_CODE))
				strategiesWithRanking.add(draftStrategy);
		}
		
		return Integer.toString(strategiesWithRanking.size());
	}

	private String getDraftStrategyCount()
	{
		int count = getProject().getStrategyPool().getDraftStrategiesAsVector().size();
		return Integer.toString(count);
	}

	private String getTargetWithGoalCount()
	{
		ORefList targetRefs = getProject().getTargetPool().getRefList();
		targetRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());
		ORefSet targetsWithGoals = new ORefSet();
		for (int index = 0; index < targetRefs.size(); ++index)
		{
			ORef targetRef = targetRefs.get(index);
			AbstractTarget target = AbstractTarget.findTarget(getProject(), targetRef);
			if (target.getGoalRefs().hasRefs())
				targetsWithGoals.add(targetRef);
		}
		
		return Integer.toString(targetsWithGoals.size());
	}

	private String getThreatTargetLinkCount()
	{
		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			threatTargetCount += upstreamThreats.size();		
		}
		
		return Integer.toString(threatTargetCount);
	}
	
	private String getThreatTargetLinkWithRatingCount() throws Exception
	{
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetWithRatingCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			for(ORef threatRef : upstreamThreats)
			{
				int ratingValue = helper.calculateThreatRatingBundleValue(threatRef, target.getRef());
				if (ratingValue > 0)
					++threatTargetWithRatingCount;
			}
		}
		
		return Integer.toString(threatTargetWithRatingCount);
	}

	private String getThreatWithTaxonomyCount()
	{
		Vector<Cause> threats = getProject().getCausePool().getDirectThreatsAsVector();
		int count = 0;
		for(Cause threat : threats)
		{
			if (threat.getData(Cause.TAG_TAXONOMY_CODE).length() > 1)
				++count;
		}
		
		return Integer.toString(count);
	}

	private String getThreatCount()
	{
		int count = getProject().getCausePool().getDirectThreatsAsVector().size();
		
		return Integer.toString(count);
	}

	private String getTargetWithSimpleViabilityCount()
	{
		return getTargetCountForMode(ViabilityModeQuestion.SIMPLE_MODE_CODE);
	}

	private String getTargetWithKeaCount()
	{
		return getTargetCountForMode(ViabilityModeQuestion.TNC_STYLE_CODE);
	}

	private String getTargetCountForMode(String tncModeCode)
	{
		int count = 0;
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			
			if (target.getViabilityMode().equals(tncModeCode))
				++count;
		}
		
		return Integer.toString(count);
	}

	private String getObjectPoolCountAsString(int objectType)
	{
		int resourceCount = getObjectPoolCount(objectType);
		return Integer.toString(resourceCount);
	}

	protected int getObjectPoolCount(int objectType)
	{
		return getProject().getPool(objectType).size();
	}

	private String getProjectScopeWordCount()
	{
		int scopeCount = getProject().getMetadata().getProjectScope().length();
		return Integer.toString(scopeCount);
	}

	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Dashboard find(ObjectManager objectManager, ORef ref)
	{
		return (Dashboard) objectManager.findObject(ref);
	}
	
	public static Dashboard find(Project project, ORef ref)
	{
		return find(project.getObjectManager(), ref);
	}
	
	@Override
	void clear()
	{
		super.clear();
		
		teamMemberCount = new PseudoStringData(PSEUDO_TEAM_MEMBER_COUNT);
		projectScopeWordCount = new PseudoStringData(PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		targetCount = new PseudoStringData(PSEUDO_TARGET_COUNT);
		humanWelfareTargetCount = new PseudoStringData(PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		targetWithKeaCount = new PseudoStringData(PSEUDO_TARGET_WITH_KEA_COUNT);
		targetWithSimpleViabilityCount = new PseudoStringData(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
		threatCount = new PseudoStringData(PSEUDO_THREAT_COUNT);
		threatWithTaxonomyCount = new PseudoStringData(PSEUDO_THREAT_WITH_TAXONOMY_COUNT);
		threatTargetLinkCount = new PseudoStringData(PSEUDO_THREAT_TARGET_LINK_COUNT);
		threatTargetLinkWithRatingCount = new PseudoStringData(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT);
		goalCount = new PseudoStringData(PSEUDO_GOAL_COUNT);
		draftStrategyCount = new PseudoStringData(PSEUDO_DRAFT_STRATEGY_COUNT);
		rankedDraftStrategyCount = new PseudoStringData(PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
		stragtegyCount = new PseudoStringData(PSEUDO_STRATEGY_COUNT);
		strategyWithTaxonomyCount = new PseudoStringData(PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		resultsChainCount = new PseudoStringData(PSEUDO_RESULTS_CHAIN_COUNT);
		objectiveCount = new PseudoStringData(PSEUDO_OBJECTIVE_COUNT);
		resultsChainWithObjectiveCount = new PseudoStringData(PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
		objectivesRelevantToStrategiesPercentage = new PseudoStringData(PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);
		irrelenvatStrategiesToObjectivesCount = new PseudoStringData(PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT);
		keaIndicatorsCount = new PseudoStringData(PSEUDO_KEA_INDICATORS_COUNT);
		factorIndicatorsCount = new PseudoStringData(PSEUDO_FACTOR_INDICATORS_COUNT);
		objectivesRelevantToIndicatorsPercentage = new PseudoStringData(PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE);
		projectPlanningStartDate = new PseudoStringData(PSEUDO_PROJECT_PLANNING_START_DATE);
		projectPlanningEndDate = new PseudoStringData(PSEUDO_PROJECT_PLANNING_END_DATE);
		strategiesWithActivitiesCount = new PseudoStringData(PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT);
		activitiesCount = new PseudoStringData(PSEUDO_ACTIVITIES_COUNT);
		activitiesAndTasksCount = new PseudoStringData(PSEUDO_ACTIVITIES_AND_TASKS_COUNT);
		activitiesAndTasksWithAssignmentsCount = new PseudoStringData(PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT);
		indictorsWithMethodsCount = new PseudoStringData(PSEUDO_INDICATORS_WITH_METHODS_COUNT);
		methodsCount = new PseudoStringData(PSEUDO_METHODS_COUNT);
		indicatorsCount = new PseudoStringData(PSEUDO_INDICATORS_COUNT);
		methodsAndTasksCount = new PseudoStringData(PSEUDO_METHODS_AND_TASKS_COUNT);
		methodsAndTasksWithAssignmentsCount = new PseudoStringData(PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT);
		workPlanStartDate = new PseudoStringData(PSEUDO_WORK_PLAN_START_DATE);
		workPlanEndDate = new PseudoStringData(PSEUDO_WORK_PLAN_END_DATE);
		totalProjectResourcesCost = new PseudoStringData(PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS);
		totalProjectExpenses = new PseudoStringData(PSEUDO_TOTAL_PROJECT_EXPENSES);
		projectBudget = new PseudoStringData(PSEUDO_PROJECT_BUDGET);
		currecnySymbol = new PseudoStringData(PSEUDO_CURRENCY_SYMBOL);
		budgetSecuredPercent = new PseudoStringData(PSEUDO_BUDGET_SECURED_PERCENT);
		strategiesAndActivitiesWithProgressReportCount = new PseudoStringData(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT);
		strategiesAndActivitiesWithProgressReportPercent = new PseudoStringData(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT);
		indicatorsAndMethodsWithProgressReportCount = new PseudoStringData(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT);
		indicatorsAndMethodsWithProgressReportPercent = new PseudoStringData(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT);
		targetWithGoalsCount = new PseudoStringData(PSEUDO_TARGETS_WITH_GOALS_COUNT);
		effectiveStatusMap = new PseudoStringChoiceMapData(PSEUDO_EFFECTIVE_STATUS_MAP);
		
		addPresentationDataField(PSEUDO_TEAM_MEMBER_COUNT, teamMemberCount);
		addPresentationDataField(PSEUDO_PROJECT_SCOPE_WORD_COUNT, projectScopeWordCount);
		addPresentationDataField(PSEUDO_TARGET_COUNT, targetCount);
		addPresentationDataField(PSEUDO_HUMAN_WELFARE_TARGET_COUNT, humanWelfareTargetCount);
		addPresentationDataField(PSEUDO_TARGET_WITH_KEA_COUNT, targetWithKeaCount);
		addPresentationDataField(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT, targetWithSimpleViabilityCount);
		addPresentationDataField(PSEUDO_THREAT_COUNT, threatCount);
		addPresentationDataField(PSEUDO_THREAT_WITH_TAXONOMY_COUNT, threatWithTaxonomyCount);
		addPresentationDataField(PSEUDO_THREAT_TARGET_LINK_COUNT, threatTargetLinkCount);
		addPresentationDataField(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT, threatTargetLinkWithRatingCount);
		addPresentationDataField(PSEUDO_GOAL_COUNT, goalCount);
		addPresentationDataField(PSEUDO_DRAFT_STRATEGY_COUNT, draftStrategyCount);
		addPresentationDataField(PSEUDO_RANKED_DRAFT_STRATEGY_COUNT, rankedDraftStrategyCount);
		addPresentationDataField(PSEUDO_STRATEGY_COUNT, stragtegyCount);
		addPresentationDataField(PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT, strategyWithTaxonomyCount);
		addPresentationDataField(PSEUDO_RESULTS_CHAIN_COUNT, resultsChainCount);
		addPresentationDataField(PSEUDO_OBJECTIVE_COUNT, objectiveCount);
		addPresentationDataField(PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT, resultsChainWithObjectiveCount);
		addPresentationDataField(PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE, objectivesRelevantToStrategiesPercentage);
		addPresentationDataField(PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT, irrelenvatStrategiesToObjectivesCount);
		addPresentationDataField(PSEUDO_KEA_INDICATORS_COUNT, keaIndicatorsCount);
		addPresentationDataField(PSEUDO_FACTOR_INDICATORS_COUNT, factorIndicatorsCount);
		addPresentationDataField(PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE, objectivesRelevantToIndicatorsPercentage);
		addPresentationDataField(PSEUDO_PROJECT_PLANNING_START_DATE, projectPlanningStartDate);
		addPresentationDataField(PSEUDO_PROJECT_PLANNING_END_DATE, projectPlanningEndDate);
		addPresentationDataField(PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT, strategiesWithActivitiesCount);
		addPresentationDataField(PSEUDO_ACTIVITIES_COUNT, activitiesCount);
		addPresentationDataField(PSEUDO_ACTIVITIES_AND_TASKS_COUNT, activitiesAndTasksCount);
		addPresentationDataField(PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT, activitiesAndTasksWithAssignmentsCount);
		addPresentationDataField(PSEUDO_INDICATORS_WITH_METHODS_COUNT, indictorsWithMethodsCount);
		addPresentationDataField(PSEUDO_METHODS_COUNT, methodsCount);
		addPresentationDataField(PSEUDO_INDICATORS_COUNT, indicatorsCount);
		addPresentationDataField(PSEUDO_METHODS_AND_TASKS_COUNT, methodsAndTasksCount);
		addPresentationDataField(PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT, methodsAndTasksWithAssignmentsCount);
		addPresentationDataField(PSEUDO_WORK_PLAN_START_DATE, workPlanStartDate);
		addPresentationDataField(PSEUDO_WORK_PLAN_END_DATE, workPlanEndDate);
		addPresentationDataField(PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS, totalProjectResourcesCost);
		addPresentationDataField(PSEUDO_TOTAL_PROJECT_EXPENSES, totalProjectExpenses);
		addPresentationDataField(PSEUDO_PROJECT_BUDGET, projectBudget);
		addPresentationDataField(PSEUDO_CURRENCY_SYMBOL, currecnySymbol);
		addPresentationDataField(PSEUDO_BUDGET_SECURED_PERCENT, budgetSecuredPercent);
		addPresentationDataField(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT, strategiesAndActivitiesWithProgressReportCount);
		addPresentationDataField(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT, strategiesAndActivitiesWithProgressReportPercent);
		addPresentationDataField(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT, indicatorsAndMethodsWithProgressReportCount);
		addPresentationDataField(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT, indicatorsAndMethodsWithProgressReportPercent);
		addPresentationDataField(PSEUDO_TARGETS_WITH_GOALS_COUNT, targetWithGoalsCount);
		addPresentationDataField(PSEUDO_EFFECTIVE_STATUS_MAP, effectiveStatusMap);
	}
	
	public static final String OBJECT_NAME = "Dashboard";
	
	public static final String PSEUDO_TEMP_TAG = "TempTag";
	public static final String PSEUDO_TEAM_MEMBER_COUNT = "TeamMemberCount";
	public static final String PSEUDO_PROJECT_SCOPE_WORD_COUNT = "ProjectScopeWordCount";
	public static final String PSEUDO_TARGET_COUNT = "TargetCount";
	public static final String PSEUDO_HUMAN_WELFARE_TARGET_COUNT = "HumanWelfareTargetCount";
	public static final String PSEUDO_TARGET_WITH_KEA_COUNT = "TargetWithKeaCount";
	public static final String PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT = "TargetWithSimpleViabilityCount";
	public static final String PSEUDO_THREAT_COUNT = "ThreatCount";
	public static final String PSEUDO_THREAT_WITH_TAXONOMY_COUNT = "ThreatWithTaxonomyCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_COUNT = "ThreatTargetLinkCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT = "ThreatTargetLinkWithRatingCount";
	public static final String PSEUDO_TARGETS_WITH_GOALS_COUNT = "TargetsWithGoalsCount";
	public static final String PSEUDO_GOAL_COUNT = "GoalCount";
	public static final String PSEUDO_DRAFT_STRATEGY_COUNT = "DraftStrategyCount";
	public static final String PSEUDO_RANKED_DRAFT_STRATEGY_COUNT = "RankedDraftStrategyCount";
	public static final String PSEUDO_STRATEGY_COUNT = "StrategyCount";
	public static final String PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT = "StrategyWithTaxonomyCount";
	public static final String PSEUDO_RESULTS_CHAIN_COUNT = "ResultsChainCount";
	public static final String PSEUDO_OBJECTIVE_COUNT = "ObjectiveCount";
	public static final String PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT = "ResultsChainWithObjectiveCount";
	public static final String PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE = "ObjectivesRelevantToStrategiesPercentage";
	public static final String PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT = "IrrelenvatStratgiesToObjectivesCount";
	public static final String PSEUDO_KEA_INDICATORS_COUNT = "KeaIndicatorsCount";
	public static final String PSEUDO_FACTOR_INDICATORS_COUNT = "FactorIndicatorsCount";
	public static final String PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE = "ObjectivesRelevantToIndicatorsPercentage";
	public static final String PSEUDO_PROJECT_PLANNING_START_DATE = "ProjectPlanningStartDate";
	public static final String PSEUDO_PROJECT_PLANNING_END_DATE = "ProjectPlanningEndDate";
	public static final String PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT = "StrategiesWithActivitiesCount";
	public static final String PSEUDO_ACTIVITIES_COUNT = "ActivitiesCount";
	public static final String PSEUDO_ACTIVITIES_AND_TASKS_COUNT = "ActivitiesAndTasksCount";
	public static final String PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT = "ActivitiesAndTasksWithAssignmentsCount";
	public static final String PSEUDO_INDICATORS_WITH_METHODS_COUNT = "IndicatorsWithMethodsCount";
	public static final String PSEUDO_METHODS_COUNT = "MethodsCount";
	public static final String PSEUDO_INDICATORS_COUNT = "IndicatorsCount";
	public static final String PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT = "MethodsAndTasksWithAssignmentsCount";
	public static final String PSEUDO_METHODS_AND_TASKS_COUNT = "MethodsAndTasksCount";
	public static final String PSEUDO_WORK_PLAN_START_DATE = "WorkPlanStartDate";
	public static final String PSEUDO_WORK_PLAN_END_DATE = "WorkPlanEndDate";
	public static final String PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS = "TotalProjectResourcesCosts";
	public static final String PSEUDO_TOTAL_PROJECT_EXPENSES = "TotalProjectExpenses";
	public static final String PSEUDO_PROJECT_BUDGET = "ProjectBudget";
	public static final String PSEUDO_CURRENCY_SYMBOL = "CurrencySymbo";
	public static final String PSEUDO_BUDGET_SECURED_PERCENT = "BudgetSecuredPercent";
	public static final String PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT = "StrategiesAndAcitivitiesWithProgressReportCount";
	public static final String PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT = "StrategiesAndActivitiesWithProgressReportPercent";
	public static final String PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT = "IndicatorsAndMethodsWithProgressReportCount";
	public static final String PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT = "IndicatorsAndMethodsWithProgressReportPercent";
	public static final String PSEUDO_EFFECTIVE_STATUS_MAP = "effectiveStatusMap";

	private DashboardRowDefinitionManager rowDefinitionManager;
	
	private PseudoStringChoiceMapData effectiveStatusMap;
	private PseudoStringData teamMemberCount;
	private PseudoStringData projectScopeWordCount;
	private PseudoStringData targetCount;
	private PseudoStringData humanWelfareTargetCount;
	private PseudoStringData targetWithKeaCount;
	private PseudoStringData targetWithSimpleViabilityCount;
	private PseudoStringData threatCount;
	private PseudoStringData threatWithTaxonomyCount;
	private PseudoStringData threatTargetLinkCount;
	private PseudoStringData threatTargetLinkWithRatingCount;
	private PseudoStringData goalCount;
	private PseudoStringData draftStrategyCount;
	private PseudoStringData rankedDraftStrategyCount;
	private PseudoStringData stragtegyCount;
	private PseudoStringData strategyWithTaxonomyCount;
	private PseudoStringData resultsChainCount;
	private PseudoStringData objectiveCount;
	private PseudoStringData resultsChainWithObjectiveCount;
	private PseudoStringData objectivesRelevantToStrategiesPercentage;
	private PseudoStringData irrelenvatStrategiesToObjectivesCount;
	private PseudoStringData keaIndicatorsCount;
	private PseudoStringData factorIndicatorsCount;
	private PseudoStringData objectivesRelevantToIndicatorsPercentage;
	private PseudoStringData projectPlanningStartDate;
	private PseudoStringData projectPlanningEndDate;
	private PseudoStringData strategiesWithActivitiesCount;
	private PseudoStringData activitiesCount;
	private PseudoStringData activitiesAndTasksCount;
	private PseudoStringData activitiesAndTasksWithAssignmentsCount;
	private PseudoStringData indictorsWithMethodsCount;
	private PseudoStringData methodsCount;
	private PseudoStringData indicatorsCount;
	private PseudoStringData methodsAndTasksCount;
	private PseudoStringData methodsAndTasksWithAssignmentsCount;
	private PseudoStringData workPlanStartDate;
	private PseudoStringData workPlanEndDate;
	private PseudoStringData totalProjectResourcesCost;
	private PseudoStringData totalProjectExpenses;
	private PseudoStringData projectBudget;
	private PseudoStringData currecnySymbol;
	private PseudoStringData budgetSecuredPercent;
	private PseudoStringData strategiesAndActivitiesWithProgressReportCount;
	private PseudoStringData strategiesAndActivitiesWithProgressReportPercent;
	private PseudoStringData indicatorsAndMethodsWithProgressReportCount;
	private PseudoStringData indicatorsAndMethodsWithProgressReportPercent;
	private PseudoStringData targetWithGoalsCount;
}
