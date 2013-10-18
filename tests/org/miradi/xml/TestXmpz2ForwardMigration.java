package org.miradi.xml;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.xmpz2.Xmpz2ForwardMigration;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;
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
	
	public void testImportingRemovedTncFields() throws Exception
	{
		UnicodeXmlWriter projectWriter = TestXmpz2XmlImporter.createWriter(getProject());
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		Document document = Xmpz2ForwardMigration.convertToDocument(stringInputputStream);
		Element rootElement = document.getDocumentElement();
		NodeList tncProjectDataNodes = rootElement.getElementsByTagName(Xmpz2XmlConstants.PREFIX + Xmpz2XmlConstants.TNC_PROJECT_DATA);
		for (int index = 0; index < tncProjectDataNodes.getLength(); ++index)
		{
			Node node = tncProjectDataNodes.item(index);
			
			Element legacyOrganizationalPrioritiesNode = document.createElement(Xmpz2XmlConstants.PREFIX + Xmpz2ForwardMigration.createLegacyTncOrganizationlPrioritesElementName());
			Element legacyOrganizationalPrioritiesCodeNode = document.createElement(Xmpz2XmlConstants.CODE_ELEMENT_NAME);
			legacyOrganizationalPrioritiesCodeNode.setTextContent("Some value");
			legacyOrganizationalPrioritiesNode.appendChild(legacyOrganizationalPrioritiesCodeNode);
			node.appendChild(legacyOrganizationalPrioritiesNode);
			
			Element legacyProjectPlaceTypesNode = document.createElement(Xmpz2XmlConstants.PREFIX + Xmpz2ForwardMigration.createLegacyTncProjectPlaceTypesElementName());
			Element legacyProjectPlaceTypesCodeNode = document.createElement(Xmpz2XmlConstants.CODE_ELEMENT_NAME);
			legacyProjectPlaceTypesCodeNode.setTextContent("Some value");
			legacyProjectPlaceTypesNode.appendChild(legacyProjectPlaceTypesCodeNode);
			node.appendChild(legacyProjectPlaceTypesNode);
		}
		
		String updatedXmlAsString = HtmlUtilities.toXmlString(document);
		System.out.println(updatedXmlAsString);
		Xmpz2ForwardMigration migration = new Xmpz2ForwardMigration();
		InputStreamWithSeek inputStream = migration.migrate(new StringInputStreamWithSeek(updatedXmlAsString));
		
		if (!new Xmpz2XmlValidator().isValid(inputStream))
			fail("Project should validate after xml has been migrated?");
	}
	
	public void testImportingLegalOlderSchemaVersion() throws Exception
	{
		UnicodeXmlWriter projectWriter = TestXmpz2XmlImporter.createWriter(getProject());
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		Document document = Xmpz2ForwardMigration.convertToDocument(stringInputputStream);
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
}
