/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(Dashboard.on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. (Dashboard."Benetech"), Palo Alto, California. 

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

package org.miradi.schemas;

import org.miradi.objects.Dashboard;

public class DashboardSchema extends BaseObjectSchema
{
	public DashboardSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
	
		createFieldSchemaCodeToChoiceMap(Dashboard.TAG_PROGRESS_CHOICE_MAP);
		createFieldSchemaCodeToUserStringMap(Dashboard.TAG_COMMENTS_MAP);
		createFieldSchemaCodeToCodeListMap(Dashboard.TAG_FLAGS_MAP);
		createFieldSchemaCode(Dashboard.TAG_CURRENT_DASHBOARD_TAB).setNavigationField();

		createPseudoStringField(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_TARGET_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_CONTRIBUTING_FACTOR_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_SIMPLE_RATING_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_STRESS_BASED_RATING_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_GOAL_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_RESULTS_CHAIN_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVE_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGIES_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoStringField(Dashboard.PSEUDO_KEA_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE);
		createPseudoStringField(Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_ACTIVITIES_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_METHODS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_WORK_PLAN_START_DATE);
		createPseudoStringField(Dashboard.PSEUDO_WORK_PLAN_END_DATE);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_PROPOSED_BUDGET);
		createPseudoStringField(Dashboard.PSEUDO_CURRENCY_SYMBOL);
		createPseudoStringField(Dashboard.PSEUDO_BUDGET_SECURED_PERCENT);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoStringField(Dashboard.PSEUDO_TARGETS_WITH_GOALS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_CONCEPTUAL_MODEL_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_ALL_FACTOR_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_IRRELEVANT_TO_OBJECIVES_PERCENTAGE);
		createPseudoStringField(Dashboard.PSEUDO_TARGET_WITH_KEA_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_DIRECT_THREAT_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_THREAT_REDUCTION_RESULT_INDICATORS_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OTHER_ORGANIZATION_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_WITH_DESIRED_FUTURE_STATUS_SPECIFIED_PERCENTAGE);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_WITH_NO_MEASUREMENT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_WITH_ONE_MEASUREMENT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_INDICATORS_WITH_MORE_THAN_ONE_MEASUREMENT_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVES_WITH_NO_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVES_WITH_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_OBJECTIVES_WITH_MORE_THAN_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_ACTION_BUDGET);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_MONITORING_BUDGET);
		createPseudoStringField(Dashboard.PSEUDO_TOTAL_FACTOR_COUNT);
	}
}
