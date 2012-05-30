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

package org.miradi.xml.xmpz2;

import java.util.HashMap;

import org.miradi.objects.Assignment;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ExpenseAssignment;
import org.miradi.objects.Factor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.ObjectTreeTableConfiguration;
import org.miradi.objects.Objective;
import org.miradi.objects.Organization;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResourceAssignment;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TncProjectData;

public class Xmpz2TagToElementNameMap implements Xmpz2XmlConstants
{
	public Xmpz2TagToElementNameMap()
	{
		fieldTagToElementMap = createTagToElementMap();
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
		
		map.put(METHOD, createTaskMap());
		map.put(ACTIVITY, createTaskMap());
		map.put(TASK, createTaskMap());
		map.put(PROJECT_RESOURCE, createProjectResourceMap());
		map.put(INDICATOR, createIndicatorMap());
		map.put(OBJECTIVE, createDesireMap());
		map.put(GOAL, createDesireMap());
		map.put(PROJECT_METADATA, createProjectMetadataMap());
		map.put(ACCOUNTING_CODE, createBaseObjectMap());
		map.put(FUNDING_SOURCE, createBaseObjectMap());
		map.put(KEY_ECOLOGICAL_ATTRIBUTE, createKeyEcologicalAttributeMap());
		map.put(CONCEPTUAL_MODEL, createDiagramObjectMap());
		map.put(CAUSE, createCauseMap());
		map.put(STRATEGY, createStrategyMap());
		map.put(BIODIVERSITY_TARGET, createTargetMap());
		map.put(HUMAN_WELFARE_TARGET, createAbstractTargetMap());
		map.put(INTERMEDIATE_RESULTS, createFactorMap());
		map.put(RESULTS_CHAIN, createDiagramObjectMap());
		map.put(THREAT_REDUCTION_RESULTS, createFactorMap());
		map.put(TEXT_BOX, createFactorMap());
		map.put(MEASUREMENT, createMeasurementMap());
		map.put(STRESS, createStressMap());
		map.put(GROUP_BOX, createFactorMap());
		map.put(SUB_TARGET, createSubTargetMap());
		map.put(ORGANIZATION, createOrganizationMap());
		map.put(PROGRESS_PERCENT, createProgressPercentMap());
		map.put(SCOPE_BOX, createScopeBoxMap());
		map.put(DIAGRAM_FACTOR, createBaseObjectMap());
		map.put(DIAGRAM_LINK, createBaseObjectMap());
		map.put(EXPENSE_ASSIGNMENT, createExpenseAssignmentMap());
		map.put(RESOURCE_ASSIGNMENT, createResourceAssignmentMap());
		map.put(IUCN_REDLIST_SPECIES, createBaseObjectMap());
		map.put(OTHER_NOTABLE_SPECIES, createBaseObjectMap());
		map.put(AUDIENCE, createBaseObjectMap());
		map.put(OBJECT_TREE_TABLE_CONFIGURATION, createObjectTreeTableConfigurationMap());
		map.put(TNC_PROJECT_DATA, createTncProjectDataMap());
		map.put(TAGGED_OBJECT_SET_ELEMENT_NAME, createBaseObjectMap());
		
		return map;
	}
	
	private HashMap<String, String> createObjectTreeTableConfigurationMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(ObjectTreeTableConfiguration.TAG_DIAGRAM_DATA_INCLUSION, DIAGRAM_DATA_INCLUSION);
		
