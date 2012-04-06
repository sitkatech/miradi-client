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

import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.questions.BudgetTimePeriodQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.QuarterColumnsVisibilityQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.schemas.WcpaProjectDataSchema;
import org.miradi.utils.CodeList;
import org.miradi.xml.generic.XmlSchemaCreator;
import org.miradi.xml.wcs.XmpzXmlConstants;

public class ProjectMetadataExporter implements XmpzXmlConstants
{
	public ProjectMetadataExporter(Xmpz2XmlUnicodeWriter writerToUse)
	{
		writer = writerToUse;
	}
	
	public void writeBaseObjectDataSchemaElement() throws Exception
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

		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_NAME, getMetadata(), ProjectMetadata.TAG_PROJECT_NAME);
		writeShareOutsideOrganizationElement();
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_LANGUAGE, getMetadata(), ProjectMetadata.TAG_PROJECT_LANGUAGE);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, getMetadata(), ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_URL, getMetadata(), ProjectMetadata.TAG_PROJECT_URL);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_DESCRIPTION, getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_STATUS, getMetadata(), ProjectMetadata.TAG_PROJECT_STATUS);
		getWriter().writeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_NEXT_STEPS, getMetadata(), ProjectMetadata.TAG_NEXT_STEPS);
		writeOverallProjectThreatRating();
		writeOverallProjectViabilityRating();
		writeExternalAppIds();
		getWriter().writeCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_THREAT_RATING_MODE, new ThreatRatingModeChoiceQuestion(), getMetadata().getThreatRatingMode());

		ChoiceQuestion quarterColumnsVisibilityQuestion = getProject().getQuestion(QuarterColumnsVisibilityQuestion.class);
		getWriter().writeCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY, quarterColumnsVisibilityQuestion, getMetadata().getData(ProjectMetadata.TAG_QUARTER_COLUMNS_VISIBILITY));

		ChoiceQuestion budgetTimePeriodQuestion = getProject().getQuestion(BudgetTimePeriodQuestion.class);
		getWriter().writeCodeElement(PROJECT_SUMMARY, ProjectMetadata.TAG_WORKPLAN_TIME_UNIT, budgetTimePeriodQuestion, getMetadata().getData(ProjectMetadata.TAG_WORKPLAN_TIME_UNIT));

		getWriter().writeEndElement(PROJECT_SUMMARY);
	}
	
	private void writeShareOutsideOrganizationElement() throws Exception
	{
		String shareOutSideOfTnc = "0";
		if (getTncProjectData().canShareOutsideOfTnc())
			shareOutSideOfTnc = BooleanData.BOOLEAN_TRUE;
		
		getWriter().writeElement(PROJECT_SUMMARY + XmlSchemaCreator.PROJECT_SHARE_OUTSIDE_ORGANIZATION, shareOutSideOfTnc);
	}
	
	private void writeOverallProjectThreatRating() throws Exception
	{
		int rawOverallProjectThreatRatingCode = getProject().getProjectSummaryThreatRating();
		if (rawOverallProjectThreatRatingCode == 0)
			return;
		
		getWriter().writeStartElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
		getWriter().writeXmlText(Integer.toString(rawOverallProjectThreatRatingCode));
		getWriter().writeEndElement(PROJECT_SUMMARY + OVERALL_PROJECT_THREAT_RATING);
	}
	
	private void writeOverallProjectViabilityRating() throws Exception
	{
		String code = Target.computeTNCViability(getProject());
		ChoiceItem choiceItem = getProject().getQuestion(StatusQuestion.class).findChoiceByCode(code);
		
		getWriter().writeStartElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING);
		getWriter().writeXmlText(choiceItem.getCode());
		getWriter().writeEndElement(PROJECT_SUMMARY + OVERALL_PROJECT_VIABILITY_RATING);
	}
	
	private void writeExternalAppIds() throws Exception
	{
		getWriter().writeStartElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));

		String stringRefMapAsString = getMetadata().getData(ProjectMetadata.TAG_XENODATA_STRING_REF_MAP);
		StringRefMap stringRefMap = new StringRefMap(stringRefMapAsString);
		Set<String> keys = stringRefMap.getKeys();
		for(String key: keys)
		{
			ORef xenodataRef = stringRefMap.getValue(key);
			if (xenodataRef.isInvalid())
			{
				EAM.logWarning("Invalid Xenodata ref found for key: " + key + " while exporting.");
				continue;
			}

			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
			getWriter().writeStartElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
			getWriter().writeElement(EXTERNAL_APP_ELEMENT_NAME, key);
			getWriter().writeElement(PROJECT_ID, projectId);
			getWriter().writeEndElement(EXTERNAL_PROJECT_ID_ELEMENT_NAME);
		}
		
		getWriter().writeEndElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY, Xenodata.TAG_PROJECT_ID));
	}

	private TncProjectData getTncProjectData()
	{
		final ORef ref = getWriter().getProject().getSingletonObjectRef(TncProjectDataSchema.getObjectType());
		return TncProjectData.find(getWriter().getProject(), ref);
	}
	
	private WcpaProjectData getWcpaProjectData()
	{
		ORef wcpaProjectDataRef = getProject().getSingletonObjectRef(WcpaProjectDataSchema.getObjectType());
		return WcpaProjectData.find(getProject(), wcpaProjectDataRef);
	}
	
	private void writeProjectSummaryScopeSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_SCOPE);
		
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SHORT_PROJECT_SCOPE);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_SCOPE);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_VISION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SCOPE_COMMENTS);		
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_AREA);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROJECT_AREA_NOTES);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_HUMAN_POPULATION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_HUMAN_POPULATION_NOTES);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_SOCIAL_CONTEXT);
		writeCodeListElement(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORIES);		
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_PROTECTED_AREA_CATEGORY_NOTES);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_LEGAL_STATUS);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_LEGISLATIVE);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_PHYSICAL_DESCRIPTION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_BIOLOGICAL_DESCRIPTION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_SOCIO_ECONOMIC_INFORMATION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_HISTORICAL_DESCRIPTION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_CULTURAL_DESCRIPTION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_ACCESS_INFORMATION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_VISITATION_INFORMATION);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_CURRENT_LAND_USES);
		writeElementWithSameTag(XmpzXmlConstants.PROJECT_SUMMARY_SCOPE, getWcpaProjectData(), WcpaProjectData.TAG_MANAGEMENT_RESOURCES);				
		
		getWriter().writeEndElement(PROJECT_SUMMARY_SCOPE);
	}
	
	private void writeProjectSummaryLocationSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_LOCATION);
		
		createGeospatialLocationField();
		writeCodeListElement(XmpzXmlConstants.PROJECT_SUMMARY_LOCATION, ProjectMetadata.TAG_COUNTRIES, getMetadata(), ProjectMetadata.TAG_COUNTRIES);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_STATE_AND_PROVINCES);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_MUNICIPALITIES);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LEGISLATIVE_DISTRICTS);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LOCATION_DETAIL);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_SITE_MAP_REFERENCE);
		writeElementWithSameTag(PROJECT_SUMMARY_LOCATION, getMetadata(), ProjectMetadata.TAG_LOCATION_COMMENTS);
			
		getWriter().writeEndElement(PROJECT_SUMMARY_LOCATION);
	}
	
	private void createGeospatialLocationField() throws Exception
	{
		String latitude = getMetadata().getLatitude();
		String longitude = getMetadata().getLongitude();
		if(latitude.length()==0 && longitude.length()==0)
			return;

		getWriter().writeStartElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY_LOCATION, PROJECT_LOCATION));
		
		getWriter().writeStartElement(GEOSPATIAL_LOCATION);
		getWriter().writeElement(LATITUDE, latitude);
		getWriter().writeElement(LONGITUDE, longitude);
		getWriter().writeEndElement(GEOSPATIAL_LOCATION);
		
		getWriter().writeEndElement(getWriter().appendParentNameToChildName(PROJECT_SUMMARY_LOCATION, PROJECT_LOCATION));
	}
	
	private void writeProjectSummaryPlanningSchemaElement() throws Exception
	{
		getWriter().writeStartElement(PROJECT_SUMMARY_PLANNING);

		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_START_DATE);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_EXPECTED_END_DATE);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_WORKPLAN_START_DATE);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_WORKPLAN_END_DATE);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FISCAL_YEAR_START);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FULL_TIME_EMPLOYEE_DAYS_PER_YEAR);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_PLANNING_COMMENTS);
		
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_TYPE);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_SYMBOL);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_CURRENCY_DECIMAL_PLACES);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_TOTAL_BUDGET_FOR_FUNDING);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_BUDGET_SECURED_PERCENT);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_KEY_FUNDING_SOURCES);
		writeElementWithSameTag(PROJECT_SUMMARY_PLANNING, getMetadata(),ProjectMetadata.TAG_FINANCIAL_COMMENTS);
		
		getWriter().writeEndElement(PROJECT_SUMMARY_PLANNING);
	}
	
	private void writeElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		String convertedElementName = getWriter().appendParentNameToChildName(parentElementName, tag);
		getWriter().writeElement(convertedElementName, object.getData(tag));
	}
	
	private void writeCodeListElement(String parentElementName, String poolElementName, BaseObject object, String tag) throws Exception
	{
		CodeList codes = object.getCodeList(tag);
		if (codes.hasData())
			getWriter().writeCodeListElement(parentElementName + poolElementName, codes);
	}
	
	private Xmpz2XmlUnicodeWriter getWriter()
	{
		return writer;
	}
	
	private Project getProject()
	{
		return getWriter().getProject();
	}

	private Xmpz2XmlUnicodeWriter writer;
}
