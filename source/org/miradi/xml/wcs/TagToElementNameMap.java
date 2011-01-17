/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.wcs;

import java.util.HashMap;

import org.miradi.objects.AccountingCode;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.FundingSource;
import org.miradi.objects.Goal;
import org.miradi.objects.GroupBox;
import org.miradi.objects.HumanWelfareTarget;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.Organization;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.ScopeBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.TextBox;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.xml.generic.XmlSchemaCreator;

public class TagToElementNameMap implements XmpzXmlConstants
{
	public TagToElementNameMap()
	{
		createTagToElementMap();
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
	
	private void createTagToElementMap()
	{
		fieldTagToElementMap = new HashMap<String, HashMap<String,String>>();
		
		fieldTagToElementMap.put(METHOD, createTaskMap());
		fieldTagToElementMap.put(ACTIVITY, createTaskMap());
		fieldTagToElementMap.put(TASK, createTaskMap());
		fieldTagToElementMap.put(PROJECT_RESOURCE, createProjectResourceMap());
		fieldTagToElementMap.put(INDICATOR, createIndicatorMap());
		fieldTagToElementMap.put(OBJECTIVE, createObjectiveMap());
		fieldTagToElementMap.put(GOAL, createGoalMap());
		fieldTagToElementMap.put(PROJECT_METADATA, createProjectMetadataMap());
		fieldTagToElementMap.put(ACCOUNTING_CODE, createAccountingCodeMap());
		fieldTagToElementMap.put(FUNDING_SOURCE, createFundingSourceMap());
		fieldTagToElementMap.put(KEY_ECOLOGICAL_ATTRIBUTE, createKeyEcologicalAttributeMap());
		fieldTagToElementMap.put(CONCEPTUAL_MODEL, createConceptualModelMap());
		fieldTagToElementMap.put(CAUSE, createCauseMap());
		fieldTagToElementMap.put(STRATEGY, createStrategyMap());
		fieldTagToElementMap.put(BIODIVERSITY_TARGET, createTargetMap());
		fieldTagToElementMap.put(INTERMEDIATE_RESULTS, createInterMediateResultMap());
		fieldTagToElementMap.put(RESULTS_CHAIN, createResultsChainMap());
		fieldTagToElementMap.put(THREAT_REDUCTION_RESULTS, createThreatReductionResultsMap());
		fieldTagToElementMap.put(TEXT_BOX, createTextBoxMap());
		fieldTagToElementMap.put(MEASUREMENT, createMeasurementMap());
		fieldTagToElementMap.put(STRESS, createStressMap());
		fieldTagToElementMap.put(GROUP_BOX, createGroupBoxMap());
		fieldTagToElementMap.put(SUB_TARGET, createSubTargetMap());
		fieldTagToElementMap.put(ORGANIZATION, createOrganizationMap());
		fieldTagToElementMap.put(PROGRESS_PERCENT, createProgressPercentMap());
		fieldTagToElementMap.put(SCOPE_BOX, createScopeBoxMap());
		fieldTagToElementMap.put(HUMAN_WELFARE_TARGET, createHumanWelfareTargetMap());
		fieldTagToElementMap.put(DIAGRAM_FACTOR, createBaseObjectMap());
		fieldTagToElementMap.put(DIAGRAM_LINK, createBaseObjectMap());
		fieldTagToElementMap.put(EXPENSE_ASSIGNMENT, createBaseObjectMap());
		fieldTagToElementMap.put(RESOURCE_ASSIGNMENT, createBaseObjectMap());
		fieldTagToElementMap.put(IUCN_REDLIST_SPECIES, createBaseObjectMap());
		fieldTagToElementMap.put(OTHER_NOTABLE_SPECIES, createBaseObjectMap());
		fieldTagToElementMap.put(AUDIENCE, createBaseObjectMap());
		fieldTagToElementMap.put(OBJECT_TREE_TABLE_CONFIGURATION, createBaseObjectMap());
		fieldTagToElementMap.put(TNC_PROJECT_DATA, createTncProjectDataMap());
	}

	private HashMap<String, String> createBaseObjectMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(BaseObject.TAG_LABEL, "Name");
		return map;
	}

