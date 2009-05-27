/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class TestConProXmlImporter extends TestCaseWithProject
{
	public TestConProXmlImporter(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
	}
	
	public void testImportConProProject() throws Exception
	{
		getProject().populateEverything();
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
		
		File afterXmlOutFile = createTempFileFromName("conproAfterFirstImport.xml");
		ProjectForTesting projectToFill1 = new ProjectForTesting("ProjectToFill1");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			String firstExport = convertFileContentToString(beforeXmlOutFile);
			
			importProject(beforeXmlOutFile, projectToFill1);
			verifyThreatStressRatingPoolContents(getProject(), projectToFill1);
			verifyObjectiveLabelsAndUnsplitLabel(projectToFill1);
			stripDelimiterTagFromObjectiveNames(projectToFill1);
			
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
	
	private void verifyObjectiveLabelsAndUnsplitLabel(ProjectForTesting projectToFill1) throws Exception
	{
		ORefList objectiveRefs = projectToFill1.getObjectivePool().getORefList();
		for (int index = 0; index < objectiveRefs.size(); ++index)
		{
			Objective objective = Objective.find(projectToFill1, objectiveRefs.get(index));
			String rawLabel = objective.getLabel();
			String expectedLabel = "123|Some Objective label|Some objective full text data";
			assertEquals("wrong objective label?", expectedLabel, rawLabel);
			
			
			String[] splittedFields = rawLabel.split("\\|");
			objective.setData(Objective.TAG_SHORT_LABEL, splittedFields[0]);
			objective.setData(Objective.TAG_LABEL, splittedFields[1]);
			objective.setData(Objective.TAG_FULL_TEXT, splittedFields[2]);
		}
	}
	
	private void stripDelimiterTagFromObjectiveNames(ProjectForTesting projectToFill1) throws Exception
	{
		ORefList objectiveRefs = projectToFill1.getObjectivePool().getORefList();
		for (int index = 0; index < objectiveRefs.size(); ++index)
		{
			Objective objective = Objective.find(projectToFill1, objectiveRefs.get(index));
			String rawLabel = objective.getLabel();
			String strippedLabel = rawLabel.replaceAll("\\|", "");
			projectToFill1.setObjectData(objectiveRefs.get(index), Objective.TAG_LABEL, strippedLabel);
		}
	}

	private void verifyThreatStressRatingPoolContents(ProjectForTesting project, ProjectForTesting filledProject)
	{
		int originalProjectObjectCount =  getProject().getPool(ThreatStressRating.getObjectType()).getRefList().size();	
		int filledProjectObjectCount =  filledProject.getPool(ThreatStressRating.getObjectType()).getRefList().size();
		assertEquals("not same Threat stress rating object count?", originalProjectObjectCount, filledProjectObjectCount);
	}

	public void verifyImportingMethods() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImport.xml");
		ProjectForTesting projectWithMethodData = new ProjectForTesting("ProjectWithMethodData");
		
		Indicator indicator = projectWithMethodData.createAndPopulateIndicator();		
		createStrategyWithIndicator(projectWithMethodData, indicator);
		
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
			projectWithMethodData.close();
		}
		
	}
	
	private void verifyImportingWhos() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproBeforeImportTestingWho.xml");
		ProjectForTesting projectWithWhoData = new ProjectForTesting("ProjectWithWhoData");
		Indicator indicator = projectWithWhoData.createAndPopulateIndicator();
		
		createStrategyWithIndicator(projectWithWhoData, indicator);
		
		Task task = projectWithWhoData.createAndPopulateTask("some label");
		ORefList taskRefs = new ORefList(task.getRef());
		indicator.setData(Indicator.TAG_METHOD_IDS, taskRefs.convertToIdList(Task.getObjectType()).toString());

		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFillWithWho");
		try
		{
			exportProject(beforeXmlOutFile, projectWithWhoData);
			importProject(beforeXmlOutFile, projectToFill);
			
			ORefList indicatorRefs = projectToFill.getIndicatorPool().getORefList();
			
			assertTrue("has no indicator?", indicatorRefs.size() > 1);
			assertTrue("ref list does not contain indicator ref?", indicatorRefs.contains(indicator.getRef()));
		}
		finally 
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
			projectWithWhoData.close();
		}
	}

	private void createStrategyWithIndicator(ProjectForTesting project, Indicator indicator)	throws Exception
	{
		Strategy strategy = project.createAndPopulateStrategy();
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(indicator.getId());
		strategy.setData(Strategy.TAG_INDICATOR_IDS, indicatorIds.toString());
	}

	private void importProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{		
		ConProXmlImporter conProXmlImporter = new ConProXmlImporter(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		try
		{
			conProXmlImporter.importConProProject(fileInputStream);
		}
		finally
		{
			fileInputStream.close();
		}
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
