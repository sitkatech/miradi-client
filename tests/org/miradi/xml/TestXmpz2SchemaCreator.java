/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.main.ResourcesHandler;
import org.miradi.main.TestCaseWithProject;
import org.miradi.xml.wcs.Xmpz2XmlValidator;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2SchemaWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class TestXmpz2SchemaCreator extends TestCaseWithProject
{
	public TestXmpz2SchemaCreator(String name)
	{
		super(name);
	}
	
	public void testAgainstStaticSchema() throws Exception
	{
		String expectedSchema = normalizeNewLines(getExpectedLines());
		String actualSchema = normalizeNewLines(getActualSchema());
		
		assertEquals("Generated schema doesnt match existing?", expectedSchema, actualSchema);
	}

	private String normalizeNewLines(String expectedLines)
	{
		return expectedLines.replaceAll("\\r\\n", "\\n");
	}

	public String getExpectedLines() throws Exception
	{
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(Xmpz2XmlValidator.XMPZ2_SCHEMA_FILE_RELATIVE_PATH);
		FileInputStream fileInputStream = new FileInputStream(resourceURL.getFile());
		DataInputStream in = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		StringBuffer allLines = new StringBuffer();
		String expectedLine;
		while ((expectedLine = bufferedReader.readLine()) != null)   
		{
			allLines.append(expectedLine);
			allLines.append("\n");
		}

		return allLines.toString();
	}

	public String getActualSchema() throws Exception, IOException
	{
		UnicodeWriter stringWriter = UnicodeStringWriter.create();
		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(stringWriter);
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator(writer);
		creator.writeRncSchema();
		stringWriter.flush();

		return stringWriter.toString();
	}
}
