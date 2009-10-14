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

import org.martus.util.UnicodeWriter;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Organization;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.WcpaProjectData;
import org.miradi.project.Project;
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
		
		getWriter().writeln("<?xml version='1.0' encoding='UTF-8' ?>");
		writeStartElementWithAttribute(getWriter(), CONSERVATION_PROJECT, XMLNS, NAME_SPACE);
		
		writeProjectSummaryElement();
		writeProjectResourceObjectSchemaElement();
		writeOrganizationObjectSchemaElement();
		writeProjectSummaryScopeSchemaElement();
		writeProjectSummaryLocationSchemaElement();
		
//FIXME urgent - wcs - uncomment and make it validate		
//		writeProjectSummaryPlanningSchemaElement();
//
//		writeTncProjectDataSchemaElement();
//		writeWwfProjectDataSchemaElement();
//		writeWcsDataSchemaElement();
//		writeRareProjectDataSchemaElement();
//		writeFosProjectDataSchemaElement();
//		
//		writeConceptualModelSchemaElement();
//		writeResultsChainSchemaElement();
//		writeDiagramFactorSchemaElement();
//		writeDiagramLinkSchemaElement();
//				
//		writeBiodiversityTargetObjectSchemaElement();
//		writeHumanWelfareTargetSchemaElement();
//		writeCauseObjectSchemaElement();
//		writeStrategyObjectSchemaElement();
//		writeThreatReductionResultsObjectSchemaElement();
//		writeIntermediateResultObjectSchemaElement();
//		writeGroupBoxObjectSchemaElement();
//		writeTextBoxObjectSchemaElement();
//		writeScopeBoxObjectSchemaElement();
//		writeKeyEcologicalAttributeObjectSchemaElement();
//		writeStressObjectSchemaElement();
//		writeSubTargetObjectSchemaElement();
//		writeGoalObjectSchemaElement();
//		writeObjectiveSchemaElement();
//		writeIndicatorObjectSchemaElement();
//		writeActivityObjectSchemaElement();
//		writeMethodObjectSchemaElement();
//		writeTaskObjectSchemaElement();
//		writeResourceAssignmentObjectSchemaElement();
//		writeExpenseAssignmentObjectSchemaElement();
//		writeProgressReportObjectSchemaElement();
//		writeProgressPercentObjectSchemaElement();
//		writeThreatTargetThreatRatingElement();
//		writeSimpleThreatRatingSchemaElement();
//		writeStressBasedThreatRatingElement();
//		writeMeasurementObjectSchemaElement();
//		writeAccountingCodeObjectSchemaElement();
//		writeFundingSourceObjectSchemaElement();
		
		writeEndElement(out, CONSERVATION_PROJECT);
	}
