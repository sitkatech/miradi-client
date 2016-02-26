/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.schemas;

import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.*;

public class ProjectMetadataSchema extends BaseObjectSchema
{
	public ProjectMetadataSchema()
	{
		super();
	}
	
	@Override
	protected void fillFieldSchemas()
	{
		super.fillFieldSchemas();
		
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_PROJECT_NAME);
		createFieldSchemaChoice(ProjectMetadata.TAG_PROJECT_LANGUAGE, new MajorLanguagesQuestion());
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_PROJECT_URL);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROJECT_SCOPE);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_SCOPE_COMMENTS);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROJECT_VISION);
		
		createFieldSchemaDate(ProjectMetadata.TAG_START_DATE);
		createFieldSchemaDate(ProjectMetadata.TAG_EXPECTED_END_DATE);
		createFieldSchemaDate(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		
		createFieldSchemaInteger(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		createFieldSchemaChoice(ProjectMetadata.TAG_CURRENCY_TYPE, CurrencyTypeQuestion.class);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		createFieldSchemaNumber(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		createFieldSchemaPercentage(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		createFieldSchemaChoice(ProjectMetadata.TAG_FISCAL_YEAR_START, FiscalYearStartQuestion.class);
		createFieldSchemaNumber(ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		createFieldSchemaRequiredChoice(ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, QuarterColumnsVisibilityQuestion.class);
		createFieldSchemaRequiredChoice(ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY, DayColumnsVisibilityQuestion.class);
		createFieldSchemaRequiredChoice(ProjectMetadata.TAG_PLANNING_TREE_TARGET_NODE_POSITION, PlanningTreeTargetPositionQuestion.class);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_WORK_UNIT_RATE_DESCRIPTION);

		createFieldSchemaFloat(ProjectMetadata.TAG_PROJECT_LATITUDE);
		createFieldSchemaFloat(ProjectMetadata.TAG_PROJECT_LONGITUDE);
		// NOTE: ProjectArea should be a float, but can't be for legacy reasons
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_PROJECT_AREA);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROJECT_AREA_NOTES);

		createFieldSchemaCodeList(ProjectMetadata.TAG_COUNTRIES, getQuestion(CountriesQuestion.class));
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_STATE_AND_PROVINCES);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_MUNICIPALITIES);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_LOCATION_DETAIL);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_LOCATION_COMMENTS);

		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		createFieldSchemaDate(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		createFieldSchemaDate(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		createFieldSchemaChoice(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, BudgetTimePeriodQuestion.class);
		createFieldSchemaChoice(ProjectMetadata.TAG_WORKPLAN_DISPLAY_MODE, WorkPlanDisplayModeQuestion.class);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PLANNING_COMMENTS);
		
		createFieldSchemaInteger(ProjectMetadata.TAG_HUMAN_POPULATION);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_SOCIAL_CONTEXT);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_SITE_MAP_REFERENCE);

		createFieldSchemaCodeList(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getQuestion(ProtectedAreaCategoryQuestion.class));
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);

		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_PROJECT_STATUS);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_NEXT_STEPS);

		createFieldSchemaCode(ProjectMetadata.TAG_CURRENT_WIZARD_SCREEN_NAME).setNavigationField();

		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_NUMBER);
		createFieldSchemaDate(ProjectMetadata.TAG_TNC_WORKBOOK_VERSION_DATE);
		createFieldSchemaDate(ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		createFieldSchemaMultiLineUserText(ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		createFieldSchemaCodeList(ProjectMetadata.TAG_TNC_OPERATING_UNITS, getQuestion(TncOperatingUnitsQuestion.class));
		createFieldSchemaCodeList(ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, getQuestion(TncTerrestrialEcoRegionQuestion.class));
		createFieldSchemaCodeList(ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, getQuestion(TncMarineEcoRegionQuestion.class));
		createFieldSchemaCodeList(ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, getQuestion(TncFreshwaterEcoRegionQuestion.class));
		
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		createFieldSchemaSingleLineUserText(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		
		createFieldSchemaChoice(ProjectMetadata.TAG_DIAGRAM_FONT_SIZE, FontSizeQuestion.class);
		createFieldSchemaChoice(ProjectMetadata.TAG_DIAGRAM_FONT_FAMILY, FontFamiliyQuestion.class);
		createFieldSchemaRequiredChoice(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.class);
		createFieldSchemaStringRefMap(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP);
		createFieldSchemaChoice(ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE, TargetModeQuestion.class);
		createFieldSchemaChoice(ProjectMetadata.TAG_BIOPHYSICAL_FACTOR_MODE, FactorModeQuestion.class);
		createFieldSchemaChoice(ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION, DiagramObjectDataInclusionQuestion.class);
		
		createPseudoFieldSchemaString(ProjectMetadata.PSEUDO_TAG_PROJECT_FILENAME);
		createPseudoFieldSchemaString(ProjectMetadata.PSEUDO_TAG_ALL_THREAT_CLASSIFICATIONS);
	}

	@Override
	public String getXmpz2ElementName()
	{
		return PROJECT_SUMMARY;
	}
	
	public static int getObjectType()
	{
		return ObjectType.PROJECT_METADATA;
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getObjectName()
	{
		return OBJECT_NAME;
	}
	
	public static final String OBJECT_NAME = "ProjectMetadata";
}
