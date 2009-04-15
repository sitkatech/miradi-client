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
import org.miradi.exceptions.UnsupportedOldVersionSchemaException;
import org.miradi.ids.BaseId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Objective;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.ProjectForTesting;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;
import org.miradi.xml.conpro.exporter.ConproXmlExporterVersion2;

public class TestConproXmlImporterVersion2 extends TestCaseWithProject
{
	public TestConproXmlImporterVersion2(String name)
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
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		
		File afterXmlOutFile = createTempFileFromName("conproVersion2AfterFirstImport.xml");
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

	private void importProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{		
		ConproXmlImporterVersion2 conProXmlImporter = new ConproXmlImporterVersion2(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		try
		{
			//FIXME this is a temp method to output the xml into a file. remove when class is done 
			//OutputToFileForDevelopment(fileInputStream);	
			conProXmlImporter.importConProProject(fileInputStream);
		}
		finally
		{
			fileInputStream.close();	
		}
	}

//	private void OutputToFileForDevelopment(FileInputStreamWithSeek fileInputStream) throws FileNotFoundException, IOException
//	{
//		File outFile = new File("c:\\conproImportTempFile.xml");
//		FileOutputStream out = new FileOutputStream(outFile);
//		byte buf[]=new byte[1024];
//		int len;
//		while((len=fileInputStream.read(buf))>0)
//		{
//			out.write(buf,0,len);
//		}
//		out.close();
//		
//		fileInputStream.seek(0);
//	}

	private void exportProject(File afterXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{
		new ConproXmlExporterVersion2(projectToFill1).export(afterXmlOutFile);
	}

	private String convertFileContentToString(File fileToConvert) throws Exception
	{
	    return new UnicodeReader(fileToConvert).readAll();
	}
	
	public void testGenereratXPath() throws Exception
	{
		String expectedPath = "cp:SomeElement/cp:SomeOtherElement";
		
		String[] pathElements = new String[]{"SomeElement", "SomeOtherElement"}; 
		String generatedPath = new ConproXmlImporterVersion2(getProject()).generatePath(pathElements);
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
	
	public void testUnsupportedOldSchemaVersion() throws Exception
	{
		getProject().populateEverything();
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		ProjectForTesting projectToFill1 = new ProjectForTesting("ProjectToFill1");
		try
		{
			new ConproXmlExporter(getProject()).export(beforeXmlOutFile);
			importOldProject(beforeXmlOutFile, projectToFill1);
			fail("should have fialed due to importing unsupported old project");
		}
		catch (UnsupportedOldVersionSchemaException ignoreException)
		{
		}
		finally
		{
			beforeXmlOutFile.delete();
			projectToFill1.close();
		}
	}
	
	private void importOldProject(File beforeXmlOutFile, ProjectForTesting projectToFill1) throws Exception
	{		
		ConproXmlImporterVersion2 conProXmlImporter = new ConproXmlImporterVersion2(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		conProXmlImporter.importConProProject(fileInputStream);
	}
}