//
//	private void writeFundingSourceObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, FUNDING_SOURCE);
//		writeEndElement(out, FUNDING_SOURCE);
//	}
//
//	private void writeAccountingCodeObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, ACCOUNTING_CODE);
//		writeEndElement(out, ACCOUNTING_CODE);
//	}
//
//	private void writeMeasurementObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, MEASUREMENT);
//		writeEndElement(out, MEASUREMENT);
//	}
//
//	private void writeStressBasedThreatRatingElement() throws Exception
//	{
//		writeStartElement(out, STRESS_BASED_THREAT_RATING);
//		writeEndElement(out, STRESS_BASED_THREAT_RATING);
//	}
//
//	private void writeSimpleThreatRatingSchemaElement() throws Exception
//	{
//		writeStartElement(out, SIMPLE_BASED_THREAT_RATING);
//		writeEndElement(out, SIMPLE_BASED_THREAT_RATING);
//	}
//
//	private void writeThreatTargetThreatRatingElement() throws Exception
//	{
//		writeStartElement(out, THREAT_RATING);
//		writeEndElement(out, THREAT_RATING);
//	}
//
//	private void writeProgressPercentObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, PROGRESS_PERCENT);
//		writeEndElement(out, PROGRESS_PERCENT);
//	}
//
//	private void writeProgressReportObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, PROGRESS_REPORT);
//		writeEndElement(out, PROGRESS_REPORT);
//	}
//
//	private void writeExpenseAssignmentObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, EXPENSE_ASSIGNMENT);
//		writeEndElement(out, EXPENSE_ASSIGNMENT);
//	}
//
//	private void writeResourceAssignmentObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, RESOURCE_ASSIGNMENT);
//		writeEndElement(out, RESOURCE_ASSIGNMENT);
//	}
//
//	private void writeTaskObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, TASK);
//		writeEndElement(out, TASK);
//	}
//
//	private void writeMethodObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, METHOD);
//		writeEndElement(out, METHOD);
//	}
//
//	private void writeActivityObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, ACTIVITY);
//		writeEndElement(out, ACTIVITY);
//	}
//
//	private void writeObjectiveSchemaElement() throws Exception
//	{
//		writeStartElement(out, OBJECTIVE);
//		writeEndElement(out, OBJECTIVE);
//	}
//
//	private void writeIndicatorObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, INDICATOR);
//		writeEndElement(out, INDICATOR);
//	}
//
//	private void writeGoalObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, GOAL);
//		writeEndElement(out, GOAL);
//	}
//
//	private void writeSubTargetObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, SUB_TARGET);
//		writeEndElement(out, SUB_TARGET);
//	}
//
//	private void writeStressObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, STRESS);
//		writeEndElement(out, STRESS);
//	}
//
//	private void writeKeyEcologicalAttributeObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, KEY_ECOLOGICAL_ATTRIBUTE);
//		writeEndElement(out, KEY_ECOLOGICAL_ATTRIBUTE);
//	}
//
//	private void writeScopeBoxObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, SCOPE_BOX);
//		writeEndElement(out, SCOPE_BOX);
//	}
//
//	private void writeTextBoxObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, TEXT_BOX);
//		writeEndElement(out, TEXT_BOX);
//	}
//
//	private void writeGroupBoxObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, GROUP_BOX);
//		writeEndElement(out, GROUP_BOX);
//	}
//
//	private void writeIntermediateResultObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, INTERMEDIATE_RESULTS);
//		writeEndElement(out, INTERMEDIATE_RESULTS);
//	}
//
//	private void writeThreatReductionResultsObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, THREAT_REDUCTION_RESULTS);
//		writeEndElement(out, THREAT_REDUCTION_RESULTS);
//	}
//
//	private void writeStrategyObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, STRATEGY);
//		writeEndElement(out, STRATEGY);
//	}
//
//	private void writeCauseObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, CAUSE);
//		writeEndElement(out, CAUSE);
//	}
//
//	private void writeHumanWelfareTargetSchemaElement() throws Exception
//	{
//		writeStartElement(out, HUMAN_WELFARE_TARGET);
//		writeEndElement(out, HUMAN_WELFARE_TARGET);
//	}
//
//	private void writeBiodiversityTargetObjectSchemaElement() throws Exception
//	{
//		writeStartElement(out, BIODIVERSITY_TARGET);
//		writeEndElement(out, BIODIVERSITY_TARGET);
//	}
//
//	private void writeDiagramLinkSchemaElement() throws Exception
//	{
//		writeStartElement(out, DIAGRAM_LINK);
//		writeEndElement(out, DIAGRAM_LINK);
//	}
//
//	private void writeDiagramFactorSchemaElement() throws Exception
//	{
//		writeStartElement(out, DIAGRAM_FACTOR);
//		writeEndElement(out, DIAGRAM_FACTOR);
//	}
//
//	private void writeResultsChainSchemaElement() throws Exception
//	{
//		writeStartElement(out, RESULTS_CHAIN);
//		writeEndElement(out, RESULTS_CHAIN);
//	}
//
//	private void writeConceptualModelSchemaElement() throws Exception
//	{
//		writeStartElement(out, CONCEPTUAL_MODEL);
//		writeEndElement(out, CONCEPTUAL_MODEL);
//	}
//
//	private void writeFosProjectDataSchemaElement() throws Exception
//	{
//		writeStartElement(out, FOS_PROJECT_DATA);
//		writeEndElement(out, FOS_PROJECT_DATA);
//	}
//
//	private void writeRareProjectDataSchemaElement() throws Exception
//	{
//		writeStartElement(out, RARE_PROJECT_DATA);
//		writeEndElement(out, RARE_PROJECT_DATA);
//	}
//
//	private void writeWcsDataSchemaElement() throws Exception
//	{
//		writeStartElement(out, WCS_PROJECT_DATA);
//		writeEndElement(out, WCS_PROJECT_DATA);
//	}
//
//	private void writeWwfProjectDataSchemaElement() throws Exception
//	{
//		writeStartElement(out, WWF_PROJECT_DATA);
//		writeEndElement(out, WWF_PROJECT_DATA);
//	}
//
//	private void writeTncProjectDataSchemaElement() throws Exception
//	{
//		writeStartElement(out, TNC_PROJECT_DATA);
//		writeEndElement(out, TNC_PROJECT_DATA);
//	}
//
//	private void writeProjectSummaryPlanningSchemaElement() throws Exception
//	{
//		writeStartElement(out, PROJECT_SUMMARY_PLANNING);
//		writeEndElement(out, PROJECT_SUMMARY_PLANNING);
//	}
//
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
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_RED_LIST_SPECIES);
		writeOptionalElementWithSameTag(WcsXmlConstants.PROJECT_SUMMARY_SCOPE, getMetadata(), ProjectMetadata.TAG_OTHER_NOTABLE_SPECIES);
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

	private void writeOrganizationObjectSchemaElement() throws Exception
	{
		writeStartContainerElement(ORGANIZATION);
		ORefList organizationRefs = getProject().getPool(Organization.getObjectType()).getSortedRefList();
		for (int index = 0; index < organizationRefs.size(); ++index)
		{
			Organization organization = Organization.find(getProject(), organizationRefs.get(index));
			writeStartElementWithAttribute(getWriter(), ORGANIZATION, ID, organization.getId().toString());			
			writeOptionalElementWithSameTag(ORGANIZATION, organization, XmlSchemaCreator.LABEL_ELEMENT_NAME);					
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_SHORT_LABEL);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_ROLES_DESCRIPTION);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_CONTACT_FIRST_NAME);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_CONTACT_LAST_NAME);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_EMAIL);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_PHONE_NUMBER);
			writeOptionalElementWithSameTag(ORGANIZATION, organization, Organization.TAG_COMMENTS);
	
			writeEndElement(out, ORGANIZATION);
		}
		
		writeEndContainerElement(ORGANIZATION);
	}

	private void writeProjectResourceObjectSchemaElement() throws Exception
	{
		writeStartContainerElement(PROJECT_RESOURCE);
		ProjectResource[] resources = getProject().getResourcePool().getAllProjectResources();
		for (int index = 0; index < resources.length; ++index)
		{
			ProjectResource resource = resources[index];
			writeStartElementWithAttribute(getWriter(), PROJECT_RESOURCE, ID, resource.getId().toString());			
			writeElementWithSameTag(PROJECT_RESOURCE, resource, XmlSchemaCreator.LABEL_ELEMENT_NAME);
			writeCodeElement(PROJECT_RESOURCE, XmlSchemaCreator.RESOURCE_TYPE_ELEMENT_NAME, resource.getProjectTypeCode());
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_GIVEN_NAME);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_SUR_NAME);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_INITIALS);
			writeCodeListElement(PROJECT_RESOURCE, XmlSchemaCreator.RESOURCE_ROLE_CODES_ELEMENT_NAME, resource.getRoleCodes());
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_ORGANIZATION);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_POSITION);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_LOCATION);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_PHONE_NUMBER);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_PHONE_NUMBER_MOBILE);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_PHONE_NUMBER_HOME);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_PHONE_NUMBER_OTHER);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_EMAIL);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_ALTERNATIVE_EMAIL);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_IM_ADDRESS);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_IM_SERVICE);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_DATE_UPDATED);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_COST_PER_UNIT);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_COMMENTS);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_CUSTOM_FIELD_1);
			writeOptionalElementWithSameTag(PROJECT_RESOURCE, resource, ProjectResource.TAG_CUSTOM_FIELD_2);
			writeEndElement(out, PROJECT_RESOURCE);
		}
		
		writeEndContainerElement(PROJECT_RESOURCE);
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

		writeEndElement(out, PROJECT_SUMMARY);
	}	
	
	private void writeOptionalElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(parentElementName, tag, object, tag);
	}
	
	private void writeElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeElement(parentElementName, tag, object, tag);
	}
	
	private void writeElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, object, tag);
	}
	
	private void writeOptionalElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(getWriter(), parentElementName + elementName, object, tag);
	}
	
	
	private void writeStartContainerElement(String startElementName) throws Exception
	{
		writeStartElement(out, createContainerElementName(startElementName));
	}

	private void writeEndContainerElement(String endElementName) throws Exception
	{
		writeEndElement(out, createContainerElementName(endElementName));
	}
	
	private String createContainerElementName(String startElementName)
	{
		return startElementName + WcsXmlConstants.CONTAINER_ELEMENT_TAG;
	}
		
	private void writeCodeListElement(String parentElementName, String containerElementName, BaseObject object, String tag) throws Exception
	{
		writeCodeListElement(parentElementName, containerElementName, object.getCodeList(tag));
	}
	
	private void writeCodeListElement(String parentElementName, String containerElementName, CodeList codes) throws Exception
	{
		writeStartContainerElement(parentElementName + containerElementName);
		for (int index = 0; index < codes.size(); ++index)
		{
			writeElement(getWriter(), XmlSchemaCreator.CODE_ELEMENT_NAME, codes.get(index));
		}
		
		writeEndContainerElement(parentElementName + containerElementName);
	}
	
	private void writeCodeElement(String parentElementName, String elementName, String code) throws Exception
	{
		writeStartElement(getWriter(), parentElementName + elementName);
		writeXmlEncodedData(getWriter(), code);
		writeEndElement(getWriter(), parentElementName + elementName);
	}
	
	private String createParentAndChildElementName(String parentElementName, String childElementName)
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
	
	private UnicodeWriter getWriter()
	{
		return out;
	}

	private UnicodeWriter out;
}
