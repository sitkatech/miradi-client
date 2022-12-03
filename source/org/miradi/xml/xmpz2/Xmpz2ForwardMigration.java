/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.xml.xmpz2;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.exceptions.XmlVersionTooOldException;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.migrations.forward.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.*;
import org.miradi.questions.DayColumnsVisibilityQuestion;
import org.miradi.schemas.*;
import org.miradi.utils.BiDirectionalHashMap;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.StringUtilities;
import org.miradi.utils.XmlUtilities2;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter;
import org.w3c.dom.*;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.*;

import static org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter.FIELD_TAG_ESCAPE_TOKEN;
import static org.miradi.xml.xmpz2.objectExporters.ExtraDataExporter.TYPE_ID_TAG_SPLIT_TOKEN;

public class Xmpz2ForwardMigration
{
	public Xmpz2MigrationResult migrate(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		// note that this xmpz2 migration only handles structural migrations required to ensure that the resulting xml validates according to the current schema (rnc)
		// project migrations are handled subsequently to this as part of the project opening processes (via MigrationManager)

		Document document = convertToDocument(projectAsInputStream);
		Element rootElement = document.getDocumentElement();
		int xmpz2DocumentSchemaVersion = getXmpz2DocumentSchemaVersion(rootElement);
		boolean schemaVersionWasUpdated = updateXmpz2SchemaVersionToCurrentVersion(rootElement);
		removeLegacyTncFields(rootElement);
		removeHumanWellbeingTargetCalculatedThreatRatingElement(rootElement);
		renameTncFields(document);
		renameLeaderResourceFields(document);
		adjustWhoWhenAssignedFields(document);
		adjustDiagramFactorFontColorFields(document);
		addDayColumnsVisibilityField(document);
		moveIndicatorWorkPlanDataToExtraData(document);
		moveProjectStatusDataToExtraData(document);
		moveTaggedObjectSetTaggedFactorRefListToExtraData(document);
		addUUIDFields(document);
		moveIndicatorRatingSourceToExtraData(document);
		moveMeasurementSourceToExtraData(document);
		removeRelevantDiagramFactorIdsElement(rootElement, Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION);
		removeRelevantDiagramFactorIdsElement(rootElement, Xmpz2XmlConstants.LEGACY_ASSUMPTION);
		moveStrategyStandardClassificationToExtraData(document);
		moveCauseStandardClassificationToExtraData(document);
		removeDiagramFactorStyleHeaderHeightElement(rootElement);
		moveDiagramFactorTextBoxZOrderCodeToExtraData(document);
		addDiagramFactorZIndexField(document);
		moveDiagramFactorStyleToExtraData(document);
		addDiagramLinkZIndexField(document);
		moveDiagramLinkColorToExtraData(document);
		renameAnalyticalQuestionAndAssumptionFields(document);

		final String migratedXmlAsString = HtmlUtilities.toXmlString(document);

		return new Xmpz2MigrationResult(new StringInputStreamWithSeek(migratedXmlAsString), schemaVersionWasUpdated, xmpz2DocumentSchemaVersion);
	}

	private void moveIndicatorWorkPlanDataToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node indicatorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.INDICATOR));
		if (indicatorPool != null)
		{
			NodeList indicatorNodes = indicatorPool.getChildNodes();
			for (int index = 0; index < indicatorNodes.getLength(); ++index)
			{
				Node indicatorNode = indicatorNodes.item(index);
				if (indicatorNode != null && indicatorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					removeChildren(indicatorNode, new String[]{Xmpz2XmlConstants.INDICATOR + Xmpz2XmlConstants.TIME_PERIOD_COSTS,});

					// note: work plan data is deprecated for indicators but we need to keep it around for migrations
					// see related changes in Xmpz2ExtraDataImporter where these fields are imported

					moveIndicatorAssignedLeaderResourceIdToExtraData(document, indicatorNode);
					moveIndicatorResourceAssignmentIdListToExtraData(document, indicatorNode);
					moveIndicatorExpenseAssignmentRefListToExtraData(document, indicatorNode);
				}
			}
		}
	}

	private void moveIndicatorAssignedLeaderResourceIdToExtraData(Document document, Node indicatorNode) throws Exception
	{
		String idAsString = getAttributeValue(indicatorNode, Xmpz2XmlConstants.ID);

		String assignedLeaderResourceIdElementName = Xmpz2XmlConstants.INDICATOR + Xmpz2XmlConstants.ASSIGNED_LEADER_RESOURCE_ID;
		String tagName = BaseObject.TAG_ASSIGNED_LEADER_RESOURCE;

		Node assignedLeaderResourceIdNode = findNode(indicatorNode.getChildNodes(), assignedLeaderResourceIdElementName);
		if (assignedLeaderResourceIdNode != null && assignedLeaderResourceIdNode.getNodeType() == Node.ELEMENT_NODE)
		{
			NodeList resourceIdNodes = assignedLeaderResourceIdNode.getChildNodes();

			if (resourceIdNodes.getLength() == 1)
			{
				Node resourceIdNode = resourceIdNodes.item(0);
				String resourceIdAsString = resourceIdNode.getTextContent();
				ORef leaderRef = new ORef(ObjectType.PROJECT_RESOURCE, new BaseId(resourceIdAsString));
				String extraDataItemName = ExtraDataExporter.getExtraDataItemName(IndicatorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
				String extraDataItemValue = leaderRef.toJson().toString();
				moveDataToExtraData(document, extraDataItemName, extraDataItemValue);
			}

			indicatorNode.removeChild(assignedLeaderResourceIdNode);
		}
	}

	private void moveIndicatorResourceAssignmentIdListToExtraData(Document document, Node indicatorNode) throws Exception
	{
		String idAsString = getAttributeValue(indicatorNode, Xmpz2XmlConstants.ID);

		String resourceAssignmentIdsElementName = Xmpz2XmlConstants.INDICATOR + Xmpz2XmlConstants.RESOURCE_ASSIGNMENT + Xmpz2XmlConstants.IDS;
		String tagName = BaseObject.TAG_RESOURCE_ASSIGNMENT_IDS;

		Node resourceAssignmentIdsNode = findNode(indicatorNode.getChildNodes(), resourceAssignmentIdsElementName);
		if (resourceAssignmentIdsNode != null && resourceAssignmentIdsNode.getNodeType() == Node.ELEMENT_NODE)
		{
			IdList idList = new IdList(ObjectType.RESOURCE_ASSIGNMENT);

			NodeList resourceAssignmentIdNodes = resourceAssignmentIdsNode.getChildNodes();
			for (int index = 0; index < resourceAssignmentIdNodes.getLength(); ++index)
			{
				Node resourceAssignmentIdNode = resourceAssignmentIdNodes.item(index);
				if (resourceAssignmentIdNode != null && resourceAssignmentIdNode.getNodeType() == Node.ELEMENT_NODE)
				{
					String resourceAssignmentId = resourceAssignmentIdNode.getTextContent().trim();
					idList.add(new BaseId(resourceAssignmentId));
				}
			}

			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(IndicatorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = idList.toJson().toString();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			indicatorNode.removeChild(resourceAssignmentIdsNode);
		}
	}

	private void moveIndicatorExpenseAssignmentRefListToExtraData(Document document, Node indicatorNode) throws Exception
	{
		String idAsString = getAttributeValue(indicatorNode, Xmpz2XmlConstants.ID);

		String expenseAssignmentIdsTagName = Xmpz2XmlConstants.INDICATOR + Xmpz2XmlConstants.EXPENSE_ASSIGNMENT + Xmpz2XmlConstants.IDS;
		String tagName = BaseObject.TAG_EXPENSE_ASSIGNMENT_REFS;

		Node expenseAssignmentIdsNode = findNode(indicatorNode.getChildNodes(), expenseAssignmentIdsTagName);
		if (expenseAssignmentIdsNode != null && expenseAssignmentIdsNode.getNodeType() == Node.ELEMENT_NODE)
		{
			ORefList refList = new ORefList();

			NodeList expenseAssignmentIdNodes = expenseAssignmentIdsNode.getChildNodes();
			for (int index = 0; index < expenseAssignmentIdNodes.getLength(); ++index)
			{
				Node expenseAssignmentIdNode = expenseAssignmentIdNodes.item(index);
				if (expenseAssignmentIdNode != null && expenseAssignmentIdNode.getNodeType() == Node.ELEMENT_NODE)
				{
					String expenseAssignmentId = expenseAssignmentIdNode.getTextContent().trim();
					refList.add(new ORef(ObjectType.EXPENSE_ASSIGNMENT, new BaseId(expenseAssignmentId)));
				}
			}

			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(IndicatorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = refList.toJson().toString();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			indicatorNode.removeChild(expenseAssignmentIdsNode);
		}
	}

	private void moveProjectStatusDataToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		Node projectSummaryNode = findNode(rootElement.getChildNodes(), Xmpz2XmlConstants.PROJECT_SUMMARY);
		if (projectSummaryNode != null)
		{
			moveProjectStatusElementToExtraData(document, projectSummaryNode, Xmpz2XmlConstants.PROJECT_SUMMARY + ProjectMetadata.TAG_PROJECT_STATUS, ProjectMetadata.TAG_PROJECT_STATUS);
			moveProjectStatusElementToExtraData(document, projectSummaryNode, Xmpz2XmlConstants.PROJECT_SUMMARY + ExtendedProgressReport.TAG_LESSONS_LEARNED, ProjectMetadata.TAG_TNC_LESSONS_LEARNED);
			moveProjectStatusElementToExtraData(document, projectSummaryNode, Xmpz2XmlConstants.PROJECT_SUMMARY + ProjectMetadata.TAG_NEXT_STEPS, ProjectMetadata.TAG_NEXT_STEPS);
		}
	}

	private void moveProjectStatusElementToExtraData(Document document, Node projectSummaryNode, final String elementNameWithoutAlias, String fieldTag) throws Exception
	{
		Node nodeToMove = findNode(projectSummaryNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(ProjectMetadataSchema.OBJECT_NAME, new BaseId(""), fieldTag.replace(TYPE_ID_TAG_SPLIT_TOKEN, FIELD_TAG_ESCAPE_TOKEN));
			String extraDataItemValue = getFormattedNodeContent(nodeToMove);
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			projectSummaryNode.removeChild(nodeToMove);
		}
	}

	private void moveTaggedObjectSetTaggedFactorRefListToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node taggedObjectSetPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.TAGGED_OBJECT_SET_ELEMENT_NAME));
		if (taggedObjectSetPool != null)
		{
			NodeList taggedObjectSetNodes = taggedObjectSetPool.getChildNodes();
			for (int index = 0; index < taggedObjectSetNodes.getLength(); ++index)
			{
				Node taggedObjectSetNode = taggedObjectSetNodes.item(index);
				if (taggedObjectSetNode != null && taggedObjectSetNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveTaggedObjectSetFactorIdsToExtraData(document, taggedObjectSetNode);
				}
			}
		}
	}

	private void moveTaggedObjectSetFactorIdsToExtraData(Document document, Node taggedObjectSetNode) throws Exception
	{
		String idAsString = getAttributeValue(taggedObjectSetNode, Xmpz2XmlConstants.ID);

		String factorIdsElementName = TaggedObjectSetSchema.OBJECT_NAME + Xmpz2XmlConstants.TAGGED_FACTOR_IDS;
		String tagName = TaggedObjectSet.TAG_TAGGED_OBJECT_REFS;

		Node factorIdsNode = findNode(taggedObjectSetNode.getChildNodes(), factorIdsElementName);
		if (factorIdsNode != null && factorIdsNode.getNodeType() == Node.ELEMENT_NODE)
		{
			ORefList refList = new ORefList();

			NodeList wrappedByDiagramFactorIdNodes = factorIdsNode.getChildNodes();

			for (int index = 0; index < wrappedByDiagramFactorIdNodes.getLength(); ++index)
			{
				Node wrappedByDiagramFactorIdNode = wrappedByDiagramFactorIdNodes.item(index);
				if (wrappedByDiagramFactorIdNode != null && wrappedByDiagramFactorIdNode.getNodeType() == Node.ELEMENT_NODE)
				{
					NodeList factorIdNodes = wrappedByDiagramFactorIdNode.getChildNodes();
					for (int j = 0; j < factorIdNodes.getLength(); ++j)
					{
						Node factorIdNode = factorIdNodes.item(j);
						NodeList idNodes = factorIdNode.getChildNodes();
						if (idNodes.getLength() == 1)
						{
							Node idNode = idNodes.item(0);
							String factorId = idNode.getTextContent().trim();
							int factorObjectType = mapWrappedDiagramFactorIdToObjectType(factorIdNode);
							refList.add(new ORef(factorObjectType, new BaseId(factorId)));
						}
					}
				}
			}

			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(TaggedObjectSetSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = refList.toJson().toString();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			taggedObjectSetNode.removeChild(factorIdsNode);
		}
	}

	private int mapWrappedDiagramFactorIdToObjectType(Node factorIdNode) throws Exception
	{
		int mappedObjectType = ObjectType.FAKE;

		String factorIdNodeName = factorIdNode.getNodeName().replace(Xmpz2XmlConstants.PREFIX, "");
		switch (factorIdNodeName)
		{
			case Xmpz2XmlConstants.BIODIVERSITY_TARGET + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.TARGET;
				break;
			case Xmpz2XmlConstants.HUMAN_WELFARE_TARGET + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.TARGET;
				break;
			case Xmpz2XmlConstants.BIOPHYSICAL_FACTOR + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.BIOPHYSICAL_FACTOR;
				break;
			case Xmpz2XmlConstants.BIOPHYSICAL_RESULT + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.BIOPHYSICAL_RESULT;
				break;
			case Xmpz2XmlConstants.CAUSE + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.CAUSE;
				break;
			case Xmpz2XmlConstants.STRATEGY + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.STRATEGY;
				break;
			case Xmpz2XmlConstants.THREAT_REDUCTION_RESULT + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.THREAT_REDUCTION_RESULT;
				break;
			case Xmpz2XmlConstants.INTERMEDIATE_RESULT + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.INTERMEDIATE_RESULT;
				break;
			case Xmpz2XmlConstants.GROUP_BOX + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.GROUP_BOX;
				break;
			case Xmpz2XmlConstants.TEXT_BOX + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.TEXT_BOX;
				break;
			case Xmpz2XmlConstants.SCOPE_BOX + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.SCOPE_BOX;
				break;
			case Xmpz2XmlConstants.ACTIVITY + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.TASK;
				break;
			case Xmpz2XmlConstants.STRESS + Xmpz2XmlConstants.ID:
				mappedObjectType = ObjectType.STRESS;
				break;
			default:
				throw new Exception("Could not map object type for element name: " + factorIdNodeName);
		}

		return mappedObjectType;
	}

	private void moveIndicatorRatingSourceToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node indicatorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.INDICATOR));
		if (indicatorPool != null)
		{
			NodeList indicatorNodes = indicatorPool.getChildNodes();
			for (int index = 0; index < indicatorNodes.getLength(); ++index)
			{
				Node indicatorNode = indicatorNodes.item(index);
				if (indicatorNode != null && indicatorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveIndicatorRatingSourceElementToExtraData(document, indicatorNode);
				}
			}
		}
	}

	private void moveIndicatorRatingSourceElementToExtraData(Document document, Node indicatorNode) throws Exception
	{
		String idAsString = getAttributeValue(indicatorNode, Xmpz2XmlConstants.ID);

		String elementNameWithoutAlias = Xmpz2XmlConstants.INDICATOR + Xmpz2XmlConstants.RATING + Xmpz2XmlConstants.SOURCE;
		String tagName = Indicator.TAG_RATING_SOURCE;

		Node nodeToMove = findNode(indicatorNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(IndicatorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			indicatorNode.removeChild(nodeToMove);
		}
	}

	private void moveMeasurementSourceToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node measurementPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.MEASUREMENT));
		if (measurementPool != null)
		{
			NodeList measurementNodes = measurementPool.getChildNodes();
			for (int index = 0; index < measurementNodes.getLength(); ++index)
			{
				Node measurementNode = measurementNodes.item(index);
				if (measurementNode != null && measurementNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveMeasurementSourceElementToExtraData(document, measurementNode);
				}
			}
		}
	}
	
	private void moveMeasurementSourceElementToExtraData(Document document, Node measurementNode) throws Exception
	{
		String idAsString = getAttributeValue(measurementNode, Xmpz2XmlConstants.ID);

		String elementNameWithoutAlias = Xmpz2XmlConstants.MEASUREMENT + Xmpz2XmlConstants.SOURCE;
		String tagName = Measurement.TAG_STATUS_CONFIDENCE;

		Node nodeToMove = findNode(measurementNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(MeasurementSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			measurementNode.removeChild(nodeToMove);
		}
	}

	private void moveStrategyStandardClassificationToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node strategyPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.STRATEGY));
		if (strategyPool != null)
		{
			NodeList strategyNodes = strategyPool.getChildNodes();
			for (int index = 0; index < strategyNodes.getLength(); ++index)
			{
				Node strategyNode = strategyNodes.item(index);
				if (strategyNode != null && strategyNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveStrategyStandardClassificationElementToExtraData(document, strategyNode);
				}
			}
		}
	}

	private void moveStrategyStandardClassificationElementToExtraData(Document document, Node strategyNode) throws Exception
	{
		String idAsString = getAttributeValue(strategyNode, Xmpz2XmlConstants.ID);

		String elementNameWithoutAlias = Xmpz2XmlConstants.STRATEGY_STANDARD_CLASSIFICATION;
		String tagName = Strategy.TAG_STANDARD_CLASSIFICATION_V11_CODE;

		Node nodeToMove = findNode(strategyNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(StrategySchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			strategyNode.removeChild(nodeToMove);
		}
	}

	private void moveCauseStandardClassificationToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node causePool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.CAUSE));
		if (causePool != null)
		{
			NodeList causeNodes = causePool.getChildNodes();
			for (int index = 0; index < causeNodes.getLength(); ++index)
			{
				Node causeNode = causeNodes.item(index);
				if (causeNode != null && causeNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveCauseStandardClassificationElementToExtraData(document, causeNode);
				}
			}
		}
	}

	private void moveCauseStandardClassificationElementToExtraData(Document document, Node causeNode) throws Exception
	{
		String idAsString = getAttributeValue(causeNode, Xmpz2XmlConstants.ID);

		String elementNameWithoutAlias = Xmpz2XmlConstants.CAUSE_STANDARD_CLASSIFICATION;
		String tagName = Cause.TAG_STANDARD_CLASSIFICATION_V11_CODE;

		Node nodeToMove = findNode(causeNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(CauseSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			causeNode.removeChild(nodeToMove);
		}
	}

	private void removeRelevantDiagramFactorIdsElement(Element rootElement, String XmlElementName)
	{
		Node objectPoolToUpdate = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(XmlElementName));
		if (objectPoolToUpdate == null)
			return;

		NodeList children = objectPoolToUpdate.getChildNodes();
		for (int index = 0; index < children.getLength(); ++index)
		{
			Node objectToUpdate = children.item(index);
			if (objectToUpdate == null)
				continue;

			removeChildren(objectToUpdate, new String[]{XmlElementName + Xmpz2XmlConstants.RELEVANT_DIAGRAM_FACTOR_IDS,});
		}
	}

	private void removeDiagramFactorStyleHeaderHeightElement(Element rootElement)
	{
		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));
		if (diagramFactorPool == null)
			return;

		NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
		for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
		{
			Node diagramFactorNode = diagramFactorNodes.item(index);
			if (diagramFactorNode != null && diagramFactorNode.getNodeType() == Node.ELEMENT_NODE)
			{
				String diagramFactorStyleElementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_FACTOR + Xmpz2XmlConstants.STYLE;
				Node diagramFactorStyleElementNode = findNode(diagramFactorNode, diagramFactorStyleElementNameWithoutAlias);
				if (diagramFactorStyleElementNode != null && diagramFactorStyleElementNode.getNodeType() == Node.ELEMENT_NODE)
				{
					String styleElementNameWithoutAlias = Xmpz2XmlConstants.STYLE;
					Node styleElementNode = findNode(diagramFactorStyleElementNode, styleElementNameWithoutAlias);
					if (styleElementNode != null && styleElementNode.getNodeType() == Node.ELEMENT_NODE)
					{
						removeChildren(styleElementNode, new String[]{Xmpz2XmlConstants.DIAGRAM_FACTOR + MigrationTo84.TAG_HEADER_HEIGHT,});
					}
				}
			}
		}
	}

	private void moveDiagramFactorTextBoxZOrderCodeToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));
		if (diagramFactorPool != null)
		{
			NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
			for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
			{
				Node diagramFactorNode = diagramFactorNodes.item(index);
				if (diagramFactorNode != null && diagramFactorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveDiagramFactorTextBoxZOrderCodeElementToExtraData(document, diagramFactorNode);
				}
			}
		}
	}

	private void moveDiagramFactorTextBoxZOrderCodeElementToExtraData(Document document, Node diagramFactorNode) throws Exception
	{
		String idAsString = getAttributeValue(diagramFactorNode, Xmpz2XmlConstants.ID);

		String tagName = MigrationTo85.TAG_TEXT_BOX_Z_ORDER_CODE;
		String elementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_FACTOR + MigrationTo85.TAG_TEXT_BOX_Z_ORDER_CODE;

		Node nodeToMove = findNode(diagramFactorNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(DiagramFactorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			diagramFactorNode.removeChild(nodeToMove);
		}
	}

	private void addDiagramFactorZIndexField(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));
		if (diagramFactorPool != null)
		{
			NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
			for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
			{
				Node diagramFactorNode = diagramFactorNodes.item(index);
				if (diagramFactorNode != null && diagramFactorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					addZIndexField(document, diagramFactorNode, Xmpz2XmlConstants.DIAGRAM_FACTOR);
				}
			}
		}
	}

	private void moveDiagramFactorStyleToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));
		if (diagramFactorPool != null)
		{
			NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
			for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
			{
				Node diagramFactorNode = diagramFactorNodes.item(index);
				if (diagramFactorNode != null && diagramFactorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveDiagramFactorStyleElementToExtraData(document, diagramFactorNode);
				}
			}
		}
	}

	private void moveDiagramFactorStyleElementToExtraData(Document document, Node diagramFactorNode) throws Exception
	{
		String idAsString = getAttributeValue(diagramFactorNode, Xmpz2XmlConstants.ID);

		String elementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_FACTOR + Xmpz2XmlConstants.STYLE;
		Node diagramFactorStyleElementNode = findNode(diagramFactorNode, elementNameWithoutAlias);
		if (diagramFactorStyleElementNode != null && diagramFactorStyleElementNode.getNodeType() == Node.ELEMENT_NODE)
		{
			String styleElementNameWithoutAlias = Xmpz2XmlConstants.STYLE;
			Node styleElementNode = findNode(diagramFactorStyleElementNode, styleElementNameWithoutAlias);
			if (styleElementNode != null && styleElementNode.getNodeType() == Node.ELEMENT_NODE)
			{
				String fontColorTagName = DiagramFactor.TAG_FOREGROUND_COLOR;
				String fontColorElementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_FACTOR + DiagramFactor.TAG_FOREGROUND_COLOR;

				moveDiagramFactorStyleElementFieldToExtraData(document, diagramFactorNode, styleElementNode, fontColorElementNameWithoutAlias, fontColorTagName);

				String backgroundColorTagName = DiagramFactor.TAG_BACKGROUND_COLOR;
				String backgroundColorElementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_FACTOR + DiagramFactor.TAG_BACKGROUND_COLOR;

				moveDiagramFactorStyleElementFieldToExtraData(document, diagramFactorNode, styleElementNode, backgroundColorElementNameWithoutAlias, backgroundColorTagName);
			}
		}
	}

	private void moveDiagramFactorStyleElementFieldToExtraData(Document document, Node diagramFactorNode, Node styleElementNode, String elementNameWithoutAlias, String tagName) throws Exception
	{
		String idAsString = getAttributeValue(diagramFactorNode, Xmpz2XmlConstants.ID);

		Node nodeToMove = findNode(styleElementNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(DiagramFactorSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			styleElementNode.removeChild(nodeToMove);
		}
	}

	private void addDiagramLinkZIndexField(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramLinkPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_LINK));
		if (diagramLinkPool != null)
		{
			NodeList diagramLinkNodes = diagramLinkPool.getChildNodes();
			for (int index = 0; index < diagramLinkNodes.getLength(); ++index)
			{
				Node diagramLinkNode = diagramLinkNodes.item(index);
				if (diagramLinkNode != null && diagramLinkNode.getNodeType() == Node.ELEMENT_NODE)
				{
					addZIndexField(document, diagramLinkNode, Xmpz2XmlConstants.DIAGRAM_LINK);
				}
			}
		}
	}

	private void moveDiagramLinkColorToExtraData(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramLinkPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_LINK));
		if (diagramLinkPool != null)
		{
			NodeList diagramLinkNodes = diagramLinkPool.getChildNodes();
			for (int index = 0; index < diagramLinkNodes.getLength(); ++index)
			{
				Node diagramLinkNode = diagramLinkNodes.item(index);
				if (diagramLinkNode != null && diagramLinkNode.getNodeType() == Node.ELEMENT_NODE)
				{
					moveDiagramLinkColorElementToExtraData(document, diagramLinkNode);
				}
			}
		}
	}

	private void moveDiagramLinkColorElementToExtraData(Document document, Node diagramLinkNode) throws Exception
	{
		String idAsString = getAttributeValue(diagramLinkNode, Xmpz2XmlConstants.ID);

		String tagName = MigrationTo86.TAG_COLOR;
		String elementNameWithoutAlias = Xmpz2XmlConstants.DIAGRAM_LINK + MigrationTo86.TAG_COLOR;

		Node nodeToMove = findNode(diagramLinkNode, elementNameWithoutAlias);
		if (nodeToMove != null && nodeToMove.getNodeType() == Node.ELEMENT_NODE)
		{
			String extraDataItemName = ExtraDataExporter.getExtraDataItemName(DiagramLinkSchema.OBJECT_NAME, new BaseId(idAsString), tagName);
			String extraDataItemValue = nodeToMove.getTextContent();
			moveDataToExtraData(document, extraDataItemName, extraDataItemValue);

			diagramLinkNode.removeChild(nodeToMove);
		}
	}

	private void addZIndexField(Document document, Node diagramFactorOrLinkElement, String elementName) throws Exception
	{
		Node zIndexElement = findNode(diagramFactorOrLinkElement.getChildNodes(), elementName + Xmpz2XmlConstants.Z_INDEX);
		if (zIndexElement == null)
		{
			final String alias = getNameSpaceAliasName(document.getDocumentElement());
			Node newNode = document.createElement(alias + COLON + elementName + Xmpz2XmlConstants.Z_INDEX);
			newNode.setTextContent("0");
			diagramFactorOrLinkElement.appendChild(newNode);
		}
	}

	private void addUUIDFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		NodeList childNodes = rootElement.getChildNodes();

		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node childNode = childNodes.item(index);
			addUUIDFieldToElementIfApplicable(document, childNode);
		}
	}

	private void addUUIDFieldToElementIfApplicable(Document document, Node node) throws Exception
	{
		NamedNodeMap attributes = node.getAttributes();
		if (attributes != null)
		{
			Node attributeNode = attributes.getNamedItem(Xmpz2XmlConstants.ID);
			if (attributeNode != null && attributeNode.getNodeType() == Node.ATTRIBUTE_NODE)
			{
				final String alias = getNameSpaceAliasName(document.getDocumentElement());
				String parentNodeName = node.getNodeName().replace(alias, "").replace(COLON, "");
				String uuidNodeName = parentNodeName + Xmpz2XmlConstants.UUID.toUpperCase();
				Node uuidNode = findNode(node, uuidNodeName);
				if (uuidNode == null)
				{
					Node uuidNodeNew = document.createElement(alias + COLON + uuidNodeName);
					uuidNodeNew.setTextContent(UUID.randomUUID().toString());
					node.appendChild(uuidNodeNew);
				}
			}
		}

		NodeList childNodes = node.getChildNodes();

		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node childNode = childNodes.item(index);
			addUUIDFieldToElementIfApplicable(document, childNode);
		}
	}

	private void moveDataToExtraData(Document document, String extraDataItemName, String extraDataItemValue) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		Node extraDataNode = findNode(rootElement.getChildNodes(), Xmpz2XmlConstants.EXTRA_DATA);
		if (extraDataNode != null && extraDataNode.getNodeType() == Node.ELEMENT_NODE)
		{
			NodeList extraDataSectionNodes = extraDataNode.getChildNodes();
			for (int index = 0; index < extraDataSectionNodes.getLength(); ++index)
			{
				Node extraDataSectionNode = extraDataSectionNodes.item(index);
				if (extraDataSectionNode != null && extraDataSectionNode.getNodeType() == Node.ELEMENT_NODE)
				{
					String owner = getAttributeValue(extraDataSectionNode, Xmpz2XmlConstants.EXTRA_DATA_SECTION_OWNER_ATTRIBUTE);
					if (owner.equals(Xmpz2XmlConstants.MIRADI_CLIENT_EXTRA_DATA_SECTION))
					{
						Node newExtraDataItemNode = document.createElement(alias + COLON +  Xmpz2XmlConstants.EXTRA_DATA_ITEM);
						((Element) newExtraDataItemNode).setAttribute(Xmpz2XmlConstants.EXTRA_DATA_ITEM_NAME, extraDataItemName);

						Node newExtraDataItemValueNode = document.createElement(alias + COLON + Xmpz2XmlConstants.EXTRA_DATA_ITEM_VALUE);

						extraDataItemValue = StringUtilities.escapeQuotesWithBackslash(extraDataItemValue);
						extraDataItemValue = XmlUtilities2.getXmlEncoded(extraDataItemValue);
						newExtraDataItemValueNode.setTextContent(extraDataItemValue);
						newExtraDataItemNode.appendChild(newExtraDataItemValueNode);

						extraDataSectionNode.appendChild(newExtraDataItemNode);

						break;
					}
				}
			}
		}
	}

	private void renameAnalyticalQuestionAndAssumptionFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node analyticalQuestionPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION));
		if (analyticalQuestionPool != null)
		{
			renameAssumptionFields(document);
			renameAnalyticalQuestionFields(document);
		}
	}

	private void renameAssumptionFields(Document document) throws Exception
	{
		copyAndRenameObjectPool(document, Xmpz2XmlConstants.LEGACY_ASSUMPTION, Xmpz2XmlConstants.SUB_ASSUMPTION);
		copyAndRenameObjectPool(document, Xmpz2XmlConstants.LEGACY_ASSUMPTION + Xmpz2XmlConstants.TAXONOMY_ASSOCIATION, Xmpz2XmlConstants.SUB_ASSUMPTION + Xmpz2XmlConstants.TAXONOMY_ASSOCIATION);
		renameDiagramFactorWrappedFactors(document, Xmpz2XmlConstants.LEGACY_ASSUMPTION, Xmpz2XmlConstants.SUB_ASSUMPTION);
		renamePlanningViewConfigurations(document, Xmpz2XmlConstants.LEGACY_ASSUMPTION, Xmpz2XmlConstants.SUB_ASSUMPTION);
	}

	private void renameAnalyticalQuestionFields(Document document) throws Exception
	{
		copyAndRenameObjectPool(document, Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION, Xmpz2XmlConstants.ASSUMPTION);
		copyAndRenameObjectPool(document, Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION + Xmpz2XmlConstants.TAXONOMY_ASSOCIATION, Xmpz2XmlConstants.ASSUMPTION + Xmpz2XmlConstants.TAXONOMY_ASSOCIATION);
		renameDiagramFactorWrappedFactors(document, Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION, Xmpz2XmlConstants.ASSUMPTION);
		renamePlanningViewConfigurations(document, Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION, Xmpz2XmlConstants.ASSUMPTION);
	}

	private Node getOrCreateObjectPool(Document document, String objectName) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		Node objectPool = findNodeStrictNameMatch(document, rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(objectName));
		if (objectPool == null)
		{
			objectPool = document.createElement(alias + COLON + Xmpz2XmlWriter.createPoolElementName(objectName));
			rootElement.appendChild(objectPool);
		}

		return objectPool;
	}

	private void copyAndRenameObjectPool(Document document, String oldObjectName, String newObjectName) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node newObjectPool = getOrCreateObjectPool(document, newObjectName);

		Node oldObjectPool = findNodeStrictNameMatch(document, rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(oldObjectName));
		if (oldObjectPool != null)
		{
			NodeList oldObjectPoolChildNodes = oldObjectPool.getChildNodes();
			for (int index = 0; index < oldObjectPoolChildNodes.getLength(); ++index)
			{
				Node oldObjectPoolChildNode = oldObjectPoolChildNodes.item(index);
				if (oldObjectPoolChildNode != null && oldObjectPoolChildNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Node newObjectElement = copyAndRenameElement(document, oldObjectPoolChildNode, oldObjectName, newObjectName);
					newObjectPool.appendChild(newObjectElement);
				}
			}

			rootElement.removeChild(oldObjectPool);
		}
	}

	private Node copyAndRenameElement(Document document, Node elementToCopy, String oldObjectName, String newObjectName) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		String elementToCopyName = elementToCopy.getNodeName().replace(alias, "").replace(COLON, "");
		String newElementName = elementToCopyName.replace(oldObjectName, newObjectName);

		// hack for special cases that doesn't fit general pattern
		if (oldObjectName.equals(Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION))
		{
			if (elementToCopyName.equals(Xmpz2XmlConstants.LEGACY_ANALYTICAL_QUESTION + Xmpz2XmlConstants.LEGACY_ASSUMPTION_IDS))
				newElementName = Xmpz2XmlConstants.ASSUMPTION + Xmpz2XmlConstants.SUB_ASSUMPTION_IDS;

			if (elementToCopyName.equals(Xmpz2XmlConstants.ASSUMPTION + Xmpz2XmlConstants.ID))
				newElementName = Xmpz2XmlConstants.SUB_ASSUMPTION + Xmpz2XmlConstants.ID;
		}

		Node newElement = document.createElement(alias + COLON + newElementName);

		if (elementToCopy.hasAttributes())
		{
			NamedNodeMap attributesToCopy = elementToCopy.getAttributes();
			for (int index = 0; index < attributesToCopy.getLength(); ++index)
			{
				final Node attributeToCopy = attributesToCopy.item(index);
				((Element)newElement).setAttribute(attributeToCopy.getNodeName(), attributeToCopy.getTextContent());
			}
		}

		if (elementToCopy.hasChildNodes())
		{
			if (!elementToCopy.getFirstChild().getNodeName().startsWith(alias))
			{
				NodeList childElementsToClone = elementToCopy.getChildNodes();
				for (int index = 0; index < childElementsToClone.getLength(); ++index)
				{
					Node childNodeToClone = childElementsToClone.item(index);
					Node newChildElement = childNodeToClone.cloneNode(true);
					newElement.appendChild(newChildElement);
				}
			}
			else
			{
				NodeList childElementsToCopy = elementToCopy.getChildNodes();
				for (int index = 0; index < childElementsToCopy.getLength(); ++index)
				{
					Node childNodeToCopy = childElementsToCopy.item(index);
					if (childNodeToCopy != null && childNodeToCopy.getNodeType() == Node.ELEMENT_NODE)
					{
						Node newElementChildNode = copyAndRenameElement(document, childNodeToCopy, oldObjectName, newObjectName);
						newElement.appendChild(newElementChildNode);
					}
				}
			}
		}

		return newElement;
	}

	private void renameDiagramFactorWrappedFactors(Document document, String oldObjectName, String newObjectName) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));
		if (diagramFactorPool != null)
		{
			NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
			for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
			{
				Node diagramFactorNode = diagramFactorNodes.item(index);
				if (diagramFactorNode != null && diagramFactorNode.getNodeType() == Node.ELEMENT_NODE)
				{
					renameDiagramFactorWrappedFactorId(document, diagramFactorNode, oldObjectName, newObjectName);
				}
			}
		}
	}

	private void renameDiagramFactorWrappedFactorId(Document document, Node diagramFactor, String oldObjectName, String newObjectName) throws Exception
	{
		Node diagramFactorWrappedFactorIdNode = findNode(diagramFactor.getChildNodes(), Xmpz2XmlConstants.DIAGRAM_FACTOR + Xmpz2XmlConstants.WRAPPED_FACTOR_ID_ELEMENT_NAME);
		if (diagramFactorWrappedFactorIdNode != null)
		{
			Node wrappedByDiagramFactorIdNode = findNode(diagramFactorWrappedFactorIdNode.getChildNodes(), Xmpz2XmlConstants.WRAPPED_BY_DIAGRAM_FACTOR_ID_ELEMENT_NAME);
			if (wrappedByDiagramFactorIdNode != null)
				renameWrappedByDiagramFactorId(document, wrappedByDiagramFactorIdNode, oldObjectName, newObjectName);
		}
	}

	private void renameWrappedByDiagramFactorId(Document document, Node wrappedByDiagramFactorIdNode, String oldObjectName, String newObjectName) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		Node oldObjectIdNode = findNodeStrictNameMatch(document, wrappedByDiagramFactorIdNode.getChildNodes(), oldObjectName + Xmpz2XmlConstants.ID);
		if (oldObjectIdNode != null)
		{
			String oldObjectIdNodeName = oldObjectIdNode.getNodeName().replace(alias, "").replace(COLON, "");
			String newObjectIdNodeName = oldObjectIdNodeName.replace(oldObjectName, newObjectName);

			Node newObjectIdElement = document.createElement(alias + COLON + newObjectIdNodeName);
			newObjectIdElement.setTextContent(oldObjectIdNode.getTextContent());
			wrappedByDiagramFactorIdNode.appendChild(newObjectIdElement);

			wrappedByDiagramFactorIdNode.removeChild(oldObjectIdNode);
		}
	}

	private void renamePlanningViewConfigurations(Document document, String oldObjectName, String newObjectName) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		Element rootElement = document.getDocumentElement();

		Node planningViewConfigurationPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION));
		if (planningViewConfigurationPool != null)
		{
			NodeList planningViewConfigurationNodes = planningViewConfigurationPool.getChildNodes();
			for (int index = 0; index < planningViewConfigurationNodes.getLength(); ++index)
			{
				Node planningViewConfigurationNode = planningViewConfigurationNodes.item(index);
				if (planningViewConfigurationNode != null && planningViewConfigurationNode.getNodeType() == Node.ELEMENT_NODE)
				{
					Node rowObjectTypesContainerNode = findNode(planningViewConfigurationNode.getChildNodes(), Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION + Xmpz2XmlConstants.ROW_CONFIGURATION_CODES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG);
					if (rowObjectTypesContainerNode != null)
					{
						ArrayList<String> codesToKeep = new ArrayList<String>();

						NodeList codeList = rowObjectTypesContainerNode.getChildNodes();
						int codeCount = codeList.getLength();

						for (int i = 0; i < codeCount; ++i)
						{
							Node code = codeList.item(i);
							String codeValue = code.getTextContent().trim().replaceAll(StringUtilities.NEW_LINE, StringUtilities.EMPTY_SPACE);
							if (codeValue.equals(oldObjectName))
							{
								codesToKeep.add(newObjectName);
							}
							else
							{
								codesToKeep.add(codeValue);
							}
						}

						for (int i = 0; i < codeCount; ++i)
						{
							removeChildren(rowObjectTypesContainerNode, new String[]{Xmpz2XmlConstants.CODE_ELEMENT_NAME,});
						}

						for (String code : codesToKeep)
						{
							if (!code.isEmpty())
							{
								Node newNode = document.createElement(alias + COLON +  Xmpz2XmlConstants.CODE_ELEMENT_NAME);
								newNode.setTextContent(code);
								rowObjectTypesContainerNode.appendChild(newNode);
							}
						}
					}
				}
			}
		}
	}

	private void renameTncFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		Node tncProjectDataNode = findNode(rootElement.getChildNodes(), Xmpz2XmlConstants.TNC_PROJECT_DATA);
		if (tncProjectDataNode != null)
		{	
			BiDirectionalHashMap legacyToNewTncFieldNamesMap = createLegacyTncToNewFieldNamesMap();
			renameElements(document, tncProjectDataNode, legacyToNewTncFieldNamesMap);
		}
	}
	
	private BiDirectionalHashMap createLegacyTncToNewFieldNamesMap()
	{
		BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
		oldToNewTagMap.put(Xmpz2XmlConstants.TNC_PROJECT_DATA + MigrationTo10.LEGACY_TAG_MAKING_THE_CASE, Xmpz2XmlConstants.TNC_PROJECT_DATA + MigrationTo10.TAG_OVERALL_PROJECT_GOAL);
		oldToNewTagMap.put(Xmpz2XmlConstants.TNC_PROJECT_DATA + MigrationTo10.LEGACY_TAG_CAPACITY_AND_FUNDING, Xmpz2XmlConstants.TNC_PROJECT_DATA + MigrationTo10.TAG_FINANCIAL_PLAN);
		
		return oldToNewTagMap;
	}

	private void renameElements(Document document, Node parentNode, BiDirectionalHashMap fromToNameMap) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());
		HashSet<String> keys = fromToNameMap.getKeys();
		for(String fromName : keys)
		{
			Node childNode = findNode(parentNode, fromName);
			if (childNode == null)
				continue;

			String textToTransferToNewNode = childNode.getTextContent();
			parentNode.removeChild(childNode);
			final String toName = fromToNameMap.getValue(fromName);
			Node newNode = document.createElement(alias + COLON +  toName);
			newNode.setTextContent(textToTransferToNewNode);
			parentNode.appendChild(newNode);
		}
	}

	private void renameLeaderResourceFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node strategyPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.STRATEGY));
		BiDirectionalHashMap oldToNewTagMap = createLeaderResourceToNewFieldNamesMap(Xmpz2XmlConstants.STRATEGY);
		renameLeaderResourceFields(document, strategyPool, oldToNewTagMap);

		Node taskPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.TASK));
		oldToNewTagMap = createLeaderResourceToNewFieldNamesMap(Xmpz2XmlConstants.TASK);
		renameLeaderResourceFields(document, taskPool, oldToNewTagMap);

		Node indicatorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.INDICATOR));
		oldToNewTagMap = createLeaderResourceToNewFieldNamesMap(Xmpz2XmlConstants.INDICATOR);
		renameLeaderResourceFields(document, indicatorPool, oldToNewTagMap);
	}

	private void renameLeaderResourceFields(Document document, Node objectPool, BiDirectionalHashMap oldToNewTagMap) throws Exception
	{
		if (objectPool == null)
			return;

		NodeList children = objectPool.getChildNodes();
		for (int index = 0; index < children.getLength(); ++index)
		{
			Node childNode = children.item(index);
			if (childNode != null)
				replaceElements(document, childNode, oldToNewTagMap);
		}
	}

	private BiDirectionalHashMap createLeaderResourceToNewFieldNamesMap(String objectName)
	{
		BiDirectionalHashMap oldToNewTagMap = new BiDirectionalHashMap();
		oldToNewTagMap.put(objectName + MigrationTo19.LEGACY_TAG_LEADER_RESOURCE + Xmpz2XmlConstants.ID, objectName + MigrationTo19.TAG_ASSIGNED_LEADER_RESOURCE + Xmpz2XmlConstants.ID);

		return oldToNewTagMap;
	}

	private void adjustWhoWhenAssignedFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		Node planningViewConfigurationPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION));

		if (planningViewConfigurationPool == null)
			return;

		NodeList planningViewConfigurationNodes = planningViewConfigurationPool.getChildNodes();
		for (int index = 0; index < planningViewConfigurationNodes.getLength(); ++index)
		{
			Node planningViewConfiguration = planningViewConfigurationNodes.item(index);
			if (planningViewConfiguration != null)
			{
				Node columnNamesContainer = findNode(planningViewConfiguration.getChildNodes(), Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION + Xmpz2XmlConstants.COLUMN_CONFIGURATION_CODES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG);
				if (columnNamesContainer != null)
				{
					ArrayList<String> codesToKeep = new ArrayList<String>();

					NodeList codeList = columnNamesContainer.getChildNodes();
					int codeCount = codeList.getLength();

					for (int i = 0; i < codeCount; ++i)
					{
						Node code = codeList.item(i);
						String codeValue = code.getTextContent().trim().replaceAll(StringUtilities.NEW_LINE, StringUtilities.EMPTY_SPACE);
						if (codeValue.equals(MigrationTo20.LEGACY_READABLE_ASSIGNED_WHEN_TOTAL_CODE))
							codesToKeep.add(MigrationTo20.READABLE_TIMEFRAME_TOTAL_CODE);
						else if (codeValue.equals(MigrationTo20.LEGACY_READABLE_ASSIGNED_WHO_TOTAL_CODE));
							// skip - will be deleted
						else
							codesToKeep.add(codeValue);
					}

					for (int i = 0; i < codeCount; ++i)
					{
						removeChildren(columnNamesContainer, new String[]{Xmpz2XmlConstants.CODE_ELEMENT_NAME,});
					}

					for (String code : codesToKeep)
					{
						if (!code.isEmpty())
						{
							Node newNode = document.createElement(alias + COLON +  Xmpz2XmlConstants.CODE_ELEMENT_NAME);
							newNode.setTextContent(code);
							columnNamesContainer.appendChild(newNode);
						}
					}
				}
			}
		}
	}

	private void adjustDiagramFactorFontColorFields(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node diagramFactorPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.DIAGRAM_FACTOR));

		if (diagramFactorPool == null)
			return;

		NodeList diagramFactorNodes = diagramFactorPool.getChildNodes();
		for (int index = 0; index < diagramFactorNodes.getLength(); ++index)
		{
			Node diagramFactor = diagramFactorNodes.item(index);
			if (diagramFactor != null)
			{
				Node diagramFactorStyle = findNode(diagramFactor.getChildNodes(), Xmpz2XmlConstants.DIAGRAM_FACTOR + Xmpz2XmlConstants.STYLE);
				if (diagramFactorStyle != null)
				{
					Node style = findNode(diagramFactorStyle.getChildNodes(), Xmpz2XmlConstants.STYLE);
					if (style != null)
					{
						Node diagramFactorFontColor = findNode(style.getChildNodes(), Xmpz2XmlConstants.DIAGRAM_FACTOR + Xmpz2XmlConstants.DIAGRAM_FACTOR_FOREGROUND_COLOR_ELEMENT_NAME);
						if (diagramFactorFontColor != null)
						{
							String fontColorValue = diagramFactorFontColor.getTextContent().trim();
							if (fontColorValue.equalsIgnoreCase(MigrationTo69.LEGACY_RED_HEX))
							{
								diagramFactorFontColor.setTextContent(MigrationTo69.RED_HEX);
							}
						}
					}
				}
			}
		}
	}

	private void addDayColumnsVisibilityField(Document document) throws Exception
	{
		Element rootElement = document.getDocumentElement();

		Node projectPlanningElement = findNode(rootElement.getChildNodes(), Xmpz2XmlConstants.PROJECT_SUMMARY_PLANNING);

		if (projectPlanningElement == null)
			return;

		Node dayColumnsVisibilityElement = findNode(projectPlanningElement.getChildNodes(), Xmpz2XmlConstants.PROJECT_SUMMARY_PLANNING + Xmpz2XmlConstants.DAY_COLUMNS_VISIBILITY);
		if (dayColumnsVisibilityElement == null)
		{
			final String alias = getNameSpaceAliasName(document.getDocumentElement());
			Node newNode = document.createElement(alias + COLON +  Xmpz2XmlConstants.PROJECT_SUMMARY_PLANNING + Xmpz2XmlConstants.DAY_COLUMNS_VISIBILITY);
			newNode.setTextContent(DayColumnsVisibilityQuestion.SHOW_DAY_COLUMNS_CODE_READABLE);
			projectPlanningElement.appendChild(newNode);
		}
	}

	private void replaceElements(Document document, Node parentNode, BiDirectionalHashMap fromToNameMap) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());
		HashSet<String> keys = fromToNameMap.getKeys();
		for(String fromName : keys)
		{
			Node childNode = findNode(parentNode, fromName);
			if (childNode == null)
				continue;

			NodeList childNodeList = childNode.getChildNodes();
			parentNode.removeChild(childNode);
			final String toName = fromToNameMap.getValue(fromName);
			Node newNode = document.createElement(alias + COLON +  toName);
			for (int index = 0; index < childNodeList.getLength(); ++index)
			{
				Node grandchildNode = childNodeList.item(index);
				newNode.appendChild(grandchildNode);
			}
			parentNode.appendChild(newNode);
		}
	}

	private Node findNode(Node parentNode, final String elementNameWithoutAlias)
	{
		return findNode(parentNode.getChildNodes(), elementNameWithoutAlias);
	}

	private void removeHumanWellbeingTargetCalculatedThreatRatingElement(Element rootElement)
	{
		Node humanWellbeingTargetPool = findNode(rootElement.getChildNodes(), Xmpz2XmlWriter.createPoolElementName(Xmpz2XmlConstants.HUMAN_WELFARE_TARGET));
		if (humanWellbeingTargetPool == null)
			return;
		
		NodeList children = humanWellbeingTargetPool.getChildNodes();
		for (int index = 0; index < children.getLength(); ++index)
		{
			Node humanWellbeingTarget = children.item(index);
			if (humanWellbeingTarget == null)
				continue;
			
			removeChildren(humanWellbeingTarget, new String[]{Xmpz2XmlConstants.HUMAN_WELFARE_TARGET + Xmpz2XmlConstants.CALCULATED_THREAT_RATING,});
		}
	}

	private void removeLegacyTncFields(Element rootElement)
	{
		Node tncProjectDataNode = findNode(rootElement.getChildNodes(), Xmpz2XmlConstants.TNC_PROJECT_DATA);
		if (tncProjectDataNode != null)
			removeLegacyTncChildren(tncProjectDataNode);
	}

	private Node findNode(NodeList children, final String elementNameWithoutAlias)
	{
		for (int index = 0; index < children.getLength(); ++index)
		{
			Node childNode = children.item(index);
			if (childNode.getNodeName().endsWith(elementNameWithoutAlias))
				return childNode;
		}
		
		return null;
	}

	private Node findNodeStrictNameMatch(Document document, NodeList children, final String elementNameWithoutAlias) throws Exception
	{
		final String alias = getNameSpaceAliasName(document.getDocumentElement());

		for (int index = 0; index < children.getLength(); ++index)
		{
			Node childNode = children.item(index);
			if (childNode.getNodeName().equals(alias + COLON + elementNameWithoutAlias))
				return childNode;
		}

		return null;
	}

	private void removeLegacyTncChildren(Node tncProjectDataNode)
	{
		String[] elementNamesToRemove = new String[]{createLegacyTncOrganizationalPrioritiesElementName(), createLegacyTncProjectPlaceTypesElementName(), };
		removeChildren(tncProjectDataNode, elementNamesToRemove);
	}
	
	private void removeChildren(Node nodeToRemoveElementsFrom, String[] elementNames)
	{
		NodeList children = nodeToRemoveElementsFrom.getChildNodes();
		Vector<Node> childrenToRemove = new Vector<Node>();
		for(String elementNameToRemove : elementNames)
		{
			final Node nodeToRemove = findNode(children, elementNameToRemove);
			childrenToRemove.add(nodeToRemove);
		}
		
		childrenToRemove.removeAll(Collections.singleton(null));
		for(Node childNodeToRemove : childrenToRemove)
		{
			nodeToRemoveElementsFrom.removeChild(childNodeToRemove);
		}
	}

	public static String createLegacyTncProjectPlaceTypesElementName()
	{
		return Xmpz2XmlConstants.TNC_PROJECT_DATA + "TNC" +  MigrationTo11.LEGACY_TAG_TNC_PROJECT_TYPES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG;
	}

	public static String createLegacyTncOrganizationalPrioritiesElementName()
	{
		return Xmpz2XmlConstants.TNC_PROJECT_DATA + "TNC" + MigrationTo11.LEGACY_TAG_TNC_ORGANIZATIONAL_PRIORITIES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG;
	}

	private boolean updateXmpz2SchemaVersionToCurrentVersion(Element rootElement) throws Exception
	{
		boolean schemaVersionWasUpdated = false;
		int readInSchemaVersion = getXmpz2DocumentSchemaVersion(rootElement);
		if (readInSchemaVersion < LOWEST_SCHEMA_VERSION)
		{
			throw new XmlVersionTooOldException(Integer.toString(LOWEST_SCHEMA_VERSION), Integer.toString(readInSchemaVersion));
		}
		
		if (readInSchemaVersion <  Integer.parseInt(NAME_SPACE_VERSION))
		{
			setNameSpaceVersion(rootElement, NAME_SPACE_VERSION);
			schemaVersionWasUpdated = true;
		}

		return schemaVersionWasUpdated;
	}

	private int getXmpz2DocumentSchemaVersion(Element rootElement) throws Exception
	{
		final String currentNamespace = getNameSpace(rootElement);
		String readInSchemaVersionAsString = AbstractXmlImporter.getSchemaVersionToImport(currentNamespace);
		return Integer.parseInt(readInSchemaVersionAsString);
	}

	private String getAttributeValue(Node elementNode, String attributeName)
	{
		NamedNodeMap attributes = elementNode.getAttributes();
		Node attributeNode = attributes.getNamedItem(attributeName);
		return attributeNode.getNodeValue();
	}

	public static void setNameSpaceVersion(Element rootElement, String newNameSpaceVersion) throws Exception
	{
		final String attributeName = getNamespaceAttributeName(rootElement);
		final String nameSpaceWithVersion = PARTIAL_NAME_SPACE + newNameSpaceVersion;
		rootElement.setAttribute(attributeName, nameSpaceWithVersion);
	}

	private static String getNameSpace(Element rootElement) throws Exception
	{
		return rootElement.getAttribute(getNamespaceAttributeName(rootElement));
	}

	private static String getNamespaceAttributeName(Element rootElement) throws Exception
	{
		final String alias = getNameSpaceAliasName(rootElement);
		return  XMLNS + COLON + alias;
	}
	
	private static String getNameSpaceAliasName(Element rootElement) throws Exception
	{
		NamedNodeMap attributes = rootElement.getAttributes();
		for (int index = 0; index < attributes.getLength(); ++index)
		{
			final Node attribute = attributes.item(index);
			final String nodeName = attribute.getNodeName();
			if (nodeName.startsWith(Xmpz2XmlConstants.XMLNS))
				return extractAlias(nodeName);
		}
		throw new Exception("Could not find xmlns attribute!");
	}

	private static String extractAlias(String namespaceAttributeName)
	{
		final String aliasNameLeftOver = namespaceAttributeName.replaceAll(Xmpz2XmlConstants.XMLNS + Xmpz2XmlConstants.COLON, "");
		
		return aliasNameLeftOver;
	}

	public static Document convertToDocument(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		InputSource inputSource = new InputSource(projectAsInputStream);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(inputSource);
		
		return document;
	}

	private String getFormattedNodeContent(Node node) throws Exception
	{
		Document document = HtmlUtilities.createDomDocument(HtmlUtilities.wrapInHtmlTags(""));
		document.getDocumentElement().setAttributeNS(Xmpz2XmlConstants.W3_XMLNS, Xmpz2XmlConstants.NAME_SPACE_ATTRIBUTE_NAME, Xmpz2XmlConstants.NAME_SPACE);
		return Xmpz2XmlImporter.getFormattedNodeContent(node, document);
	}

	private static final int LOWEST_SCHEMA_VERSION = Xmpz2XmlConstants.LOWEST_SCHEMA_VERSION;
	private static final String NAME_SPACE_VERSION = Xmpz2XmlConstants.NAME_SPACE_VERSION;
	private static final String XMLNS = "xmlns";
	private static final String COLON = ":";
	private static final String PARTIAL_NAME_SPACE = "http://xml.miradi.org/schema/ConservationProject/";
}