	private HashMap<String, String> createTaskMap()
	{
		HashMap<String, String> taskMap = new HashMap<String, String>();
		taskMap.put(Task.TAG_LABEL, "Name");
		taskMap.put(Task.TAG_SHORT_LABEL, "Id");
		taskMap.put(Task.TAG_SUBTASK_IDS, XmpzXmlConstants.SUB_TASK_IDS);
		return taskMap;
	}

	private HashMap<String, String> createIndicatorMap()
	{
		//Indicator: has no status tag Status,
		HashMap<String, String> indicatorMap = new HashMap<String, String>();
		indicatorMap.put(Indicator.TAG_LABEL, "Name");
		indicatorMap.put(Indicator.TAG_SHORT_LABEL, "Id");
		indicatorMap.put(Indicator.TAG_DETAIL, DETAILS);
		indicatorMap.put(Indicator.TAG_VIABILITY_RATINGS_COMMENT, "Comments");
		indicatorMap.put(Indicator.TAG_FUTURE_STATUS_DETAIL, "FutureStatusDetails");
		indicatorMap.put(Indicator.TAG_METHOD_IDS, XmpzXmlConstants.METHOD_IDS);
		
		return indicatorMap;
	}

	private HashMap<String, String> createObjectiveMap()
	{
		HashMap<String, String> objectiveMap = new HashMap<String, String>();
		objectiveMap.put(Objective.TAG_LABEL, "Name");
		objectiveMap.put(Objective.TAG_SHORT_LABEL, "Id");
		objectiveMap.put(Objective.TAG_FULL_TEXT, DETAILS);
		
		return objectiveMap;
	}
	
	private HashMap<String, String> createGoalMap()
	{
		HashMap<String, String> goalMap = new HashMap<String, String>();
		goalMap.put(Goal.TAG_LABEL, "Name");
		goalMap.put(Goal.TAG_SHORT_LABEL, "Id");
		goalMap.put(Goal.TAG_FULL_TEXT, DETAILS);

		return goalMap;
	}

