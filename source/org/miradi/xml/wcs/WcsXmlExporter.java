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

import java.awt.Point;
import java.util.Set;

import org.martus.util.UnicodeWriter;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FosProjectData;
import org.miradi.objects.Organization;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.RareProjectData;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Target;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.WcpaProjectData;
import org.miradi.objects.WcsProjectData;
import org.miradi.objects.WwfProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;
import org.miradi.utils.PointList;
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
		new ProjectResourcePoolExporter(this).exportObjectPool();
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
		new DiagramFactorPoolExporter(this).exportObjectPool();
		writeDiagramLinkSchemaElement();
		writeBiodiversityTargetObjectSchemaElement();
		new HumanWelfareTargetPoolExporter(this).exportObjectPool();
		new CauseContainerExporter(this).exportObjectPool();
		new StrategyContainerExporter(this).exportObjectPool();
		new ThreatReductionResultsContainerExporter(this).exportObjectPool();
		new IntermediateResultContainerExporter(this).exportObjectPool();
		new GroupBoxContainerExporter(this).exportObjectPool();
		new TextBoxContainerExporter(this).exportObjectPool();
		new ScopeBoxContainerExporter(this).exportObjectPool();
		new KeyEcologicalAttributeContainerExporter(this).exportObjectPool();
		new StressContainerExporter(this).exportObjectPool();
		new SubTargetContainerExporter(this).exportObjectPool();
		new GoalPoolExporter(this).exportObjectPool();
		new ObjectivePoolExporter(this).exportObjectPool();
		new IndicatorContainerExporter(this).exportObjectPool();
		new ActivityPoolExporter(this).exportObjectPool();
		new MethodContainerExporter(this).exportObjectPool();
		new TaskContainerExporter(this).exportObjectPool();
		new ProgressReportContainerExporter(this).exportObjectPool();
		new ProgressPercentPoolExporter(this).exportObjectPool();
		new MeasurementContainerExporter(this).exportObjectPool();
		new AccountingCodePoolExporter(this).exportObjectPool();
		new FundingSourceContainerExporter(this).exportObjectPool();
		new ExpenseAssignmentPoolExporter(this).exportObjectPool();
		new ResourceAssignmentPoolExporter(this).exportObjectPool();
		new ThreatTargetThreatRatingElementExporter(this).exportObjectPool();
		
		writeEndElement(out, CONSERVATION_PROJECT);
	}
	
	private void writeBiodiversityTargetObjectSchemaElement() throws Exception
	{
		new BiodiversityTargetPoolExporter(this).exportObjectPool();
	}

	private void writeDiagramLinkSchemaElement() throws Exception
	{
		writeStartPoolElement(DIAGRAM_LINK);
		ORefList diagramLinkRefs = getProject().getDiagramFactorLinkPool().getSortedRefList();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(getProject(), diagramLinkRefs.get(index));
			writeStartElementWithAttribute(getWriter(), DIAGRAM_LINK, ID, diagramLink.getId().toString());
			writeWrappedFactorLinkId(diagramLink);
			writeFromDiagramFactorId(diagramLink);
			writeToDiagramFactorId(diagramLink);
			writeDiagramLinkBendPoints(diagramLink);
			
			writeIds(DIAGRAM_LINK, GROUP_BOX_DIAGRAM_LINK_CHILDREN_ID, WcsXmlConstants.DIAGRAM_LINK_ID_ELEMENT_NAME, diagramLink.getGroupedDiagramLinkRefs());
			writeCodeElement(DIAGRAM_LINK, DiagramLink.TAG_COLOR, diagramLink.getColorChoiceItem().getCode());
			
			writeEndElement(out, DIAGRAM_LINK);
		}
		
		writeEndPoolElement(DIAGRAM_LINK);
	}
	
	private void writeDiagramLinkBendPoints(DiagramLink diagramLink) throws Exception
	{
		writeStartElement(DIAGRAM_LINK + BEND_POINTS_ELEMENT_NAME);
		PointList bendPoints = diagramLink.getBendPoints();
		for (int index = 0; index < bendPoints.size(); ++index)
		{
			writeDiagramPoint(bendPoints.get(index));
		}
		
		writeEndElement(DIAGRAM_LINK + BEND_POINTS_ELEMENT_NAME);
	}

	private void writeFromDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		writeStartElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
		writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor fromFactor = diagramLink.getFromDiagramFactor().getWrappedFactor();
		String fromFactorTypeName = getFactorTypeName(fromFactor);
		writeElement(fromFactorTypeName, ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		writeEndElement(LINKABLE_FACTOR_ID);
		writeEndElement(DIAGRAM_LINK + FROM_DIAGRAM_FACTOR_ID);
	}
	
	private void writeToDiagramFactorId(DiagramLink diagramLink) throws Exception
	{
		writeStartElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
		writeStartElement(LINKABLE_FACTOR_ID);
		
		Factor toFactor = diagramLink.getToDiagramFactor().getWrappedFactor();
		String toFactorTypeName = getFactorTypeName(toFactor);
		writeElement(toFactorTypeName, ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		writeEndElement(LINKABLE_FACTOR_ID);
		writeEndElement(DIAGRAM_LINK + TO_DIAGRAM_FACTOR_ID);
	}

	private void writeWrappedFactorLinkId(DiagramLink diagramLink) throws Exception
	{
		writeStartElement(DIAGRAM_LINK + WRAPPED_FACTOR_LINK_ID_ELEMENT_NAME);
		writeStartElement(WRAPPED_BY_DIAGRAM_LINK_ID_ELEMENT_NAME);
		
		writeElement("FactorLink", ID_ELEMENT_NAME, diagramLink.getWrappedId().toString());
		
		writeEndElement(WRAPPED_BY_DIAGRAM_LINK_ID_ELEMENT_NAME);
		writeEndElement(DIAGRAM_LINK + WRAPPED_FACTOR_LINK_ID_ELEMENT_NAME);
	}

	public String getFactorTypeName(Factor wrappedFactor)
	{
		if (Target.is(wrappedFactor))
			return WcsXmlConstants.BIODIVERSITY_TARGET;
		
		//FIXME urgent - wcs  need to use object schema name
		if (Cause.is(wrappedFactor))
			return "Cause";
		
		return wrappedFactor.getTypeName();
	}
	
	public void writeDiagramPoint(Point point) throws Exception
	{
		writeStartElement(DIAGRAM_POINT_ELEMENT_NAME);
		writeElement(getWriter(), X_ELEMENT_NAME, point.x);
		writeElement(getWriter(), Y_ELEMENT_NAME, point.y);
		writeEndElement(DIAGRAM_POINT_ELEMENT_NAME);
	}

	private void writeResultsChainSchemaElement() throws Exception
	{
		writeDiagram(RESULTS_CHAIN, ResultsChainDiagram.getObjectType());
	}

	private void writeConceptualModelSchemaElement() throws Exception
	{
		writeDiagram(CONCEPTUAL_MODEL, ConceptualModelDiagram.getObjectType());
	}

	private void writeDiagram(String diagramElementName, int diagramObjectType) throws Exception
	{
		writeStartPoolElement(diagramElementName);
		ORefList diagramObjectRefs = getProject().getPool(diagramObjectType).getSortedRefList();
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject conceptualModel = DiagramObject.findDiagramObject(getProject(), diagramObjectRefs.get(index));
			writeStartElementWithAttribute(getWriter(), diagramElementName, ID, conceptualModel.getId().toString());			
			writeOptionalElementWithSameTag(diagramElementName, conceptualModel, ConceptualModelDiagram.TAG_LABEL);					
			writeOptionalElementWithSameTag(diagramElementName, conceptualModel, ConceptualModelDiagram.TAG_SHORT_LABEL);
			writeOptionalElementWithSameTag(diagramElementName, conceptualModel, ConceptualModelDiagram.TAG_DETAIL);
			writeIds(diagramElementName, DiagramObject.TAG_DIAGRAM_FACTOR_IDS, DIAGRAM_FACTOR_ID_ELEMENT_NAME, conceptualModel.getAllDiagramFactorRefs());
			writeIds(diagramElementName, DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, DIAGRAM_LINK_ID_ELEMENT_NAME, conceptualModel.getAllDiagramLinkRefs());
			writeCodeListElement(diagramElementName, XmlSchemaCreator.HIDDEN_TYPES_ELEMENT_NAME, conceptualModel.getHiddenTypes());
			writeIds(diagramElementName, WcsXmlConstants.SELECTED_TAGGED_OBJECT_SET_IDS, "TaggedObjectSetId", conceptualModel.getSelectedTaggedObjectSetRefs());
			
			writeEndElement(out, diagramElementName);
		}
		
		writeEndPoolElement(diagramElementName);
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
		
		writeCodeElement(FOS_PROJECT_DATA, FosProjectData.TAG_TRAINING_TYPE, getFosProjectData().getData(FosProjectData.TAG_TRAINING_TYPE));
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_TRAINING_DATES);
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_TRAINERS);
		writeOptionalElementWithSameTag(FOS_PROJECT_DATA, getFosProjectData(), FosProjectData.TAG_COACHES);
		
		writeEndElement(out, FOS_PROJECT_DATA);
	}

	private void writeRareProjectDataSchemaElement() throws Exception
	{
		writeStartElement(out, RARE_PROJECT_DATA);
		 
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_COHORT);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_THREATS_ADDRESSED_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_NUMBER_OF_COMMUNITIES_IN_CAMPAIGN_AREA);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_AUDIENCE);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_BIODIVERSITY_HOTSPOTS);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_COMMON_NAME);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_SCIENTIFIC_NAME);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_FLAGSHIP_SPECIES_DETAIL);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_THEORY_OF_CHANGE);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_SLOGAN);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_SUMMARY_OF_KEY_MESSAGES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_MAIN_ACTIVITIES_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_THREAT_REDUCTION_OBJECTIVE_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_MONITORING_OBJECTIVE_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_COURSE_MANAGER_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_REGIONAL_DIRECTOR_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_CAMPAIGN_MANAGER_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_LOCAL_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_BINGO_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_THREAT_REDUCTION_PARTNER_CONTACT_NOTES);
		writeOptionalElementWithSameTag(WCS_PROJECT_DATA, getRareProjectData(), RareProjectData.TAG_MONITORING_PARTNER_CONTACT_NOTES);
		
		writeEndElement(out, RARE_PROJECT_DATA);
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
		writeStartPoolElement(ORGANIZATION);
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
		
		writeEndPoolElement(ORGANIZATION);
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
	
	public void writeOptionalElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(parentElementName, tag, object, tag);
	}
	
	public void writeElementWithSameTag(String parentElementName, BaseObject object, String tag) throws Exception
	{
		writeElement(parentElementName, tag, object, tag);
	}
	
	private void writeElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, object, tag);
	}
	
	public void writeElement(String parentElementName, String elementName, String data) throws Exception
	{
		writeElement(getWriter(), parentElementName + elementName, data);
	}
	
	public void writeOptionalElement(String parentElementName, String elementName, BaseObject object, String tag) throws Exception
	{
		writeOptionalElement(getWriter(), parentElementName + elementName, object, tag);
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
		writeCodeListElement(parentElementName, poolElementName, object.getCodeList(tag));
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
	
	public void writeCodeElement(String parentElementName, String elementName, String code) throws Exception
	{
		writeStartElement(getWriter(), parentElementName + elementName);
		writeXmlEncodedData(getWriter(), code);
		writeEndElement(getWriter(), parentElementName + elementName);
	}
	
	public void writeOptionalCodeElement(String parentElementName, String elementName, String code) throws Exception
	{
		if (!code.isEmpty())
		{
			writeStartElement(getWriter(), parentElementName + elementName);
			writeXmlEncodedData(getWriter(), code);
			writeEndElement(getWriter(), parentElementName + elementName);
		}
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
