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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.martus.util.inputstreamwithseek.FileInputStreamWithSeek;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.ProjectResource;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatStressRating;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.Xenodata;
import org.miradi.project.ProjectForTesting;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.questions.TncProjectSharingQuestion;
import org.miradi.questions.TrendQuestion;
import org.miradi.utils.CodeList;
import org.miradi.xml.conpro.ConProMiradiXml;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class TestConproXmlImporter extends TestCaseWithProject
{
	public TestConproXmlImporter(String name)
	{
		super(name);
	}
	
	public void testDuplicateXenodataObjects() throws Exception
	{
		ORef xenodataRef = getProject().createObject(Xenodata.getObjectType());
		getProject().fillObjectUsingCommand(xenodataRef, Xenodata.TAG_PROJECT_ID, "99999");
		assertEquals("wrong xenodata count?", 1, getProject().getPool(Xenodata.getObjectType()).size());
		
		StringRefMap idRefMap = new StringRefMap();
		idRefMap.add(ConProMiradiXml.CONPRO_CONTEXT, xenodataRef);
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_XENODATA_STRING_REF_MAP, idRefMap.toString());
		
		exportImportInto(getProject());
		assertEquals("wrong xenodata count?", 1, getProject().getPool(Xenodata.getObjectType()).size());
	}
	
	public void testAvoidConflictingActivityAndMethodIds() throws Exception
	{
		getProject().createActivity();
		Task method = getProject().createMethod();
		
		ProjectForTesting firstTry = new ProjectForTesting(getName());
		String xml = exportImportInto(firstTry);

		ORefList taskRefs = firstTry.getPool(Task.getObjectType()).getORefList();
		taskRefs.remove(method.getRef());
		ORef conflictingRef = taskRefs.get(0);
		
		String oldMethodId = "<method id=\"" + method.getId() + "\"";
		assertTrue("Couldn't find old method id in xml", xml.contains(oldMethodId));
		String newMethodId = "<method id=\"" + conflictingRef.getObjectId() + "\"";
		xml = xml.replace(oldMethodId, newMethodId);
		
		ProjectForTesting secondTry = new ProjectForTesting(getName());
		importXmlStringIntoProject(xml, secondTry);
	}

	private String exportImportInto(ProjectForTesting firstTry)	throws IOException, Exception, UnsupportedEncodingException
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		UnicodeWriter writer = new UnicodeWriter(bytes);
		new ConproXmlExporter(getProject()).exportProject(writer);
		writer.flush();
		bytes.close();
		String xml = new String(bytes.toByteArray(), "UTF-8");
		
		importXmlStringIntoProject(xml, firstTry);
		return xml;
	}

	private void importXmlStringIntoProject(String xml, ProjectForTesting firstTry) throws UnsupportedEncodingException, Exception, IOException
	{
		InputStreamWithSeek in = new StringInputStreamWithSeek(xml);
		try
		{
			new ConproXmlImporter(firstTry).importConProProject(in);
		}
		finally
		{
			in.close();
		}
	}
	
	public void testImportConProProject() throws Exception
	{
		getProject().populateEverything();
		ORef tncProjectDataRef = getProject().getSingletonObjectRef(TncProjectData.getObjectType());
		
		getProject().fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_SHARING_CODE, TncProjectSharingQuestion.SHARE_WITH_ANYONE);
		verifyImport();
		
		getProject().fillObjectUsingCommand(tncProjectDataRef, TncProjectData.TAG_PROJECT_SHARING_CODE, TncProjectSharingQuestion.SHARE_TNC_ONLY);
		verifyImport();
	}

	private void verifyImport() throws IOException, Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		
		File afterXmlOutFile = createTempFileFromName("conproVersion2AfterFirstImport.xml");
		ProjectForTesting projectToFill1 = new ProjectForTesting("ProjectToFill1");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			String firstExport = convertFileContentToString(beforeXmlOutFile);
			
			verifyImportEmptyProject(beforeXmlOutFile, projectToFill1);
			verifyThreatStressRatingPoolContents(getProject(), projectToFill1);
			verifyObjectiveLabelsAndUnsplitLabel(projectToFill1);
			verifyConcatenatedProjectScopeAndDescription(projectToFill1);
			stripDelimiterTagFromObjectiveNames(projectToFill1);
	
			workAroundCpmzMissingEndDateField(projectToFill1);
			
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

	private void workAroundCpmzMissingEndDateField(ProjectForTesting projectToFill1) throws Exception
	{
		assertEquals("Project End Date exists, so might be time to delete this test?", "", projectToFill1.getMetadata().getExpectedEndDate());
		String expectedEndDate = getProject().getMetadata().getExpectedEndDate();
		projectToFill1.fillObjectUsingCommand(projectToFill1.getMetadata(), ProjectMetadata.TAG_EXPECTED_END_DATE, expectedEndDate);
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
	
	private void verifyConcatenatedProjectScopeAndDescription(ProjectForTesting projectToFill1) throws Exception
	{
		ProjectMetadata projectMetadata = projectToFill1.getMetadata();
		String projectScope = projectMetadata.getProjectScope();
		String expectedProjectScopeValue = "Project Description:\nSome project description\n\nSite/Scope Description:\nSome project scope";
		assertEquals("wrong project scope?", expectedProjectScopeValue, projectScope);
		
		String projectDescription = expectedProjectScopeValue.replaceAll("Project Description:\n", "");
		final String scopeLabel = "Site/Scope Description:";
		int scopeLabelIndex = expectedProjectScopeValue.indexOf(scopeLabel);
		projectDescription = projectDescription.substring(0, scopeLabelIndex - scopeLabel.length());
		projectToFill1.setObjectData(projectMetadata, ProjectMetadata.TAG_PROJECT_DESCRIPTION, projectDescription);
		
		int lastScopeLabelIndex = projectScope.lastIndexOf(scopeLabel);
		projectScope = projectScope.substring(lastScopeLabelIndex, projectScope.length());
		projectScope = projectScope.replaceAll(scopeLabel, "");
		projectScope = projectScope.replaceAll("\n", "");
		projectToFill1.setObjectData(projectMetadata, ProjectMetadata.TAG_PROJECT_SCOPE, projectScope);
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
		ConproXmlImporter conProXmlImporter = new ConproXmlImporter(projectToFill1);
		FileInputStreamWithSeek fileInputStream = new FileInputStreamWithSeek(beforeXmlOutFile); 
		try
		{
			//FIXME urgent: this is a temp method to output the xml into a file. remove when class is done
			// maybe use a runtime flag to decide whether to putput for debugging or not?
			//The first time this is needed again while debugging conpro use the current debugging flag and make 
			//sure the .xml file is placed in the project dir.
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
		String generatedPath = new ConproXmlImporter(getProject()).generatePath(pathElements);
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
	
	public void testProjectWithSharedIndicator() throws Exception
	{
		File xmlFile = createTempFileFromName("$$$$conproVersion2BeforeImport.xml");
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		
		Indicator indicatorToBeShared = projectToFill.createIndicator();
		IdList indicatorIds = new IdList(Indicator.getObjectType());
		indicatorIds.add(indicatorToBeShared.getId());
		
		DiagramFactor diagramFactorThreat = projectToFill.createDiagramFactorAndAddToDiagram(Cause.getObjectType());
		Cause threat = (Cause) diagramFactorThreat.getWrappedFactor();
		projectToFill.fillObjectUsingCommand(threat, Cause.TAG_INDICATOR_IDS, indicatorIds.toString());
		projectToFill.enableAsThreat(threat);
		
		DiagramFactor diagramFactorTarget = projectToFill.createDiagramFactorAndAddToDiagram(Target.getObjectType());
		projectToFill.createDiagramLink(diagramFactorThreat, diagramFactorTarget);
		
		Target target = (Target) diagramFactorTarget.getWrappedFactor();
		projectToFill.turnOnTncMode(target);
		projectToFill.populateTarget(target);
		ORefList keaRefs = target.getKeyEcologicalAttributeRefs();
		KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(projectToFill, keaRefs.get(0));
		indicatorIds.addAll(kea.getIndicatorIds());
		projectToFill.fillObjectUsingCommand(kea, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorIds.toString());
		try
		{
			exportProject(xmlFile, projectToFill);
			importProject(xmlFile, new ProjectForTesting("ImportedProject"));
			fail("Project import should have failed since the indicator is shared between the cause and kea");
		}
		catch (Exception ignoreExpectedException)
		{
		}
		finally
		{
			xmlFile.delete();
			projectToFill.close();
		}
	}
	
	public void testEmptyProject() throws Exception
	{
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		try
		{
			verifyEmpyProject(beforeXmlOutFile);
			verifyImportEmptyProject(beforeXmlOutFile, projectToFill);
		}
		finally
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
		}
	}

	private void verifyImportEmptyProject(File beforeXmlOutFile, ProjectForTesting projectToFill)
	{
		try
		{
			importProject(beforeXmlOutFile, projectToFill);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			fail("Emty project could not be imported?");
		}
	}

	private void verifyEmpyProject(File beforeXmlOutFile)
	{
		try
		{
			new ConproXmlExporter(getProject()).export(beforeXmlOutFile);
		}
		catch (Exception e)
		{
			fail("Emty project could not be exported?");	
		}
	}
	
	public void testProjectResource() throws Exception
	{
		ProjectResource teamMember = getProject().createProjectResource();
		CodeList roleCodes = new CodeList();
		roleCodes.add(ResourceRoleQuestion.TEAM_MEMBER_ROLE_CODE);
		getProject().fillObjectUsingCommand(teamMember, ProjectResource.TAG_ROLE_CODES, roleCodes.toString());
		assertEquals("wrong project resource count?", 1, getProject().getResourcePool().getRefSet().size());
		
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		try
		{
			verifyEmpyProject(beforeXmlOutFile);
			verifyImportEmptyProject(beforeXmlOutFile, projectToFill);
			ORefSet resourceRefs = projectToFill.getResourcePool().getRefSet();
			assertEquals("wrong project resource count?", 1, resourceRefs.size());
			for(ORef resourceRef : resourceRefs)
			{
				ProjectResource resource = ProjectResource.find(projectToFill, resourceRef);
				assertEquals("wrong given name?", "[Unspecified]", resource.getGivenName());
				assertEquals("wrong sur name?", "[Unspecified]", resource.getSurName());
				assertEquals("wrong email?", "[Unspecified]", resource.getEmail());
			}
		}
		finally
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
		}
	}
	
	public void testMeasurementTrendCode() throws Exception
	{
		Measurement measurement = getProject().createMeasurement();
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_TREND, TrendQuestion.STRONG_DECREASE_CODE);
		
		Indicator indicator = getProject().createIndicator();
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, new ORefList(measurement));
		
		File beforeXmlOutFile = createTempFileFromName("conproVersion2BeforeImport.xml");
		ProjectForTesting projectToFill = new ProjectForTesting("ProjectToFill");
		try
		{
			exportProject(beforeXmlOutFile, getProject());
			importProject(beforeXmlOutFile, projectToFill);
			
			ORefList measurementRefs = projectToFill.getPool(Measurement.getObjectType()).getORefList();
			assertEquals("incorrect measurement count?", 1, measurementRefs.size());
			ORef measurementRef = measurementRefs.get(0);
			Measurement importedMeasurement = Measurement.find(projectToFill, measurementRef);
			assertEquals("wrong trend?", TrendQuestion.STRONG_DECREASE_CODE, importedMeasurement.getData(Measurement.TAG_TREND));
		}
		finally
		{
			beforeXmlOutFile.delete();
			projectToFill.close();
		}
	}
}
