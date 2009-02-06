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
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Factor;
import org.miradi.objects.Target;

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
			project.createObjectAndReturnId(ObjectType.TARGET, targetId);
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
						Factor target = Target.find(unzippedProject, new ORef(Target.getObjectType(), targetId));
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
