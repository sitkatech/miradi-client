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

package org.miradi.xml.xmpz2.objectExporters;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.Target;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.project.Project;
import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.FosTrainingTypeQuestion;
import org.miradi.questions.QuarterColumnsVisibilityQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.FosProjectDataSchema;
import org.miradi.schemas.RareProjectDataSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.schemas.WcsProjectDataSchema;
import org.miradi.schemas.WwfProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.miradi.xml.xmpz2.Xmpz2XmlWriter;

public class ProjectMetadataExporter implements XmpzXmlConstants
{
	public ProjectMetadataExporter(Xmpz2XmlWriter writerToUse)
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
		writeProjectSummaryElement(ProjectMetadata.TAG_PROJECT_STATUS);
		writeProjectSummaryElement(ProjectMetadata.TAG_NEXT_STEPS);
		writeOverallProjectThreatRating();
		writeOverallProjectViabilityRating();
		writeExternalAppIds();
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion(), getMetadata().getThreatRatingMode());

		ChoiceQuestion quarterColumnsVisibilityQuestion = getProject().getQuestion(QuarterColumnsVisibilityQuestion.class);
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, quarterColumnsVisibilityQuestion, getMetadata().getData(ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY));

		ChoiceQuestion budgetTimePeriodQuestion = getProject().getQuestion(BudgetTimePeriodQuestion.class);
		getWriter().writeNonOptionalCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, budgetTimePeriodQuestion, getMetadata().getData(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT));

		getWriter().writeEndElement(PROJECT_SUMMARY);
	}
	
	private void writeShareOutsideOrganizationElement() throws Exception
	{
		getWriter().writeBooleanElement(PROJECT_SUMMARY + XmlSchemaCreator.PROJECT_SHARE_OUTSIDE_ORGANIZATION, getTncProjectData().canShareOutsideOfTnc());
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
		String code = Target.computeTNCViability(getProject());
		ChoiceItem choiceItem = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(code);
		
		getWriter().writeElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING, choiceItem.getCode());
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
	
	private void writeProjectSummaryScopeSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_SCOPE);
		
		writeProjetScopeElement(ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		writeProjetScopeElement(ProjectMetadata.TAG_PROJECT_SCOPE);
		writeProjetScopeElement(ProjectMetadata.TAG_PROJECT_VISION);
		writeProjetScopeElement(ProjectMetadata.TAG_SCOPE_COMMENTS);		
		writeProjetScopeElement(ProjectMetadata.TAG_PROJECT_AREA);
		writeProjetScopeElement(ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		writeProjetScopeElement(ProjectMetadata.TAG_HUMAN_POPULATION);
		writeProjetScopeElement(ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		writeProjetScopeElement(ProjectMetadata.TAG_SOCIAL_CONTEXT);
		writeCodeListElement(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES);		
		writeProjetScopeElement(ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
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
		writeCodeListElement(XmpzXmlConstants.PROJECT_SUMMARY_LOCATION, ProjectMetadata.TAG_COUNTRIES, getMetadata(), ProjectMetadata.TAG_COUNTRIES);
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
		
		getWriter().writeEndElement(PROJECT_SUMMARY_PLANNING);
	}
	
	private void writeTncElement() throws Exception
	{
		getWriter().writeStartElement(TNC_PROJECT_DATA);
		
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_PROJECT_PLACE_TYPES, getTncProjectData(), TncProjectData.TAG_PROJECT_PLACE_TYPES);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_ORGANIZATIONAL_PRIORITIES, getTncProjectData(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES);
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENTS);
		writeElement(TNC_PROJECT_DATA, getTncProjectData(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		exportTncOperatingUnits();
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_TERRESTRIAL_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_MARINE_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_FRESHWATER_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION);
		writeProjectMetadataElement(TNC_PROJECT_DATA, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		
		writeTncElement(TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD);
		writeTncElement(TncProjectData.TAG_PROJECT_LEVEL_COMMENTS);
		writeTncElement(TncProjectData.TAG_PROJECT_CITATIONS);
		writeTncElement(TncProjectData.TAG_CAP_STANDARDS_SCORECARD);
		
		getWriter().writeEndElement(TNC_PROJECT_DATA);
	}
	
	private void writeWwfElement() throws Exception
	{
		getWriter().writeStartElement(WWF_PROJECT_DATA);
		
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_MANAGING_OFFICES, getWwfProjectData(), WwfProjectData.TAG_MANAGING_OFFICES);
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_REGIONS, getWwfProjectData(), WwfProjectData.TAG_REGIONS);
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_ECOREGIONS, getWwfProjectData(), WwfProjectData.TAG_ECOREGIONS);
		
		getWriter().writeEndElement(WWF_PROJECT_DATA);
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
	
	public void exportTncOperatingUnits() throws Exception
	{
		CodeList codes = getOperatingUnitsWithoutLegacyCode(getMetadata());
		getWriter().writeCodeList(TNC_PROJECT_DATA + XmlSchemaCreator.TNC_OPERATING_UNITS, codes);
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
	
	private void writeCodeListElement(String parentElementName, String poolElementName, BaseObject object, String tag) throws Exception
	{
		CodeList codes = object.getCodeList(tag);
		getWriter().writeCodeList(parentElementName + poolElementName, codes);
	}
	
	private void writeWcpaElement(final String tag) throws Exception
	{
		writeElement(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), tag);
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
	
	private void writeProjetScopeElement(final String tag) throws Exception
	{
		writeProjectMetadataElement(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, tag);
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
