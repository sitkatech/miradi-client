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

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import org.miradi.diagram.ThreatTargetChainWalker;
import org.miradi.dialogs.dashboard.DashboardRowDefinitionManager;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.CodeChoiceMap;
import org.miradi.objecthelpers.CodeCodeListMap;
import org.miradi.objecthelpers.CodeToStringMap;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objecthelpers.TimePeriodCosts;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;

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
			
			if (fieldTag.equals(PSEUDO_CONTRIBUTING_FACTOR_COUNT))
				return getContributingFactorCount();

			if (fieldTag.equals(PSEUDO_THREAT_WITH_TAXONOMY_COUNT))
				return getThreatWithTaxonomyCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_COUNT))
				return getThreatTargetLinkCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_WITH_SIMPLE_RATING_COUNT))
				return getThreatTargetLinkWithRatingCount();
			
			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_WITH_STRESS_BASED_RATING_COUNT))
				return getThreatTargetLinkWithStressBasedRatingCount();
			
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
			
			if (fieldTag.equals(PSEUDO_STRATEGIES_RELEVANT_TO_OBJECTIVES_PERCENTAGE))
				return getStrategiesReleventToObjectivesPercentage();
			
			if (fieldTag.equals(PSEUDO_KEA_INDICATORS_COUNT))
				return getKeaIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_FACTOR_INDICATORS_COUNT))
				return getFactorIndicatorCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_COUNT))
				return getObjectiveRelevantToIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_PROJECT_PLANNING_START_DATE))
				return getUserEnteredPlanningStartDate();
			
			if (fieldTag.equals(PSEUDO_PROJECT_PLANNING_END_DATE))
				return getUserEnteredPlanningEndDate();
			
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
			
			if (fieldTag.equals(PSEUDO_CURRENCY_SYMBOL))
				return getCurrencySymbol();
			
			if (fieldTag.equals(PSEUDO_TOTAL_PROPOSED_BUDGET))
				return getTotalProposedBudget();
			
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
			
			if (fieldTag.equals(PSEUDO_CONCEPTUAL_MODEL_COUNT))
				return getConceptualModelCount();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_RELEVANT_TO_OBJECTIVES_PERCENTAGE))
				return getIndicatorsRelevantToObjectivesPercentage();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_IRRELEVANT_TO_OBJECIVES_PERCENTAGE))
				return getIndicatorsIrrelevantToObjectivesPercentage();
			
			if (fieldTag.equals(PSEUDO_TARGET_WITH_KEA_INDICATORS_COUNT))
				return getTargetWithKeaInidicatorsCount();
			
			if (fieldTag.equals(PSEUDO_SIMPLE_VIABILITY_INDICATORS_COUNT))
				return getSimpleViabilityIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_INDICATORS_COUNT))
				return getNumberOfTargetsWithSimpleViabilityIndicators();
			
			if (fieldTag.equals(PSEUDO_DIRECT_THREAT_INDICATORS_COUNT))
				return getDirectThreatIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_THREAT_REDUCTION_RESULT_INDICATORS_COUNT))
				return getThreatReductionResultIndicatorsCount();
			
			if (fieldTag.equals(PSEUDO_OTHER_ORGANIZATION_COUNT))
				return getObjectPoolCountAsString(Organization.getObjectType());
			
			if (fieldTag.equals(PSEUDO_INDICATORS_WITH_DESIRED_FUTURE_STATUS_SPECIFIED_PERCENTAGE))
				return getIndicatorsWithDesiredFutureStatusSpecifiedPercentage();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_WITH_NO_MEASUREMENT_COUNT))
				return getIndicatorsWithNoMeasurementCount();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_WITH_ONE_MEASUREMENT_COUNT))
				return getIndicatorsWithOneMeasurementCount();
			
			if (fieldTag.equals(PSEUDO_INDICATORS_WITH_MORE_THAN_ONE_MEASUREMENT_COUNT))
				return getIndicatorsWithMoreThanOneMeasurementCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_WITH_NO_PERCENT_COMPLETE_RECORD_COUNT))
				return getObjectivesWithNoPercentCompleteRecordCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_WITH_ONE_PERCENT_COMPLETE_RECORD_COUNT))
				return getObjectivesWithOnePercentCompleteRecordCount();
			
			if (fieldTag.equals(PSEUDO_OBJECTIVES_WITH_MORE_THAN_ONE_PERCENT_COMPLETE_RECORD_COUNT))
				return getObjectivesWithMoreThanOnePercentCompleteRecordCount();
			
			if (fieldTag.equals(PSEUDO_TOTAL_ACTION_BUDGET))
				return getTotalActionBudget();
			
			if (fieldTag.equals(PSEUDO_TOTAL_MONITORING_BUDGET))
				return getTotalMonitoringBudget();
			
			if (fieldTag.equals(PSEUDO_TOTAL_FACTOR_COUNT))
				return getTotalUniqueFactorCount();
					
			return super.getPseudoData(fieldTag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error Retrieving Data");
		}
	}
	
	private String getUserEnteredPlanningStartDate()
	{
		Vector<String> startDateTags = new Vector<String>();
		startDateTags.add(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		startDateTags.add(ProjectMetadata.TAG_START_DATE);
			
		return firstNonBlank(startDateTags);
	}
	
	private String getUserEnteredPlanningEndDate()
	{
		Vector<String> endDateTags = new Vector<String>();
		endDateTags.add(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		endDateTags.add(ProjectMetadata.TAG_EXPECTED_END_DATE);
		
		return firstNonBlank(endDateTags);
	}

	private String firstNonBlank(Vector<String> tags)
	{
		for(String tag : tags)
		{
			String data = getProject().getMetadata().getData(tag);
			if(data.length() != 0)
				return data;
		}
		
		return "";
	}

	private String getTotalUniqueFactorCount()
	{
		int[] factorTypesToCount = new int[]{
				Target.getObjectType(),
				HumanWelfareTarget.getObjectType(),
				Cause.getObjectType(),
				Strategy.getObjectType(),
		};
		
		ORefSet conceptualModelRefs = getProject().getConceptualModelDiagramPool().getRefSet();
		HashSet<Factor> allFactorRefs = new HashSet<Factor>();
		for(ORef conceptualModelRef : conceptualModelRefs)
		{
			ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(getProject(), conceptualModelRef);
			for (int index = 0; index < factorTypesToCount.length; ++index)
			{
				HashSet<Factor> factorsOnDiagram = conceptualModel.getFactorsOfType(factorTypesToCount[index]);
				allFactorRefs.addAll(factorsOnDiagram);
			}
		}

		return Integer.toString(allFactorRefs.size());
	}

	private String getTotalActionBudget() throws Exception
	{
		return calculateTotalCostForType(Strategy.getObjectType());
	}

	private String getTotalMonitoringBudget() throws Exception
	{
		return calculateTotalCostForType(Indicator.getObjectType());
	}
	
	private String calculateTotalCostForType(int objectType) throws Exception
	{
		Vector<BaseObject> baseObjects = getProject().getPool(objectType).getAllObjects();
		OptionalDouble totalCost = new OptionalDouble();
		for(BaseObject baseObject : baseObjects)
		{
			OptionalDouble totalBudgetCost = baseObject.getTotalBudgetCost();
			totalCost = totalCost.add(totalBudgetCost);
		}
		
		return totalCost.toString();
	}

	private String getIndicatorsWithNoMeasurementCount() throws Exception
	{
		final int LOWER_BOUND = 0;
		final int UPPER_BOUND = 0;

		return getMeasurementCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getIndicatorsWithOneMeasurementCount() throws Exception
	{
		final int LOWER_BOUND = 1;
		final int UPPER_BOUND = 1;

		return getMeasurementCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getIndicatorsWithMoreThanOneMeasurementCount() throws Exception
	{
		final int LOWER_BOUND = 2;
		final int UPPER_BOUND = Integer.MAX_VALUE;
		
		return getMeasurementCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getObjectivesWithNoPercentCompleteRecordCount() throws Exception
	{		
		final int LOWER_BOUND = 0;
		final int UPPER_BOUND = 0;
	
		return getProgressPercentCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getObjectivesWithOnePercentCompleteRecordCount() throws Exception
	{
		final int LOWER_BOUND = 1;
		final int UPPER_BOUND = 1;
		
		return getProgressPercentCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getObjectivesWithMoreThanOnePercentCompleteRecordCount() throws Exception
	{
		
		final int LOWER_BOUND = 2;
		final int UPPER_BOUND = Integer.MAX_VALUE;
		
		return getProgressPercentCount(LOWER_BOUND, UPPER_BOUND);
	}

	private String getProgressPercentCount(final int LOWER_BOUND, final int UPPER_BOUND) throws Exception
	{
		return getAnnotationCountWithinBounds(Objective.getObjectType(), Objective.TAG_PROGRESS_PERCENT_REFS, LOWER_BOUND, UPPER_BOUND);
	}
	
	private String getMeasurementCount(final int LOWER_BOUND, final int UPPER_BOUND) throws Exception
	{
		return getAnnotationCountWithinBounds(Indicator.getObjectType(), Indicator.TAG_MEASUREMENT_REFS, LOWER_BOUND, UPPER_BOUND);
	}
	
	private String getAnnotationCountWithinBounds(int objectType, String tag, final int LOWER_BOUND, final int UPPER_BOUND)	throws Exception
	{
		Vector<BaseObject> baseObjects = getProject().getPool(objectType).getAllObjects();
		ORefSet baseObjectsWithAnnotationCountWithinBounds = new ORefSet();
		for(BaseObject baseObject : baseObjects)
		{
			int listSize = baseObject.getRefList(tag).size();
			if (listSize >= LOWER_BOUND && listSize <= UPPER_BOUND)
				baseObjectsWithAnnotationCountWithinBounds.addRef(baseObject);
		}
		
		return Integer.toString(baseObjectsWithAnnotationCountWithinBounds.size());
	}

	private String getIndicatorsWithDesiredFutureStatusSpecifiedPercentage()
	{
		Indicator[] indicators = getAllActiveIndicators();
		ORefSet indicatorsWithFutureStatusSpecified = new ORefSet();
		for (int index = 0; index < indicators.length; ++index)
		{
			Indicator indicator = indicators[index];
			if (hasAnyFutureStatusInformation(indicator))
				indicatorsWithFutureStatusSpecified.add(indicator.getRef());
		}
		
		return calculatePercentage(indicatorsWithFutureStatusSpecified.size(), indicators.length);
	}

	private boolean hasAnyFutureStatusInformation(Indicator indicator)
	{
		String[] futureStatusTags = {
			Indicator.TAG_FUTURE_STATUS_COMMENTS,
			Indicator.TAG_FUTURE_STATUS_DATE,
			Indicator.TAG_FUTURE_STATUS_DETAIL,
			Indicator.TAG_FUTURE_STATUS_RATING,
			Indicator.TAG_FUTURE_STATUS_SUMMARY,
		};
		for(String tag : futureStatusTags)
		{
			if(!indicator.getField(tag).isEmpty())
				return true;
		}
		
		return false;
	}

	private String getThreatReductionResultIndicatorsCount()
	{
		int factorType = ThreatReductionResult.getObjectType();
		ORefSet factorRefs = getProject().getPool(factorType).getRefSet();
		return getIndicatorCountForFactors(factorRefs);
	}

	private String getIndicatorCountForFactors(ORefSet factorRefs)
	{
		ORefSet factorsWithIndicators = new ORefSet();
		for (ORef factorRef : factorRefs)
		{
			Factor factor = Factor.findFactor(getProject(), factorRef);
			if (factor.getOnlyDirectIndicatorRefs().hasRefs())
				factorsWithIndicators.add(factorRef);
			
		}
		
		return Integer.toString(factorsWithIndicators.size());
	}

	private String getDirectThreatIndicatorsCount()
	{
		Vector<BaseObject> directThreats = new Vector<BaseObject>(getProject().getCausePool().getDirectThreatsAsVector());
		ORefList directThreatRefs = new ORefList(directThreats);

		return getIndicatorCountForFactors(new ORefSet(directThreatRefs));
	}

	private String getNumberOfTargetsWithSimpleViabilityIndicators()
	{
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		ORefSet targetWithSimpleViabilityIndicators = new ORefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			if (target.getOnlyDirectIndicatorRefs().hasRefs())
				targetWithSimpleViabilityIndicators.add(targetRef);
		}
		
		return Integer.toString(targetWithSimpleViabilityIndicators.size());
	}

	private String getSimpleViabilityIndicatorsCount()
	{
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		ORefSet simpleViabilityIndicatorsRefs = new ORefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			simpleViabilityIndicatorsRefs.addAllRefs(target.getOnlyDirectIndicatorRefs());
		}
		
		return Integer.toString(simpleViabilityIndicatorsRefs.size());
	}

	private String getTargetWithKeaInidicatorsCount()
	{
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		ORefSet targetWithKeaIndicators = new ORefSet();
		for (ORef targetRef : targetRefs)
		{
			if (hasKeaWithIndicators(targetRef))
				targetWithKeaIndicators.add(targetRef);
		}
		
		return Integer.toString(targetWithKeaIndicators.size());
	}

	private boolean hasKeaWithIndicators(ORef targetRef)
	{
		Target target = Target.find(getProject(), targetRef);
		ORefSet keaRefs = new ORefSet(target.getKeyEcologicalAttributeRefs());
		for (ORef keaRef : keaRefs)
		{
			KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getProject(), keaRef);
			if (kea.getIndicatorRefs().hasRefs())
				return true;
		}
		
		return false;
	}

	private String getIndicatorsIrrelevantToObjectivesPercentage() throws Exception
	{
		ORefSet indicatorRefs = getAllActiveIndicatorRefs();
		ORefSet indicatorsRelevantToObjectives = getIndicatorsRelevantToObjectives();
		ORefSet indicatorsIrrelevantToObjectives = new ORefSet(indicatorRefs);
		indicatorsIrrelevantToObjectives.removeAll(indicatorsRelevantToObjectives);
		
		return calculatePercentage(indicatorsIrrelevantToObjectives.size(), indicatorRefs.size());
	}

	private String getIndicatorsRelevantToObjectivesPercentage() throws Exception
	{
		ORefSet indicatorsRelevantToObjectives = getIndicatorsRelevantToObjectives();
		
		return calculatePercentage(indicatorsRelevantToObjectives.size(), getAllActiveIndicators().length);
	}

	private ORefSet getIndicatorsRelevantToObjectives() throws Exception
	{
		ORefSet objectiveRefs = getProject().getObjectivePool().getRefSet();
		ORefSet indicatorsRelevantToObjectives = new ORefSet();
		for (ORef objectiveRef : objectiveRefs)
		{
			Objective objective = Objective.find(getProject(), objectiveRef);
			ORefList relevantIndicatorRefs = objective.getRelevantIndicatorRefList();
			for(int index = 0; index < relevantIndicatorRefs.size(); ++index)
			{
				ORef indicatorRef = relevantIndicatorRefs.get(index);
				Indicator indicator = Indicator.find(getProject(), indicatorRef);
				if(indicator.isActive())
					indicatorsRelevantToObjectives.add(indicatorRef);
			}
		}
		
		return indicatorsRelevantToObjectives;
	}

	private String getConceptualModelCount()
	{
		int count = getProject().getConceptualModelDiagramPool().size();
		
		return Integer.toString(count);
	}

	public DashboardRowDefinitionManager getDashboardRowDefinitionManager()
	{
		if (rowDefinitionManager == null)
			rowDefinitionManager = new DashboardRowDefinitionManager();
		
		return rowDefinitionManager;
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
		baseObjects.addAll(Arrays.asList(getAllActiveIndicators()));
		baseObjects.addAll(getProject().getTaskPool().getAllMethods());
		
		return baseObjects;
	}

	private Indicator[] getAllActiveIndicators()
	{
		Indicator[] allIndicators = getProject().getIndicatorPool().getAllIndicators();
		Vector<Indicator> activeIndicators = new Vector<Indicator>();
		for(int index = 0; index < allIndicators.length; ++index)
		{
			if(allIndicators[index].isActive())
				activeIndicators.add(allIndicators[index]);
		}
		return activeIndicators.toArray(new Indicator[0]);
	}
	
	private ORefSet getAllActiveIndicatorRefs()
	{
		return new ORefSet(getAllActiveIndicators());
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
	
	private String getTotalProposedBudget()
	{
		return getProject().getMetadata().getData(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
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

	private String getCurrencySymbol()
	{
		return getProject().getMetadata().getData(ProjectMetadata.TAG_CURRENCY_SYMBOL);
	}

	private String getMethodsCount()
	{
		int count = getProject().getTaskPool().getAllMethods().size();
		return Integer.toString(count);
	}

	private String getIndicatorsWithMethodsCount()
	{
		ORefSet indicatorRefs = getAllActiveIndicatorRefs();
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

	private String getObjectiveRelevantToIndicatorsCount() throws Exception
	{
		ORefSet objectiveRefs = getProject().getObjectivePool().getRefSet();
		ORefSet objectivesRelevantToIndictors = new ORefSet();
		for(ORef objectiveRef : objectiveRefs)
		{
			Objective objective = Objective.find(getProject(), objectiveRef);
			if (objective.getRelevantIndicatorRefList().hasRefs())
				objectivesRelevantToIndictors.add(objectiveRef);
		}
		
		return Integer.toString(objectivesRelevantToIndictors.size());
	}

	private String getFactorIndicatorCount()
	{
		ORefSet keaIndicatorRefs = getKeaIndicatorRefs();
		ORefSet allIndicatorRefs = getAllActiveIndicatorRefs();
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
			if(kea.isActive())
				keaIndicatorRefs.addAllRefs(kea.getIndicatorRefs());
		}
		return keaIndicatorRefs;
	}

	private String getStrategiesReleventToObjectivesPercentage() throws Exception
	{
		Vector<Strategy> strategies = getProject().getStrategyPool().getNonDraftStrategiesAsVector();
		Vector<Strategy> relevantStrategiesToObjectives = new Vector<Strategy>();
		ORefSet strategiesRelevantToObjectives = getStrategiesRelevantToObjectives();
		for (Strategy strategy : strategies)
		{	
			if (strategiesRelevantToObjectives.contains(strategy.getRef()))
				relevantStrategiesToObjectives.add(strategy);
		}
		
		return calculatePercentage(relevantStrategiesToObjectives.size(), strategies.size());	
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
		float percentage = ((float)numinator / (float)denominator) * 100;
		if (Float.isNaN(percentage))
			return "";

		int roundedPercent = Math.round(percentage);
		return Integer.toString(roundedPercent);
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
		Set<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			threatTargetCount += upstreamThreats.size();		
		}
		
		return Integer.toString(threatTargetCount);
	}
	
	private String getThreatTargetLinkWithStressBasedRatingCount() throws Exception
	{
		return getThreatTargetLinkWithRatingCount(ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
	}
	
	private String getThreatTargetLinkWithRatingCount() throws Exception
	{
		return getThreatTargetLinkWithRatingCount(ThreatRatingModeChoiceQuestion.SIMPLE_BASED_CODE);
	}

	private String getThreatTargetLinkWithRatingCount(String threatRatingMode) throws Exception
	{
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		Set<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetWithRatingCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			for(ORef threatRef : upstreamThreats)
			{
				int ratingValue = helper.calculateThreatRatingBundleValue(threatRatingMode, threatRef, target.getRef());
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
	
	private String getContributingFactorCount()
	{
		int count = getProject().getCausePool().getContributingFactors().size();
		
		return Integer.toString(count);
	}

	private String getTargetWithSimpleViabilityCount() throws Exception
	{
		return getParentCountWithAnnotations(Target.TAG_INDICATOR_IDS);
	}

	private String getTargetWithKeaCount() throws Exception
	{
		return getParentCountWithAnnotations(Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
	}
	
	private String getParentCountWithAnnotations(String tag) throws Exception
	{
		int count = 0;
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			if (target.getRefList(tag).hasRefs())
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

	private int getWordCount(String text) 
	{
		String[] splitted = text.split("\\s+");
		if (splitted.length == 1)
		{
			String singleSplitValue = splitted[0];
			if (singleSplitValue.equals(""))
				return 0;
		}

		return splitted.length;
	}
	
	private String getProjectScopeWordCount()
	{
		String scope = getProject().getMetadata().getProjectScope();
		int wordCount = getWordCount(scope);
		
		return Integer.toString(wordCount);
	}
	
	public CodeToStringMap getCommentsMap()
	{
		return getCodeStringMapData(TAG_COMMENTS_MAP);
	}

	public CodeChoiceMap getProgressChoiceMap()
	{
		return getCodeChoiceMapData(TAG_PROGRESS_CHOICE_MAP);
	}

	public CodeCodeListMap getFlagsMap()
	{
		return getCodeCodeListMapData(TAG_FLAGS_MAP);
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
		
		createStringChoiceMapField(TAG_PROGRESS_CHOICE_MAP);
		createStringStringMapField(TAG_COMMENTS_MAP);
		createStringCodeListMapField(TAG_FLAGS_MAP);
		createCodeField(TAG_CURRENT_DASHBOARD_TAB);
		setNonUserField(TAG_CURRENT_DASHBOARD_TAB);

		createPseudoStringField(PSEUDO_TEAM_MEMBER_COUNT);
		createPseudoStringField(PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		createPseudoStringField(PSEUDO_TARGET_COUNT);
		createPseudoStringField(PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		createPseudoStringField(PSEUDO_TARGET_WITH_KEA_COUNT);
		createPseudoStringField(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
		createPseudoStringField(PSEUDO_THREAT_COUNT);
		createPseudoStringField(PSEUDO_CONTRIBUTING_FACTOR_COUNT);
		createPseudoStringField(PSEUDO_THREAT_WITH_TAXONOMY_COUNT);
		createPseudoStringField(PSEUDO_THREAT_TARGET_LINK_COUNT);
		createPseudoStringField(PSEUDO_THREAT_TARGET_LINK_WITH_SIMPLE_RATING_COUNT);
		createPseudoStringField(PSEUDO_THREAT_TARGET_LINK_WITH_STRESS_BASED_RATING_COUNT);
		createPseudoStringField(PSEUDO_GOAL_COUNT);
		createPseudoStringField(PSEUDO_DRAFT_STRATEGY_COUNT);
		createPseudoStringField(PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
		createPseudoStringField(PSEUDO_STRATEGY_COUNT);
		createPseudoStringField(PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		createPseudoStringField(PSEUDO_RESULTS_CHAIN_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVE_COUNT);
		createPseudoStringField(PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);
		createPseudoStringField(PSEUDO_STRATEGIES_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoStringField(PSEUDO_KEA_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_FACTOR_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_PROJECT_PLANNING_START_DATE);
		createPseudoStringField(PSEUDO_PROJECT_PLANNING_END_DATE);
		createPseudoStringField(PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT);
		createPseudoStringField(PSEUDO_ACTIVITIES_COUNT);
		createPseudoStringField(PSEUDO_ACTIVITIES_AND_TASKS_COUNT);
		createPseudoStringField(PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_WITH_METHODS_COUNT);
		createPseudoStringField(PSEUDO_METHODS_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_METHODS_AND_TASKS_COUNT);
		createPseudoStringField(PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT);
		createPseudoStringField(PSEUDO_WORK_PLAN_START_DATE);
		createPseudoStringField(PSEUDO_WORK_PLAN_END_DATE);
		createPseudoStringField(PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS);
		createPseudoStringField(PSEUDO_TOTAL_PROJECT_EXPENSES);
		createPseudoStringField(PSEUDO_TOTAL_PROPOSED_BUDGET);
		createPseudoStringField(PSEUDO_CURRENCY_SYMBOL);
		createPseudoStringField(PSEUDO_BUDGET_SECURED_PERCENT);
		createPseudoStringField(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT);
		createPseudoStringField(PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoStringField(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoStringField(PSEUDO_TARGETS_WITH_GOALS_COUNT);
		createPseudoStringField(PSEUDO_CONCEPTUAL_MODEL_COUNT);
		createPseudoStringField(PSEUDO_ALL_FACTOR_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoStringField(PSEUDO_INDICATORS_IRRELEVANT_TO_OBJECIVES_PERCENTAGE);
		createPseudoStringField(PSEUDO_TARGET_WITH_KEA_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_DIRECT_THREAT_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_THREAT_REDUCTION_RESULT_INDICATORS_COUNT);
		createPseudoStringField(PSEUDO_OTHER_ORGANIZATION_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_WITH_DESIRED_FUTURE_STATUS_SPECIFIED_PERCENTAGE);
		createPseudoStringField(PSEUDO_INDICATORS_WITH_NO_MEASUREMENT_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_WITH_ONE_MEASUREMENT_COUNT);
		createPseudoStringField(PSEUDO_INDICATORS_WITH_MORE_THAN_ONE_MEASUREMENT_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVES_WITH_NO_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVES_WITH_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(PSEUDO_OBJECTIVES_WITH_MORE_THAN_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(PSEUDO_TOTAL_ACTION_BUDGET);
		createPseudoStringField(PSEUDO_TOTAL_MONITORING_BUDGET);
		createPseudoStringField(PSEUDO_TOTAL_FACTOR_COUNT);
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
	public static final String PSEUDO_CONTRIBUTING_FACTOR_COUNT = "ContributingFactorCount";
	public static final String PSEUDO_THREAT_WITH_TAXONOMY_COUNT = "ThreatWithTaxonomyCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_COUNT = "ThreatTargetLinkCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_WITH_SIMPLE_RATING_COUNT = "ThreatTargetLinkWithSimpleRatingCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_WITH_STRESS_BASED_RATING_COUNT = "ThreatTargetLinkWithStressedBasedRatingCount";
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
	public static final String PSEUDO_STRATEGIES_RELEVANT_TO_OBJECTIVES_PERCENTAGE = "IrrelenvatStratgiesToObjectivesPercentage";
	public static final String PSEUDO_KEA_INDICATORS_COUNT = "KeaIndicatorsCount";
	public static final String PSEUDO_FACTOR_INDICATORS_COUNT = "FactorIndicatorsCount";
	public static final String PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_COUNT = "ObjectivesRelevantToIndicatorsCount";
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
	public static final String PSEUDO_CURRENCY_SYMBOL = "CurrencySymbo";
	public static final String PSEUDO_TOTAL_PROPOSED_BUDGET = "TotalProposedBudget";
	public static final String PSEUDO_BUDGET_SECURED_PERCENT = "BudgetSecuredPercent";
	public static final String PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT = "StrategiesAndAcitivitiesWithProgressReportCount";
	public static final String PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT = "StrategiesAndActivitiesWithProgressReportPercent";
	public static final String PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT = "IndicatorsAndMethodsWithProgressReportCount";
	public static final String PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT = "IndicatorsAndMethodsWithProgressReportPercent";
	public static final String PSEUDO_CONCEPTUAL_MODEL_COUNT = "ConceptualModelCount";
	public static final String PSEUDO_ALL_FACTOR_COUNT = " AllFactorCount";
	public static final String PSEUDO_INDICATORS_RELEVANT_TO_OBJECTIVES_PERCENTAGE = "IndicatorsRelevantToObjectivesPercentage";
	public static final String PSEUDO_INDICATORS_IRRELEVANT_TO_OBJECIVES_PERCENTAGE = "IndicatorsIrrelevantToObjectivesPercentage";
	public static final String PSEUDO_TARGET_WITH_KEA_INDICATORS_COUNT = "TargetWithKeaIndicatorsCount";
	public static final String PSEUDO_SIMPLE_VIABILITY_INDICATORS_COUNT = "simpleViabilityIndicatorsCount";
	public static final String PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_INDICATORS_COUNT = "targetWithSimpleViabilityIndicatorsCount";
	public static final String PSEUDO_DIRECT_THREAT_INDICATORS_COUNT = "DirectThreatIndicatorsCount";
	public static final String PSEUDO_THREAT_REDUCTION_RESULT_INDICATORS_COUNT = "ThreatReductionResultIndicatorsCount";
	public static final String PSEUDO_OTHER_ORGANIZATION_COUNT = "OtherOrganizationCount";
	public static final String PSEUDO_INDICATORS_WITH_DESIRED_FUTURE_STATUS_SPECIFIED_PERCENTAGE = "IndicatorsWithDesiredFutureStatusSpecifiedPercentage";
	public static final String PSEUDO_INDICATORS_WITH_NO_MEASUREMENT_COUNT = "IndicatorsWithNoMeasurementCount";
	public static final String PSEUDO_INDICATORS_WITH_ONE_MEASUREMENT_COUNT = "IndicatorsWithOneMeasurementCount";
	public static final String PSEUDO_INDICATORS_WITH_MORE_THAN_ONE_MEASUREMENT_COUNT = "IndicatorsWithMoreThanOneMeasurementCount";
	public static final String PSEUDO_OBJECTIVES_WITH_NO_PERCENT_COMPLETE_RECORD_COUNT = "ObjectivesWithNoPercentCompleteRecordCount";
	public static final String PSEUDO_OBJECTIVES_WITH_ONE_PERCENT_COMPLETE_RECORD_COUNT = "ObjectivesWithOnePercentCompleteRecordCount";
	public static final String PSEUDO_OBJECTIVES_WITH_MORE_THAN_ONE_PERCENT_COMPLETE_RECORD_COUNT = "ObjectivesWithMoreThanOnePercentCompleteRecordCount";
	public static final String PSEUDO_TOTAL_ACTION_BUDGET = "TotalActionBudget";
	public static final String PSEUDO_TOTAL_MONITORING_BUDGET = "TotalMonitoringBudget";
	public static final String PSEUDO_TOTAL_FACTOR_COUNT = "TotalFactorCount";
	
	public static final String TAG_PROGRESS_CHOICE_MAP = "ProgressChoiceMap";
	public static final String TAG_COMMENTS_MAP = "CommentsMap";
	public static final String TAG_FLAGS_MAP = "FlagsMap";
	public static final String TAG_CURRENT_DASHBOARD_TAB = "CurrentDashboardTab";
	
	public static final String DASHBOARD_CONCEPTUALIZE_TAB_CODE = "Conceptualize";
	public static final String DASHBOARD_PLAN_TAB_CODE = "Plan";
	public static final String DASHBOARD_IMPLEMENT_TAB_CODE = "Implement";
	public static final String DASHBOARD_ANALYZE_TAB_CODE = "Analyze";
	public static final String DASHBOARD_LEARN_TAB_CODE = "Learn";

	private DashboardRowDefinitionManager rowDefinitionManager;
}
