/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.MiradiShareProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.Target;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;
import org.miradi.questions.*;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.MiradiShareProjectDataSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.xmpz2.SingletonBaseObjectExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class SingletonObjectExporter implements Xmpz2XmlConstants
{
	public SingletonObjectExporter(Xmpz2XmlWriter writerToUse)
	{
		writer = writerToUse;
	}
	
	public void writeBaseObjectDataSchemaElement() throws Exception
	{
		writeProjectMetadata();
		writeTncElement();
		writeWwfElement();
		writeWcsElement();
		writeRareElement();
		writeFosElement();
		writeMiradiShareProjectDataElement();
	}

	private void writeProjectMetadata() throws Exception
	{
		ProjectMetadata projectMetadata = getMetadata();
		BaseObjectSchema baseObjectSchema = projectMetadata.getSchema();

		writeProjectSummary(projectMetadata, baseObjectSchema);
		writeProjectSummaryScopeSchemaElement();
		writeProjectSummaryLocationSchemaElement();
		writeProjectSummaryPlanningSchemaElement();
	}

	private ProjectMetadata getMetadata()
	{
		return getProject().getMetadata();
	}
	
	private void writeProjectSummary(BaseObject baseObject, BaseObjectSchema baseObjectSchema) throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY);

		getWriter().writeElement(PROJECT_SUMMARY, getMetadata(), ProjectMetadata.TAG_PROJECT_NAME);
		writeShareOutsideOrganizationElement();
		writeProjectSummaryElement(ProjectMetadata.TAG_PROJECT_LANGUAGE);
		writeProjectSummaryElement(ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		writeProjectSummaryElement(ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		writeProjectSummaryElement(ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeProjectSummaryElement(ProjectMetadata.TAG_PROJECT_URL);
		writeProjectSummaryElement(ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		writeOverallProjectThreatRating();
		writeOverallProjectViabilityRating();
		writeOverallProjectViabilityFutureRating();
		writeExternalAppIds();
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion(), getMetadata().getThreatRatingMode());
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE, new TargetModeQuestion(), getMetadata().getData(ProjectMetadata.TAG_HUMAN_WELFARE_TARGET_MODE));
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_FACTOR_MODE, new FactorModeQuestion(), getMetadata().getData(ProjectMetadata.TAG_FACTOR_MODE));

		ChoiceQuestion budgetTimePeriodQuestion = StaticQuestionManager.getQuestion(BudgetTimePeriodQuestion.class);
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, budgetTimePeriodQuestion, getMetadata().getData(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT));

		ORefList progress_report_refs = new ORefList(getMetadata().getData(ProjectMetadata.TAG_EXTENDED_PROGRESS_REPORT_REFS));
		getWriter().writeNonOptionalReflist(PROJECT_SUMMARY + EXTENDED_PROGRESS_REPORT_IDS, EXTENDED_PROGRESS_REPORT, progress_report_refs);

		getWriter().writeEndElement(PROJECT_SUMMARY);
	}
	
	private void writeShareOutsideOrganizationElement() throws Exception
	{
		getWriter().writeChoiceData(PROJECT_SUMMARY + PROJECT_SHARE_OUTSIDE_ORGANIZATION, getTncProjectData(), TncProjectData.TAG_PROJECT_SHARING_CODE);
	}
	
	private void writeOverallProjectThreatRating() throws Exception
	{
		int rawOverallProjectThreatRatingCode = getProject().getProjectSummaryThreatRating();
		if (rawOverallProjectThreatRatingCode == 0)
			return;
		
		getWriter().writeElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING, Integer.toString(rawOverallProjectThreatRatingCode));
	}
	
	private void writeOverallProjectViabilityRating() throws Exception
	{
		String code = Target.computeOverallProjectViability(getProject());
		ChoiceItem choiceItem = StaticQuestionManager.getQuestion(StatusQuestion.class).findChoiceByCode(code);
		
		getWriter().writeElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING, choiceItem.getCode());
	}
	
	private void writeOverallProjectViabilityFutureRating() throws Exception
	{
		String code = Target.computeOverallProjectFutureViability(getProject());
		ChoiceItem choiceItem = StaticQuestionManager.getQuestion(StatusQuestion.class).findChoiceByCode(code);

		getWriter().writeElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_FUTURE_RATING, choiceItem.getCode());
	}

	private void writeExternalAppIds() throws Exception
	{
		String stringRefMapAsString = getMetadata().getData(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP);
		getWriter().writeRefMapXenoData(stringRefMapAsString);
	}

	protected WcpaProjectData getWcpaProjectData()
	{
		ORef wcpaProjectDataRef = getProject().getSingletonObjectRef(WcpaProjectDataSchema.getObjectType());
		return WcpaProjectData.find(getProject(), wcpaProjectDataRef);
	}
	
	protected TncProjectData getTncProjectData()
	{
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		return TncProjectData.find(getProject(), tncProjectDataRef);
	}
	
	protected WwfProjectData getWwfProjectData()
	{
		ORef wwfProjectDataRef = getProject().getSingletonObjectRef(WwfProjectDataSchema.getObjectType());
		return WwfProjectData.find(getProject(), wwfProjectDataRef);
	}

	protected WcsProjectData getWcsProjectData()
	{
		ORef wwfProjectDataRef = getProject().getSingletonObjectRef(WcsProjectDataSchema.getObjectType());
		return WcsProjectData.find(getProject(), wwfProjectDataRef);
	}
	
	protected RareProjectData getRareProjectData()
	{
		ORef rareProjectDataRef = getProject().getSingletonObjectRef(RareProjectDataSchema.getObjectType());
		return RareProjectData.find(getProject(), rareProjectDataRef);
	}
	
	protected FosProjectData getFosProjectData()
	{
		ORef fosProjectDataRef = getProject().getSingletonObjectRef(FosProjectDataSchema.getObjectType());
		return FosProjectData.find(getProject(), fosProjectDataRef);
	}
	
	private MiradiShareProjectData getMiradiShareProjectData()
	{
		ORef miradiShareProjectDataRef = getProject().getSingletonObjectRef(MiradiShareProjectDataSchema.getObjectType());
		return MiradiShareProjectData.find(getProject(), miradiShareProjectDataRef);
	}
	
	private void writeProjectSummaryScopeSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_SCOPE);
		
		writeProjectScopeElement(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		writeProjectScopeElement(ProjectMetadata.TAG_PROJECT_SCOPE);
		writeProjectScopeElement(ProjectMetadata.TAG_PROJECT_VISION);
		writeProjectScopeElement(ProjectMetadata.TAG_SCOPE_COMMENTS);
		writeProjectScopeElement(ProjectMetadata.TAG_PROJECT_AREA);
		writeProjectScopeElement(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		writeProjectScopeElement(ProjectMetadata.TAG_HUMAN_POPULATION);
		writeProjectScopeElement(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		writeProjectScopeElement(ProjectMetadata.TAG_SOCIAL_CONTEXT);
		writeCodeListElement(PROJECT_SUMMARY_SCOPE, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, ProtectedAreaCategoryQuestion.class);		
		writeProjectScopeElement(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		writeWcpaElement(WcpaProjectData.TAG_LEGAL_STATUS);
		writeWcpaElement(WcpaProjectData.TAG_LEGISLATIVE);
		writeWcpaElement(WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		writeWcpaElement(WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		writeWcpaElement(WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		writeWcpaElement(WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		writeWcpaElement(WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		writeWcpaElement(WcpaProjectData.TAG_ACCESS_INFORMATION);
		writeWcpaElement(WcpaProjectData.TAG_VISITATION_INFORMATION);
		writeWcpaElement(WcpaProjectData.TAG_CURRENT_LAND_USES);
		writeWcpaElement(WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
		
		getWriter().writeEndElement(PROJECT_SUMMARY_SCOPE);
	}
	
	private void writeProjectSummaryLocationSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_LOCATION);
		
		createGeospatialLocationField();
		writeCodeListElement(PROJECT_SUMMARY_LOCATION, ProjectMetadata.TAG_COUNTRIES, getMetadata(), ProjectMetadata.TAG_COUNTRIES, CountriesQuestion.class);
		writeProjectLocationElement(ProjectMetadata.TAG_STATE_AND_PROVINCES);
		writeProjectLocationElement(ProjectMetadata.TAG_MUNICIPALITIES);
		writeProjectLocationElement(ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		writeProjectLocationElement(ProjectMetadata.TAG_LOCATION_DETAIL);
		writeProjectLocationElement(ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		writeProjectLocationElement(ProjectMetadata.TAG_LOCATION_COMMENTS);
			
		getWriter().writeEndElement(PROJECT_SUMMARY_LOCATION);
	}
	
	private void createGeospatialLocationField() throws Exception
	{
		String latitude = getMetadata().getLatitude();
		String longitude = getMetadata().getLongitude();
		if(latitude.length()==0 && longitude.length()==0)
			return;

		final String elementName = getWriter().appendChildNameToParentName(PROJECT_SUMMARY_LOCATION, PROJECT_LOCATION);
		getWriter().writeStartElement(elementName);
		
		getWriter().writeStartElement(GEOSPATIAL_LOCATION);
		getWriter().writeElement(LATITUDE, latitude);
		getWriter().writeElement(LONGITUDE, longitude);
		getWriter().writeEndElement(GEOSPATIAL_LOCATION);
		
		getWriter().writeEndElement(elementName);
	}
	
	private void writeProjectSummaryPlanningSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_PLANNING);

		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_START_DATE);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_EXPECTED_END_DATE);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_WORKPLAN_START_DATE);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_WORKPLAN_END_DATE);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_FISCAL_YEAR_START);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_PLANNING_COMMENTS);
		
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_CURRENCY_TYPE);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_CURRENCY_SYMBOL);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_WORK_PLAN_DIAGRAM_DATA_INCLUSION);
		writeProjectSummaryPlanningElement(ProjectMetadata.TAG_WORK_UNIT_RATE_DESCRIPTION);

		ChoiceQuestion quarterColumnsVisibilityQuestion = StaticQuestionManager.getQuestion(QuarterColumnsVisibilityQuestion.class);
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY_PLANNING, ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, quarterColumnsVisibilityQuestion, getMetadata().getData(ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY));

		ChoiceQuestion dayColumnsVisibilityQuestion = StaticQuestionManager.getQuestion(DayColumnsVisibilityQuestion.class);
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY_PLANNING, ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY, dayColumnsVisibilityQuestion, getMetadata().getData(ProjectMetadata.TAG_DAY_COLUMNS_VISIBILITY));

		getWriter().writeEndElement(PROJECT_SUMMARY_PLANNING);
	}
	
	private void writeTncElement() throws Exception
	{
		getWriter().writeStartElement(TNC_PROJECT_DATA);
		
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		writeElement(TNC_PROJECT_DATA, getTncProjectData(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		exportTncOperatingUnits();
		writeCodeListElement(TNC_PROJECT_DATA, TNC_TERRESTRIAL_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, TncTerrestrialEcoRegionQuestion.class);
		writeCodeListElement(TNC_PROJECT_DATA, TNC_MARINE_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, TncMarineEcoRegionQuestion.class);
		writeCodeListElement(TNC_PROJECT_DATA, TNC_FRESHWATER_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, TncFreshwaterEcoRegionQuestion.class);
		
		writeTncElement(TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		writeTncElement(TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		writeTncElement(TncProjectData.TAG_PROJECT_CITATIONS);
		writeTncElement(TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
		
		writeTncElement(TncProjectData.TAG_MAKING_THE_CASE);
		writeTncElement(TncProjectData.TAG_RISKS);
		writeTncElement(TncProjectData.TAG_CAPACITY_AND_FUNDING);
		writeTncElement(TncProjectData.TAG_FUNDRAISING_PLAN);
		writeChoiceData(TNC_PROJECT_DATA, TncProjectData.TAG_PROJECT_FOCUS, getTncProjectData(), TncProjectData.TAG_PROJECT_FOCUS);
		writeChoiceData(TNC_PROJECT_DATA, TncProjectData.TAG_PROJECT_SCALE, getTncProjectData(), TncProjectData.TAG_PROJECT_SCALE);

		getWriter().writeEndElement(TNC_PROJECT_DATA);
	}
	
	private void writeWwfElement() throws Exception
	{
		getWriter().writeStartElement(WWF_PROJECT_DATA);
		
		writeWwfCodeListElement(WwfProjectData.TAG_MANAGING_OFFICES, WwfManagingOfficesQuestion.class);
		writeWwfCodeListElement(WwfProjectData.TAG_REGIONS, WwfRegionsQuestion.class);
		writeWwfCodeListElement(WwfProjectData.TAG_ECOREGIONS, WwfEcoRegionsQuestion.class);
		
		getWriter().writeEndElement(WWF_PROJECT_DATA);
	}

	private void writeWwfCodeListElement(String tag, Class questionClassToUse) throws Exception
	{
		writeCodeListElement(WWF_PROJECT_DATA, tag, getWwfProjectData(), tag, questionClassToUse);
	}
	
	private void writeWcsElement() throws Exception
	{
		getWriter().writeStartElement(WCS_PROJECT_DATA);

		writeWcsProjectDataField(WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		writeWcsProjectDataField(WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
		writeWcsProjectDataField(WcsProjectData.TAG_SWOT_COMPLETED);
		writeWcsProjectDataField(WcsProjectData.TAG_SWOT_URL);
		writeWcsProjectDataField(WcsProjectData.TAG_STEP_COMPLETED);
		writeWcsProjectDataField(WcsProjectData.TAG_STEP_URL);
		
		getWriter().writeEndElement(WCS_PROJECT_DATA);
	}
	
	private void writeRareElement() throws Exception
	{
		String rareParentElementName = RARE_PROJECT_DATA;
		getWriter().writeStartElement(rareParentElementName);
		 
		writeRareElement(RareProjectData.TAG_COHORT);
		writeRareElement(RareProjectData.LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		writeRareElement(RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		writeRareElement(RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		writeRareElement(RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		writeRareElement(RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		writeRareElement(RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		writeRareElement(RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		writeRareElement(RareProjectData.TAG_CAMPAIGN_SLOGAN);
		writeRareElement(RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		writeRareElement(RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
		
		getWriter().writeEndElement(rareParentElementName);
	}
	
	private void writeFosElement() throws Exception
	{
		getWriter().writeStartElement(FOS_PROJECT_DATA);
		
		getWriter().writeNonOptionalCodeElement(FOS_PROJECT_DATA, FosProjectData.TAG_TRAINING_TYPE, new FosTrainingTypeQuestion(), getFosProjectData().getData(FosProjectData.TAG_TRAINING_TYPE));
		writeFosElement(FosProjectData.TAG_TRAINING_DATES);
		writeFosElement(FosProjectData.TAG_TRAINERS);
		writeFosElement(FosProjectData.TAG_COACHES);
		
		getWriter().writeEndElement(FOS_PROJECT_DATA);
	}
	
	private void writeMiradiShareProjectDataElement() throws Exception
	{
		SingletonBaseObjectExporter exporter = new SingletonBaseObjectExporter(getWriter(), MiradiShareProjectDataSchema.getObjectType());
		exporter.writeBaseObjectDataSchemaElement(getMiradiShareProjectData());
	}
	
	public void exportTncOperatingUnits() throws Exception
	{
		CodeList codes = getOperatingUnitsWithoutLegacyCode(getMetadata());
		getWriter().writeCodeList(TNC_PROJECT_DATA + TNC_OPERATING_UNITS, StaticQuestionManager.getQuestion(TncOperatingUnitsQuestion.class), codes);
	}
	
	public static CodeList getOperatingUnitsWithoutLegacyCode(ProjectMetadata projectMetadata) throws Exception
	{
		CodeList codes = projectMetadata.getCodeList(ProjectMetadata.TAG_TNC_OPERATING_UNITS);
		final String LEGACY_CODE_TO_BE_REMOVED = "PACIF";
		if (codes.contains(LEGACY_CODE_TO_BE_REMOVED))
		{
			codes.removeCode(LEGACY_CODE_TO_BE_REMOVED);
			if (!codes.contains(TncOperatingUnitsQuestion.TNC_SUPERSEDED_OU_CODE))
				codes.add(TncOperatingUnitsQuestion.TNC_SUPERSEDED_OU_CODE);
		}
		
		return codes;
	}
	
	private void writeElement(String parentElementName, BaseObject object, String tag) throws Exception
	{
		String convertedElementName = getWriter().appendChildNameToParentName(parentElementName, tag);
		getWriter().writeElement(convertedElementName, object.getData(tag));
	}
	
	private void writeCodeListElement(String parentElementName, String poolElementName, BaseObject object, String tag, Class questionClassToUse) throws Exception
	{
		CodeList codes = object.getCodeList(tag);
		getWriter().writeCodeList(parentElementName + poolElementName, StaticQuestionManager.getQuestion(questionClassToUse), codes);
	}
	
	private void writeChoiceData(String parentElementName, String poolElementName, BaseObject baseObject, String tag) throws Exception
	{
		getWriter().writeChoiceData(parentElementName + poolElementName, baseObject, tag);
	}
	
	private void writeWcpaElement(final String tag) throws Exception
	{
		writeElement(PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), tag);
	}
	
	private void writeTncElement(final String tag) throws Exception
	{
		writeElement(TNC_PROJECT_DATA, getTncProjectData(), tag);
	}
	
	private void writeFosElement(final String tag) throws Exception
	{
		writeElement(FOS_PROJECT_DATA, getFosProjectData(), tag);
	}
	
	private void writeRareElement(final String tag) throws Exception
	{
		writeElement(RARE_PROJECT_DATA, getRareProjectData(), tag);
	}
	
	private void writeWcsProjectDataField(final String tag) throws Exception
	{
		writeElement(WCS_PROJECT_DATA, getWcsProjectData(), tag);
	}
	
	private void writeProjectSummaryPlanningElement(final String tag) throws Exception
	{
		writeProjectMetadataElement(PROJECT_SUMMARY_PLANNING, tag);
	}
	
	private void writeProjectSummaryElement(final String tag) throws Exception
	{
		writeProjectMetadataElement(PROJECT_SUMMARY, tag);
	}
	
	private void writeProjectScopeElement(final String tag) throws Exception
	{
		writeProjectMetadataElement(PROJECT_SUMMARY_SCOPE, tag);
	}
	
	private void writeProjectLocationElement(final String tag) throws Exception
	{
		writeProjectMetadataElement(PROJECT_SUMMARY_LOCATION, tag);
	}
	
	private void writeProjectMetadataElement(final String elementName, final String tag) throws Exception
	{
		getWriter().writeElement(elementName, getMetadata(), tag);
	}
	
	private Xmpz2XmlWriter getWriter()
	{
		return writer;
	}
	
	private Project getProject()
	{
		return getWriter().getProject();
	}

	private Xmpz2XmlWriter writer;
}
