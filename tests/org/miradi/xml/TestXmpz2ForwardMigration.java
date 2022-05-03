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

package org.miradi.xml;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.MigrationTo10;
import org.miradi.migrations.forward.MigrationTo19;
import org.miradi.migrations.forward.MigrationTo20;
import org.miradi.objects.BaseObject;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.xmpz2.*;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.Vector;


public class TestXmpz2ForwardMigration extends TestCaseWithProject
{
	public TestXmpz2ForwardMigration(String name)
	{
		super(name);
	}
	
	public void testRenameTncFieldsMigration() throws Exception
	{
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		NodeList tncProjectDataNodes = rootElement.getElementsByTagName(PREFIX + TNC_PROJECT_DATA);
		for (int index = 0; index < tncProjectDataNodes.getLength(); ++index)
		{
			Node node = tncProjectDataNodes.item(index);
			
			appendChildNodeWithSampleText(document, node, TNC_PROJECT_DATA + MigrationTo10.LEGACY_TAG_MAKING_THE_CASE);
			appendChildNodeWithSampleText(document, node, TNC_PROJECT_DATA + MigrationTo10.LEGACY_TAG_CAPACITY_AND_FUNDING);
		}
		
		verifyMigratedXmpz2(document);
	}

	public void testRenameLeaderResourceFieldsMigration() throws Exception
	{
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		NodeList strategyDataNodes = rootElement.getElementsByTagName(PREFIX + Xmpz2XmlConstants.STRATEGY);
		for (int index = 0; index < strategyDataNodes.getLength(); ++index)
		{
			Node node = strategyDataNodes.item(index);

			appendChildNodeWithSampleText(document, node, Xmpz2XmlConstants.STRATEGY + MigrationTo19.LEGACY_TAG_LEADER_RESOURCE + Xmpz2XmlConstants.ID);
		}

		verifyMigratedXmpz2(document);
	}

	public void testRenameWhenAssignedFieldsMigration() throws Exception
	{
		getProject().createAndPopulateObjectTreeTableConfiguration();

		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();

		NodeList planningViewConfigurationColumnNamesContainer = rootElement.getElementsByTagName(PREFIX + Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION + Xmpz2XmlConstants.COLUMN_CONFIGURATION_CODES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG);
		for (int index = 0; index < planningViewConfigurationColumnNamesContainer.getLength(); ++index)
		{
			Node container = planningViewConfigurationColumnNamesContainer.item(index);

			Element codeWhenTotalNode = document.createElement(PREFIX + CODE_ELEMENT_NAME);
			codeWhenTotalNode.setTextContent(MigrationTo20.LEGACY_READABLE_ASSIGNED_WHEN_TOTAL_CODE);
			container.appendChild(codeWhenTotalNode);
		}

		verifyMigratedXmpz2(document);
	}

	public void testRemoveWhoAssignedFieldsMigration() throws Exception
	{
		getProject().createAndPopulateObjectTreeTableConfiguration();

		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();

		NodeList planningViewConfigurationColumnNamesContainer = rootElement.getElementsByTagName(PREFIX + Xmpz2XmlConstants.OBJECT_TREE_TABLE_CONFIGURATION + Xmpz2XmlConstants.COLUMN_CONFIGURATION_CODES + Xmpz2XmlConstants.CONTAINER_ELEMENT_TAG);
		for (int index = 0; index < planningViewConfigurationColumnNamesContainer.getLength(); ++index)
		{
			Node container = planningViewConfigurationColumnNamesContainer.item(index);

			Element codeWhoTotalNode = document.createElement(PREFIX + CODE_ELEMENT_NAME);
			codeWhoTotalNode.setTextContent(MigrationTo20.LEGACY_READABLE_ASSIGNED_WHO_TOTAL_CODE);
			container.appendChild(codeWhoTotalNode);
		}

		verifyMigratedXmpz2(document);
	}

	private void appendChildNodeWithSampleText(Document document, Node node, final String childElementName)
	{
		appendChildNodeWithSampleText(document, node, childElementName, "Some value");
	}

	private void appendChildNodeWithSampleText(Document document, Node node, final String childElementName, final String value)
	{
		Element childNode = document.createElement(PREFIX + childElementName);
		childNode.setTextContent(value);
		node.appendChild(childNode);
	}
	
	public void testLegacyHumanWellbeingTargetCalculatedThreatRating() throws Exception
	{
		getProject().createHumanWelfareTarget();
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		NodeList humanWellBeingTargets = rootElement.getElementsByTagName(PREFIX + HUMAN_WELFARE_TARGET);
		for (int index = 0; index < humanWellBeingTargets.getLength(); ++index)
		{
			Node node = humanWellBeingTargets.item(index);
			Element nodeToBeRemovedByMigration = document.createElement(PREFIX + HUMAN_WELFARE_TARGET + CALCULATED_THREAT_RATING);
			node.appendChild(nodeToBeRemovedByMigration);
		}
		
		verifyMigratedXmpz2(document);
	}
	
