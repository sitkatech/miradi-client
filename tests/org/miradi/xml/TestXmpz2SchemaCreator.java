/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import java.io.IOException;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.utils.StringUtilities;
import org.miradi.xml.xmpz2.Xmpz2XmlValidator;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2SchemaWriter;
import org.miradi.xml.xmpz2.xmpz2schema.Xmpz2XmlSchemaCreator;

public class TestXmpz2SchemaCreator extends TestAbstractSchemaCreator
{
	public TestXmpz2SchemaCreator(String name)
	{
		super(name);
	}
	
	public void testAgainstStaticSchema() throws Exception
	{
		String expectedSchema = normalizeSchemas(getExpectedLines());
		String actualSchema = normalizeSchemas(getActualSchema().trim());
		
		assertEquals("Generated schema doesnt match existing?", expectedSchema, actualSchema);
	}

	private String normalizeSchemas(String linesInSchema)
	{
		final String normalizedNewLines = linesInSchema.replaceAll("\\r\\n", "\n");
		return removeHeaderComments(normalizedNewLines);
	}

	private String removeHeaderComments(final String normalizedNewLines)
	{
		String[] lines = normalizedNewLines.split(StringUtilities.NEW_LINE);
		StringBuffer appendedLines = new StringBuffer();
		for (String line : lines)
		{
			if (!line.startsWith("#"))
			{
				appendedLines.append(line);
				appendedLines.append(StringUtilities.NEW_LINE);
			}
		}
		
		return appendedLines.toString();
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

	@Override
	public String getStaticSchemaPath()
	{
		return Xmpz2XmlValidator.XMPZ2_SCHEMA_FILE_RELATIVE_PATH;
	}

}
