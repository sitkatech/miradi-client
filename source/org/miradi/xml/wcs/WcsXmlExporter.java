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

import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.FosTrainingTypeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.xml.XmlExporter;
import org.miradi.xml.generic.XmlSchemaCreator;

public class WcsXmlExporter extends XmlExporter implements WcsXmlConstants
{
	public WcsXmlExporter(Project projectToUse)
	{
		super(projectToUse);
	}

	@Override
	public void exportProject(UnicodeWriter outToUse) throws Exception
	{
		setWriter(outToUse);
		
		getWriter().writeln("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>");
		writeStartElementWithAttribute(getWriter(), CONSERVATION_PROJECT, XMLNS, NAME_SPACE);
		
		writeProjectSummaryElement();
		new ProjectResourcePoolExporter(this).exportXml();
		new OrganizationPoolExporter(this).exportXml();
		writeProjectSummaryScopeSchemaElement();
		writeProjectSummaryLocationSchemaElement();
		writeProjectSummaryPlanningSchemaElement();
		
		writeTncProjectDataSchemaElement();
		writeWwfProjectDataSchemaElement();
		writeWcsDataSchemaElement();
		writeRareProjectDataSchemaElement();
		writeFosProjectDataSchemaElement();
		
		new ConceptualModelPoolExporter(this).exportXml();
		new ResultsChainPoolExporter(this).exportXml();
		new DiagramFactorPoolExporter(this).exportXml();
		new DiagramLinkPoolExporter(this).exportXml();
		new BiodiversityTargetPoolExporter(this).exportXml();
		new HumanWelfareTargetPoolExporter(this).exportXml();
		new CausePoolExporter(this).exportXml();
		new StrategyPoolExporter(this).exportXml();
		new ThreatReductionResultsPoolExporter(this).exportXml();
		new IntermediateResultPoolExporter(this).exportXml();
		new GroupBoxPoolExporter(this).exportXml();
		new TextBoxPoolExporter(this).exportXml();
		new ScopeBoxPoolExporter(this).exportXml();
		new KeyEcologicalAttributePoolExporter(this).exportXml();
		new StressPoolExporter(this).exportXml();
		new SubTargetPoolExporter(this).exportXml();
		new GoalPoolExporter(this).exportXml();
		new ObjectivePoolExporter(this).exportXml();
		new IndicatorPoolExporter(this).exportXml();
		new ActivityPoolExporter(this).exportXml();
		new MethodPoolExporter(this).exportXml();
		new TaskPoolExporter(this).exportXml();
		new ProgressReportPoolExporter(this).exportXml();
		new ProgressPercentPoolExporter(this).exportXml();
		new MeasurementPoolExporter(this).exportXml();
		new AccountingCodePoolExporter(this).exportXml();
		new FundingSourcePoolExporter(this).exportXml();
		new ExpenseAssignmentPoolExporter(this).exportXml();
		new ResourceAssignmentPoolExporter(this).exportXml();
		new ThreatTargetThreatRatingElementExporter(this).exportXml();
		new IucnRedListspeciesPoolExporter(this).exportXml();
		new OtherNotableSpeciesPoolExporter(this).exportXml();
		new AudiencePoolExporter(this).exportXml();
		new PlanningViewConfigurationPoolExporter(this).exportXml();
		
		writeEndElement(out, CONSERVATION_PROJECT);
	}
	
	public void writeIds(String parentElementName, String childElementName, String idElementName, ORefList refList) throws Exception
	{
		writeStartElement(getWriter(), createParentAndChildElementName(parentElementName, childElementName));
		for (int index = 0; index < refList.size(); ++index)
		{
			writeElement(getWriter(), idElementName, refList.get(index).getObjectId().toString());
		}
		
		writeEndElement(getWriter(), createParentAndChildElementName(parentElementName, childElementName));
	}

