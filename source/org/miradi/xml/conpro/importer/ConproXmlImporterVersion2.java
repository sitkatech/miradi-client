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
package org.miradi.xml.conpro.importer;

import java.awt.Point;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.martus.util.MultiCalendar;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.ThreatTargetChainObject;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.UnsupportedOldVersionSchemaException;
import org.miradi.exceptions.ValidationException;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objecthelpers.ThreatTargetVirtualLink;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Indicator;
import org.miradi.objects.IntermediateResult;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.ValueOption;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.project.ProjectChainObject;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.conpro.ConProMiradiCodeMapHelperVersion2;
import org.miradi.xml.conpro.ConProMiradiXmlVersion2;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidatorVersion2;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ConproXmlImporterVersion2 implements ConProMiradiXmlVersion2
{
	public ConproXmlImporterVersion2(Project projectToFill) throws Exception
	{
		project = projectToFill;
		codeMapHelper = new ConProMiradiCodeMapHelperVersion2();

		wrappedToDiagramMap = new HashMap<ORef, ORef>();
	}
		
	public void importConProProject(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		loadXml(projectAsInputStream);
		importXml();
	}
	
	public void importConProjectNumbers(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		loadXml(projectAsInputStream);
		Node projectSumaryNode = getNode(getRootNode(), PROJECT_SUMMARY);
		ORef metadataRef = getProject().getMetadata().getRef();
		importProjectId(projectSumaryNode, metadataRef);
	}

	private void loadXml(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		InputSource inputSource = new InputSource(projectAsInputStream);
		document = createDocument(inputSource);
				
		String nameSpaceUri = document.getDocumentElement().getNamespaceURI();
		if (!isSameNameSpace(nameSpaceUri))
		{
			throw new Exception("Name space mismatch should be: " + PARTIAL_NAME_SPACE + " <br> however it is: " + nameSpaceUri); 
		}
				
		if (isUnsupportedNewVersion(nameSpaceUri))
		{
			throw new UnsupportedNewVersionSchemaException();
		}
		
		if (isUnsupportedOldVersion(nameSpaceUri))
		{
			throw new UnsupportedOldVersionSchemaException();
		}
		
		projectAsInputStream.seek(0);			
		if (!new ConProMiradiXmlValidatorVersion2().isValid(projectAsInputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
		
		xPath = createXPath();
	}

	private void importXml() throws Exception
	{
		importProjectSummaryElement();
		
		importStrategies();
		importThreats();
		importMethods();
		importIndicators();
		importObjectives();
		importTargets();
		
		setDiagramFactorDefaultLocations();
		
		createDefaultObjects();
		attachObjectivesToHolder();
		attachIndicatorsToHolder();
		updateObjectiveRelevantStrategyList();
		createTaggedThreatChains();
	}

	private void importStrategies() throws Exception
	{
		NodeList strategyNodeList = getNodes(getRootNode(), STRATEGIES, STRATEGY);
		for (int nodeIndex = 0; nodeIndex < strategyNodeList.getLength(); ++nodeIndex) 
		{
			Node strategyNode = strategyNodeList.item(nodeIndex);
			String strategyId = getAttributeValue(strategyNode, ID);
			ORef strategyRef = getProject().createObject(Strategy.getObjectType(), new BaseId(strategyId));
			importField(strategyNode, NAME, strategyRef, Strategy.TAG_LABEL);
			importField(strategyNode, TAXONOMY_CODE, strategyRef, Strategy.TAG_TAXONOMY_CODE);
			
			importStrategyStatus(strategyNode, strategyRef);
			importField(strategyNode, COMMENT, strategyRef, Strategy.TAG_COMMENT);
			importField(strategyNode, LEGACY_TNC_STRATEGY_RATING, strategyRef, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
			importProgressReports(strategyNode, strategyRef, Strategy.TAG_PROGRESS_REPORT_REFS);
			importActivities(strategyNode, strategyRef);
			
			createDiagramFactorAndAddToDiagram(strategyRef);
		}
	}

	private void importStrategyStatus(Node strategyNode, ORef strategyRef) throws Exception
	{
		String data = getNodeContent(strategyNode, SELECTED);
		String draftStatusValue = Strategy.STATUS_REAL;
		if (isDraft(data))
			draftStatusValue = Strategy.STATUS_DRAFT;
		
		setData(strategyRef, Strategy.TAG_STATUS, draftStatusValue);
	}

	private boolean isDraft(String data)
	{
		if (data.length() == 0)
			return false;
			
		return !isTrue(data);
	}
	
	private boolean isTrue(String value)
	{
		if (value.equals(BooleanData.BOOLEAN_TRUE))
			return true;
		
		if (value.equals(BooleanData.BOOLEAN_FALSE))
			return false;
		
		return Boolean.parseBoolean(value);
	}

	private void importActivities(Node strategyNode, ORef strategyRef) throws Exception
	{
		ORefList activityRefs = new ORefList();
		NodeList activityNodeList = getNodes(strategyNode, ACTIVITIES, ACTIVITY);
		for (int nodeIndex = 0; nodeIndex < activityNodeList.getLength(); ++nodeIndex) 
		{
			Node activityNode = activityNodeList.item(nodeIndex);
			ORef activityRef = getProject().createObject(Task.getObjectType());
			activityRefs.add(activityRef);
					
			importField(activityNode, NAME, activityRef, Task.TAG_LABEL);
			importWhenOverride(activityNode, activityRef);
			importProgressReports(activityNode, activityRef, Task.TAG_PROGRESS_REPORT_REFS);
		}
		
		setIdListFromRefListData(strategyRef, Strategy.TAG_ACTIVITY_IDS, activityRefs, Task.getObjectType());
	}

	private void importWhenOverride(Node activityNode, ORef activityRef) throws Exception
	{
		String startDateAsString = getNodeContent(activityNode, ACTIVITY_START_DATE);
		String endDateAsString = getNodeContent(activityNode, ACTIVITY_END_DATE);
		if (startDateAsString.length() > 0 && endDateAsString.length() > 0)
		{
			MultiCalendar startDate = MultiCalendar.createFromIsoDateString(startDateAsString);
			MultiCalendar endDate = MultiCalendar.createFromIsoDateString(endDateAsString);
			DateRange dateRange = new DateRange(startDate, endDate);
			setData(activityRef, Task.TAG_WHEN_OVERRIDE, dateRange.toJson().toString());
		}
	}
	
	private void importObjectives() throws Exception
	{
		NodeList objectiveNodeList = getNodes(getRootNode(), OBJECTIVES, OBJECTIVE);
		for (int nodeIndex = 0; nodeIndex < objectiveNodeList.getLength(); ++nodeIndex) 
		{
			Node objectiveNode = objectiveNodeList.item(nodeIndex);
			String objectiveId = getAttributeValue(objectiveNode, ID);
			ORef objectiveRef = getProject().createObject(Objective.getObjectType(), new BaseId(objectiveId));
			
			importRelevantIndicators(objectiveNode, objectiveRef);
			importField(objectiveNode, NAME, objectiveRef, Objective.TAG_LABEL);
			importField(objectiveNode, COMMENT, objectiveRef, Objective.TAG_COMMENTS);
			importProgressPercents(objectiveNode, objectiveRef);
		}
	}

	private void importProgressPercents(Node objectiveNode, ORef objectiveRef) throws Exception
	{
		ORefList progressPercentRefs = new ORefList();
		NodeList progressPercentNodeList = getNodes(objectiveNode, PROGRESS_PERCENT_REPORTS, PROGRESS_PERCENT_REPORT);
		for (int nodeIndex = 0; nodeIndex < progressPercentNodeList.getLength(); ++nodeIndex) 
		{
			Node progressPercentNode = progressPercentNodeList.item(nodeIndex);			
			ORef progressPercentRef = getProject().createObject(ProgressPercent.getObjectType());
			
			importField(progressPercentNode, PROGRESS_PERCENT_COMPLETE, progressPercentRef, ProgressPercent.TAG_PERCENT_COMPLETE);
			importField(progressPercentNode, PROGRESS_PERCENT_DATE, progressPercentRef, ProgressPercent.TAG_DATE);
			importField(progressPercentNode, PROGRESS_PERCENT_COMMENT, progressPercentRef, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES);

			progressPercentRefs.add(progressPercentRef);
		}
			
		setData(objectiveRef, Objective.TAG_PROGRESS_PERCENT_REFS, progressPercentRefs);
	}

	private void importRelevantIndicators(Node objectiveNode, ORef objectiveRef) throws Exception
	{
		RelevancyOverrideSet relevantIndicators = new RelevancyOverrideSet();
		NodeList indicatorIdNodes = getNodes(objectiveNode, INDICATORS, INDICATOR_ID);
		for (int nodeIndex = 0; nodeIndex < indicatorIdNodes.getLength(); ++nodeIndex) 
		{
			Node indicatorNode = indicatorIdNodes.item(nodeIndex);
			BaseId indicatorId = new BaseId(indicatorNode.getTextContent());
			ORef indicatorRef = new ORef(Indicator.getObjectType(), indicatorId);
			relevantIndicators.add(new RelevancyOverride(indicatorRef, true));
		}
		
		setData(objectiveRef, Objective.TAG_RELEVANT_INDICATOR_SET, relevantIndicators.toString());
	}

	private void importMethods() throws Exception
	{
		NodeList methodNodeList = getNodes(getRootNode(), METHODS, METHOD);
		for (int nodeIndex = 0; nodeIndex < methodNodeList.getLength(); ++nodeIndex) 
		{
			Node methodNode = methodNodeList.item(nodeIndex);
			String methodId = getAttributeValue(methodNode, ID);
			ORef methodRef = getProject().createObject(Task.getObjectType(), new BaseId(methodId));
			
			importField(methodNode, METHOD_NAME, methodRef, Task.TAG_LABEL);
			importField(methodNode, METHOD_DETAIL, methodRef, Task.TAG_DETAILS);
			importField(methodNode, METHOD_ANNUAL_COST, methodRef, Task.TAG_BUDGET_COST_OVERRIDE);

			importField(methodNode, METHOD_COMMENT, methodRef, Task.TAG_COMMENT);
		}
	}
	
	private void importIndicators() throws Exception
	{
		NodeList indicatorNodeList = getNodes(getRootNode(), INDICATORS, INDICATOR);
		for (int nodeIndex = 0; nodeIndex < indicatorNodeList.getLength(); ++nodeIndex) 
		{
			Node indicatorNode = indicatorNodeList.item(nodeIndex);
			String indicatorId = getAttributeValue(indicatorNode, ID);
			ORef indicatorRef = getProject().createObject(Indicator.getObjectType(), new BaseId(indicatorId));
			
			importField(indicatorNode, NAME, indicatorRef, Indicator.TAG_LABEL);
			updateIndicatorMethodList(indicatorNode, indicatorRef);
			importCodeField(indicatorNode, PRIORITY, indicatorRef, Indicator.TAG_PRIORITY, getCodeMapHelper().getConProToMiradiRatingMap());
			importProgressReports(indicatorNode, indicatorRef, Indicator.TAG_PROGRESS_REPORT_REFS);
			importMeasurements(indicatorNode, indicatorRef);
			importBudgetData(indicatorNode, indicatorRef);
			importField(indicatorNode, COMMENT, indicatorRef, Indicator.TAG_COMMENT);
		}
	}
	
	private void updateIndicatorMethodList(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		ORefList methodRefs = new ORefList();
		NodeList methodNodeList = getNodes(indicatorNode, METHODS, METHOD_ID);
		for (int nodeIndex = 0; nodeIndex < methodNodeList.getLength(); ++nodeIndex) 
		{
			Node methodNode = methodNodeList.item(nodeIndex);
			String methodId = methodNode.getTextContent();
			ORef methodRef = new ORef(Task.getObjectType(), new BaseId(methodId));
			methodRefs.add(methodRef);
		}
			
		setIdListFromRefListData(indicatorRef, Indicator.TAG_METHOD_IDS, methodRefs, Task.getObjectType());
	}
	
	private void importMeasurements(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		ORefList measurementRefs = new ORefList();
		NodeList measurementNodeList = getNodes(indicatorNode, MEASUREMENTS, MEASUREMENT);
		for (int nodeIndex = 0; nodeIndex < measurementNodeList.getLength(); ++nodeIndex) 
		{
			Node measurementNode = measurementNodeList.item(nodeIndex);			
			ORef measurementRef = getProject().createObject(Measurement.getObjectType());
			
			importField(measurementNode, MEASUREMENT_SUMMARY, measurementRef, Measurement.TAG_SUMMARY);
			importField(measurementNode, MEASUREMENT_DATE, measurementRef, Measurement.TAG_DATE);
			importCodeField(measurementNode, MEASUREMENT_STATUS_CONFIDENCE, measurementRef, Measurement.TAG_STATUS_CONFIDENCE, getCodeMapHelper().getConProToMiradiStatusConfidenceMap());
			importCodeField(measurementNode, MEASUREMENT_TREND, measurementRef, Measurement.TAG_TREND, getCodeMapHelper().getConProToMiradiTrendMap());
			importCodeField(measurementNode, MEASUREMENT_RATING, measurementRef, Measurement.TAG_STATUS, getCodeMapHelper().getConProToMiradiRankingMap());


			measurementRefs.add(measurementRef);
		}
			
		setData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, measurementRefs);
	}

	private void importBudgetData(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		//FIXME need to import budget data by creating new assignment
	}

	private void importProgressReports(Node parentNode, ORef parentRef, String tag) throws Exception
	{
		ORefList progressReportRefs = new ORefList();
		NodeList progressReportNodeList = getNodes(parentNode, PROGRESS_REPORTS, PROGRESS_REPORT);
		for (int nodeIndex = 0; nodeIndex < progressReportNodeList.getLength(); ++nodeIndex) 
		{
			ORef progressReportRef = getProject().createObject(ProgressReport.getObjectType());
			
			Node progressReportNode = progressReportNodeList.item(nodeIndex);
			importField(progressReportNode, PROGRESS_REPORT_DATE, progressReportRef, ProgressReport.TAG_PROGRESS_DATE);
			importCodeField(progressReportNode, PROGRESS_REPORT_STATUS, progressReportRef, ProgressReport.TAG_PROGRESS_STATUS, getCodeMapHelper().getConProToMiradiProgressStatusMap());
			importField(progressReportNode, PROGRESS_REPORT_COMMENT, progressReportRef, ProgressReport.TAG_DETAILS);

			progressReportRefs.add(progressReportRef);
		}
			
		setData(parentRef, tag, progressReportRefs);
	}

	private void importThreats() throws Exception
	{
		NodeList threatNodeList = getNodes(getRootNode(), THREATS, THREAT);
		for (int nodeIndex = 0; nodeIndex < threatNodeList.getLength(); ++nodeIndex) 
		{
			Node threatNode = threatNodeList.item(nodeIndex);
			String threatId = getAttributeValue(threatNode, ID);
			ORef threatRef = getProject().createObject(Cause.getObjectType(), new BaseId(threatId));
			setData(threatRef, Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
			
			importField(threatNode, NAME, threatRef, Cause.TAG_LABEL);
			importField(threatNode, THREAT_TAXONOMY_CODE, threatRef, Cause.TAG_TAXONOMY_CODE);
			
			createDiagramFactorAndAddToDiagram(threatRef);
		}
	}

	private void importViability(Node keaNode, ORef targetRef, ORef keaRef) throws Exception
	{
		NodeList keaNodeList = getNodes(keaNode, VIABILITY_ASSESSMENTS, VIABILITY_ASSESSMENT);
		for (int nodeIndex = 0; nodeIndex < keaNodeList.getLength(); ++nodeIndex) 
		{
			Node viabilityAssessmentNode = keaNodeList.item(nodeIndex);
			setData(targetRef, Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
			
			String existingKeaIdsAsString = getProject().getObjectData(targetRef, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS);
			IdList keaIds = new IdList(KeyEcologicalAttribute.getObjectType(), existingKeaIdsAsString);
			if(!keaIds.contains(keaRef))
				keaIds.add(keaRef.getObjectId());
			setData(targetRef, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaIds.toString());
			
			ORef indicatorRef = getNodeAsRef(viabilityAssessmentNode, INDICATOR_ID, Indicator.getObjectType());
			
			ORefSet allKeaIndicatorRefSet = new ORefSet(indicatorRef);
			KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getProject(), keaRef);
			ORefList currentKeaIndicators = kea.getIndicatorRefs();
			allKeaIndicatorRefSet.addAllRefs(currentKeaIndicators);
			ORefList indicatorRefList = new ORefList(allKeaIndicatorRefSet);
			setData(keaRef, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorRefList.convertToIdList(Indicator.getObjectType()).toString());
			
			importIndicatorThresholds(viabilityAssessmentNode, indicatorRef);		
			
			importCodeField(viabilityAssessmentNode, DESIRED_VIABILITY_RATING, indicatorRef, Indicator.TAG_FUTURE_STATUS_RATING, getCodeMapHelper().getConProToMiradiRankingMap());
			importField(viabilityAssessmentNode, DESIRED_RATING_DATE, indicatorRef, Indicator.TAG_FUTURE_STATUS_DATE);
			importField(viabilityAssessmentNode, KEA_AND_INDICATOR_COMMENT, indicatorRef, Indicator.TAG_DETAIL);
			importField(viabilityAssessmentNode, INDICATOR_RATING_COMMENT, indicatorRef, Indicator.TAG_VIABILITY_RATINGS_COMMENT);
			importField(viabilityAssessmentNode, DESIRED_RATING_COMMENT, indicatorRef, Indicator.TAG_FUTURE_STATUS_COMMENT);
		}
	}

	private void importIndicatorThresholds(Node viabilityAssessmentNode, ORef indicatorRef) throws Exception
	{
		StringMap thresholds = new StringMap();
		String poorThreshold = getNodeContent(viabilityAssessmentNode, INDICATOR_DESCRIPTION_POOR);
		String fairThreshold = getNodeContent(viabilityAssessmentNode, INDICATOR_DESCRIPTION_FAIR);
		String goodThreshold = getNodeContent(viabilityAssessmentNode, INDICATOR_DESCRIPTION_GOOD);
		String veryGoodThreshold = getNodeContent(viabilityAssessmentNode, INDICATOR_DESCRIPTION_VERY_GOOD);
		thresholds.add(StatusQuestion.POOR, poorThreshold);
		thresholds.add(StatusQuestion.FAIR, fairThreshold);
		thresholds.add(StatusQuestion.GOOD, goodThreshold);
		thresholds.add(StatusQuestion.VERY_GOOD, veryGoodThreshold);
		setData(indicatorRef, Indicator.TAG_INDICATOR_THRESHOLD, thresholds.toString());
	}

	private void importKeyEcologicalAttributes(Node targetNode, ORef targetRef) throws Exception
	{
		NodeList keaNodeList = getNodes(targetNode, KEY_ATTRIBUTES, KEY_ATTRIBUTE);
		for (int nodeIndex = 0; nodeIndex < keaNodeList.getLength(); ++nodeIndex) 
		{
			Node keaNode = keaNodeList.item(nodeIndex);
			ORef keaRef = getProject().createObject(KeyEcologicalAttribute.getObjectType());
			importField(keaNode, NAME, keaRef, KeyEcologicalAttribute.TAG_LABEL);
			importCodeField(keaNode, CATEGORY, keaRef, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, getCodeMapHelper().getConProToMiradiKeaTypeMap());
			
			importViability(keaNode, targetRef, keaRef);
		}
	}

	private Document createDocument(InputSource inputSource) throws ParserConfigurationException, SAXException, IOException
	{
		DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
		documentFactory.setNamespaceAware(true);
		DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
		
		return documentBuilder.parse(inputSource);
	}

	private XPath createXPath()
	{
		XPathFactory xPathFactory = XPathFactory.newInstance();
		XPath  thisXPath = xPathFactory.newXPath();
		thisXPath.setNamespaceContext(new ConProMiradiNameSpaceContextVersion2());
		
		return thisXPath;
	}

	private void importProjectSummaryElement() throws Exception
	{
		ORef metadataRef = getProject().getMetadata().getRef();
		Node projectSumaryNode = getNode(getRootNode(), PROJECT_SUMMARY);
		
		String tncProjectSharingXmlValue = getAttributeValue(projectSumaryNode, SHARE_OUTSIDE_ORGANIZATION);
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectData.getObjectType());
		importCodeField(tncProjectDataRef, TncProjectData.TAG_PROJECT_SHARING_CODE, getCodeMapHelper().getConProToMiradiTncProjectSharingMap(), tncProjectSharingXmlValue);
		importOrganizationalPriority(projectSumaryNode, tncProjectSharingXmlValue, tncProjectDataRef);
		importProjectTypes(tncProjectSharingXmlValue, tncProjectDataRef);
		
		importField(projectSumaryNode, NAME, metadataRef,ProjectMetadata.TAG_PROJECT_NAME);
		importProjectId(projectSumaryNode, metadataRef);
		importField(projectSumaryNode, START_DATE, metadataRef, ProjectMetadata.TAG_START_DATE);
		importField(projectSumaryNode, AREA_SIZE, metadataRef, ProjectMetadata.TAG_TNC_SIZE_IN_HECTARES);	
		importField(projectSumaryNode, new String[]{GEOSPATIAL_LOCATION, LATITUDE}, metadataRef, ProjectMetadata.TAG_PROJECT_LATITUDE);
		importField(projectSumaryNode, new String[]{GEOSPATIAL_LOCATION, LONGITUDE}, metadataRef, ProjectMetadata.TAG_PROJECT_LONGITUDE);
		importField(projectSumaryNode, DESCRIPTION_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_SCOPE);
		importField(projectSumaryNode, GOAL_COMMENT, metadataRef, ProjectMetadata.TAG_PROJECT_VISION);
		importField(projectSumaryNode, PLANNING_TEAM_COMMENT, metadataRef, ProjectMetadata.TAG_TNC_PLANNING_TEAM_COMMENT);
		importField(projectSumaryNode, LESSONS_LEARNED, metadataRef, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
		importThreatRatingValueInMode(projectSumaryNode, metadataRef);
		importTeamMembers(projectSumaryNode, metadataRef);
		
		String[] ecoRegionElementHierarchy = new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, ECOREGIONS, ECOREGION_CODE}; 
		String[] allEcoregionCodes = extractNodesAsList(generatePath(ecoRegionElementHierarchy));
		setData(metadataRef, ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, extractEcoregions(allEcoregionCodes, TncTerrestrialEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, extractEcoregions(allEcoregionCodes, TncMarineEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, extractEcoregions(allEcoregionCodes, TncFreshwaterEcoRegionQuestion.class).toString());
		importField(projectSumaryNode, EXPORT_DATE, metadataRef, ProjectMetadata.TAG_TNC_DATABASE_DOWNLOAD_DATE);
		
		importCodeListField(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, COUNTRIES, COUNTRY_CODE}), metadataRef, ProjectMetadata.TAG_COUNTRIES);
		importCodeListField(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, OUS, OU_CODE}), metadataRef, ProjectMetadata.TAG_TNC_OPERATING_UNITS);
	}

	private void importOrganizationalPriority(Node projectSumaryNode, String tncProjectSharingXmlValue, ORef tncProjectDataRef)	throws Exception
	{
		importCodeListField(projectSumaryNode, ORGANIZATIONAL_PRIORITIES, ORGANIZATIONAL_PRIORITY, tncProjectDataRef, TncProjectData.TAG_ORGANIZATIONAL_PRIORITIES, getCodeMapHelper().getConProToMiradiOrganizationalPrioritiesMap());
	}
	
	private void importProjectTypes(String tncProjectSharingXmlValue, ORef tncProjectDataRef)	throws Exception
	{
		importCodeListField(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, PROJECT_PLACE_TYPES, PROJECT_PLACE_TYPE}), tncProjectDataRef, TncProjectData.TAG_PROJECT_PLACE_TYPES);
	}
	
	private void importProjectId(Node projectSumaryNode, ORef metadataRef) throws Exception
	{
		NodeList projectIdNodes = getNodes(projectSumaryNode, new String[]{PROJECT_ID});
		StringRefMap stringRefMap = new StringRefMap();
		for (int nodeIndex = 0; nodeIndex < projectIdNodes.getLength(); ++nodeIndex) 
		{
			Node projectIdNode = projectIdNodes.item(nodeIndex);
			
			String projectId = getSafeNodeContent(projectIdNode);
			ORef xenodataRef = getProject().createObject(Xenodata.getObjectType());
			getProject().setObjectData(xenodataRef, Xenodata.TAG_PROJECT_ID, projectId);

			String contextAttributeValue = getAttributeValue(projectIdNode, CONTEXT_ATTRIBUTE);
			stringRefMap.add(contextAttributeValue, xenodataRef);
		}
		
		getProject().setObjectData(metadataRef, ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, stringRefMap.toString());
	}

	private void importTeamMembers(Node projectSumaryNode, ORef metadataRef) throws Exception
	{
		NodeList teamMemberNodeList = getNodes(projectSumaryNode, TEAM, PERSON);
		for (int nodeIndex = 0; nodeIndex < teamMemberNodeList.getLength(); ++nodeIndex) 
		{
			Node teamMemberNode = teamMemberNodeList.item(nodeIndex);
			ORef projectResourceRef = getProject().createObject(ProjectResource.getObjectType());
			
			importProjectResourceRoles(teamMemberNode, projectResourceRef);		
			importField(teamMemberNode, GIVEN_NAME, projectResourceRef, ProjectResource.TAG_GIVEN_NAME);
			importField(teamMemberNode, SUR_NAME, projectResourceRef, ProjectResource.TAG_SUR_NAME);
			importField(teamMemberNode, EMAIL, projectResourceRef, ProjectResource.TAG_EMAIL);
			importField(teamMemberNode, PHONE, projectResourceRef, ProjectResource.TAG_PHONE_NUMBER);
			importField(teamMemberNode, ORGANIZATION, projectResourceRef, ProjectResource.TAG_ORGANIZATION);
		}
	}

	private void importProjectResourceRoles(Node teamMemberNode, ORef projectResourceRef) throws Exception
	{
		CodeList roleCodes = new CodeList();
		roleCodes.add(ResourceRoleQuestion.TeamMemberRoleCode);
		
		NodeList roleNodeList = getNodes(teamMemberNode, new String[]{ROLE});
		for (int nodeIndex = 0; nodeIndex < roleNodeList.getLength(); ++nodeIndex)
		{
			Node roleNode = roleNodeList.item(nodeIndex);
			if (roleNode.getTextContent().equals(TEAM_LEADER_VALUE))
				roleCodes.add(ResourceRoleQuestion.TeamLeaderCode);		
		}
	
		setData(projectResourceRef, ProjectResource.TAG_ROLE_CODES, roleCodes.toString());
	}

	public void importTargets() throws Exception
	{
		NodeList targetNodeList = getNodes(getRootNode(), TARGETS, TARGET);
		for (int nodeIndex = 0; nodeIndex < targetNodeList.getLength(); ++nodeIndex) 
		{
			Node targetNode = targetNodeList.item(nodeIndex);
			String targetId = getAttributeValue(targetNode, ID);
			ORef targetRef = getProject().createObject(Target.getObjectType(), new BaseId(targetId));
			
			importField(targetNode, TARGET_NAME, targetRef, Target.TAG_LABEL);
			importField(targetNode, TARGET_DESCRIPTION, targetRef, Target.TAG_TEXT);
			importField(targetNode, TARGET_DESCRIPTION_COMMENT, targetRef, Target.TAG_COMMENT);
			importField(targetNode, TARGET_VIABILITY_COMMENT, targetRef	, Target.TAG_CURRENT_STATUS_JUSTIFICATION);
			importCodeField(targetNode, TARGET_VIABILITY_RANK, targetRef, Target.TAG_TARGET_STATUS, getCodeMapHelper().getConProToMiradiRankingMap());
			importOptionalTargetViabilityRankElement(targetNode, targetRef);
			importCodeListField(targetNode, HABITAT_TAXONOMY_CODES, HABITAT_TAXONOMY_CODE, targetRef, Target.TAG_HABITAT_ASSOCIATION, getCodeMapHelper().getConProToMiradiHabitiatCodeMap());
			
			importSubTargets(targetNode, targetRef);
			createDiagramFactorAndAddToDiagram(targetRef);
			importThreatToTargetAssociations(targetNode, targetRef);
			importStrategyThreatTargetAssociations(targetNode, targetRef);
			importStresses(targetNode, targetRef);
			
			importKeyEcologicalAttributes(targetNode, targetRef);
		}
	}

	private void importOptionalTargetViabilityRankElement(Node targetNode, ORef targetRef) throws Exception
	{
		Node node = getNode(targetNode, TARGET_VIABILITY_RANK);
		if (node == null)
			return;
		
		String targetViabilityMode = getAttributeValue(node, TARGET_VIABILITY_MODE);
		importCodeField(targetRef, Target.TAG_VIABILITY_MODE, getCodeMapHelper().getConProToMiradiViabilityModeMap(), targetViabilityMode);
	}
		
	private void importStrategyThreatTargetAssociations(Node targetNode, ORef targetRef) throws Exception
	{
		NodeList strategyThreatTargetAssociations = getNodes(targetNode, STRATEGY_THREAT_TARGET_ASSOCIATIONS, STRATEGY_THREAT_TARGET_ASSOCIATION);
		for (int nodeIndex = 0; nodeIndex < strategyThreatTargetAssociations.getLength(); ++nodeIndex)
		{
			Node strategyThreatTargetAssociationNode = strategyThreatTargetAssociations.item(nodeIndex);
			ORef threatRef = getNodeAsRef(strategyThreatTargetAssociationNode, THREAT_ID, Cause.getObjectType());
			ORef strategyRef = getNodeAsRef(strategyThreatTargetAssociationNode, STRATEGY_ID, Strategy.getObjectType());
			createFactorLinkAndAddToDiagram(strategyRef, threatRef);
		}
	}

	private void importThreatToTargetAssociations(Node targetNode, ORef targetRef) throws Exception
	{
		SimpleThreatRatingFramework framework = getProject().getSimpleThreatRatingFramework();
		NodeList threatTargetAssociations = getNodes(targetNode, THREAT_TARGET_ASSOCIATIONS, THREAT_TARGET_ASSOCIATION);
		ThreatRatingCommentsData threatRatingCommentsData = getProject().getSingletonThreatRatingCommentsData();
		for (int nodeIndex = 0; nodeIndex < threatTargetAssociations.getLength(); ++nodeIndex)
		{
			Node threatTargetAssociationNode = threatTargetAssociations.item(nodeIndex);
			ORef threatRef = getNodeAsRef(threatTargetAssociationNode, THREAT_ID, Cause.getObjectType());
			createFactorLinkAndAddToDiagram(threatRef, targetRef);
			
			ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
			importThreatRatingField(threatTargetAssociationNode, THREAT_SCOPE, framework, bundle, Stress.TAG_SCOPE);
			importThreatRatingField(threatTargetAssociationNode, THREAT_SEVERITY, framework, bundle, Stress.TAG_SEVERITY);
			importThreatRatingField(threatTargetAssociationNode, THREAT_IRREVERSIBILITY, framework, bundle, ThreatStressRating.TAG_IRREVERSIBILITY);			
			framework.saveBundle(bundle);
			
			String threatTargetKey = ThreatRatingCommentsData.createKey(threatRef, targetRef);
			importThreatRatingCommentsField(threatTargetAssociationNode, THREAT_TARGET_COMMENT, threatRatingCommentsData, threatTargetKey);
		}
	}
	
	private void importThreatRatingField(Node threatTargetAssociationNode, String element, SimpleThreatRatingFramework framework, ThreatRatingBundle bundle, String criterionLabel) throws Exception
	{
		String rawCode = getNodeContent(threatTargetAssociationNode, element);
		if (rawCode.length() == 0)
			return;
		
		String convertedCode = getCodeMapHelper().getConProToMiradiRatingMap().get(rawCode);
		ValueOption valueOption = framework.findValueOptionByNumericValue(Integer.parseInt(convertedCode));
		BaseId criterionId = framework.findCriterionByLabel(criterionLabel).getId();
		bundle.setValueId(criterionId, valueOption.getId());
	}

	private void importSubTargets(Node targetNode, ORef targetRef) throws Exception
	{
		ORefList subTargetRefs = new ORefList();
		NodeList subTargetNodes = getNodes(targetNode, NESTED_TARGETS, NESTED_TARGET);
		for (int nodeIndex = 0; nodeIndex < subTargetNodes.getLength(); ++nodeIndex)
		{
			ORef subTargetRef = getProject().createObject(SubTarget.getObjectType());
			Node subTargetNode = subTargetNodes.item(nodeIndex);
			
			importField(subTargetNode, NAME, subTargetRef, SubTarget.TAG_LABEL);
			importField(subTargetNode, COMMENT, subTargetRef, SubTarget.TAG_DETAIL);
			
			subTargetRefs.add(subTargetRef);
		}
		
		setData(targetRef, Target.TAG_SUB_TARGET_REFS, subTargetRefs);
	}

	private void importStresses(Node targetNode, ORef targetRef) throws Exception
	{
		ORefList stressRefs = new ORefList();
		NodeList stressNodes = getNodes(targetNode, STRESSES, STRESS);
		for (int nodeIndex = 0; nodeIndex < stressNodes.getLength(); ++nodeIndex)
		{
			ORef stressRef = getProject().createObject(Stress.getObjectType());
			Node stressNode = stressNodes.item(nodeIndex);
			
			importField(stressNode, NAME, stressRef, Stress.TAG_LABEL); 
			importCodeField(stressNode, STRESS_SEVERITY, stressRef, Stress.TAG_SEVERITY, getCodeMapHelper().getConProToMiradiRatingMap());
			importCodeField(stressNode, STRESS_SCOPE, stressRef, Stress.TAG_SCOPE, getCodeMapHelper().getConProToMiradiRatingMap());
			stressRefs.add(stressRef);
			setData(targetRef, Target.TAG_STRESS_REFS, stressRefs);
			
			createThreatStressRatings(targetRef, stressRef);
			populateThreatStressRatings(stressNode, targetRef, stressRef);
		}
	}
	
	private void createThreatStressRatings(ORef targetRef, ORef stressRef) throws Exception
	{
		ThreatTargetChainObject threatTargetChainObject = new ThreatTargetChainObject(getProject());
		Target target = Target.find(getProject(), targetRef);
		ORefSet upstreatThreatRefs = threatTargetChainObject.getUpstreamThreatRefsFromTarget(target);
		for(ORef threatRef : upstreatThreatRefs)
		{
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
			getProject().createObject(ThreatStressRating.getObjectType(), extraInfo);
		}
	}

	private void populateThreatStressRatings(Node stressNode, ORef targetRef, ORef stressRef) throws Exception
	{
		ThreatTargetVirtualLink threatTargetVirtualLink = new ThreatTargetVirtualLink(getProject());
		NodeList threatStressRatingNodes = getNodes(stressNode, THREAT_STRESS_RATINGS, THREAT_STRESS_RATING);
		for (int nodeIndex = 0; nodeIndex < threatStressRatingNodes.getLength(); ++nodeIndex)
		{
			Node threatStressRatingNode = threatStressRatingNodes.item(nodeIndex);
			
			ORef threatRef = getNodeAsRef(threatStressRatingNode, THREAT_ID, Cause.getObjectType());			
			ORef threatStressRatingRef = threatTargetVirtualLink.findThreatStressRatingReferringToStress(threatRef, targetRef, stressRef);
			
			importCodeField(threatStressRatingNode, CONTRIBUTING_RANK, threatStressRatingRef, ThreatStressRating.TAG_CONTRIBUTION, getCodeMapHelper().getConProToMiradiRatingMap());
			importCodeField(threatStressRatingNode, IRREVERSIBILITY_RANK, threatStressRatingRef, ThreatStressRating.TAG_IRREVERSIBILITY, getCodeMapHelper().getConProToMiradiRatingMap());
			
			setData(threatStressRatingRef, ThreatStressRating.TAG_IS_ACTIVE, BooleanData.BOOLEAN_TRUE);
		}		
	}

	private CodeList extractEcoregions(String[] allEcoregionCodes, Class questionClass)
	{
		ChoiceQuestion question = getProject().getQuestion(questionClass);
		CodeList ecoregionCodes = new CodeList();
		for (int i= 0; i < allEcoregionCodes.length; ++i)
		{
			ChoiceItem choiceItem = question.findChoiceByCode(allEcoregionCodes[i]);
			if (choiceItem != null)
				ecoregionCodes.add(allEcoregionCodes[i]);
		}
		
		return ecoregionCodes;
	}

	private void importCodeListField(String path, ORef objectRef, String tag) throws Exception
	{
		CodeList codes = new CodeList(extractNodesAsList(path));
		setData(objectRef, tag, codes.toString());
	}

	private void importThreatRatingValueInMode(Node projectSummaryNode, ORef metadataRef) throws Exception
	{
		String stresslessThreatRatingValue = getPathData(projectSummaryNode, new String[]{STRESSLESS_THREAT_RANK, });
		String threatRatingModeCode = getThreatRatingMode(stresslessThreatRatingValue);
		setData(metadataRef, ProjectMetadata.TAG_THREAT_RATING_MODE, threatRatingModeCode);
	}

	private String getThreatRatingMode(String stresslessThreatRating)
	{
		if (stresslessThreatRating.length() > 0)
			return ThreatRatingModeChoiceQuestion.SIMPLE_BASED_CODE;
		
		return ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE;
	}
	
	private void importThreatRatingCommentsField(Node node, String path, ThreatRatingCommentsData threatRatingCommentsData, String threatTargetKey) throws Exception
	{
		String threatRatingComment = getPathData(node, new String[]{path,});
		
		StringMap threatRatingCommentsMap = threatRatingCommentsData.getThreatRatingCommentsMap();
		threatRatingCommentsMap.add(threatTargetKey, threatRatingComment);
		String threatRatingCommentsTag = threatRatingCommentsData.getThreatRatingCommentsMapTag();
		importField(threatRatingCommentsData.getRef(), threatRatingCommentsTag, threatRatingCommentsMap.toString());
	}
	
	private void importField(Node node, String path, ORef ref, String tag) throws Exception
	{
		importField(node, new String[]{path,}, ref, tag);
	}
	
	private void importField(Node node, String[] elements, ORef ref, String tag) throws Exception 
	{
		String data = getPathData(node, elements);
		importField(ref, tag, data);
	}

	private String getPathData(Node node, String[] elements) throws XPathExpressionException
	{
		String generatedPath = generatePath(elements);
		return getXPath().evaluate(generatedPath, node);
	}

	private void importField(ORef ref, String tag, String data)	throws Exception
	{
		setData(ref, tag, data);
	}
		
	private void importCodeField(Node node, String element, ORef ref, String tag, HashMap<String, String> map) throws Exception
	{
		importCodeField(node, new String[]{element}, ref, tag, map);
	}
	
	private void importCodeField(Node node, String[] elements, ORef ref, String tag, HashMap<String, String> map) throws Exception
	{
		String rawCode = getPathData(node, elements);
		importCodeField(ref, tag, map, rawCode);
	}

	private void importCodeField(ORef ref, String tag, HashMap<String, String> map, String rawCode) throws Exception
	{
		String safeCode = getCodeMapHelper().getSafeXmlCode(map, rawCode);
		importField(ref, tag, safeCode);
	}
	
	private void importCodeListField(Node node, String parentElement, String childElement, ORef ref, String tag, HashMap<String, String> conProToMiradiCodeMap) throws Exception
	{
		CodeList codes = new CodeList();
		NodeList nodes = getNodes(node, new String[]{parentElement, childElement});
		for (int nodeIndex = 0; nodeIndex < nodes.getLength(); ++nodeIndex)
		{
			Node thisNode = nodes.item(nodeIndex);
			String conProCode = thisNode.getTextContent();
			String miradiCode = conProToMiradiCodeMap.get(conProCode);
			if (miradiCode != null)
				codes.add(miradiCode);
		}
		
		setData(ref, tag, codes.toString());
	}
	
	private void setIdListFromRefListData(ORef ref, String tag, ORefList refList, int type) throws Exception
	{
		setData(ref, tag, refList.convertToIdList(type).toString());
	}
	
	private void setData(ORef ref, String tag, String data) throws Exception
	{
		getProject().setObjectData(ref, tag, data.trim());
	}
	
	private void setData(ORef ref, String tag, ORefList refList) throws Exception
	{
		getProject().setObjectData(ref, tag, refList.toString());
	}

	public String generatePath(String[] pathElements)
	{
		String path = "";
		for (int index = 0; index < pathElements.length; ++index)
		{
			path += getPrefixedElement(pathElements[index]);
			if ((index + 1) < pathElements.length)
				path += "/";
		}
		return path;
	}
	
	public String[] extractNodesAsList(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		NodeList nodeList = (NodeList) expression.evaluate(getDocument(), XPathConstants.NODESET);
		Vector<String> nodes = new Vector();
		for (int nodeIndex = 0; nodeIndex < nodeList.getLength(); ++nodeIndex) 
		{
			nodes.add(nodeList.item(nodeIndex).getTextContent());
		}
		
		return nodes.toArray(new String[0]);
	}
	
	private String getAttributeValue(Node elementNode, String attributeName)
	{
		NamedNodeMap attributes = elementNode.getAttributes();
		Node attributeNode = attributes.getNamedItem(attributeName);
		return attributeNode.getNodeValue();
	}
	
	private ORef getNodeAsRef(Node node, String element, int type) throws Exception
	{
		String idAsString = getNodeContent(node, element);
		return new ORef(type, new BaseId(idAsString));
	}
	
	private Node getNode(Node node, String element) throws Exception
	{
		String path = generatePath(new String[]{element});
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(node, XPathConstants.NODE);
	}
	
	private Node getRootNode() throws Exception
	{
		return getNode(generatePath(new String[]{CONSERVATION_PROJECT}));
	}
	
	private Node getNode(String path) throws Exception
	{
		XPathExpression expression = getXPath().compile(path);
		return (Node) expression.evaluate(getDocument(), XPathConstants.NODE);
	}
	
	private String getNodeContent(Node node, String element) throws Exception
	{
		Node foundNode = getNode(node, element);
		return getSafeNodeContent(foundNode);
	}

	private String getSafeNodeContent(Node foundNode)
	{
		if (foundNode == null)
			return "";
		
		return foundNode.getTextContent();
	}
	
	private NodeList getNodes(Node node, String[] pathElements) throws Exception
	{
		String path = generatePath(pathElements);
		XPathExpression expression = getXPath().compile(path);
		
		return (NodeList) expression.evaluate(node, XPathConstants.NODESET);
	}
	
	private NodeList getNodes(Node node, String containerName, String contentName) throws Exception
	{
		return getNodes(node, new String[]{containerName, contentName});
	}
	
	private String getPrefixedElement(String elementName)
	{
		return PREFIX + elementName;
	}
	
	private Project getProject()
	{
		return project;
	}
	
	public Document getDocument()
	{
		return document;
	}

	public XPath getXPath()
	{
		return xPath;
	}
	
	private ConProMiradiCodeMapHelperVersion2 getCodeMapHelper()
	{
		return codeMapHelper;
	}
	
	private void createDiagramFactorAndAddToDiagram(ORef factorRef) throws Exception
	{
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(factorRef);
		ORef diagramFactorRef = getProject().createObject(DiagramFactor.getObjectType(), extraInfo);
		wrappedToDiagramMap.put(factorRef, diagramFactorRef);
		appendRefToDiagramObject(DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorRef);
	}
	
	private ORef createFactorLinkAndAddToDiagram(ORef fromRef, ORef toRef) throws Exception
	{
		ORef foundFactorLinkRef = getExistingLink(fromRef, toRef);
		if (!foundFactorLinkRef.isInvalid())
			return foundFactorLinkRef;
		
		CreateFactorLinkParameter extraInfo = new CreateFactorLinkParameter(fromRef, toRef);
		ORef createdFactorLinkRef = getProject().createObject(FactorLink.getObjectType(), extraInfo);
		
		ORef fromDiagramFactorRef = wrappedToDiagramMap.get(fromRef);
		ORef toDiagramFactorRef = wrappedToDiagramMap.get(toRef);
			
		CreateDiagramFactorLinkParameter diagramLinkExtraInfo = new CreateDiagramFactorLinkParameter(createdFactorLinkRef, fromDiagramFactorRef, toDiagramFactorRef);
		ORef diagramLinkRef = getProject().createObject(DiagramLink.getObjectType(), diagramLinkExtraInfo);
		wrappedToDiagramMap.put(createdFactorLinkRef, diagramLinkRef);
		appendRefToDiagramObject(DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkRef);
		
		return createdFactorLinkRef;
	}
	
	private ORef getExistingLink(ORef fromRef, ORef toRef)
	{
		Factor fromFactor = Factor.findFactor(getProject(), fromRef);
		Factor toFactor = Factor.findFactor(getProject(), toRef);
		return getProject().getFactorLinkPool().getLinkedRef(fromFactor, toFactor);
	}

	private void appendRefToDiagramObject(String tag, ORef refToAdd) throws Exception
	{
		ORef conceptualModelRef = getFirstAndOnlyDiagramObjectRef();

		IdList idList = new IdList(refToAdd.getObjectType(), getProject().getObjectData(conceptualModelRef, tag));
		idList.add(refToAdd.getObjectId());
		
		setData(conceptualModelRef, tag, idList.toString());
	}

	private ORef getFirstAndOnlyDiagramObjectRef()
	{
		ORefList conceptualModelRefs = getProject().getConceptualModelDiagramPool().getRefList();
		ORef conceptualModelRef = conceptualModelRefs.get(0);
		return conceptualModelRef;
	}
	
	private boolean isUnsupportedNewVersion(String nameSpaceUri) throws Exception
	{
		return getSchemaVersionToImport(nameSpaceUri).compareTo(NAME_SPACE_VERSION) == 0;
	}
	
	private boolean isUnsupportedOldVersion(String nameSpaceUri) throws Exception
	{
		return getSchemaVersionToImport(nameSpaceUri).compareTo(NAME_SPACE_VERSION) < 0;
	}

	private boolean isSameNameSpace(String nameSpaceUri) throws Exception
	{
		return nameSpaceUri.startsWith(PARTIAL_NAME_SPACE);
	}
	
	private String getSchemaVersionToImport(String nameSpaceUri) throws Exception
	{
		int lastSlashIndexBeforeVersion = nameSpaceUri.lastIndexOf("/");
		String versionAsString = nameSpaceUri.substring(lastSlashIndexBeforeVersion + 1, nameSpaceUri.length());
		
		return versionAsString;
	}
	
	private void setDiagramFactorDefaultLocations() throws Exception
	{
		ORefList nonDraftStrategyRefs = new ORefList(getProject().getStrategyPool().getNonDraftStrategies());
		setDiagramFactorLocation(nonDraftStrategyRefs, NON_DRAFT_STRATEGY_X_COLUMN);
		
		ORefList draftStrategyRefs = new ORefList(getProject().getStrategyPool().getDraftAndNonDraftStrategies());
		setDiagramFactorLocation(draftStrategyRefs, DRAFT_STRATEGY_X_COLUMN);
		
		setDiagramFactorLocation(getProject().getPool(Cause.getObjectType()).getORefList(), CAUSE_X_COLUMN);
		setDiagramFactorLocation(getProject().getPool(Target.getObjectType()).getORefList(), TARGET_X_COLUMN);
	}
			
	private void setDiagramFactorLocation(ORefList factorRefs, int xPosition) throws Exception
	{
		DiagramObject diagramObject = ConceptualModelDiagram.find(getProject(), getFirstAndOnlyDiagramObjectRef());
		int VERTICAL_SPACE_INBETWEEN = 90;
		for (int index = 0; index < factorRefs.size(); ++index)
		{
			int rowIndex = (index + 1);
			int y = rowIndex * VERTICAL_SPACE_INBETWEEN;
			Point location = new Point(xPosition, y);
			
			DiagramFactor diagramFactor = diagramObject.getDiagramFactor(factorRefs.get(index));
			CommandSetObjectData setLocation = new CommandSetObjectData(diagramFactor.getRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(location));
			getProject().executeCommand(setLocation);
		}
	}
	
	private void createDefaultObjects() throws Exception
	{
		CommandCreateObject createResultsChain = new CommandCreateObject(ResultsChainDiagram.getObjectType());
		getProject().executeCommand(createResultsChain);
		
		String DEFAUL_RESULTS_CHAIN_LABEL = EAM.text("Placeholder - From ConPro");
		CommandSetObjectData setResultsChainLabel = new CommandSetObjectData(createResultsChain.getObjectRef(), ResultsChainDiagram.TAG_LABEL, DEFAUL_RESULTS_CHAIN_LABEL);
		getProject().executeCommand(setResultsChainLabel);
		
		final String OBJECTIVE_CONTAINER_LABEL = EAM.text("Objective Holder");
		final Point OBJECTIVE_CONTAINER_LOCATION = new Point(30, 30);
		ORef objectiveHolderDiagramFactoRef = createIntermediateResultAsContainer(OBJECTIVE_CONTAINER_LABEL, OBJECTIVE_CONTAINER_LOCATION);
		DiagramFactor objectiveHolderDiagramFactor = DiagramFactor.find(getProject(), objectiveHolderDiagramFactoRef);
		objectiveHolderRef = objectiveHolderDiagramFactor.getWrappedORef();
		
		final String INDICATOR_CONTAINER_LABEL = EAM.text("Indicator Holder");
		final Point INDICATOR_CONTAINER_LOCATION = new Point(30, 100);
		ORef indicatorHolderDiagramFactorRef = createIntermediateResultAsContainer(INDICATOR_CONTAINER_LABEL, INDICATOR_CONTAINER_LOCATION);
		DiagramFactor indicatorHolderDiagramFactor = DiagramFactor.find(getProject(), indicatorHolderDiagramFactorRef);
		indicatorHolderRef = indicatorHolderDiagramFactor.getWrappedORef();
		
		IdList idList = new IdList(DiagramFactor.getObjectType());
		idList.addRef(objectiveHolderDiagramFactoRef);
		idList.addRef(indicatorHolderDiagramFactorRef);
		CommandSetObjectData addDiagramFactor = new CommandSetObjectData(createResultsChain.getObjectRef(), ResultsChainDiagram.TAG_DIAGRAM_FACTOR_IDS, idList.toString());
		getProject().executeCommand(addDiagramFactor);
	}

	private ORef createIntermediateResultAsContainer(String label, Point location)	throws CommandFailedException
	{
		CommandCreateObject createIntermediateResults = new CommandCreateObject(IntermediateResult.getObjectType());
		getProject().executeCommand(createIntermediateResults);
		
		CommandSetObjectData setName = new CommandSetObjectData(createIntermediateResults.getObjectRef(), IntermediateResult.TAG_LABEL, label);
		getProject().executeCommand(setName);
			
		CreateDiagramFactorParameter extraInfo = new CreateDiagramFactorParameter(createIntermediateResults.getObjectRef());
		CommandCreateObject createDiagramFactor = new CommandCreateObject(DiagramFactor.getObjectType(), extraInfo);
		getProject().executeCommand(createDiagramFactor);
		
		CommandSetObjectData setLocation = new CommandSetObjectData(createDiagramFactor.getObjectRef(), DiagramFactor.TAG_LOCATION, EnhancedJsonObject.convertFromPoint(location));
		getProject().executeCommand(setLocation);
		
		return createDiagramFactor.getObjectRef();
	}
	
	private void attachObjectivesToHolder() throws Exception
	{
		IdList objectiveIds = new IdList(Objective.getObjectType());
		NodeList objectiveNodeList = getNodes(getRootNode(), OBJECTIVES, OBJECTIVE);
		for (int nodeIndex = 0; nodeIndex < objectiveNodeList.getLength(); ++nodeIndex) 
		{
			Node objectiveNode = objectiveNodeList.item(nodeIndex);
			String objectiveId = getAttributeValue(objectiveNode, ID);
			objectiveIds.add(new BaseId(objectiveId));
		}
		
		CommandSetObjectData setIndicatorIds = new CommandSetObjectData(getObjectiveHolderRef(), IntermediateResult.TAG_OBJECTIVE_IDS, objectiveIds.toString());
		getProject().executeCommand(setIndicatorIds);	
	}
	
	private void attachIndicatorsToHolder() throws Exception
	{
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		NodeList indicatorNodeList = getNodes(getRootNode(), INDICATORS, INDICATOR);
		for (int nodeIndex = 0; nodeIndex < indicatorNodeList.getLength(); ++nodeIndex) 
		{
			Node indicatorNode = indicatorNodeList.item(nodeIndex);
			String indicatorId = getAttributeValue(indicatorNode, ID);
			ORef indicatorRef = new ORef(Indicator.getObjectType(), new BaseId(indicatorId));
			Indicator indicator = Indicator.find(getProject(), indicatorRef);
			if (!indicator.isViabilityIndicator())
				indicatorIds.addRef(indicatorRef);
		}
		
		CommandSetObjectData setIndicatorIds = new CommandSetObjectData(getIndicatorHolderRef(), IntermediateResult.TAG_INDICATOR_IDS, indicatorIds.toString());
		getProject().executeCommand(setIndicatorIds);
	}
	
	private void updateObjectiveRelevantStrategyList() throws Exception
	{
		NodeList strategyNodeList = getNodes(getRootNode(), STRATEGIES, STRATEGY);
		for (int nodeIndex = 0; nodeIndex < strategyNodeList.getLength(); ++nodeIndex) 
		{
			Node strategyNode = strategyNodeList.item(nodeIndex);
			String strategyIdAsString = getAttributeValue(strategyNode, ID);
			BaseId strategyId = new BaseId(strategyIdAsString);
		
			NodeList objectiveNodeList = getNodes(strategyNode, OBJECTIVES, OBJECTIVE_ID);
			updateObjectiveRelevancyList(new ORef(Strategy.getObjectType(), strategyId), objectiveNodeList);
		}
	}
	
	private void updateObjectiveRelevancyList(ORef strategyRef, NodeList objectiveNodeList) throws Exception
	{
		for (int nodeIndex = 0; nodeIndex < objectiveNodeList.getLength(); ++nodeIndex) 
		{
			Node objectiveNode = objectiveNodeList.item(nodeIndex);
			String objectiveId = objectiveNode.getTextContent();
			ORef objectiveRef = new ORef(Objective.getObjectType(), new BaseId(objectiveId));
			
			RelevancyOverrideSet relevancyOverrideSet = new RelevancyOverrideSet();
			Objective objective = Objective.find(getProject(), objectiveRef);
			relevancyOverrideSet.addAll(objective.getStrategyActivityRelevancyOverrideSet());
			relevancyOverrideSet.add(new RelevancyOverride(strategyRef, true));
			CommandSetObjectData addStrategy = new CommandSetObjectData(objectiveRef, Objective.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancyOverrideSet.toString());
			getProject().executeCommand(addStrategy);
		}
	}
	
	private void createTaggedThreatChains() throws Exception
	{
		highOrAboveRankedThreatsTag = createLabeledTag(EAM.text("High/Very-High Threat Chains"));
		ORef mediumOrBelowRankedThreatsTag = createLabeledTag(EAM.text("Low/Medium/Unrated Threat Chains"));
		
		CodeList highOrAboveRatingCodes = createHighAndAboveThreatRatingCodeList();
		addThreatChainsToTag(highOrAboveRatingCodes, highOrAboveRankedThreatsTag, mediumOrBelowRankedThreatsTag);
		addHighOrAboveTagToConceptualModel(new ORefList(highOrAboveRankedThreatsTag));
	}

	private void addThreatChainsToTag(CodeList highOrAboveCodes, ORef highOrAboveRankedThreatsTagToUse, ORef mediumOrBelowRankedThreatsTag) throws Exception
	{
		ORefSet mediumOrBelowRankedThreatRefs = new ORefSet();
		ORefSet highOrAboveRankedThreatRefs = new ORefSet();
		
		ProjectChainObject chainObject = new ProjectChainObject();
		Cause[] threats = getProject().getCausePool().getDirectThreats();
		for (int index = 0; index < threats.length; ++index)
		{
			Cause threat = threats[index];
			ChoiceItem threatRatingChoice = getProject().getThreatRatingFramework().getThreatThreatRatingValue(threat.getRef());			
			ORefSet factorRefsInChain = chainObject.buildNormalChainAndGetFactorRefs(threat);
			if (threatRatingChoice != null && highOrAboveCodes.contains(threatRatingChoice.getCode()))
				highOrAboveRankedThreatRefs.addAll(factorRefsInChain);
			else
				mediumOrBelowRankedThreatRefs.addAll(factorRefsInChain);
		}
		
		ORefSet allLeftOverStrategyTargetRefs = getAllNonChainedStrategyAndTargets(mediumOrBelowRankedThreatRefs, highOrAboveRankedThreatRefs);
		highOrAboveRankedThreatRefs.addAll(allLeftOverStrategyTargetRefs);
		mediumOrBelowRankedThreatRefs.addAll(allLeftOverStrategyTargetRefs);
		
		tagFactors(highOrAboveRankedThreatsTagToUse, highOrAboveRankedThreatRefs);
		tagFactors(mediumOrBelowRankedThreatsTag, mediumOrBelowRankedThreatRefs);
	}

	private ORefSet getAllNonChainedStrategyAndTargets(ORefSet mediumOrBelowRankedThreatRefs, ORefSet highOrAboveRankedThreatRefs)
	{
		ORefList targetRefs = getProject().getTargetPool().getORefList();
		ORefList strategyRefs = getProject().getStrategyPool().getORefList();
		ORefSet allLeftOverStrategyTargetRefs = new ORefSet();
		allLeftOverStrategyTargetRefs.addAllRefs(targetRefs);
		allLeftOverStrategyTargetRefs.addAllRefs(strategyRefs);
		
		ORefSet allChainedRefs = new ORefSet();
		allChainedRefs.addAll(highOrAboveRankedThreatRefs);
		allChainedRefs.addAll(mediumOrBelowRankedThreatRefs);
		
		allLeftOverStrategyTargetRefs.removeAll(allChainedRefs);
		
		return allLeftOverStrategyTargetRefs;
	}

	private void tagFactors(ORef tagRef, ORefSet factorRefs) throws CommandFailedException
	{
		CommandSetObjectData tagFactors = new CommandSetObjectData(tagRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, factorRefs.toRefList().toString());
		getProject().executeCommand(tagFactors);
	}
	
	private void addHighOrAboveTagToConceptualModel(ORefList tagRefs) throws Exception
	{
		CommandSetObjectData addTags = new CommandSetObjectData(getFirstAndOnlyDiagramObjectRef(), ConceptualModelDiagram.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, tagRefs.toString());
		getProject().executeCommand(addTags);
	}

	private CodeList createHighAndAboveThreatRatingCodeList()
	{
		return new CodeList(new String[]{ThreatRatingQuestion.HIGH_RATING_CODE, ThreatRatingQuestion.VERY_HIGH_RATING_CODE});
	}
	
	private ORef createLabeledTag(String tagLabel) throws CommandFailedException
	{
		CommandCreateObject createCommand = new CommandCreateObject(TaggedObjectSet.getObjectType());
		getProject().executeCommand(createCommand);
		
		ORef tagRef = createCommand.getObjectRef();
		CommandSetObjectData setLabel = new CommandSetObjectData(tagRef, TaggedObjectSet.TAG_LABEL, tagLabel);
		getProject().executeCommand(setLabel);
		
		return tagRef;
	}
		
	private ORef getObjectiveHolderRef()
	{
		return objectiveHolderRef;
	}
	
	private ORef getIndicatorHolderRef()
	{
		return indicatorHolderRef;
	}
	
	public ORef getHighOrAboveRankedThreatsTag()
	{
		return highOrAboveRankedThreatsTag;
	}

	private Project project;
	private XPath xPath;
	private Document document;
	private ConProMiradiCodeMapHelperVersion2 codeMapHelper;
	private HashMap<ORef, ORef> wrappedToDiagramMap;
	private ORef objectiveHolderRef;
	private ORef indicatorHolderRef;
	private ORef highOrAboveRankedThreatsTag;
	
	public static final String PREFIX = "cp:";
	
	private static final int NON_DRAFT_STRATEGY_X_COLUMN = 30;
	private static final int DRAFT_STRATEGY_X_COLUMN = 270;
	private static final int CAUSE_X_COLUMN = 510;
	private static final int TARGET_X_COLUMN = 750;
}
