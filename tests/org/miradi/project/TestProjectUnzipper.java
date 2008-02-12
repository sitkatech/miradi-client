/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.martus.util.DirectoryUtils;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.project.ProjectZipper;

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
		FactorId targetId = new FactorId(39);
		
		File originalDirectory = createTempDirectory();
		try
		{
			Project project = new Project();
			project.createOrOpen(originalDirectory);
			project.createObject(ObjectType.TARGET, targetId);
			project.close();

			File zip = createTempFile();
			try
			{
				ProjectZipper.createProjectZipFile(zip, originalDirectory);
				EAM.setLogToString();
				EAM.setLogLevel(EAM.LOG_DEBUG);
				boolean isImportable = ProjectUnzipper.isZipFileImportable(zip);
				assertTrue("isn't importable? " + EAM.getLoggedString(), isImportable);
				
				String projectFilename = "UnzippedProject";
				File fakeHomeDirectory = createTempDirectory();
				try
				{
					File unzippedDirectory = new File(fakeHomeDirectory, projectFilename);
					Project unzippedProject= new Project();
					try
					{
						ProjectUnzipper.unzipToProjectDirectory(zip, fakeHomeDirectory, projectFilename);
						unzippedProject.createOrOpen(unzippedDirectory);
						Factor target = unzippedProject.findNode(targetId);
						assertNotNull("didn't find the target we wrote?", target);
					}
					finally
					{
						unzippedProject.close();
						DirectoryUtils.deleteEntireDirectoryTree(unzippedDirectory);
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
			DirectoryUtils.deleteEntireDirectoryTree(originalDirectory);
		}

	}
	
	public void testUnzipEmptyFilename() throws Exception
	{
		File originalDirectory = createTempDirectory();
		try
		{
			Project project = new Project();
			project.createOrOpen(originalDirectory);
			project.close();
			
			File zip = createTempFile();
			try
			{
				ProjectZipper.createProjectZipFile(zip, originalDirectory);
				String projectFilename = "";
				File fakeHomeDirectory = createTempDirectory();
				try
				{
					ProjectUnzipper.unzipToProjectDirectory(zip, fakeHomeDirectory, projectFilename);
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