	private HashMap<String, String> createProjectMetadataMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ProjectMetadata.TAG_PROJECT_SCOPE, "ScopeDescription");
		map.put(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "ScopeName");
		map.put(ProjectMetadata.TAG_PROJECT_VISION, "VisionStatementText");
		map.put(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE, "VisionLabel");	
		map.put(ProjectMetadata.TAG_TNC_LESSONS_LEARNED, "LessonsLearned");
		map.put(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER, "CAPWorkbookVersionNumber");
		map.put(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE, "CAPWorkbookVersionDate");
		map.put(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE, "ConProDatabaseDownloadDate");
		map.put(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT, "PlanningTeamLegacy");
		map.put(ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, "TerrestrialEcoregion");
		map.put(ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, "MarineEcoregion");
		map.put(ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, "FreshwaterEcoregion");
		map.put(ProjectMetadata.TAG_LOCATION_DETAIL, "LocationDetails");
		map.put(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, "ConProProjectNumber");

		return map;
	}
	
	private HashMap<String, String> createTncProjectDataMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ProjectMetadata.TAG_TNC_OPERATING_UNITS, XmlSchemaCreator.TNC_OPERATING_UNITS);
		
		return map;
	}

	private HashMap<String, String> createAccountingCodeMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(AccountingCode.TAG_LABEL, "Name");

		return map;
	}

	private HashMap<String, String> createFundingSourceMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(FundingSource.TAG_LABEL, "Name");

		return map;
	}

	private HashMap<String, String> createKeyEcologicalAttributeMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(KeyEcologicalAttribute.TAG_LABEL, "Name");
		map.put(KeyEcologicalAttribute.TAG_SHORT_LABEL, "Id");
		map.put(KeyEcologicalAttribute.TAG_DESCRIPTION, "Comments");
		
		return map;
	}

	private HashMap<String, String> createConceptualModelMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ConceptualModelDiagram.TAG_LABEL, "Name");
		map.put(ConceptualModelDiagram.TAG_SHORT_LABEL, "Id");
		map.put(ConceptualModelDiagram.TAG_DETAIL, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createCauseMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Cause.TAG_LABEL, "Name");
		map.put(Cause.TAG_SHORT_LABEL, "Id");
		map.put(Cause.TAG_TAXONOMY_CODE, "StandardClassification");
		map.put(Cause.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createStrategyMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Strategy.TAG_LABEL, "Name");
		map.put(Strategy.TAG_SHORT_LABEL, "Id");
		map.put(Strategy.TAG_TAXONOMY_CODE, "StandardClassification");
		map.put(Strategy.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createTargetMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Target.TAG_LABEL, "Name");
		map.put(Target.TAG_SHORT_LABEL, "Id");
		map.put(Target.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createInterMediateResultMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(IntermediateResult.TAG_LABEL, "Name");
		map.put(IntermediateResult.TAG_SHORT_LABEL, "Id");
		map.put(IntermediateResult.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createResultsChainMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ResultsChainDiagram.TAG_LABEL, "Name");
		map.put(ResultsChainDiagram.TAG_SHORT_LABEL, "Id");
		map.put(ResultsChainDiagram.TAG_DETAIL, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createThreatReductionResultsMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ThreatReductionResult.TAG_LABEL, "Name");
		map.put(ThreatReductionResult.TAG_SHORT_LABEL, "Id");
		map.put(ThreatReductionResult.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createTextBoxMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(TextBox.TAG_LABEL, "Name");
		map.put(TextBox.TAG_SHORT_LABEL, "Id");
		map.put(TextBox.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createMeasurementMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Measurement.TAG_LABEL, "Name");
		map.put(Measurement.TAG_STATUS_CONFIDENCE, "Source");
		map.put(Measurement.TAG_SUMMARY, "MeasurementValue");
		map.put(Measurement.TAG_STATUS, "Rating");
		
		return map;
	}

	private HashMap<String, String> createStressMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Stress.TAG_LABEL, "Name");
		map.put(Stress.TAG_SHORT_LABEL, "Id");
		map.put(Stress.TAG_DETAIL, DETAILS);
		map.put(Stress.PSEUDO_STRESS_RATING, "Magnitude");
		
		return map;
	}

	private HashMap<String, String> createGroupBoxMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(GroupBox.TAG_LABEL, "Name");
		map.put(GroupBox.TAG_SHORT_LABEL, "Id");
		map.put(GroupBox.TAG_TEXT, DETAILS);
		
		
		return map;
	}

	private HashMap<String, String> createSubTargetMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SubTarget.TAG_LABEL, "Name");
		map.put(SubTarget.TAG_SHORT_LABEL, "Id");
		map.put(SubTarget.TAG_DETAIL, DETAILS);
		
		return map;

	}

	private HashMap<String, String> createOrganizationMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(Organization.TAG_LABEL, "Name");
		map.put(Organization.TAG_SHORT_LABEL, "Id");
		map.put(Organization.TAG_CONTACT_FIRST_NAME, "GivenName");
		map.put(Organization.TAG_CONTACT_LAST_NAME, "Surname");
		
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
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(ScopeBox.TAG_LABEL, "Name");
		map.put(Organization.TAG_SHORT_LABEL, "Id");
		map.put(ScopeBox.TAG_TEXT, DETAILS);
		map.put(ScopeBox.TAG_SCOPE_BOX_TYPE_CODE, XmlSchemaCreator.SCOPE_BOX_COLOR_ELEMENT_NAME);
		
		return map;
	}

	private HashMap<String, String> createHumanWelfareTargetMap()
	{
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(HumanWelfareTarget.TAG_LABEL, "Name");
		map.put(HumanWelfareTarget.TAG_SHORT_LABEL, "Id");
		map.put(HumanWelfareTarget.TAG_TEXT, DETAILS);
		
		return map;
	}

	private HashMap<String, String> createProjectResourceMap()
	{
		HashMap<String, String> projectResourceMap = new HashMap<String, String>();
		projectResourceMap.put(ProjectResource.TAG_LABEL, "Name");
		projectResourceMap.put(ProjectResource.TAG_GIVEN_NAME, "GivenName");
		projectResourceMap.put(ProjectResource.TAG_INITIALS, "Resource_Id");
		projectResourceMap.put(ProjectResource.TAG_SUR_NAME, "Surname");
		projectResourceMap.put(ProjectResource.TAG_PHONE_NUMBER, "OfficePhoneNumber");
		projectResourceMap.put(ProjectResource.TAG_COST_PER_UNIT, "DailyRate");
		
		return projectResourceMap;
	}
	
	private HashMap<String, HashMap<String, String>> fieldTagToElementMap;
	private static final String DETAILS = "Details";
}