	public void testImportingRemovedTncFields() throws Exception
	{
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		NodeList tncProjectDataNodes = rootElement.getElementsByTagName(PREFIX + TNC_PROJECT_DATA);
		for (int index = 0; index < tncProjectDataNodes.getLength(); ++index)
		{
			Node node = tncProjectDataNodes.item(index);
			
			appendContainerWithSampleCode(document, node, Xmpz2ForwardMigration.createLegacyTncOrganizationalPrioritiesElementName());
			appendContainerWithSampleCode(document, node, Xmpz2ForwardMigration.createLegacyTncProjectPlaceTypesElementName());
		}
		
		verifyMigratedXmpz2(document);
	}

	private void appendContainerWithSampleCode(Document document, Node node, final String containerName)
	{
		Element containerNode = document.createElement(PREFIX + containerName);
		Element codeNode = document.createElement(CODE_ELEMENT_NAME);
		codeNode.setTextContent("Some value");
		containerNode.appendChild(codeNode);
		node.appendChild(containerNode);
	}
	
	public void testImportingLegalOlderSchemaVersion() throws Exception
	{
		int olderSchemaVersion = 228;
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		Xmpz2ForwardMigration.setNameSpaceVersion(rootElement, String.valueOf(olderSchemaVersion));
		
		String updatedXmlAsString = HtmlUtilities.toXmlString(document);
		if (new Xmpz2XmlSilentValidatorForTesting().isValid(new StringInputStreamWithSeek(updatedXmlAsString)))
			fail("Project should not validate due to incorrect schema?");
		
		Xmpz2ForwardMigration migration = new Xmpz2ForwardMigration();
		Xmpz2MigrationResult migrationResult = migration.migrate(new StringInputStreamWithSeek(updatedXmlAsString));
		InputStreamWithSeek inputStream = migrationResult.getStringInputStreamWithSeek();
		if (!new Xmpz2XmlValidator().isValid(inputStream))
			fail("Project should validate after xml has been migrated?");
		assertTrue("Expected schema version to be updated", migrationResult.getSchemaVersionWasUpdated());
		assertEquals("Expected document schema version to match", migrationResult.getDocumentSchemaVersion(), olderSchemaVersion);
	}

	public void testAdditionOfUUIDFields() throws Exception
	{
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();

		NodeList childNodes = rootElement.getChildNodes();

		Vector<ImmutablePair<Node, Node>> nodesToRemove = new Vector<ImmutablePair<Node, Node>>();

		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node node = childNodes.item(index);
			nodesToRemove.addAll(removeUUIDFields(node));
		}

		for (ImmutablePair<Node, Node> nodePair : nodesToRemove)
		{
			nodePair.right.removeChild(nodePair.left);
		}

		verifyMigratedXmpz2(document);
	}

	private Vector<ImmutablePair<Node, Node>> removeUUIDFields(Node node)
	{
		Vector<ImmutablePair<Node, Node>> nodesToRemove = new Vector<ImmutablePair<Node, Node>>();

		if (node.getNodeName().endsWith(BaseObject.TAG_UUID))
		{
			nodesToRemove.add(new ImmutablePair<>(node, node.getParentNode()));
		}

		NodeList childNodes = node.getChildNodes();
		for (int index = 0; index < childNodes.getLength(); ++index)
		{
			Node childNode = childNodes.item(index);
			nodesToRemove.addAll(removeUUIDFields(childNode));
		}

		return nodesToRemove;
	}

	private Document convertProjectToDocument() throws Exception
	{
		UnicodeXmlWriter projectWriter = TestXmpz2XmlImporter.createWriter(getProject());
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputStream = new StringInputStreamWithSeek(exportedProjectXml);
		Document document = Xmpz2ForwardMigration.convertToDocument(stringInputStream);
		return document;
	}
	
	private void verifyMigratedXmpz2(Document document) throws Exception
	{
		String updatedXmlAsString = HtmlUtilities.toXmlString(document);
		Xmpz2ForwardMigration migration = new Xmpz2ForwardMigration();
		Xmpz2MigrationResult migrationResult = migration.migrate(new StringInputStreamWithSeek(updatedXmlAsString));
		InputStreamWithSeek inputStream = migrationResult.getStringInputStreamWithSeek();
		if (!new Xmpz2XmlValidator().isValid(inputStream))
			fail("Project should validate after xml has been migrated?");
	}
	
	private static final String RAW_PREFIX = "miradi";
	private static final String COLON = ":";
	private static final String PREFIX = RAW_PREFIX + COLON;
	private static final String CODE_ELEMENT_NAME = "code";
	private static final String TNC_PROJECT_DATA = "TNCProjectData";
	private static final String HUMAN_WELFARE_TARGET = "HumanWellbeingTarget";
	private static final String CALCULATED_THREAT_RATING = "CalculatedThreatRating";
}