	private void writeFosProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, FOS_PROJECT_DATA);
		
		writeCodeElement(FOS_PROJECT_DATA, FosProjectData.TAG_TRAINING_TYPE, new FosTrainingTypeQuestion(), getFosProjectData().getData(FosProjectData.TAG_TRAINING_TYPE));
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_TRAINING_DATES);
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_TRAINERS);
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_COACHES);
		
		writeEndElement(out, FOS_PROJECT_DATA);
	}

	private void writeRareProjectDataSchemaElement() throws Exception
	{
		String rareParentElementName = RARE_PROJECT_DATA;
		writeStartElement(out, rareParentElementName);
		 
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_COHORT);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.LEGACY_TAG_THREATS_ADDRESSED_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_SLOGAN);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_COURSE_MANAGER_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_REGIONAL_DIRECTOR_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_MANAGER_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_LOCAL_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_BINGO_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_THREAT_REDUCTION_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(rareParentElementName, getRareProjectData(), RareProjectData.TAG_MONITORING_PARTNER_CONTACT_NOTES);
		
		writeEndElement(out, rareParentElementName);
	}

	private void writeWcsDataSchemaElement() throws Exception
	{
		writeStartElement(out, WCS_PROJECT_DATA);

		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_ORGANIZATIONAL_FOCUS);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_ORGANIZATIONAL_LEVEL);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_SWOT_COMPLETED);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_SWOT_URL);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_STEP_COMPLETED);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getWcsProjectData(), WcsProjectData.TAG_STEP_URL);
		
		writeEndElement(out, WCS_PROJECT_DATA);
	}

	private void writeWwfProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, WWF_PROJECT_DATA);
		
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_MANAGING_OFFICES, getWwfProjectData(), WwfProjectData.TAG_MANAGING_OFFICES);
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_REGIONS, getWwfProjectData(), WwfProjectData.TAG_REGIONS);
		writeCodeListElement(WWF_PROJECT_DATA, XmlSchemaCreator.WWF_ECOREGIONS, getWwfProjectData(), WwfProjectData.TAG_ECOREGIONS);
		
		writeEndElement(out, WWF_PROJECT_DATA);
	}

	private void writeTncProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, TNC_PROJECT_DATA);
		
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getMetadata(), ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		writeProjectId();
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getTncProjectData(), TncProjectData.TAG_PROJECT_SHARING_CODE);
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_PROJECT_PLACE_TYPES, getTncProjectData(), TncProjectData.TAG_PROJECT_PLACE_TYPES);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_ORGANIZATIONAL_PRIORITIES, getTncProjectData(), TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES);
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getMetadata(), ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getTncProjectData(), TncProjectData.TAG_CON_PRO_PARENT_CHILD_PROJECT_TEXT);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_OPERATING_UNITS, getMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_TERRESTRIAL_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_MARINE_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_MARINE_ECO_REGION);
		writeCodeListElement(TNC_PROJECT_DATA, XmlSchemaCreator.TNC_FRESHWATER_ECO_REGION, getMetadata(), ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION);
		writeOptionalElementWithSameTag(TNC_PROJECT_DATA, getMetadata(), ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		
		writeEndElement(out, TNC_PROJECT_DATA);
	}
	
	private void writeProjectId() throws Exception
	{
		writeStartElement(getWriter(), createParentAndChildElementName(TNC_PROJECT_DATA, Xenodata.TAG_PROJECT_ID));
		
		writeStartElement(getWriter(), PROJECT_IDS_ELEMENT_NAME);
		String stringRefMapAsString = getProject().getMetadata().getData(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP);
		StringRefMap stringRefMap = new StringRefMap(stringRefMapAsString);
		Set keys = stringRefMap.getKeys();
		for(Object key: keys)
		{
			ORef xenodataRef = stringRefMap.getValue((String) key);
			if (xenodataRef.isInvalid())
			{
				EAM.logWarning("Invalid Xenodata ref found for key: " + key + " while exporting.");
				continue;
			}

			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);

			writeStartElement(getWriter(), Xenodata.TAG_PROJECT_ID);
			writeXmlEncodedData(out, projectId);
			writeEndElement(out, Xenodata.TAG_PROJECT_ID);
		}
		
		writeEndElement(getWriter(), PROJECT_IDS_ELEMENT_NAME);
		writeEndElement(getWriter(), createParentAndChildElementName(TNC_PROJECT_DATA, Xenodata.TAG_PROJECT_ID));
	}

	private void writeProjectSummaryPlanningSchemaElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY_PLANNING);

		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_START_DATE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_EXPECTED_END_DATE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_WORKPLAN_START_DATE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_WORKPLAN_END_DATE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FISCAL_YEAR_START);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_PLANNING_COMMENTS);
		
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_TYPE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_SYMBOL);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		
		writeEndElement(out, PROJECT_SUMMARY_PLANNING);
	}

	private void writeProjectSummaryLocationSchemaElement() throws Exception
	{
		writeStartElement(getWriter(), PROJECT_SUMMARY_LOCATION);
		
		createGeospatialLocationField();
		writeCodeListElement(WcsXmlConstants.PROJECT_SUMMARY_LOCATION, ProjectMetadata.TAG_COUNTRIES, getMetadata(), ProjectMetadata.TAG_COUNTRIES);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_STATE_AND_PROVINCES);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_MUNICIPALITIES);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LOCATION_DETAIL);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		writeOptionalElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LOCATION_COMMENTS);
			
		writeEndElement(getWriter(), PROJECT_SUMMARY_LOCATION);
	}

	private void createGeospatialLocationField() throws Exception
	{
		writeStartElement(getWriter(), createParentAndChildElementName(PROJECT_SUMMARY_LOCATION, PROJECT_LOCATION));
		
		writeStartElement(getWriter(), GEOSPATIAL_LOCATION);
		writeOptionalElement(getWriter(), LATITUDE, getMetadata().getLatitude());
		writeOptionalElement(getWriter(), LONGITUDE, getMetadata().getLongitude());
		writeEndElement(getWriter(), GEOSPATIAL_LOCATION);
		
		writeEndElement(getWriter(), createParentAndChildElementName(PROJECT_SUMMARY_LOCATION, PROJECT_LOCATION));
	}

	private void writeProjectSummaryScopeSchemaElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY_SCOPE);
		
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_SCOPE);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SCOPE_COMMENTS);		
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_AREA);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_HUMAN_POPULATION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SOCIAL_CONTEXT);
		writeCodeListElement(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES);		
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_LEGAL_STATUS);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_LEGISLATIVE);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_ACCESS_INFORMATION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_VISITATION_INFORMATION);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_CURRENT_LAND_USES);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
		
		writeEndElement(out, PROJECT_SUMMARY_SCOPE);
	}

	private void writeProjectSummaryElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY);
		
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_NAME, getMetadata(), ProjectMetadata.TAG_PROJECT_NAME);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, getMetadata(), ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_URL, getMetadata(), ProjectMetadata.TAG_PROJECT_URL);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_DESCRIPTION, getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_STATUS, getMetadata(), ProjectMetadata.TAG_PROJECT_STATUS);
		writeOptionalElement(PROJECT_SUMMARY, ProjectMetadata.TAG_NEXT_STEPS, getMetadata(), ProjectMetadata.TAG_NEXT_STEPS);
		writeOptionalOverallProjectThreatRating();

		writeEndElement(out, PROJECT_SUMMARY);
	}

	private void writeOptionalOverallProjectThreatRating() throws Exception
	{
		int rawOverallProjectThreatRatingCode = getProject().getProjectSummaryThreatRating();
		if (rawOverallProjectThreatRatingCode == 0)
			return;
		
		writeStartElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
		writeXmlEncodedData(getWriter(), Integer.toString(rawOverallProjectThreatRatingCode));
		writeEndElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
	}
	
	public void writeOptionalElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(parentElementName, tag, object, tag);
	}
	
	public void writeElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeElement(parentElementName, tag, object, tag);
	}
	
	public void writeElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, object, tag);
	}
	
	public void writeElement(String parentElementName, String elementName, String data) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, data);
	}
	
	public void writeOptionalElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeOptionalElement(getWriter(), parentElementName + convertedElementName, object, tag);
	}
	
	public void writeStartContainerElement(String startElementName) throws Exception
	{
		writeStartElement(out, createContainerElementName(startElementName));
	}

	void writeEndContainerElement(String endElementName) throws Exception
	{
		writeEndElement(out, createContainerElementName(endElementName));
	}
	
	public void writeStartPoolElement(String startElementName) throws Exception
	{
		writeStartElement(out, createPoolElementName(startElementName));
	}

	void writeEndPoolElement(String endElementName) throws Exception
	{
		writeEndElement(out, createPoolElementName(endElementName));
	}
	
	private String createContainerElementName(String startElementName)
	{
		return startElementName + CONTAINER_ELEMENT_TAG;
	}
	
	private String createPoolElementName(String startElementName)
	{
		return startElementName + POOL_ELEMENT_TAG;
	}
		
	public void writeCodeListElement(String parentElementName, String poolElementName, BaseObject object, String tag) throws Exception
	{
		CodeList codes = object.getCodeList(tag);
		writeStartContainerElement(parentElementName + poolElementName);
		for (int index = 0; index < codes.size(); ++index)
		{
			writeElement(getWriter(), XmlSchemaCreator.CODE_ELEMENT_NAME, codes.get(index));
		}
		
		writeEndContainerElement(parentElementName + poolElementName);
	}
	
	public void writeCodeElement(String parentElementName, String elementName, ChoiceQuestion question, String code) throws Exception
	{
		String convertedElementName = getConvertedElementName(parentElementName, elementName);
		writeStartElement(getWriter(), parentElementName + convertedElementName);
		
		if (doesCodeExist(question, code))
			writeXmlEncodedData(getWriter(), code);
		else
			logMissingCode(question, code);
		
		writeEndElement(getWriter(), parentElementName + convertedElementName);
	}

	public void writeOptionalCodeElement(String parentElementName, String elementName, ChoiceQuestion question, String code) throws Exception
	{
		if (code.length() == 0)
			return;
				
		if (!doesCodeExist(question, code))
		{
			logMissingCode(question, code);
			return;
		}
		
		writeCodeElement(parentElementName, elementName, question, code);
	}
	
	private void logMissingCode(ChoiceQuestion question, String code)
	{
		EAM.logWarning(code + " is a code that does not exist in the question:" + question.getClass().getSimpleName());
	}
	
	private boolean doesCodeExist(ChoiceQuestion question, String code)
	{
		ChoiceItem choiceItem = question.findChoiceByCode(code);
		if (choiceItem == null)
			return false;
		
		return true;
	}

	private String getConvertedElementName(String parentElementName,String elementName)
	{
		TagToElementNameMap map = new TagToElementNameMap();
		return map.findElementName(parentElementName, elementName);
	}
	
	public void writeStartElement(String startElementName) throws Exception
	{
		writeStartElement(getWriter(), startElementName);
	}
	
	void writeEndElement(String endElementName) throws Exception
	{
		writeEndElement(getWriter(), endElementName);
	}
	
	public String createParentAndChildElementName(String parentElementName, String childElementName)
	{
		return parentElementName + childElementName;
	}

	private ProjectMetadata getMetadata()
	{
		return getProject().getMetadata();
	}
	
	private void setWriter(UnicodeWriter outToUse)
	{
		out = outToUse;
	}
	
	public UnicodeWriter getWriter()
	{
		return out;
	}

	private UnicodeWriter out;
}
