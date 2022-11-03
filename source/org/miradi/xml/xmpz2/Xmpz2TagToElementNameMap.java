/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2;

import org.miradi.objects.*;
import org.miradi.schemas.*;

import java.util.HashMap;

public class Xmpz2TagToElementNameMap implements Xmpz2XmlConstants
{
	public Xmpz2TagToElementNameMap()
	{
		fieldTagToElementMap = createTagToElementMap();
	}
	
	public String findElementName(BaseObjectSchema baseObjectSchema, AbstractFieldSchema fieldSchema)
	{
		return findElementName(baseObjectSchema.getObjectName(), fieldSchema.getTag());
	}
	
	public String findElementName(String objectName, String tagToUse)
	{	
		if (fieldTagToElementMap.containsKey(objectName))
		{
			HashMap<String, String> objectMap = fieldTagToElementMap.get(objectName);
			if (objectMap.containsKey(tagToUse))
				return objectMap.get(tagToUse);
		}
		
		return tagToUse;
	}
	
	private HashMap<String, HashMap<String, String>> createTagToElementMap()
	{
		HashMap<String, HashMap<String,String>> map = new HashMap<String, HashMap<String,String>>();
		
		map.put(PROJECT_SUMMARY, createProjectMetadataMap());
		map.put(PROJECT_SUMMARY_PLANNING, getProjectPlanningMap());
		map.put(METHOD, createTaskMap());
		map.put(ACTIVITY, createTaskMap());
		map.put(TASK, createTaskMap());
		map.put(OUTPUT, createOutputMap());
		map.put(PROJECT_RESOURCE, createProjectResourceMap());
		map.put(INDICATOR, createIndicatorMap());
		map.put(OBJECTIVE, createDesireMap());
		map.put(GOAL, createDesireMap());
		map.put(ACCOUNTING_CODE, createBaseObjectMap());
		map.put(FUNDING_SOURCE, createBaseObjectMap());
		map.put(KEY_ECOLOGICAL_ATTRIBUTE, createKeyEcologicalAttributeMap());
		map.put(CONCEPTUAL_MODEL, createDiagramObjectMap());
		map.put(CauseSchema.OBJECT_NAME, createCauseMap());
		map.put(StrategySchema.OBJECT_NAME, createStrategyMap());
		map.put(BIODIVERSITY_TARGET, createTargetMap());
		map.put(HUMAN_WELFARE_TARGET, createAbstractTargetMap());
		map.put(BIOPHYSICAL_FACTOR, createFactorMap());
		map.put(BIOPHYSICAL_RESULT, createFactorMap());
		map.put(INTERMEDIATE_RESULT, createFactorMap());
		map.put(RESULTS_CHAIN, createDiagramObjectMap());
		map.put(THREAT_REDUCTION_RESULT, createThreatReductionResultsMap());
		map.put(TEXT_BOX, createFactorMap());
		map.put(MEASUREMENT, createMeasurementMap());
		map.put(STRESS, createStressMap());
		map.put(GROUP_BOX, createFactorMap());
		map.put(SUB_TARGET, createSubTargetMap());
		map.put(ORGANIZATION, createOrganizationMap());
		map.put(PROGRESS_PERCENT, createProgressPercentMap());
		map.put(SCOPE_BOX, createScopeBoxMap());
		map.put(DIAGRAM_FACTOR, createDiagramFactorMap());
		map.put(DIAGRAM_LINK, createDiagramLinkMap());
		map.put(EXPENSE_ASSIGNMENT, createExpenseAssignmentMap());
		map.put(RESOURCE_ASSIGNMENT, createResourceAssignmentMap());
		map.put(TIMEFRAME, createTimeframeMap());
		map.put(IUCN_REDLIST_SPECIES, createBaseObjectMap());
		map.put(OTHER_NOTABLE_SPECIES, createBaseObjectMap());
		map.put(AUDIENCE, createBaseObjectMap());
		map.put(OBJECT_TREE_TABLE_CONFIGURATION, createObjectTreeTableConfigurationMap());
		map.put(TNC_PROJECT_DATA, createTncProjectDataMap());
		map.put(TAGGED_OBJECT_SET_ELEMENT_NAME, createTaggedObjectSetMap());
		map.put(BUDGET_CATEGORY_ONE, createBaseObjectMap());
		map.put(BUDGET_CATEGORY_TWO, createBaseObjectMap());
		map.put(STRESS_BASED_THREAT_RATING, createStressBasedThreatRatingMap());
		map.put(TAXONOMY, createTaxonomyMap());
		map.put(FUTURE_STATUS, createFutureStatusMap());
		map.put(ANALYTICAL_QUESTION, createAnalyticalQuestionMap());
		map.put(ASSUMPTION, createAssumptionMap());

		return map;
	}
	
