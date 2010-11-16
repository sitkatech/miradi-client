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

package org.miradi.questions;

import java.util.HashMap;

import org.miradi.dialogs.dashboard.HtmlResourceLongDescriptionProvider;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Dashboard;
import org.miradi.project.Project;
import org.miradi.views.summary.SummaryView;
import org.miradi.wizard.WizardManager;

public class OpenStandardsPlanActionsAndMonitoringQuestion extends DynamicChoiceWithRootChoiceItem
{
	public OpenStandardsPlanActionsAndMonitoringQuestion(Project projectToUse, WizardManager wizardManagerToUse)
	{
		project = projectToUse;
		wizardManager = wizardManagerToUse;
	}

	@Override
	public ChoiceItemWithChildren createHeaderChoiceItem() throws Exception
	{
		ChoiceItemWithChildren headerChoiceItem = new ChoiceItemWithChildren("2Header", getHeaderLabel(), new HtmlResourceLongDescriptionProvider(getMainDescriptionFileName(), getSummaryOverviewStepName()));
		
		headerChoiceItem.addChild(addDevelopStrategicPlanRow());
		headerChoiceItem.addChild(addDevelopFormatMonitoringPlanRow());
		headerChoiceItem.addChild(addDevelopOperationalPlanRow());

		return headerChoiceItem;
	}

	public static String getHeaderLabel()
	{
		return EAM.text("2. Plan Actions and Monitoring");
	}
	
