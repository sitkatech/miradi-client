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

package org.miradi.dialogs.dashboard;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objects.Dashboard;
import org.miradi.questions.OpenStandardsAnalyzeUseAndAdaptQuestion;
import org.miradi.questions.OpenStandardsCaptureAndShareLearningQuestion;
import org.miradi.questions.OpenStandardsConceptualizeQuestion;
import org.miradi.questions.OpenStandardsImplementActionsAndMonitoringQuestion;
import org.miradi.questions.OpenStandardsPlanActionsAndMonitoringQuestion;
import org.miradi.utils.CodeList;

public class DashboardRowDefinitionManager
{
	public CodeList getThirdLevelCodes()
	{
		CodeList allCodes = new CodeList();
		allCodes.addAll(new OpenStandardsConceptualizeQuestion().getAllCodes());
		allCodes.addAll(new OpenStandardsPlanActionsAndMonitoringQuestion().getAllCodes());
		allCodes.addAll(new OpenStandardsImplementActionsAndMonitoringQuestion().getAllCodes());
		allCodes.addAll(new OpenStandardsAnalyzeUseAndAdaptQuestion().getAllCodes());
		allCodes.addAll(new OpenStandardsCaptureAndShareLearningQuestion().getAllCodes());
		
		return allCodes;
	}
	
	public Vector<DashboardRowDefinition> getRowDefinitions(String code) throws Exception
	{	
		Vector<DashboardRowDefinition> rowDefinitions = new Vector<DashboardRowDefinition>();
		rowDefinitions.addAll(addConceptualizeRowDefinitions(code));
		rowDefinitions.addAll(addPlanActionsAndMonitoringDefinitions(code));
		rowDefinitions.addAll(addImplementActionsAndMonitoringDefinitions(code));
		
		return rowDefinitions;
	}