	private HashMap<String, String> createFutureStatusMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(FutureStatusSchema.TAG_FUTURE_STATUS_DETAIL, DETAILS);

		return map;
	}

	private HashMap<String, String> getProjectPlanningMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, INCLUDE_WORK_PLAN_DIAGRAM_DATA);

		return map;
	}

	private HashMap<String, String> createTaxonomyMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(MiradiShareTaxonomySchema.TAG_TAXONOMY_VERSION, TAXONOMY_VERSION);
		map.put(MiradiShareTaxonomySchema.TAG_TAXONOMY_TOP_LEVEL_ELEMENT_CODES, TAXONOMY_TOP_LEVEL_ELEMENT_CODE);

		return map;
	}
	
	private HashMap<String, String> createStressBasedThreatRatingMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ThreatStressRating.TAG_CONTRIBUTION, CONTRIBUTION);
		map.put(ThreatStressRating.TAG_IRREVERSIBILITY, IRREVERSIBILITY);

		return map;
	}

	private HashMap<String, String> createObjectTreeTableConfigurationMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, DIAGRAM_DATA_INCLUSION);
		map.put(ObjectTreeTableConfiguration.TAG_DIAGRAM_FILTER, DIAGRAM_FILTER);
		map.put(ObjectTreeTableConfiguration.TAG_COL_CONFIGURATION, COLUMN_CONFIGURATION_CODES);
		map.put(ObjectTreeTableConfiguration.TAG_ROW_CONFIGURATION, ROW_CONFIGURATION_CODES);
		
		return map;
	}

	private HashMap<String, String> createBaseObjectMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseObject.TAG_LABEL, NAME);
		map.put(Target.TAG_SHORT_LABEL, IDENTIFIER);
		map.put(BaseObject.TAG_PROGRESS_REPORT_REFS, PROGRESS_REPORT_IDS);
		map.put(BaseObject.TAG_EXTENDED_PROGRESS_REPORT_REFS, EXTENDED_PROGRESS_REPORT_IDS);
		map.put(BaseObject.TAG_RESULT_REPORT_REFS, RESULTS_REPORT_IDS);
		map.put(BaseObject.TAG_OUTPUT_REFS, OUTPUT_IDS);
		map.put(BaseObject.TAG_TIMEFRAME_IDS, TIMEFRAME + IDS);
		map.put(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, RESOURCE_ASSIGNMENT + IDS);
		map.put(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS, EXPENSE_ASSIGNMENT + IDS);
		map.put(BaseObject.TAG_ASSIGNED_LEADER_RESOURCE, ASSIGNED_LEADER_RESOURCE_ID);

		return map;
	}

	private HashMap<String, String> createFactorMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Factor.TAG_TEXT, DETAILS);
		
		return map;
	}
	
	private HashMap<String, String> createThreatReductionResultsMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, RELATED_THREAT_ID);
		
		return map;
	}
	
	private HashMap<String, String> createDiagramFactorMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, GROUP_BOX_CHILDREN_IDS);
		map.put(DiagramFactor.TAG_WRAPPED_REF, WRAPPED_FACTOR_ID_ELEMENT_NAME);
		map.put(DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, TAGGED_OBJECT_SET_IDS);
		
		return map;
	}
	
	private HashMap<String, String> createDiagramLinkMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(DiagramLink.TAG_GROUPED_DIAGRAM_LINK_REFS, GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID);
		
		return map;
	}

	private HashMap<String, String> createTaskMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Task.TAG_SUBTASK_IDS, SUB_TASK_IDS);
		return map;
	}

	private HashMap<String, String> createOutputMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Output.TAG_GOAL_IDS, RELEVANT_GOAL_IDS);
		map.put(Output.TAG_OBJECTIVE_IDS, RELEVANT_OBJECTIVE_IDS);
		map.put(Output.TAG_INDICATOR_IDS, RELEVANT_INDICATOR_IDS);
		return map;
	}

	private HashMap<String, String> createAbstractAnalyticalQuestionMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(AbstractAnalyticalQuestion.TAG_INDICATOR_IDS, RELEVANT_INDICATOR_IDS);
		return map;
	}

	private HashMap<String, String> createAnalyticalQuestionMap()
	{
		HashMap<String, String> map = createAbstractAnalyticalQuestionMap();
		map.put(AnalyticalQuestion.TAG_ASSUMPTION_IDS, ASSUMPTION_IDS);
		return map;
	}

	private HashMap<String, String> createAssumptionMap()
	{
		HashMap<String, String> map = createAbstractAnalyticalQuestionMap();
		return map;
	}

	private HashMap<String, String> createIndicatorMap()
	{
		//Indicator: has no status tag Status,
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Indicator.TAG_DETAIL, DETAILS);
		map.put(Indicator.TAG_METHOD_IDS, METHOD_IDS);
		map.put(Indicator.TAG_VIABILITY_RATINGS_COMMENTS, VIABILITY_RATINGS_COMMENTS);
		map.put(Indicator.TAG_MEASUREMENT_REFS, MEASUREMENT_IDS);
		map.put(Indicator.TAG_FUTURE_STATUS_REFS, FUTURE_STATUS_IDS);
		map.put(Indicator.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, RELEVANT_STRATEGY_IDS);
		
		return map;
	}

	private HashMap<String, String> createDesireMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Desire.TAG_FULL_TEXT, DETAILS);
		map.put(Desire.TAG_PROGRESS_PERCENT_REFS, PROGRESS_PERCENT_IDS);
		map.put(Desire.TAG_RELEVANT_INDICATOR_SET, RELEVANT_INDICATOR_IDS);
		map.put(Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, RELEVANT_STRATEGY_IDS);
		
		return map;
	}
	
	private HashMap<String, String> createProjectMetadataMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(ProjectMetadata.TAG_PROJECT_SCOPE, SCOPE_DESCRIPTION);
		map.put(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, SCOPE_NAME);
		map.put(ProjectMetadata.TAG_PROJECT_VISION, VISION_STATEMENT_TEXT);
		map.put(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, VISION_LABEL);	
		map.put(ProjectMetadata.TAG_LOCATION_DETAIL, LOCATION_DETAILS);
		map.put(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, PROJECT_ID);
		map.put(TncProjectData.TAG_PROJECT_SHARING_CODE, PROJECT_SHARE_OUTSIDE_ORGANIZATION);

		return map;
	}

	private HashMap<String, String> createTncProjectDataMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		
		map.put(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, TNC_DATABASE_DOWNLOAD_DATE);
		map.put(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS, TNC_PLANNING_TEAM_COMMENTS);
		map.put(ProjectMetadata.TAG_TNC_OPERATING_UNITS, TNC_OPERATING_UNITS);
		map.put(ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, TNC_TERRESTRIAL_ECO_REGION);
		map.put(ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, TNC_MARINE_ECO_REGION);
		map.put(ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, TNC_FRESHWATER_ECO_REGION);
		map.put(TncProjectData.TAG_CAP_STANDARDS_SCORECARD, TNC_CAP_STANDARDS_SCORECARD);
		
		return map;
	}

	private HashMap<String, String> createKeyEcologicalAttributeMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(KeyEcologicalAttribute.TAG_DESCRIPTION, COMMENTS);
		
		return map;
	}

	private HashMap<String, String> createCauseMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE, STANDARD_CLASSIFICATION);
		
		return map;
	}

	private HashMap<String, String> createStrategyMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Strategy.TAG_STANDARD_CLASSIFICATION_V11_CODE, STANDARD_CLASSIFICATION);
		map.put(Strategy.TAG_ACTIVITY_IDS, ORDERED_ACTIVITY_IDS);

		return map;
	}
	
	private HashMap<String, String> createAbstractTargetMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Target.TAG_SUB_TARGET_REFS, SUB_TARGET_IDS_ELEMENT);
		map.put(Target.TAG_TARGET_STATUS, TARGET_STATUS_ELEMENT_NAME);
		map.put(Target.TAG_TARGET_FUTURE_STATUS, TARGET_FUTURE_STATUS_ELEMENT_NAME);

		return map;
	}

	private HashMap<String, String> createTargetMap()
	{
		HashMap<String, String> map = createAbstractTargetMap();
		map.put(Target.TAG_STRESS_REFS, STRESS_IDS_ELEMENT);
		
		return map;
	}

	private HashMap<String, String> createDiagramObjectMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(DiagramObject.TAG_DETAIL, DETAILS);
		map.put(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DIAGRAM_LINK_IDS);
		map.put(DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, SELECTED_TAGGED_OBJECT_SET_IDS);
		
		return map;
	}

	private HashMap<String, String> createMeasurementMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Measurement.TAG_SUMMARY, MEASUREMENT_VALUE);
		map.put(Measurement.TAG_STATUS, RATING);
		
		return map;
	}

	private HashMap<String, String> createStressMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Stress.TAG_SCOPE, SCOPE);
		map.put(Stress.TAG_SEVERITY, SEVERITY);
		map.put(Stress.TAG_DETAIL, DETAILS);
		map.put(Stress.PSEUDO_STRESS_RATING, MAGNITUDE);
		
		return map;
	}

	private HashMap<String, String> createSubTargetMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(SubTarget.TAG_DETAIL, DETAILS);
		
		return map;

	}

	private HashMap<String, String> createOrganizationMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Organization.TAG_CONTACT_FIRST_NAME, GIVEN_NAME);
		map.put(Organization.TAG_CONTACT_LAST_NAME, SURNAME);
		
		return map;
	}
	
	private HashMap<String, String> createAssignmentMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Assignment.TAG_CATEGORY_ONE_REF, BUDGET_CATEGORY_ONE_ID);
		map.put(Assignment.TAG_CATEGORY_TWO_REF, BUDGET_CATEGORY_TWO_ID);
		
		return map;
	}
	
	private HashMap<String, String> createExpenseAssignmentMap()
	{
		HashMap<String, String> map = createAssignmentMap();
		map.put(ExpenseAssignment.TAG_FUNDING_SOURCE_REF, FUNDING_SOURCE_ID);
		map.put(ExpenseAssignment.TAG_ACCOUNTING_CODE_REF, ACCOUNTING_CODE_ID);
		
		return map;
	}
	
	private HashMap<String, String> createResourceAssignmentMap()
	{
		HashMap<String, String> map = createAssignmentMap();
		map.put(ResourceAssignment.TAG_FUNDING_SOURCE_ID, FUNDING_SOURCE_ID);
		map.put(ResourceAssignment.TAG_ACCOUNTING_CODE_ID, ACCOUNTING_CODE_ID);
		
		return map;
	}

	private HashMap<String, String> createTimeframeMap()
	{
		HashMap<String, String> map = createBaseObjectMap();

		return map;
	}

	private HashMap<String, String> createProgressPercentMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createScopeBoxMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(ScopeBox.TAG_SCOPE_BOX_TYPE_CODE, SCOPE_BOX_COLOR_ELEMENT_NAME);
		
		return map;
	}

	private HashMap<String, String> createProjectResourceMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(ProjectResource.TAG_GIVEN_NAME, GIVEN_NAME);
		map.put(ProjectResource.TAG_SUR_NAME, SURNAME);
		map.put(ProjectResource.TAG_INITIALS, IDENTIFIER);
		map.put(ProjectResource.TAG_PHONE_NUMBER, OFFICE_PHONE_NUMBER);
		map.put(ProjectResource.TAG_COST_PER_UNIT, DAILY_RATE);
		map.put(ProjectResource.TAG_CUSTOM_FIELD_1, CUSTOM1);
		map.put(ProjectResource.TAG_CUSTOM_FIELD_2, CUSTOM2);
		
		return map;
	}
	
	private HashMap<String, String> createTaggedObjectSetMap()
	{
		HashMap<String, String> map = createBaseObjectMap();

		return map;
	}
	
	private HashMap<String, HashMap<String, String>> fieldTagToElementMap;
}