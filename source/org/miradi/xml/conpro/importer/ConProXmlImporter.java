/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import javax.xml.xpath.XPathFactory;

import org.martus.util.MultiCalendar;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateThreatStressRatingParameter;
import org.miradi.objecthelpers.FactorLinkSet;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverride;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objecthelpers.StringRefMap;
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
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Stress;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.ValueOption;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.project.SimpleThreatRatingFramework;
import org.miradi.project.ThreatRatingBundle;
import org.miradi.questions.BudgetCostModeQuestion;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TncFreshwaterEcoRegionQuestion;
import org.miradi.questions.TncMarineEcoRegionQuestion;
import org.miradi.questions.TncTerrestrialEcoRegionQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.xml.conpro.ConProMiradiCodeMapHelper;
import org.miradi.xml.conpro.ConProMiradiXml;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


public class ConProXmlImporter implements ConProMiradiXml
{
	public ConProXmlImporter(Project projectToFill) throws Exception
	{
		project = projectToFill;
		codeMapHelper = new ConProMiradiCodeMapHelper();

		wrappedToDiagramMap = new HashMap<ORef, ORef>();
	}
	
	public void importConProProject(InputSource inputSource) throws Exception
	{
		document = createDocument(inputSource);
		xPath = createXPath();
				
		if (!isSameNameSpace())
		{
			throw new Exception("Name space mismatch should be: " + PARTIAL_NAME_SPACE + " <br> however it is: " + getNameSpaceUrl()); 
		}
				
		if (isUnsupportedNewVersion())
		{
			throw new UnsupportedNewVersionSchemaException();
		}
		
		importXml(inputSource);
	}

