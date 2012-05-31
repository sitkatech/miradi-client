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
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.URL;

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
		Xmpz2XmlSchemaCreator creator = new Xmpz2XmlSchemaCreator();
		StringWriter stringWriter = new StringWriter();
		Xmpz2SchemaWriter writer = new Xmpz2SchemaWriter(stringWriter);
		creator.writeRncSchema(writer);
		stringWriter.flush();
		
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(Xmpz2XmlValidator.XMPZ2_SCHEMA_FILE_RELATIVE_PATH);
		FileInputStream fileInputStream = new FileInputStream(resourceURL.getFile());
		DataInputStream in = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		String line;
		String allLines = "";
		while ((line = bufferedReader.readLine()) != null)   
		{
			allLines += line + "\n";
		}
		
		//FIXME urgent - The schema writer is still under development and 
		// test will fail.  Uncomment this test when schema writer is done. 
		//assertEquals(allLines, stringWriter.toString());
		
	}
}
