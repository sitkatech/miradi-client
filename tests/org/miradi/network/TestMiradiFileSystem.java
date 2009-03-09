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
package org.miradi.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

import org.martus.util.DirectoryUtils;
import org.miradi.main.EAMTestCase;

public class TestMiradiFileSystem extends EAMTestCase
{
	public TestMiradiFileSystem(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		tempDirectory = createTempDirectory();
		remoteFilingSystem = new MiradiRemoteFileSystem(SERVER_NAME, SERVER_PORT, SERVER_APP_PATH);
		localFileSystem = new MiradiLocalFileSystem();
		localFileSystem.setDataDirectory(tempDirectory);
		
		filingSystems = new MiradiFileSystem[] {
				remoteFilingSystem,
				localFileSystem,
		};
	}
	
	@Override
	public void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		super.tearDown();
	}
	
	public void testAllFileSystems() throws Exception
	{
		for(MiradiFileSystem mfs : filingSystems)
		{
			currentFilingSystem = mfs;
			verifyBasics();
			verifyCreateAndDeleteProject();
			verifyDeleteMissingFile();
			verifyDirectoriesWithinProject();
			verifyOperationsOnNonExistantProject();
		}
	}
	
	public void verifyBasics() throws Exception
	{
		String projectName = "TestingBasics";
		File file = new File("/testfile");
		String contents = "This is line 1\nSecond line\n";
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			assertFalse(currentFilingSystem.doesFileExist(projectName, file));
			currentFilingSystem.writeFile(projectName, file, contents);
			assertTrue(currentFilingSystem.doesFileExist(projectName, file));
			String got = currentFilingSystem.readFile(projectName, file);
			assertEquals(contents, got);
		}
		finally
		{
			currentFilingSystem.deleteFile(projectName, file);
		}
		assertFalse(currentFilingSystem.doesFileExist(projectName, file));
	}
	
	public void verifyDeleteMissingFile() throws Exception
	{
		String projectName = "TestingDeleteMissing";
		File file = new File("/ThisFileNeverExists");
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			assertFalse(currentFilingSystem.doesFileExist(projectName, file));
			currentFilingSystem.deleteFile(projectName, file);
			fail("Should have thrown for deleting non-existant file");
		}
		catch(IOException ignoreExpected)
		{
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
	}
	
	public void verifyOperationsOnNonExistantProject() throws Exception
	{
		String projectName = "ThisProjectNeverExists";
		File file = new File("/ThisFileNeverExists");
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);

		assertFalse(currentFilingSystem.doesFileExist(projectName, file));
		try
		{
			currentFilingSystem.readFile(projectName, file);
			fail("Should have thrown for read in non-existant project");
		}
		catch(IOException ignoreExpected)
		{
		}

		try
		{
			currentFilingSystem.deleteFile(projectName, file);
			fail("Should have thrown for delete in non-existant project");
		}
		catch(IOException ignoreExpected)
		{
		}

		try
		{
			currentFilingSystem.writeFile(projectName, file, "data");
			fail("Should have thrown for write in non-existant project");
		}
		catch(IOException ignoreExpected)
		{
		}

		try
		{
			currentFilingSystem.deleteProject(projectName);
			fail("Should have thrown for deleting a non-existant project");
		}
		catch(FileNotFoundException ignoreExpected)
		{
		}
	}
	
	public void verifyCreateAndDeleteProject() throws Exception
	{
		String projectToCreate = "TestingCreateAndDeleteProject";
		assertFalse(currentFilingSystem.doesProjectDirectoryExist(projectToCreate));
		currentFilingSystem.createProject(projectToCreate);
		try
		{
			assertTrue(currentFilingSystem.doesProjectDirectoryExist(projectToCreate));
		}
		finally
		{
			currentFilingSystem.deleteProject(projectToCreate);
		}
		assertFalse(currentFilingSystem.doesProjectDirectoryExist(projectToCreate));
	}
	
	public void verifyDirectoriesWithinProject() throws Exception
	{
		String projectName = "TestingDirectories";
		File file = new File("/a/b/c");
		String contents = "Test Data\n";

		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			currentFilingSystem.writeFile(projectName, file, contents);
			String got = currentFilingSystem.readFile(projectName, file);
			assertEquals("Didn't read back from deep file?", contents, got);
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
		assertFalse(currentFilingSystem.doesProjectDirectoryExist(projectName));
	}
	
	public void testLocalNonDirectoryIsProject() throws Exception
	{
		String projectName = "NonDirectory";
		File nonDirectory = new File(tempDirectory, projectName);
		nonDirectory.deleteOnExit();
		FileOutputStream out = new FileOutputStream(nonDirectory);
		out.write(44);
		out.close();
		try
		{
			assertFalse(localFileSystem.doesProjectDirectoryExist(projectName));
		}
		finally
		{
			nonDirectory.delete();
		}
	}
	
	public void testRemoteProjectList() throws Exception
	{
		String projectName = "TestingProjectList";
		
		
		if(remoteFilingSystem.doesProjectDirectoryExist(projectName))
			remoteFilingSystem.deleteProject(projectName);
		{
			Set<String> projectNames = remoteFilingSystem.getProjectList();
			assertNotContains(projectName, projectNames);
		}
		remoteFilingSystem.createProject(projectName);
		try
		{
			Set<String> projectNames = remoteFilingSystem.getProjectList();
			assertContains(projectName, projectNames);
		}
		finally
		{
			remoteFilingSystem.deleteProject(projectName);
		}
		
	}

	private static final String SERVER_NAME = "localhost";
	private static final int SERVER_PORT = 7000;
	private static final String SERVER_APP_PATH = "/MiradiServer/projects/";

	private MiradiLocalFileSystem localFileSystem;
	private MiradiRemoteFileSystem remoteFilingSystem;
	private MiradiFileSystem[] filingSystems;
	private MiradiFileSystem currentFilingSystem;
	private File tempDirectory;
}
