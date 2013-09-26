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

package org.miradi.xml.xmpz2;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.exceptions.XmpzVersionTooOldException;
import org.miradi.utils.HtmlUtilities;
import org.miradi.xml.AbstractXmlImporter;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;

public class Xmpz2ForwardMigration implements Xmpz2XmlConstants
{
	public StringInputStreamWithSeek migrate(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		Document document = convertToDocument(projectAsInputStream);
		Element rootElement = document.getDocumentElement();
		final String currentNamespace = getNameSpace(rootElement);
		String readInSchemaVersionAsString = AbstractXmlImporter.getSchemaVersionToImport(currentNamespace);
		int readInSchemaVersion = Integer.parseInt(readInSchemaVersionAsString);
		if (readInSchemaVersion < LOWEST_SCHEMA_VERSION)
		{
			throw new XmpzVersionTooOldException(Integer.toString(LOWEST_SCHEMA_VERSION), readInSchemaVersionAsString);
		}
		
		if (readInSchemaVersion <  Integer.parseInt(NAME_SPACE_VERSION))
		{
			setNameSpaceVersion(rootElement, NAME_SPACE_VERSION);
		}
		
		final String migratedXmlAsString = HtmlUtilities.toXmlString(document);

		return new StringInputStreamWithSeek(migratedXmlAsString);
	}

	public static void setNameSpaceVersion(Element rootElement, String newNameSpaceVersion)
	{
		rootElement.setAttribute(NAME_SPACE_ATTRIBUTE_NAME, PARTIAL_NAME_SPACE + newNameSpaceVersion);
	}

	private String getNameSpace(Element rootElement)
	{
		return rootElement.getAttribute(NAME_SPACE_ATTRIBUTE_NAME);
	}

	public static Document convertToDocument(InputStreamWithSeek projectAsInputStream) throws Exception
	{
		InputSource inputSource = new InputSource(projectAsInputStream);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(inputSource);
		
		return document;
	}
	
	private static final int LOWEST_SCHEMA_VERSION = 228;
}
