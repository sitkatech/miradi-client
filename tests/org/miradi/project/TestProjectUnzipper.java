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
package org.miradi.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.martus.util.DirectoryUtils;
import org.miradi.database.ProjectServer;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.MiradiTestCase;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.CreateDiagramFactorLinkParameter;
import org.miradi.objecthelpers.CreateDiagramFactorParameter;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Target;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;

public class TestProjectUnzipper extends MiradiTestCase
{

	public TestProjectUnzipper(String name)
	{
		super(name);
	}
	
	public void testIsZipFileImportableWithTopLevelFile() throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(out);
		ZipEntry entryA = new ZipEntry("fileA");
		ZipEntry entryB = new ZipEntry("dirB/");
		zipOut.putNextEntry(entryA);
		zipOut.putNextEntry(entryB);
		zipOut.close();
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		ZipInputStream zipIn = new ZipInputStream(in);
		assertFalse("allowed top level file? ", ProjectUnzipper.isZipFileImportable(zipIn));
				
	}

	public void testIsZipFileImportableWithTwoTopLevelDirs() throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(out);
		ZipEntry dirA = new ZipEntry("dirA/");
		ZipEntry dirB = new ZipEntry("dirB/fileC");
		zipOut.putNextEntry(dirA);
		zipOut.putNextEntry(dirB);
		zipOut.close();
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		ZipInputStream zipIn = new ZipInputStream(in);
		assertFalse("allowed multiple top level dirs? ", ProjectUnzipper.isZipFileImportable(zipIn));
		
	}

	public void testIsZipFileImportableWithLeadingSlash() throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ZipOutputStream zipOut = new ZipOutputStream(out);
		ZipEntry fileA = new ZipEntry("/fileA");
		ZipEntry dirB = new ZipEntry("dirB/fileC");
		zipOut.putNextEntry(fileA);
		zipOut.putNextEntry(dirB);
		zipOut.close();
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		ZipInputStream zipIn = new ZipInputStream(in);
		assertFalse("allowed entry with leading slash? ", ProjectUnzipper.isZipFileImportable(zipIn));
		
	}
	
	public void testUnzip() throws Exception
	{
		File tempDirectory = createTempDirectory();
		String projectName = "testUnzip";
		try
		{
			ProjectServer database = new ProjectServer();
			database.setLocalDataLocation(tempDirectory);
			ProjectWithHelpers project = new ProjectWithHelpers(database);
			project.createOrOpenWithDefaultObjects(projectName);
			project.loadDiagramModelForTesting();
			
			ORef diagramRef = project.getTestingDiagramObject().getRef();
			IdList diagramFactorIds = new IdList(DiagramFactor.getObjectType());
			IdList diagramLinkIds = new IdList(DiagramLink.getObjectType());

			ORef threatRef = project.createObject(Cause.getObjectType());
			project.setObjectData(threatRef, Cause.TAG_IS_DIRECT_THREAT, BooleanData.BOOLEAN_TRUE);
			ORef threatDiagramFactorRef = project.createObject(DiagramFactor.getObjectType(), new CreateDiagramFactorParameter(threatRef));
			diagramFactorIds.add(threatDiagramFactorRef.getObjectId());

			ORef targetRef = project.createObject(Target.getObjectType());
			ORef targetDiagramFactorRef = project.createObject(DiagramFactor.getObjectType(), new CreateDiagramFactorParameter(targetRef));
			diagramFactorIds.add(targetDiagramFactorRef.getObjectId());

			ORef factorLinkRef = project.createObject(FactorLink.getObjectType(), new CreateFactorLinkParameter(threatRef, targetRef));
			ORef diagramLinkRef = project.createObject(DiagramLink.getObjectType(), new CreateDiagramFactorLinkParameter(factorLinkRef, threatDiagramFactorRef, targetDiagramFactorRef));
			diagramLinkIds.add(diagramLinkRef.getObjectId());
			
			project.setObjectData(DiagramObject.find(project, diagramRef), DiagramObject.TAG_DIAGRAM_FACTOR_IDS, diagramFactorIds.toString());
			project.setObjectData(DiagramObject.find(project, diagramRef), DiagramObject.TAG_DIAGRAM_FACTOR_LINK_IDS, diagramLinkIds.toString());

			SimpleThreatRatingFramework framework = project.getSimpleThreatRatingFramework();
			FactorId threatId = (FactorId) threatRef.getObjectId();
			FactorId targetId = (FactorId) targetRef.getObjectId();
			ThreatRatingBundle bundle = framework.getBundle(threatId, targetId);
			BaseId criterionId = framework.getCriterionIds().get(1);
			BaseId valueId = framework.getValueOptionIds().get(1);
			bundle.setValueId(criterionId, valueId);
			framework.saveBundle(bundle);
			project.close();

			File zip = createTempFile();
			try
			{
				File projectDirectory = new File(tempDirectory, projectName);
				ProjectMpzWriter.createProjectZipFile(zip, projectDirectory);
				EAM.setLogToString();
				EAM.setLogLevel(EAM.LOG_DEBUG);
				boolean isImportable = ProjectUnzipper.isZipFileImportable(zip);
				assertTrue("isn't importable? " + EAM.getLoggedString(), isImportable);
				
				String projectFilename = "UnzippedProject";
				File fakeHomeDirectory = createTempDirectory();
				try
				{
					Project unzippedProject= new Project();
					unzippedProject.setLocalDataLocation(fakeHomeDirectory);
					try
					{
						ProjectUnzipper.unzipToProjectDirectory(zip, fakeHomeDirectory, projectFilename);
						unzippedProject.createOrOpenWithDefaultObjectsAndDiagramHelp(projectFilename);
						assertNotNull("didn't find the target we wrote?", unzippedProject.findObject(targetRef));
						ThreatRatingBundle gotBundle = unzippedProject.getSimpleThreatRatingFramework().getBundle(threatId, targetId);
						assertEquals(valueId, gotBundle.getValueId(criterionId));
					}
					finally
					{
						unzippedProject.close();
					}
				}
				finally
				{
					DirectoryUtils.deleteEntireDirectoryTree(fakeHomeDirectory);
				}
			}
			finally
			{
				EAM.setLogLevel(EAM.LOG_NORMAL);
				EAM.setLogToConsole();
				zip.delete();
			}
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}

	}
	
	public void testUnzipEmptyFilename() throws Exception
	{
		File originalDirectory = createTempDirectory();
		String originalProjectName = "testUnzipEmptyFilename";
		try
		{
			Project project = new Project();
			project.setLocalDataLocation(originalDirectory);
			project.createOrOpenWithDefaultObjectsAndDiagramHelp(originalProjectName);
			project.close();
			
			File zip = createTempFile();
			try
			{
				ProjectMpzWriter.createProjectZipFile(zip, new File(originalDirectory, originalProjectName));
				String emptyFilename = "";
				File fakeHomeDirectory = createTempDirectory();
				try
				{
					ProjectUnzipper.unzipToProjectDirectory(zip, fakeHomeDirectory, emptyFilename);
					fail("Should have thrown for empty filename");
				}
				finally
				{
					DirectoryUtils.deleteEntireDirectoryTree(fakeHomeDirectory);
				}
			}
			catch(Exception ignoreExpected)
			{
			}
			finally
			{
				zip.delete();
			}
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(originalDirectory);
		}
	}

}
