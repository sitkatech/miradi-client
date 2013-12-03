package org.miradi.xml;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.MigrationTo10;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.xmpz2.Xmpz2ForwardMigration;
import org.miradi.xml.xmpz2.Xmpz2XmlSilentValidatorForTesting;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

public class TestXmpz2ForwardMigration extends TestCaseWithProject
{
	public TestXmpz2ForwardMigration(String name)
	{
		super(name);
	}
	
	public void validateMigratingEmptyProject() throws Exception
	{
		Document document = convertProjectToDocument();
		verifyMigratedXmpz2(document);
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
			
			appendContainerWithSampleCode(document, node, Xmpz2ForwardMigration.createLegacyTncOrganizationlPrioritesElementName());
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
		Document document = convertProjectToDocument();
		Element rootElement = document.getDocumentElement();
		Xmpz2ForwardMigration.setNameSpaceVersion(rootElement, "228");
		
		String updatedXmlAsString = HtmlUtilities.toXmlString(document);
		if (new Xmpz2XmlSilentValidatorForTesting().isValid(new StringInputStreamWithSeek(updatedXmlAsString)))
			fail("Project should not validate due to incorrect schema?");
		
		Xmpz2ForwardMigration migration = new Xmpz2ForwardMigration();
		InputStreamWithSeek inputStream = migration.migrate(new StringInputStreamWithSeek(updatedXmlAsString));
		if (!new Xmpz2XmlValidator().isValid(inputStream))
			fail("Project should validate after xml has been migrated?");
	}
	
	private Document convertProjectToDocument() throws Exception
	{
		UnicodeXmlWriter projectWriter = TestXmpz2XmlImporter.createWriter(getProject());
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		Document document = Xmpz2ForwardMigration.convertToDocument(stringInputputStream);
		return document;
	}
	
	private void verifyMigratedXmpz2(Document document) throws Exception
	{
		String updatedXmlAsString = HtmlUtilities.toXmlString(document);
		Xmpz2ForwardMigration migration = new Xmpz2ForwardMigration();
		InputStreamWithSeek inputStream = migration.migrate(new StringInputStreamWithSeek(updatedXmlAsString));
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
