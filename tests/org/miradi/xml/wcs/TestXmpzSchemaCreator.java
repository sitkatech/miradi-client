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

package org.miradi.xml.wcs;

import java.io.IOException;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.UnicodeWriter;
import org.miradi.xml.generic.SchemaWriter;
import org.miradi.xml.generic.XmlSchemaCreator;

public class TestXmpzSchemaCreator extends TestAbstractSchemaCreator
{
	public TestXmpzSchemaCreator(String name)
	{
		super(name);
	}

	public void testAgainstStaticSchema() throws Exception
	{
		String expectedSchema = getExpectedLines().trim();
		String actualSchema = getActualSchema().trim();
		
		assertEquals("Generated schema doesnt match existing?", expectedSchema, actualSchema);
	}
	
	public String getActualSchema() throws Exception, IOException
	{
		XmlSchemaCreator creator = new XmlSchemaCreator();
		UnicodeWriter stringWriter = UnicodeStringWriter.create();
		creator.printXmlRncSchema(new SchemaWriter(stringWriter));
		stringWriter.flush();

		return stringWriter.toString();
	}
	
	@Override
	public String getStaticSchemaPath()
	{
		return WcsMiradiXmlValidator.WCS_MIRADI_SCHEMA_FILE_RELATIVE_PATH;
	}
}
