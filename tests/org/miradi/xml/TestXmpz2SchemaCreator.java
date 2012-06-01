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
import java.io.StringWriter;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

import org.martus.util.UnicodeStringReader;
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
		HashSet<String> actualLinesSet = getActualLines();
		HashSet<String> expectedLinesSet = getExpectedLines();
		
		expectedLinesSet.removeAll(actualLinesSet);
		PRINT_LEFT_OVER_LINES_FOR_DEV_PURPOSES(expectedLinesSet);
		assertTrue("non matching schema lines found?", expectedLinesSet.size() == 0);
	}

	public HashSet<String> getExpectedLines() throws Exception
	{
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(Xmpz2XmlValidator.XMPZ2_SCHEMA_FILE_RELATIVE_PATH);
		FileInputStream fileInputStream = new FileInputStream(resourceURL.getFile());
		DataInputStream in = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String expectedLine;
		HashSet<String> expectedLinesSet = new HashSet<String>();
		while ((expectedLine = bufferedReader.readLine()) != null)   
		{
			expectedLinesSet.add(expectedLine);
		}

		return expectedLinesSet;
	}

	public HashSet<String> getActualLines() throws Exception, IOException
	{
		StringWriter stringWriter = new StringWriter();
		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(stringWriter);
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator(writer);
		creator.writeRncSchema();
		stringWriter.flush();
		
		UnicodeStringReader reader = new UnicodeStringReader(stringWriter.toString());
		HashSet<String> actualLinesSet = new HashSet<String>();
		String actualLine = null;
		while ((actualLine = reader.readLine()) != null)
		{
			actualLinesSet.add(actualLine);
		}

		return actualLinesSet;
	}

	private void PRINT_LEFT_OVER_LINES_FOR_DEV_PURPOSES(HashSet<String> expectedLinesSet)
	{
		Vector<String> sortedLines = new Vector<String>();
		sortedLines.addAll(expectedLinesSet);
		Collections.sort(sortedLines);
		for(String line : sortedLines)
		{
			//FIXME this is for development only!
			//System.out.println(line);
		}
	}
}