	private ChoiceItemWithChildren addDevelopOperationalPlanRow() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("2C", EAM.text("2C. Develop Operational Plan"), providerToUse);
		
		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("AssessHumanFinancialAndResources", EAM.text("Assess Human Financial and other resources"), providerToUse));
		subHeaderChoiceItem.addChild(createProjectPlanningStartEndDateRow(providerToUse));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createProjectPlanningStartEndDateRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%projectPlanningStartDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE));
		tokenReplacementMap.put("%projectPlanningEndDate", getDashboardData(Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%projectPlanningStartDate - %projectPlanningEndDate"), tokenReplacementMap);
		String leftColumnTranslatedText = EAM.text("Plan Project Lifespan and Exit Strategy");
		
		return new ChoiceItemWithChildren("PlanningStartEndDate", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren addDevelopFormatMonitoringPlanRow() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("2B", EAM.text("2B. Develop a formal monitoring plan"), providerToUse);
		
		subHeaderChoiceItem.addChild(new ChoiceItemWithChildren("DefineAudiencesAndInformationNeeds", EAM.text("Define Audiences and Information Needs:"), EAM.text("Use Comments Field?"), providerToUse));
		subHeaderChoiceItem.addChild(createIndictorsChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createFactorIndictorsRow(providerToUse));
		subHeaderChoiceItem.addChild(createObjectivesWithRelevantIndicatorsRow(providerToUse));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createObjectivesWithRelevantIndicatorsRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Finalize Monitoring Plan:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToIndicatorsPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectivesRelevantToIndicatorsPercentage % of objectives with relevant indicators"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObjectiveRelevantIndicators", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createIndictorsChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Define Indicators:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%keaIndicators", getDashboardData(Dashboard.PSEUDO_KEA_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%keaIndicators Indicators associated to KEA's"), tokenReplacementMap);

		return new ChoiceItemWithChildren("KeaIndicators", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	private ChoiceItemWithChildren createFactorIndictorsRow(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%factorIndicators", getDashboardData(Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%factorIndicators indicators associate to Factors"), tokenReplacementMap);

		return new ChoiceItemWithChildren("FactorIndicators", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren addDevelopStrategicPlanRow() throws Exception
	{
		HtmlResourceLongDescriptionProvider providerToUse = new HtmlResourceLongDescriptionProvider(DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME, getSummaryOverviewStepName());
		ChoiceItemWithChildren subHeaderChoiceItem = new ChoiceItemWithChildren("2A", EAM.text("2A. Develop Strategic Plan"), providerToUse);

		subHeaderChoiceItem.addChild(createTargetsWithGoalChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createGoalCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createDraftStrategiesCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createRankedDraftStrategiesCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createStrategyCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createStrategyWithTaxonomyCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createResultsChainCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createObjectivesCountChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createObjectivesInResultsChainsChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createFinalStrategicPlanChoiceItem(providerToUse));
		subHeaderChoiceItem.addChild(createNonContributingStrategiesChoiceItem(providerToUse));
		
		return subHeaderChoiceItem;
	}

	private ChoiceItemWithChildren createFinalStrategicPlanChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Finalize Strategic Plan");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectivesRelevantToStrategiesPercentage", getDashboardData(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectivesRelevantToStrategiesPercentage % of Objectives relevant to a Strategy"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObjectivesRelevantToStrategiesPercentage", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	private ChoiceItemWithChildren createNonContributingStrategiesChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategiesIrrelevantToObjectivesCount", getDashboardData(Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategiesIrrelevantToObjectivesCount Strategies that do not contribute to an Objective"), tokenReplacementMap);

		return new ChoiceItemWithChildren("StrategiesIrrelevantToObjectivesCount", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createObjectivesCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Develop Objectives");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%objectiveCount", getDashboardData(Dashboard.PSEUDO_OBJECTIVE_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%objectiveCount Objectives Created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ObjectivesCreated", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	private ChoiceItemWithChildren createObjectivesInResultsChainsChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		tokenReplacementMap.put("%resultsChainWithObjectiveCount", getDashboardData(Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%resultsChainWithObjectiveCount of %resultsChainCount RCs have at least 1 objective"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ResultsChainsWithObjectives", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createResultsChainCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Results Chains");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%resultsChainCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%resultsChainCount Results Chains Created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("ResultsChainsCreated", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createStrategyWithTaxonomyCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Strategies with taxonomy assingments");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategyWithTaxonomyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategyWithTaxonomyCount with taxonomy assignments"), tokenReplacementMap);

		return new ChoiceItemWithChildren("StrategyWithTaxonomyCount", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createStrategyCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%strategyCount", getDashboardData(Dashboard.PSEUDO_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%strategyCount Strategies Created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("StrategiesCreated", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createRankedDraftStrategiesCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Rank Draft Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%rankedDraftStrategiesCount", getDashboardData(Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%rankedDraftStrategiesCount Draft Strategies Ranked"), tokenReplacementMap);

		return new ChoiceItemWithChildren("DraftStrategiesRanked", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createDraftStrategiesCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Create Draft Strategies");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%draftStrategiesCount", getDashboardData(Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%draftStrategiesCount Draft Strategies Created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("DraftStrategiesCount", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createGoalCountChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%goalsCount", getDashboardData(Dashboard.PSEUDO_GOAL_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%goalsCount goals Created"), tokenReplacementMap);

		return new ChoiceItemWithChildren("GoalsCount", "", rightColumnTranslatedText, providerToUse);
	}

	private ChoiceItemWithChildren createTargetsWithGoalChoiceItem(HtmlResourceLongDescriptionProvider providerToUse) throws Exception
	{
		String leftColumnTranslatedText = EAM.text("Develop Goals for Each Target:");
		HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
		tokenReplacementMap.put("%targetWithGoalCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		tokenReplacementMap.put("%targetCount", getDashboardData(Dashboard.PSEUDO_TARGET_COUNT));
		String rightColumnTranslatedText = EAM.substitute(EAM.text("%targetWithGoalCount of %targetCount Targets have Goals"), tokenReplacementMap);

		return new ChoiceItemWithChildren("DevelopGoalsForEachTarget", leftColumnTranslatedText, rightColumnTranslatedText, providerToUse);
	}
	
	private String getMainDescriptionFileName()
	{
		return "dashboard/2.html";
	}
	
	private String getDashboardData(String tag)
	{
		return getDashboard().getData(tag);
	}

	private Dashboard getDashboard()
	{
		ORef dashboardRef = getProject().getSingletonObjectRef(Dashboard.getObjectType());
		return Dashboard.find(getProject(), dashboardRef);
	}

	private String getSummaryOverviewStepName()
	{
		return wizardManager.getOverviewStepName(SummaryView.getViewName());
	}

	private Project getProject()
	{
		return project;
	}

	private Project project;
	private WizardManager wizardManager;
	private static final String DEVELOP_STRATEGIC_PLAN_RIGHT_SIDE_FILENAME = "dashboard/2A.html";
	private static final String DEVELOP_FORMAL_MONITORING_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2B.html";
	private static final String OPERATIONAL_PLAN_RIGHT_SIDE_FILE_NAME = "dashboard/2C.html";
}
