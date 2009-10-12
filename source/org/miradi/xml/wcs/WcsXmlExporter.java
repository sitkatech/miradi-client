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
import org.miradi.objects.BaseObject;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;
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
		writeProjectSummaryPlanningSchemaElement();

		writeTncProjectDataSchemaElement();
		writeWwfProjectDataSchemaElement();
		writeWcsDataSchemaElement();
		writeRareProjectDataSchemaElement();
		writeFosProjectDataSchemaElement();
		
		writeConceptualModelSchemaElement();
		writeResultsChainSchemaElement();
		writeDiagramFactorSchemaElement();
		writeDiagramLinkSchemaElement();
				
		writeBiodiversityTargetObjectSchemaElement();
		writeHumanWelfareTargetSchemaElement();
		writeCauseObjectSchemaElement();
		writeStrategyObjectSchemaElement();
		writeThreatReductionResultsObjectSchemaElement();
		writeIntermediateResultObjectSchemaElement();
		writeGroupBoxObjectSchemaElement();
		writeTextBoxObjectSchemaElement();
		writeScopeBoxObjectSchemaElement();
		writeKeyEcologicalAttributeObjectSchemaElement();
		writeStressObjectSchemaElement();
		writeSubTargetObjectSchemaElement();
		writeGoalObjectSchemaElement();
		writeObjectiveSchemaElement();
		writeIndicatorObjectSchemaElement();
		writeActivityObjectSchemaElement();
		writeMethodObjectSchemaElement();
		writeTaskObjectSchemaElement();
		writeResourceAssignmentObjectSchemaElement();
		writeExpenseAssignmentObjectSchemaElement();
		writeProgressReportObjectSchemaElement();
		writeProgressPercentObjectSchemaElement();
		writeThreatTargetThreatRatingElement();
		writeSimpleThreatRatingSchemaElement();
		writeStressBasedThreatRatingElement();
		writeMeasurementObjectSchemaElement();
		writeAccountingCodeObjectSchemaElement();
		writeFundingSourceObjectSchemaElement();
		
		writeEndElement(out, CONSERVATION_PROJECT);
	}

	private void writeFundingSourceObjectSchemaElement() throws Exception
	{
		writeStartElement(out, FUNDING_SOURCE);
		writeEndElement(out, FUNDING_SOURCE);
	}

	private void writeAccountingCodeObjectSchemaElement() throws Exception
	{
		writeStartElement(out, ACCOUNTING_CODE);
		writeEndElement(out, ACCOUNTING_CODE);
	}

	private void writeMeasurementObjectSchemaElement() throws Exception
	{
		writeStartElement(out, MEASUREMENT);
		writeEndElement(out, MEASUREMENT);
	}

	private void writeStressBasedThreatRatingElement() throws Exception
	{
		writeStartElement(out, STRESS_BASED_THREAT_RATING);
		writeEndElement(out, STRESS_BASED_THREAT_RATING);
	}

	private void writeSimpleThreatRatingSchemaElement() throws Exception
	{
		writeStartElement(out, SIMPLE_BASED_THREAT_RATING);
		writeEndElement(out, SIMPLE_BASED_THREAT_RATING);
	}

	private void writeThreatTargetThreatRatingElement() throws Exception
	{
		writeStartElement(out, THREAT_RATING);
		writeEndElement(out, THREAT_RATING);
	}

	private void writeProgressPercentObjectSchemaElement() throws Exception
	{
		writeStartElement(out, PROGRESS_PERCENT);
		writeEndElement(out, PROGRESS_PERCENT);
	}

	private void writeProgressReportObjectSchemaElement() throws Exception
	{
		writeStartElement(out, PROGRESS_REPORT);
		writeEndElement(out, PROGRESS_REPORT);
	}

	private void writeExpenseAssignmentObjectSchemaElement() throws Exception
	{
		writeStartElement(out, EXPENSE_ASSIGNMENT);
		writeEndElement(out, EXPENSE_ASSIGNMENT);
	}

	private void writeResourceAssignmentObjectSchemaElement() throws Exception
	{
		writeStartElement(out, RESOURCE_ASSIGNMENT);
		writeEndElement(out, RESOURCE_ASSIGNMENT);
	}

	private void writeTaskObjectSchemaElement() throws Exception
	{
		writeStartElement(out, TASK);
		writeEndElement(out, TASK);
	}

	private void writeMethodObjectSchemaElement() throws Exception
	{
		writeStartElement(out, METHOD);
		writeEndElement(out, METHOD);
	}

	private void writeActivityObjectSchemaElement() throws Exception
	{
		writeStartElement(out, ACTIVITY);
		writeEndElement(out, ACTIVITY);
	}

	private void writeObjectiveSchemaElement() throws Exception
	{
		writeStartElement(out, OBJECTIVE);
		writeEndElement(out, OBJECTIVE);
	}

	private void writeIndicatorObjectSchemaElement() throws Exception
	{
		writeStartElement(out, INDICATOR);
		writeEndElement(out, INDICATOR);
	}

	private void writeGoalObjectSchemaElement() throws Exception
	{
		writeStartElement(out, GOAL);
		writeEndElement(out, GOAL);
	}

	private void writeSubTargetObjectSchemaElement() throws Exception
	{
		writeStartElement(out, SUB_TARGET);
		writeEndElement(out, SUB_TARGET);
	}

	private void writeStressObjectSchemaElement() throws Exception
	{
		writeStartElement(out, STRESS);
		writeEndElement(out, STRESS);
	}

	private void writeKeyEcologicalAttributeObjectSchemaElement() throws Exception
	{
		writeStartElement(out, KEY_ECOLOGICAL_ATTRIBUTE);
		writeEndElement(out, KEY_ECOLOGICAL_ATTRIBUTE);
	}

	private void writeScopeBoxObjectSchemaElement() throws Exception
	{
		writeStartElement(out, SCOPE_BOX);
		writeEndElement(out, SCOPE_BOX);
	}

	private void writeTextBoxObjectSchemaElement() throws Exception
	{
		writeStartElement(out, TEXT_BOX);
		writeEndElement(out, TEXT_BOX);
	}

	private void writeGroupBoxObjectSchemaElement() throws Exception
	{
		writeStartElement(out, GROUP_BOX);
		writeEndElement(out, GROUP_BOX);
	}

	private void writeIntermediateResultObjectSchemaElement() throws Exception
	{
		writeStartElement(out, INTERMEDIATE_RESULTS);
		writeEndElement(out, INTERMEDIATE_RESULTS);
	}

	private void writeThreatReductionResultsObjectSchemaElement() throws Exception
	{
		writeStartElement(out, THREAT_REDUCTION_RESULTS);
		writeEndElement(out, THREAT_REDUCTION_RESULTS);
	}

	private void writeStrategyObjectSchemaElement() throws Exception
	{
		writeStartElement(out, STRATEGY);
		writeEndElement(out, STRATEGY);
	}

	private void writeCauseObjectSchemaElement() throws Exception
	{
		writeStartElement(out, CAUSE);
		writeEndElement(out, CAUSE);
	}

	private void writeHumanWelfareTargetSchemaElement() throws Exception
	{
		writeStartElement(out, HUMAN_WELFARE_TARGET);
		writeEndElement(out, HUMAN_WELFARE_TARGET);
	}

	private void writeBiodiversityTargetObjectSchemaElement() throws Exception
	{
		writeStartElement(out, BIODIVERSITY_TARGET);
		writeEndElement(out, BIODIVERSITY_TARGET);
	}

	private void writeDiagramLinkSchemaElement() throws Exception
	{
		writeStartElement(out, DIAGRAM_LINK);
		writeEndElement(out, DIAGRAM_LINK);
	}

	private void writeDiagramFactorSchemaElement() throws Exception
	{
		writeStartElement(out, DIAGRAM_FACTOR);
		writeEndElement(out, DIAGRAM_FACTOR);
	}

	private void writeResultsChainSchemaElement() throws Exception
	{
		writeStartElement(out, RESULTS_CHAIN);
		writeEndElement(out, RESULTS_CHAIN);
	}

	private void writeConceptualModelSchemaElement() throws Exception
	{
		writeStartElement(out, CONCEPTUAL_MODEL);
		writeEndElement(out, CONCEPTUAL_MODEL);
	}

	private void writeFosProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, FOS_PROJECT_DATA);
		writeEndElement(out, FOS_PROJECT_DATA);
	}

	private void writeRareProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, RARE_PROJECT_DATA);
		writeEndElement(out, RARE_PROJECT_DATA);
	}

	private void writeWcsDataSchemaElement() throws Exception
	{
		writeStartElement(out, WCS_PROJECT_DATA);
		writeEndElement(out, WCS_PROJECT_DATA);
	}

	private void writeWwfProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, WWF_PROJECT_DATA);
		writeEndElement(out, WWF_PROJECT_DATA);
	}

	private void writeTncProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, TNC_PROJECT_DATA);
		writeEndElement(out, TNC_PROJECT_DATA);
	}

	private void writeProjectSummaryPlanningSchemaElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY_PLANNING);
		writeEndElement(out, PROJECT_SUMMARY_PLANNING);
	}

	private void writeProjectSummaryLocationSchemaElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY_LOCATION);
		writeEndElement(out, PROJECT_SUMMARY_LOCATION);
	}

	private void writeProjectSummaryScopeSchemaElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY_SCOPE);
		writeEndElement(out, PROJECT_SUMMARY_SCOPE);
	}

	private void writeOrganizationObjectSchemaElement() throws Exception
	{
		writeStartElement(out, ORGANIZATION);
		writeEndElement(out, ORGANIZATION);
	}

	private void writeProjectResourceObjectSchemaElement() throws Exception
	{
		writeWcsStartContainerElement(PROJECT_RESOURCE);
		ProjectResource[] resources = getProject().getResourcePool().getAllProjectResources();
		for (int index = 0; index < resources.length; ++index)
		{
			writeStartElementWithAttribute(getWriter(), PROJECT_RESOURCE, ID, resources[index].getId().toString());
			writeWcsElement(PROJECT_RESOURCE, XmlSchemaCreator.LABEL_ELEMENT_NAME, resources[index], XmlSchemaCreator.LABEL_ELEMENT_NAME);
			//FIXME urgent - wcs xml output uncomment and make work
			//		createTextField(BaseObject.TAG_LABEL);		
			//		createCodeListField(XmlSchemaCreator.RESOURCE_TYPE_ELEMENT_NAME);
			//		createTextField(ProjectResource.TAG_GIVEN_NAME);
			//		createTextField(ProjectResource.TAG_SUR_NAME);
			//		createTextField(ProjectResource.TAG_INITIALS);
			//		createCodeListField(XmlSchemaCreator.RESOURCE_ROLE_CODES_ELEMENT_NAME);
			//		createTextField(ProjectResource.TAG_ORGANIZATION);
			//		createTextField(ProjectResource.TAG_POSITION);
			//		createTextField(ProjectResource.TAG_LOCATION);
			//		createTextField(ProjectResource.TAG_PHONE_NUMBER);
			//		createTextField(ProjectResource.TAG_PHONE_NUMBER_MOBILE);
			//		createTextField(ProjectResource.TAG_PHONE_NUMBER_HOME);
			//		createTextField(ProjectResource.TAG_PHONE_NUMBER_OTHER);
			//		createTextField(ProjectResource.TAG_EMAIL);
			//		createTextField(ProjectResource.TAG_ALTERNATIVE_EMAIL);
			//		createTextField(ProjectResource.TAG_IM_ADDRESS);
			//		createTextField(ProjectResource.TAG_IM_SERVICE);
			//		createDateField(ProjectResource.TAG_DATE_UPDATED);
			//		createNumericField(ProjectResource.TAG_COST_PER_UNIT);
			//		createTextField(ProjectResource.TAG_COMMENTS);
			//		createTextField(ProjectResource.TAG_CUSTOM_FIELD_1);
			//		createTextField(ProjectResource.TAG_CUSTOM_FIELD_2);

			writeEndElement(out, PROJECT_RESOURCE);
		}
		
		writeWcsEndContainerElement(PROJECT_RESOURCE);
	}

	private void writeProjectSummaryElement() throws Exception
	{
		writeStartElement(out, PROJECT_SUMMARY);
		
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_NAME, getMetadata(), ProjectMetadata.TAG_PROJECT_NAME);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_DATA_EFFECTIVE_DATE, getMetadata(), ProjectMetadata.TAG_DATA_EFFECTIVE_DATE);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_PROJECT_NUMBER);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS, getMetadata(), ProjectMetadata.TAG_OTHER_ORG_RELATED_PROJECTS);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_URL, getMetadata(), ProjectMetadata.TAG_PROJECT_URL);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_DESCRIPTION, getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_PROJECT_STATUS, getMetadata(), ProjectMetadata.TAG_PROJECT_STATUS);
		writeWcsElement(PROJECT_SUMMARY, ProjectMetadata.TAG_NEXT_STEPS, getMetadata(), ProjectMetadata.TAG_NEXT_STEPS);

		writeEndElement(out, PROJECT_SUMMARY);
	}	
	
	protected void writeWcsElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, object, tag);
	}
	
	protected void writeWcsStartContainerElement(String startElementName) throws Exception
	{
		writeStartElement(out, WcsXmlConstants.CONTAINER_ELEMENT_TAG + startElementName);
	}
	
	protected void writeWcsEndContainerElement(String endElementName) throws Exception
	{
		writeEndElement(out, WcsXmlConstants.CONTAINER_ELEMENT_TAG + endElementName);
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
