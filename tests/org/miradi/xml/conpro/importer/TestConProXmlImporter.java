/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.xml.conpro.importer;

import java.io.File;

import org.martus.util.UnicodeReader;
import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Target;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class TestConProXmlImporter extends TestCaseWithProject
{
	public TestConProXmlImporter(String name)
	{
		super(name);
	}
	
	public void testImportConProProject() throws Exception
	{
		getProject().fillProjectPartially();
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
		
		new ConproXmlExporter(getProject()).export(beforeXmlOutFile);
		String beforeImportAsString = convertFileContentToString(beforeXmlOutFile);
		
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		new ConProXmlImporter(projectToFill).populateProjectFromFile(beforeXmlOutFile);
		
		File afterXmlOutFile = createTempFileFromName("conproAfterImport.xml");
		new ConproXmlExporter(projectToFill).export(afterXmlOutFile);
		String afterImportAsString = convertFileContentToString(afterXmlOutFile);
		
		//FIXME temporarly made into NotEquals so test passes for commit
		assertEquals("incorrect project values after import?", beforeImportAsString, afterImportAsString);
	}
	
	private String convertFileContentToString(File fileToConvert) throws Exception
	{
	    return new UnicodeReader(fileToConvert).readAll();
	}
	
	public void testGenereratXPath()
	{
		String expectedPath = "cp:SomeElement/cp:SomeOtherElement";
		
		String[] pathElements = new String[]{"SomeElement", "SomeOtherElement"}; 
		String generatedPath = new ConProXmlImporter(getProject()).generatePath(pathElements);
		assertEquals("xpaths are not same?", expectedPath, generatedPath);
	}
	
	public void testHighestId() throws Exception
	{
		getProject().createObject(Target.getObjectType(), new BaseId(400));
		int highestIdAfterCreate2 = getProject().getNodeIdAssigner().getHighestAssignedId();
		assertEquals("wrong highest greater than current highest id?", 400, highestIdAfterCreate2);
		
		int highestIdBeforeCreate = getProject().getNodeIdAssigner().getHighestAssignedId();
		ORef newTargetRef = getProject().createObject(Target.getObjectType(), new BaseId(20));
		assertEquals("wrong id?", "20", newTargetRef.getObjectId().toString());

		int highestIdAfterCreate = getProject().getNodeIdAssigner().getHighestAssignedId();
		assertEquals("wrong id less than current highest id?", highestIdBeforeCreate, highestIdAfterCreate);
	}
}
