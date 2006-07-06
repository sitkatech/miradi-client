/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.conservationmeasures.eam.diagram.nodetypes.NodeTypeTarget;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ConceptualModelNode;
import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;

public class TestProjectUnzipper extends EAMTestCase
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
		int targetId = 39;
		
		File originalDirectory = createTempDirectory();
		try
		{
			Project project = new Project();
			project.createOrOpen(originalDirectory);
			project.insertNodeAtId(new NodeTypeTarget(), targetId);
			
			File zip = createTempFile();
			try
			{
				ProjectZipper.createProjectZipFile(zip, originalDirectory);
				EAM.setLogToString();
				EAM.setLogLevel(EAM.LOG_DEBUG);
				boolean isImportable = ProjectUnzipper.isZipFileImportable(zip);
				assertTrue("isn't importable? " + EAM.getLoggedString(), isImportable);
				File unzippedDirectory = createTempDirectory(); 
				try
				{
					ProjectUnzipper.unzipToProjectDirectory(zip, unzippedDirectory);
					Project unzippedProject = new Project();
					unzippedProject.createOrOpen(unzippedDirectory);
					ConceptualModelNode target = unzippedProject.findNode(targetId);
					assertNotNull("didn't find the target we wrote?", target);
				}
				finally
				{
					DirectoryUtils.deleteEntireDirectoryTree(unzippedDirectory);
				}
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