	private void importXml(InputSource inputSource) throws Exception
	{
		importProjectSummaryElement();
		
		importStrategies();
		importThreats();
		importTargets();
		
		importKeyEcologicalAttributes();
		importIndicators();
		importObjectives();
		
		importViability();
		
		setDiagramFactorDefaultLocations();
		
		createDefaultObjects();
		attachObjectivesToHolder();
		attachIndicatorsToHolder();
		updateObjectiveRelevantStrategyList();
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
			importCodeField(strategyNode, LEVERAGE, strategyRef, Strategy.TAG_IMPACT_RATING, getCodeMapHelper().getConProToMiradiRatingMap());
			importCodeField(strategyNode, FEASABILITY, strategyRef, Strategy.TAG_FEASIBILITY_RATING, getCodeMapHelper().getConProToMiradiRatingMap());
			
			importStrategyStatus(strategyNode, strategyRef);
			importField(strategyNode, COMMENT, strategyRef, Strategy.TAG_COMMENT);
			importField(strategyNode, LEGACY_TNC_STRATEGY_RATING, strategyRef, Strategy.TAG_LEGACY_TNC_STRATEGY_RANKING);
			importActivities(strategyNode, strategyRef);
			
			createDiagramFactorAndAddToDiagram(strategyRef);
		}
	}

	private void importStrategyStatus(Node strategyNode, ORef strategyRef) throws Exception
	{
		String data = getNodeContent(strategyNode, SELECTED);
		boolean isNonDraft = Boolean.parseBoolean(data);
		String draftStatusValue = Strategy.STATUS_DRAFT;
		if (isNonDraft)
			draftStatusValue = Strategy.STATUS_REAL;
		
		setData(strategyRef, Strategy.TAG_STATUS, draftStatusValue);
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
			setData(activityRef, Task.TAG_BUDGET_COST_MODE, BudgetCostModeQuestion.OVERRIDE_MODE_CODE);
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
		}
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

	private void importIndicators() throws Exception
	{
		NodeList indicatorNodeList = getNodes(getRootNode(), INDICATORS, INDICATOR);
		for (int nodeIndex = 0; nodeIndex < indicatorNodeList.getLength(); ++nodeIndex) 
		{
			Node indicatorNode = indicatorNodeList.item(nodeIndex);
			String indicatorId = getAttributeValue(indicatorNode, ID);
			ORef indicatorRef = getProject().createObject(Indicator.getObjectType(), new BaseId(indicatorId));
			
			importField(indicatorNode, NAME, indicatorRef, Indicator.TAG_LABEL);
			importMethods(indicatorNode, indicatorRef);
			importCodeField(indicatorNode, PRIORITY, indicatorRef, Indicator.TAG_PRIORITY, getCodeMapHelper().getConProToMiradiRatingMap());
			importProgressReport(indicatorNode, indicatorRef);
			importField(indicatorNode, WHO_MONITORS, indicatorRef, Indicator.TAG_DETAIL);
			importBudgetData(indicatorNode, indicatorRef);
			importField(indicatorNode, COMMENT, indicatorRef, Indicator.TAG_COMMENT);
		}
	}
	
	private void importBudgetData(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		importField(indicatorNode, ANNUAL_COST, indicatorRef, Indicator.TAG_BUDGET_COST_OVERRIDE);
		setData(indicatorRef, Indicator.TAG_BUDGET_COST_MODE, BudgetCostModeQuestion.OVERRIDE_MODE_CODE);	
	}

	private void importProgressReport(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		Node progressStatusNode = getNode(indicatorNode, STATUS);
		if (progressStatusNode == null)
			return;
		
		ORef progressReportRef = getProject().createObject(ProgressReport.getObjectType());
		ORefList progressReportRefs = new ORefList(progressReportRef);
		importCodeField(indicatorNode, STATUS, progressReportRef, ProgressReport.TAG_PROGRESS_STATUS, getCodeMapHelper().getConProToMiradiProgressStatusMap());
		setData(progressReportRef, ProgressReport.TAG_PROGRESS_DATE, getProject().getMetadata().getEffectiveDate());
		setData(indicatorRef, Indicator.TAG_PROGRESS_REPORT_REFS, progressReportRefs);
	}

	private void importMethods(Node indicatorNode, ORef indicatorRef) throws Exception
	{
		String methodString = getNodeContent(indicatorNode, METHODS);
		ORef methodRef = getProject().createObject(Task.getObjectType());
		setData(methodRef, Task.TAG_LABEL, SEE_DETAILS_FIELD_METHOD_NAME);
		setData(methodRef, Task.TAG_DETAILS, methodString);
		
		ORefList methodRefs = new ORefList(methodRef);
		setIdListFromRefListData(indicatorRef, Indicator.TAG_TASK_IDS, methodRefs, Task.getObjectType());
	}

	private void importThreats() throws Exception
	{
		NodeList threatNodeList = getNodes(getRootNode(), THREATS, THREAT);
		for (int nodeIndex = 0; nodeIndex < threatNodeList.getLength(); ++nodeIndex) 
		{
			Node threatNode = threatNodeList.item(nodeIndex);
			String threatId = getAttributeValue(threatNode, ID);
			ORef threatRef = getProject().createObject(Cause.getObjectType(), new BaseId(threatId));
			
			importField(threatNode, NAME, threatRef, Cause.TAG_LABEL);
			importField(threatNode, THREAT_TAXONOMY_CODE, threatRef, Cause.TAG_TAXONOMY_CODE);
			
			createDiagramFactorAndAddToDiagram(threatRef);
		}
	}

	private void importViability() throws Exception
	{
		NodeList keaNodeList = getNodes(getRootNode(), VIABILITY, VIABILITY_ASSESSMENT);
		for (int nodeIndex = 0; nodeIndex < keaNodeList.getLength(); ++nodeIndex) 
		{
			Node viabilityAssessmentNode = keaNodeList.item(nodeIndex);
			
			ORef targetRef = getNodeAsRef(viabilityAssessmentNode, TARGET_ID, Target.getObjectType());
			setData(targetRef, Target.TAG_VIABILITY_MODE, ViabilityModeQuestion.TNC_STYLE_CODE);
			
			ORef keaRef = getNodeAsRef(viabilityAssessmentNode, KEA_ID, KeyEcologicalAttribute.getObjectType());
			IdList keaIds = new IdList(KeyEcologicalAttribute.getObjectType(), new BaseId[]{keaRef.getObjectId()});
			setData(targetRef, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keaIds.toString());
			
			ORef indicatorRef = getNodeAsRef(viabilityAssessmentNode, INDICATOR_ID, Indicator.getObjectType());
			
			IdList allKeaIndicatorIds = new IdList(Indicator.getObjectType(), new BaseId[]{indicatorRef.getObjectId()});
			KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getProject(), keaRef);
			IdList currentKeaIndicators = kea.getIndicatorIds();
			allKeaIndicatorIds.addAll(currentKeaIndicators);
			setData(keaRef, KeyEcologicalAttribute.TAG_INDICATOR_IDS, allKeaIndicatorIds.toString());
			
			importIndicatorThresholds(viabilityAssessmentNode, indicatorRef);		
			importField(viabilityAssessmentNode, CURRENT_INDICATOR_STATUS_VIABILITY, indicatorRef, Indicator.TAG_FUTURE_STATUS_RATING);
			
			importCodeField(viabilityAssessmentNode, DESIRED_VIABILITY_RATING, indicatorRef, Indicator.TAG_FUTURE_STATUS_RATING, getCodeMapHelper().getConProToMiradiRankingMap());
			importMeasurementData(viabilityAssessmentNode, indicatorRef);
			importField(viabilityAssessmentNode, DESIRED_RATING_DATE, indicatorRef, Indicator.TAG_FUTURE_STATUS_DATE);
			importField(viabilityAssessmentNode, KEA_AND_INDICATOR_COMMENT, indicatorRef, Indicator.TAG_DETAIL);
			importField(viabilityAssessmentNode, INDICATOR_RATING_COMMENT, indicatorRef, Indicator.TAG_VIABILITY_RATINGS_COMMENT);
			importField(viabilityAssessmentNode, DESIRED_RATING_COMMENT, indicatorRef, Indicator.TAG_FUTURE_STATUS_COMMENT);
			importField(viabilityAssessmentNode, VIABILITY_RECORD_COMMENT, keaRef, KeyEcologicalAttribute.TAG_DESCRIPTION);
		}
	}

	private void importMeasurementData(Node viabilityAssessmentNode, ORef indicatorRef) throws Exception
	{
		boolean currentViabilityRatingIsEmpty = isEmpty(viabilityAssessmentNode, CURRENT_VIABILITY_RATING);
		boolean currentViabilityRateDateIsEmpty = isEmpty(viabilityAssessmentNode, CURRENT_RATING_DATE);
		boolean currentConfidenceRatingIsEmpty = isEmpty(viabilityAssessmentNode, CONFIDENE_CURRENT_RATING);
		boolean currentRatingCommentIsEmpty = isEmpty(viabilityAssessmentNode, CURRENT_RATING_COMMENT);
		if (currentViabilityRatingIsEmpty && currentViabilityRateDateIsEmpty && currentConfidenceRatingIsEmpty && currentRatingCommentIsEmpty)
			return;
		
		ORef measurementRef = getProject().createObject(Measurement.getObjectType());
		setRefListData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, new ORefList(measurementRef));
		
		importCodeField(viabilityAssessmentNode, CURRENT_VIABILITY_RATING, measurementRef, Measurement.TAG_STATUS, getCodeMapHelper().getConProToMiradiRankingMap());
		importField(viabilityAssessmentNode, CURRENT_RATING_DATE, measurementRef, Measurement.TAG_DATE);
		importCodeField(viabilityAssessmentNode, CONFIDENE_CURRENT_RATING, measurementRef, Measurement.TAG_STATUS_CONFIDENCE, getCodeMapHelper().getConProToMiradiStatusConfidenceMap());
		importField(viabilityAssessmentNode, CURRENT_RATING_COMMENT, measurementRef, Measurement.TAG_COMMENT);		
	}

	private boolean isEmpty(Node node, String element) throws Exception
	{
		return getNodeContent(node, element).length() == 0;
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

	private void importKeyEcologicalAttributes() throws Exception
	{
		NodeList keaNodeList = getNodes(getRootNode(), KEY_ATTRIBUTES, KEY_ATTRIBUTE);
		for (int nodeIndex = 0; nodeIndex < keaNodeList.getLength(); ++nodeIndex) 
		{
			Node keaNode = keaNodeList.item(nodeIndex);
			String keaId = getAttributeValue(keaNode, ID);
			ORef keaRef = getProject().createObject(KeyEcologicalAttribute.getObjectType(), new BaseId(keaId));
			
			importField(keaNode, NAME, keaRef, KeyEcologicalAttribute.TAG_LABEL);
			importCodeField(keaNode, CATEGORY, keaRef, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, getCodeMapHelper().getConProToMiradiKeaTypeMap());			
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
		thisXPath.setNamespaceContext(new ConProMiradiNameSpaceContext());
		
		return thisXPath;
	}

	private void importProjectSummaryElement() throws Exception
	{
		ORef metadataRef = getProject().getMetadata().getRef();
		Node projectSumaryNode = getNode(getRootNode(), PROJECT_SUMMARY);
		
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
		importTeamMembers(projectSumaryNode, metadataRef);
		
		String[] ecoRegionElementHierarchy = new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, ECOREGIONS, ECOREGION_CODE}; 
		String[] allEcoregionCodes = extractNodesAsList(generatePath(ecoRegionElementHierarchy));
		setData(metadataRef, ProjectMetadata.TAG_TNC_TERRESTRIAL_ECO_REGION, extractEcoregions(allEcoregionCodes, TncTerrestrialEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_MARINE_ECO_REGION, extractEcoregions(allEcoregionCodes, TncMarineEcoRegionQuestion.class).toString());
		setData(metadataRef, ProjectMetadata.TAG_TNC_FRESHWATER_ECO_REGION, extractEcoregions(allEcoregionCodes, TncFreshwaterEcoRegionQuestion.class).toString());
		
		importCodeListField(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, COUNTRIES, COUNTRY_CODE}), metadataRef, ProjectMetadata.TAG_COUNTRIES);
		importCodeListField(generatePath(new String[] {CONSERVATION_PROJECT, PROJECT_SUMMARY, OUS, OU_CODE}), metadataRef, ProjectMetadata.TAG_TNC_OPERATING_UNITS);
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
			createFactorLinkAndAddToDiagram(threatRef, targetRef);
		}
	}

	private void importThreatToTargetAssociations(Node targetNode, ORef targetRef) throws Exception
	{
		SimpleThreatRatingFramework framework = getProject().getSimpleThreatRatingFramework();
		NodeList threatTargetAssociations = getNodes(targetNode, THREAT_TARGET_ASSOCIATIONS, THREAT_TARGET_ASSOCIATION);
		for (int nodeIndex = 0; nodeIndex < threatTargetAssociations.getLength(); ++nodeIndex)
		{
			Node threatTargetAssociationNode = threatTargetAssociations.item(nodeIndex);
			ORef threatRef = getNodeAsRef(threatTargetAssociationNode, THREAT_ID, Cause.getObjectType());
			ORef factorLinkRef = createFactorLinkAndAddToDiagram(threatRef, targetRef);
			
			ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
			importThreatRatingField(threatTargetAssociationNode, THREAT_SCOPE, framework, bundle, Stress.TAG_SCOPE);
			importThreatRatingField(threatTargetAssociationNode, THREAT_SEVERITY, framework, bundle, Stress.TAG_SEVERITY);
			importThreatRatingField(threatTargetAssociationNode, THREAT_IRREVERSIBILITY, framework, bundle, ThreatStressRating.TAG_IRREVERSIBILITY);			
			framework.saveBundle(bundle);
			
			importField(threatTargetAssociationNode, THREAT_TARGET_COMMENT, factorLinkRef, FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT);
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
			
			importStressesThreats(stressNode, targetRef, stressRef);
		}
		
		setData(targetRef, Target.TAG_STRESS_REFS, stressRefs);
	}
	

	private void importStressesThreats(Node stressNode, ORef targetRef, ORef stressRef) throws Exception
	{
		NodeList threatStressRatingNodes = getNodes(stressNode, THREAT_STRESS_RATINGS, THREAT_STRESS_RATING);
		
		for (int nodeIndex = 0; nodeIndex < threatStressRatingNodes.getLength(); ++nodeIndex)
		{
			Node threatStressRatingNode = threatStressRatingNodes.item(nodeIndex);
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef);
			ORef threatStressRatingRef = getProject().createObject(ThreatStressRating.getObjectType(), extraInfo);
			
			ORef threatRef = getNodeAsRef(threatStressRatingNode, THREAT_ID, Cause.getObjectType());
			updateThreatStressRatingRefs(targetRef, threatRef, threatStressRatingRef);	
			
			importCodeField(threatStressRatingNode, CONTRIBUTING_RANK, threatStressRatingRef, ThreatStressRating.TAG_CONTRIBUTION, getCodeMapHelper().getConProToMiradiRatingMap());
			importCodeField(threatStressRatingNode, IRREVERSIBILITY_RANK, threatStressRatingRef, ThreatStressRating.TAG_IRREVERSIBILITY, getCodeMapHelper().getConProToMiradiRatingMap());
		}		
	}

	private void updateThreatStressRatingRefs(ORef targetRef, ORef threatRef, ORef threatStressRatingRef) throws Exception
	{
		FactorLinkSet targetLinks = ConproXmlExporter.getThreatTargetFactorLinks(getProject(), Target.find(getProject(), targetRef));
		for(FactorLink factorLink : targetLinks)
		{
			if (factorLink.getUpstreamThreatRef().equals(threatRef))
			{
				ORefList threatStressRatingRefs = factorLink.getThreatStressRatingRefs();
				threatStressRatingRefs.add(threatStressRatingRef);
				setData(factorLink.getRef(), FactorLink.TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs);
			}
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

	private void importField(Node node, String path, ORef ref, String tag) throws Exception
	{
		importField(node, new String[]{path,}, ref, tag);
	}
	
	private void importField(Node node, String[] elements, ORef ref, String tag) throws Exception 
	{
		String generatedPath = generatePath(elements);
		String data = getXPath().evaluate(generatedPath, node);
		setData(ref, tag, data);
	}
	
	private void importCodeField(Node node, String element, ORef ref, String tag, HashMap<String, String> map) throws Exception
	{
		importCodeField(node, new String[]{element}, ref, tag, map);
	}
	
	private void importCodeField(Node node, String[] elements, ORef ref, String tag, HashMap<String, String> map) throws Exception
	{
		String generatedPath = generatePath(elements);
		String rawCode = getXPath().evaluate(generatedPath, node);
		importCodeField(ref, tag, map, rawCode);
	}

	private void importCodeField(ORef ref, String tag, HashMap<String, String> map, String rawCode) throws Exception
	{
		String safeCode = getCodeMapHelper().getSafeXmlCode(map, rawCode);
		setData(ref, tag, safeCode);
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
	
	private void setRefListData(ORef ref, String tag, ORefList refList) throws Exception
	{
		setData(ref, tag, refList.toString());
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
	
	private ConProMiradiCodeMapHelper getCodeMapHelper()
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
	
	private boolean isUnsupportedNewVersion() throws Exception
	{
		return getSchemaVersionToImport() > NAME_SPACE_VERSION;
	}

	private boolean isSameNameSpace() throws Exception
	{
		return getNameSpaceUrl().startsWith(PARTIAL_NAME_SPACE);
	}
	
	private double getSchemaVersionToImport() throws Exception
	{
		String nameSpace = getNameSpaceUrl();
		int lastSlashIndexBeforeVersion = nameSpace.lastIndexOf("/");
		String versionAsString = nameSpace.substring(lastSlashIndexBeforeVersion + 1, nameSpace.length());
		double versionToImport = Double.parseDouble(versionAsString);
		return versionToImport;
	}

	private String getNameSpaceUrl() throws Exception
	{
		Node rootNode = getRootNode();
		String nameSpace = getAttributeValue(rootNode, XMLNS);
		return nameSpace;
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
		
		final String OBJECTIVE_CONTAINER_LABEL = "Objective Holder";
		final Point OBJECTIVE_CONTAINER_LOCATION = new Point(30, 30);
		ORef objectiveHolderDiagramFactoRef = createIntermediateResultAsContainer(OBJECTIVE_CONTAINER_LABEL, OBJECTIVE_CONTAINER_LOCATION);
		DiagramFactor objectiveHolderDiagramFactor = DiagramFactor.find(getProject(), objectiveHolderDiagramFactoRef);
		objectiveHolderRef = objectiveHolderDiagramFactor.getWrappedORef();
		
		final String INDICATOR_CONTAINER_LABEL = "Indicator Holder";
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
			indicatorIds.add(new BaseId(indicatorId));
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
			
			updateObjectiveRelevancyList(strategyNode, new ORef(Strategy.getObjectType(), strategyId));
		}
	}
	
	private void updateObjectiveRelevancyList(Node strategyNode, ORef strategyRef) throws Exception
	{
		NodeList objectiveNodeList = getNodes(strategyNode, OBJECTIVES, OBJECTIVE_ID);
		for (int nodeIndex = 0; nodeIndex < objectiveNodeList.getLength(); ++nodeIndex) 
		{
			Node objectiveNode = objectiveNodeList.item(nodeIndex);
			String objectiveId = objectiveNode.getTextContent();
			ORef objectiveRef = new ORef(Objective.getObjectType(), new BaseId(objectiveId));
			
			RelevancyOverrideSet relevancyOverrideSet = new RelevancyOverrideSet();
			Objective objective = Objective.find(getProject(), objectiveRef);
			relevancyOverrideSet.addAll(objective.getStrategyRelevancyOverrideSet());
			relevancyOverrideSet.add(new RelevancyOverride(strategyRef, true));
			CommandSetObjectData addStrategy = new CommandSetObjectData(objectiveRef, Objective.TAG_RELEVANT_STRATEGY_SET, relevancyOverrideSet.toString());
			getProject().executeCommand(addStrategy);
		}
	}
	
	private ORef getObjectiveHolderRef()
	{
		return objectiveHolderRef;
	}
	
	private ORef getIndicatorHolderRef()
	{
		return indicatorHolderRef;
	}

	private Project project;
	private XPath xPath;
	private Document document;
	private ConProMiradiCodeMapHelper codeMapHelper;
	private HashMap<ORef, ORef> wrappedToDiagramMap;
	private ORef objectiveHolderRef;
	private ORef indicatorHolderRef;
	
	public static final String SEE_DETAILS_FIELD_METHOD_NAME = "See Details field";
	public static final String PREFIX = "cp:";
	
	private static final int NON_DRAFT_STRATEGY_X_COLUMN = 30;
	private static final int DRAFT_STRATEGY_X_COLUMN = 270;
	private static final int CAUSE_X_COLUMN = 510;
	private static final int TARGET_X_COLUMN = 750;
}