		return map;
	}

	private HashMap<String, String> createBaseObjectMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseObject.TAG_LABEL, NAME);
		map.put(Target.TAG_SHORT_LABEL, ID);
		map.put(BaseObject.TAG_PROGRESS_REPORT_REFS, PROGRESS_REPORT_IDS);
		map.put(BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS, RESOURCE_ASSIGNMENT + IDS);
		map.put(BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS, EXPENSE_ASSIGNMENT + IDS);

		return map;
	}

	private HashMap<String, String> createFactorMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Factor.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createTaskMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Task.TAG_SUBTASK_IDS, SUB_TASK_IDS);
		return map;
	}

	private HashMap<String, String> createIndicatorMap()
	{
		//Indicator: has no status tag Status,
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Indicator.TAG_DETAIL, DETAILS);
		map.put(Indicator.TAG_FUTURE_STATUS_DETAIL, FUTURE_STATUS_DETAILS);
		map.put(Indicator.TAG_METHOD_IDS, METHOD_IDS);
		map.put(Indicator.TAG_FUTURE_STATUS_COMMENTS, FUTURE_STATUS_COMMENTS);
		map.put(Indicator.TAG_VIABILITY_RATINGS_COMMENTS, VIABILITY_RATINGS_COMMENTS);
		map.put(Indicator.TAG_MEASUREMENT_REFS, MEASUREMENT_IDS);
		
		return map;
	}

	private HashMap<String, String> createDesireMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		map.put(Objective.TAG_FULL_TEXT, DETAILS);
		map.put(Objective.TAG_PROGRESS_PERCENT_REFS, PROGRESS_PERCENT_IDS);
		
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
		map.put(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, CONPRO_PROJECT_NUMBER);

		return map;
	}

	private HashMap<String, String> createTncProjectDataMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
		
		map.put(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, TNC_DATABASE_DOWNLOAD_DATE);
		map.put(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS, TNC_PLANNING_TEAM_COMMENTS);
		map.put(ProjectMetadata.TAG_TNC_LESSONS_LEARNED, TNC_LESSONS_LEARNED);
		map.put(ProjectMetadata.TAG_TNC_OPERATING_UNITS, TNC_OPERATING_UNITS);
		map.put(TncProjectData.TAG_PROJECT_PLACE_TYPES, TNC_PROJECT_PLACE_TYPES);
		map.put(TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, TNC_ORGANIZATIONAL_PRIORITIES);
		map.put(ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, TNC_TERRESTRIAL_ECO_REGION);
		map.put(ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, TNC_MARINE_ECO_REGION);
		map.put(ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, TNC_FRESHWATER_ECO_REGION);
		
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
		map.put(Cause.TAG_TAXONOMY_CODE, STANDARD_CLASSIFICATION);
		
		return map;
	}

	private HashMap<String, String> createStrategyMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Strategy.TAG_TAXONOMY_CODE, STANDARD_CLASSIFICATION);
		map.put(Strategy.TAG_ACTIVITY_IDS, SORTED_ACTIVITY_IDS);
		
		return map;
	}
	
	private HashMap<String, String> createAbstractTargetMap()
	{
		HashMap<String, String> map = createFactorMap();
		map.put(Target.TAG_SUB_TARGET_REFS, SUB_TARGET_IDS_ELEMENT);
		
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
		map.put(Measurement.TAG_STATUS_CONFIDENCE, SOURCE);
		map.put(Measurement.TAG_SUMMARY, MEASUREMENT_VALUE);
		map.put(Measurement.TAG_STATUS, RATING);
		
		return map;
	}

	private HashMap<String, String> createStressMap()
	{
		HashMap<String, String> map = createBaseObjectMap();
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
		map.put(Organization.TAG_CONTACT_LAST_NAME, Surname);
		
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
		map.put(ProjectResource.TAG_INITIALS, RESOURCE_ID);
		map.put(ProjectResource.TAG_SUR_NAME, Surname);
		map.put(ProjectResource.TAG_PHONE_NUMBER, OFFICE_PHONE_NUMBER);
		map.put(ProjectResource.TAG_COST_PER_UNIT, DAILY_RATE);
		
		return map;
	}
	
	private HashMap<String, HashMap<String, String>> fieldTagToElementMap;
	private static final String DETAILS = "Details";
	private static final String STANDARD_CLASSIFICATION = "StandardClassification";
	private static final String GIVEN_NAME = "GivenName";
	private static final String IDS = "Ids";
	private static final String Surname = "Surname";
	private final String SOURCE = "Source";
	private final String MEASUREMENT_VALUE = "MeasurementValue";
	private final String RATING = "Rating";
	private final String MAGNITUDE = "Magnitude";
	private final String OFFICE_PHONE_NUMBER = "OfficePhoneNumber";
	private final String RESOURCE_ID = "Resource_Id";
	private final String NAME = "Name";
	private final String COMMENTS = "Comments";
	private final String VIABILITY_RATINGS_COMMENTS = "ViabilityRatingsComments";
	private final String FUTURE_STATUS_COMMENTS = "FutureStatusComments";
	private final String FUTURE_STATUS_DETAILS = "FutureStatusDetails";
	private final String CONPRO_PROJECT_NUMBER = "ConProProjectNumber";
	private final String LOCATION_DETAILS = "LocationDetails";
	private final String VISION_LABEL = "VisionLabel";
	private final String VISION_STATEMENT_TEXT = "VisionStatementText";
	private final String SCOPE_NAME = "ScopeName";
	private final String SCOPE_DESCRIPTION = "ScopeDescription";
	private final String DIAGRAM_DATA_INCLUSION = "DiagramDataInclusion";
	private final String DAILY_RATE = "DailyRate";
}