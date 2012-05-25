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

import org.martus.util.UnicodeStringWriter;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class TestXmpz2Importer extends TestCaseWithProject
{
	public TestXmpz2Importer(String name)
	{
		super(name);
	}
	
	public void testImportEmptyProject() throws Exception
	{
		validateUsingStringWriter();
	}
	
	public void testImportFilledProject() throws Exception
	{
		getProject().populateEverything();
		validateUsingStringWriter();
	}
	
	private ProjectForTesting validateUsingStringWriter() throws Exception
	{
		UnicodeStringWriter projectWriter = createWriter(getProject());
		
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(projectToImportInto);
		
		String exportedProjectXml = projectWriter.toString();
		StringInputStreamWithSeek stringInputputStream = new StringInputStreamWithSeek(exportedProjectXml);
		try
		{
			xmlImporter.importProject(stringInputputStream);
		}
		finally
		{
			stringInputputStream.close();	
		}
		
		UnicodeStringWriter secondWriter = createWriter(projectToImportInto);
		assertEquals("Exports from projects do not match?", exportedProjectXml, secondWriter.toString());
		
		return projectToImportInto;
	}

	private UnicodeStringWriter createWriter(ProjectForTesting projectToUse) throws Exception
	{
		Xmpz2XmlExporter exporter = new Xmpz2XmlExporter(projectToUse);
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		exporter.exportProject(writer);
		writer.flush();
		
		return writer;
	}
}
