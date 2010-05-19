/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.io.File;

import org.martus.util.UnicodeReader;
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.wcs.WcsXmlExporter;

public class TestXmpzXmlImporter extends TestCaseWithProject
{
	public TestXmpzXmlImporter(String name)
	{
		super(name);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateProject();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		getProject().populateEverything();
		validateProject();
	}
	
	private void validateProject() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("XmlBeforeImport.xml");		
		File afterXmlOutFile = createTempFileFromName("XmlAfterFirstImport.xml");
		ProjectForTesting projectToFill1 = new ProjectForTesting("ProjectToFill1");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			String firstExport = convertFileContentToString(beforeXmlOutFile);
			
			importProject(beforeXmlOutFile, projectToFill1);
			
			exportProject(afterXmlOutFile, projectToFill1);
			String secondExport = convertFileContentToString(afterXmlOutFile);
			assertEquals("incorrect project values after first import?", firstExport, secondExport);
		}
		finally
		{
			beforeXmlOutFile.delete();
			afterXmlOutFile.delete();
			projectToFill1.close();
		}	
	}
	
	private void exportProject(File afterXmlOutFile, ProjectForTesting projectToExport) throws Exception
	{
		new WcsXmlExporter(projectToExport).export(afterXmlOutFile);
	}

	private void importProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{		
		XmpzXmlImporter xmlImporter = new XmpzXmlImporter(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		try
		{
			xmlImporter.importProject(fileInputStream);
		}
		finally
		{
			fileInputStream.close();	
		}
	}
	
	private String convertFileContentToString(File fileToConvert) throws Exception
	{
	    return new UnicodeReader(fileToConvert).readAll();
	}
}
