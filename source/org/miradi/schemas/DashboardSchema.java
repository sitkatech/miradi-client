/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Dashboard;

public class DashboardSchema extends BaseObjectSchema
{
	public static final String OBJECT_NAME = "Dashboard";

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

		createPseudoFieldSchemaString(Dashboard.PSEUDO_TEAM_MEMBER_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGET_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGET_WITH_KEA_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_CONTRIBUTING_FACTOR_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_WITH_TAXONOMY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_TARGET_LINK_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_SIMPLE_RATING_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_TARGET_LINK_WITH_STRESS_BASED_RATING_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_GOAL_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_DRAFT_STRATEGY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_RANKED_DRAFT_STRATEGY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGY__WITH_TAXONOMY_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_RESULTS_CHAIN_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVE_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_RESULTS_CHAIN_WITH_OBJECTIVE_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_STRATEGIES_PERCENTAGE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGIES_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_KEA_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_FACTOR_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVES_RELEVANT_TO_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_PROJECT_PLANNING_START_DATE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_PROJECT_PLANNING_END_DATE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGIES_WITH_ACTIVITIES_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_ACTIVITIES_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_ACTIVITIES_AND_TASKS_WITH_ASSIGNMENTS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_WITH_METHODS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_METHODS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_METHODS_AND_TASKS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_METHODS_AND_TASKS_WITH_ASSIGNMENT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_WORK_PLAN_START_DATE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_WORK_PLAN_END_DATE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_PROJECT_RESOURCES_COSTS);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_PROJECT_EXPENSES);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_PROPOSED_BUDGET);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_CURRENCY_SYMBOL);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_BUDGET_SECURED_PERCENT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_STRATEGIES_AND_ACTIVITIES_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_AND_METHODS_WITH_PROGRESS_REPORT_PERCENT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGETS_WITH_GOALS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_CONCEPTUAL_MODEL_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_ALL_FACTOR_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_RELEVANT_TO_OBJECTIVES_PERCENTAGE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_IRRELEVANT_TO_OBJECIVES_PERCENTAGE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGET_WITH_KEA_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_DIRECT_THREAT_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_THREAT_REDUCTION_RESULT_INDICATORS_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OTHER_ORGANIZATION_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_WITH_DESIRED_FUTURE_STATUS_SPECIFIED_PERCENTAGE);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_WITH_NO_MEASUREMENT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_WITH_ONE_MEASUREMENT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_INDICATORS_WITH_MORE_THAN_ONE_MEASUREMENT_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVES_WITH_NO_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVES_WITH_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_OBJECTIVES_WITH_MORE_THAN_ONE_PERCENT_COMPLETE_RECORD_COUNT);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_ACTION_BUDGET);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_MONITORING_BUDGET);
		createPseudoFieldSchemaString(Dashboard.PSEUDO_TOTAL_FACTOR_COUNT);
	}

	public static int getObjectType()
	{
		return ObjectType.DASHBOARD;
	}
}
