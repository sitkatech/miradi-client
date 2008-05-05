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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class TestConProXmlImporter extends TestCaseWithProject
{
	public TestConProXmlImporter(String name)
	{
		super(name);
	}
	
	public void verifyImportingMethods() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
		ProjectForTesting projectWithMethodData = new ProjectForTesting("ProjectWithMethodData");
		projectWithMethodData.createAndPopulateIndicator();
		
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		try
		{
			exportProject(beforeXmlOutFile, projectWithMethodData);
			importProject(beforeXmlOutFile, projectToFill);
			
			Task[] allTasks = projectToFill.getTaskPool().getAllTasks();
			assertTrue("has no tasks?", allTasks.length > 0);
			for (int index = 0; index < allTasks.length; ++index)
			{
				if (allTasks[index].isMethod())
				{
					assertEquals("not same task name?", ConProXmlImporter.SEE_DETAILS_FIELD_METHOD_NAME, allTasks[index].getData(Task.TAG_LABEL));
					return;
				}
			}
			
			fail("no methods found?");
		}
		finally 
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
		}
		
	}
	
	public void testImportConProProject() throws Exception
	{
		getProject().fillProjectPartially();
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
		
		File afterXmlOutFile = createTempFileFromName("conproAfterFirstImport.xml");
		ProjectForTesting projectToFill1 = new ProjectForTesting("ProjectToFill1");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			String firstExport = convertFileContentToString(beforeXmlOutFile);
			
			importProject(beforeXmlOutFile, projectToFill1);
			
			exportProject(afterXmlOutFile, projectToFill1);
			String secondExport = convertFileContentToString(afterXmlOutFile);
			assertEquals("incorrect project values after first import?", firstExport, secondExport);
			
			verifyImportingMethods();
			verifyImportingWhos();
		}
		finally
		{
			beforeXmlOutFile.delete();
			afterXmlOutFile.delete();
			
			projectToFill1.close();
		}
	}

	private void verifyImportingWhos() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImportTestingWho.xml");
		ProjectForTesting projectWithWhoData = new ProjectForTesting("ProjectWithWhoData");
		Indicator indicator = projectWithWhoData.createAndPopulateIndicator();
		Task task = projectWithWhoData.createTaskWithWho();
		ORefList taskRefs = new ORefList(task.getRef());
		indicator.setData(Indicator.TAG_TASK_IDS, taskRefs.convertToIdList(Task.getObjectType()).toString());

		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFillWithWho");
		try
		{
			exportProject(beforeXmlOutFile, projectWithWhoData);
			importProject(beforeXmlOutFile, projectToFill);
			
			Indicator[] allIndicators = projectToFill.getIndicatorPool().getAllIndicators();
			
			assertTrue("has no indicator?", allIndicators.length == 1);
			String indicatorDetailsText = allIndicators[0].getData(Indicator.TAG_DETAIL);
			assertTrue("indicator has no detail?", indicatorDetailsText.length() > 0);
			String detailsTextWitoutSemiColonSeperator = indicatorDetailsText.replace(";", "");
			assertEquals("wrong project resource name?", ProjectForTesting.PROJECT_RESOURCE_LABEL_TEXT, detailsTextWitoutSemiColonSeperator);
		}
		finally 
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
		}
	}

	private void importProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{
		new ConProXmlImporter(projectToFill1).populateProjectFromFile(beforeXmlOutFile);
	}

	private void exportProject(File afterXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{
		new ConproXmlExporter(projectToFill1).export(afterXmlOutFile);
	}

	private String convertFileContentToString(File fileToConvert) throws Exception
	{
	    return new UnicodeReader(fileToConvert).readAll();
	}
	
	public void testGenereratXPath() throws Exception
	{
		String expectedPath = "cp:SomeElement/cp:SomeOtherElement";
		
		String[] pathElements = new String[]{"SomeElement", "SomeOtherElement"}; 
		String generatedPath = new ConProXmlImporter(getProject()).generatePath(pathElements);
		assertEquals("xpaths are not same?", expectedPath, generatedPath);
	}
	
	public void testHighestId() throws Exception
	{
		getProject().createObject(Target.getObjectType(), new BaseId(400));
		int highestId1 = getProject().getNodeIdAssigner().getHighestAssignedId();
		assertEquals("wrong highest greater than current highest id?", 400, highestId1);
		
		int highestId2 = getProject().getNodeIdAssigner().getHighestAssignedId();
		ORef newTargetRef = getProject().createObject(Target.getObjectType(), new BaseId(20));
		assertEquals("wrong id?", 20, newTargetRef.getObjectId().asInt());

		int highestId3 = getProject().getNodeIdAssigner().getHighestAssignedId();
		assertEquals("wrong id less than current highest id?", highestId2, highestId3);
	}
}