	private Vector<DashboardRowDefinition> addImplementActionsAndMonitoringDefinitions(String code)
	{
		Vector<DashboardRowDefinition> rowDefinitions = new Vector<DashboardRowDefinition>();
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DETAIL_ACTIVITIES_TASKS_AND_RESPONSIBILITIES_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 of %2 Strategies have at least 1 Activity."), Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT, Dashboard.PSEUDO_STRATEGY_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Total activities created"), Dashboard.PSEUDO_ACTIVITIES_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%2 of %1 Activities and tasks have assignments"), Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT, Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT));
		}
	
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DETAIL_METHODS_TASKS_AND_RESPONSIBILITIES_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 of %2 Indicators have Methods."), Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT, Dashboard.PSEUDO_INDICATORS_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Total methods created"), Dashboard.PSEUDO_METHODS_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 of %2 Methods and Tasks have assignments"), Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT, Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT));
		}
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DEVELOP_PROJECT_TIMELINE_OR_CALENDAR_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 - %2"), Dashboard.PSEUDO_WORK_PLAN_START_DATE, Dashboard.PSEUDO_WORK_PLAN_END_DATE));
		}
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.ESTIMATE_COSTS_FOR_ACTIVITIES_AND_MONITORING_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Work Costs: %1 %2"), Dashboard.PSEUDO_CURRENCY_SYMBOL, Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Expenses: %1 %2"), Dashboard.PSEUDO_CURRENCY_SYMBOL, Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Project Budget: %1 %2"), Dashboard.PSEUDO_CURRENCY_SYMBOL, Dashboard.PSEUDO_PROJECT_BUDGET));
		}
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.DEVELOP_AND_SUBMIT_FUNDING_PROPOSALS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Total Budget for Funding: %  %1 Budget Secured"), Dashboard.PSEUDO_BUDGET_SECURED_PERCENT));
		}
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.IMPLEMENT_STRATEGIC_AND_MONITORING_PLANS))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Strategies/activities (%2%) have progress reports"), Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT, Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Indicators/methods (%2%) have progress reports"), Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT, Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT));
		}
		
		if (code.equals(OpenStandardsImplementActionsAndMonitoringQuestion.IMPLEMENT_WORK_PLAN_CODE))
		{
			;
		}
		
		return rowDefinitions;
	}

	private Vector<DashboardRowDefinition> addPlanActionsAndMonitoringDefinitions(String code)
	{
		Vector<DashboardRowDefinition> rowDefinitions = new Vector<DashboardRowDefinition>();
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_GOALS_FOR_EACH_TARGET_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 of %2 Targets have Goals"), Dashboard.PSEUDO_TARGETS_WITH_GOALS_COUNT, Dashboard.PSEUDO_TARGET_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 goals Created"), Dashboard.PSEUDO_GOAL_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.IDENTIFY_KEY_FACTORS_AND_DRAFT_STRATEGIES_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Draft Strategies Created"), Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.RANK_DRAFT_STRATEGIES_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Draft Strategies Ranked"), Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.CREATE_RESULTS_CHAINS_SHOWING_ASSUMPTIONS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Strategies Created"), Dashboard.PSEUDO_STRATEGY_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 with taxonomy assignments"), Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Results Chains Created"), Dashboard.PSEUDO_RESULTS_CHAIN_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEVELOP_OBJECTIVES_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Objectives Created"), Dashboard.PSEUDO_OBJECTIVE_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%2 of %1 RCs have at least 1 objective"), Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT, Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_STRATEGIC_PLAN_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 % of Objectives relevant to a Strategy"), Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE));		
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Strategies that do not contribute to an Objective"), Dashboard.PSEUDO_IRRELEVANT_STRATEGIES_TO_OBJECTIVES_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.FINALIZE_MONITORING_PLAN_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 % of objectives with relevant indicators"), Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_PERCENTAGE));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_AUDIENCES_AND_INFORMATION_NEEDS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Use Comments Field?")));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.DEFINE_INDICATORS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Indicators associated to KEA's"), Dashboard.PSEUDO_KEA_INDICATORS_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 indicators associate to Factors"), Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT));
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_HUMAN_FINANCIAL_AND_OTHER_RESOURCES_CODE))
		{
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.ASSESS_RISKS_CODE))
		{
		}
		
		if (code.equals(OpenStandardsPlanActionsAndMonitoringQuestion.PLAN_PROJECT_LIFESPAN_AND_EXIT_STRATEGY_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 - %2"), Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE, Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE));
		}
		
		return rowDefinitions;
	}

	private Vector<DashboardRowDefinition> addConceptualizeRowDefinitions(String code)
	{
		Vector<DashboardRowDefinition> rowDefinitions = new Vector<DashboardRowDefinition>();
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_INTIAL_TEAM_MEMBERS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Team Members: %1"), Dashboard.PSEUDO_TEAM_MEMBER_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.AGREE_ON_ROLES_AND_RESPONSIBILITIES_CODE))
		{
			addAgreeOnRolesAndResponsibilities();
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEFINE_PROJECT_SCOPE_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("Created (%1 chars)"), Dashboard.PSEUDO_PROJECT_SCOPE_WORD_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DEVELOP_MAP_OF_PROJECT_AREA_CODE))
		{
			addDevelopMapOfProjectArea();
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.SELECT_CONSERVATION_TARGETS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 created"),Dashboard.PSEUDO_TARGET_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ADD_HUMAN_WELFARE_TARGETS_IF_DESIRED_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 created"), Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.DESCRIBE_STATUS_OF_TARGETS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 targets have KEA"), Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 targets have simple viablity information"), Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_DIRECT_THREATS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%1 Direct Threats created"), Dashboard.PSEUDO_THREAT_COUNT));
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%2 of %1 have taxonomy assignments"), Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT, Dashboard.PSEUDO_THREAT_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.RANK_DIRECT_THREATS_CODE))
		{
			rowDefinitions.add(new DashboardRowDefinition(EAM.text("%2 of %1 threat/target links ranked"), Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT, Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT));
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.IDENTIFY_INDIRECT_THREATS_AND_OPPORTUNITIES_CODE))
		{
			addIdentifyIndirectThreatsAndOpportunities();
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.ASSESS_STAKEHOLDERS_CODE))
		{
			addAssessStakeholders();
		}

		if (code.equals(OpenStandardsConceptualizeQuestion.CREATE_INITIAL_CONCEPTUAL_MODEL_CODE))
		{
			addCreateInitialConceptualModel();
		}
		
		if (code.equals(OpenStandardsConceptualizeQuestion.GROUND_THRUTH_AND_REVISE_MODEL_CODE))
		{
			addGroundTruthAndReviseModel();
		}
		
		return rowDefinitions;
	}

	private void addAgreeOnRolesAndResponsibilities()
	{
	}

	private void addDevelopMapOfProjectArea()
	{
	}

	private void addIdentifyIndirectThreatsAndOpportunities()
	{
	}

	private void addAssessStakeholders()
	{
	}

	private void addCreateInitialConceptualModel()
	{
	}

	private void addGroundTruthAndReviseModel()
	{
	}	
}
