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
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.martus.util.DirectoryUtils;
import org.martus.util.DirectoryLock.AlreadyLockedException;
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
		remoteFileSystem = new MiradiRemoteFileSystem();
		remoteFileSystem.setDataLocation(SERVER_URL_STRING);
		localFileSystem = new MiradiLocalFileSystem();
		localFileSystem.setDataLocation(tempDirectory.getAbsolutePath());
		memoryFileSystem = new MiradiMemoryFileSystem();
		memoryFileSystem.setDataLocation(getName());
		assertEquals(getName(), memoryFileSystem.getDataLocation());
		
		filingSystems = new MiradiFileSystem[] {
				localFileSystem,
				remoteFileSystem,
				memoryFileSystem,
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
			verifyGetManifests();
			verifyReadMultipleFiles();
			verifyTransactions();
			verifyDotDot();
		}
	}
	
	private void verifyBasics() throws Exception
	{
		String projectName = "TestingBasics";
		File file = new File("/testfile");
		String contents = getLongString();
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			assertFalse(currentFilingSystem.doesFileExist(projectName, file));
			currentFilingSystem.writeFile(projectName, file, contents);
			assertTrue(currentFilingSystem.doesFileExist(projectName, file));
			try
			{
				String got = currentFilingSystem.readFile(projectName, file);
				assertEquals(contents, got);
			}
			finally
			{
				currentFilingSystem.deleteFile(projectName, file);
			}
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
		assertFalse(currentFilingSystem.doesFileExist(projectName, file));
	}
	
	private String getLongString()
	{
		StringBuffer buffer = new StringBuffer();
		for(int i = 0; i < 1000; ++i)
			buffer.append("0123456789");
		buffer.append("`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?");
		buffer.append("\n");
		return buffer.toString();
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
	
	private void verifyGetManifests() throws Exception
	{
		String projectName = "TestingManifests";
		File directoryWithManifest1 = new File("json/objects-1");
		File directoryWithManifest17 = new File("json/objects-17");
		File directoryWithoutManifest = new File("json/objects-39");

		final String MANIFEST_FILENAME = "manifest";
		File manifest1 = new File(directoryWithManifest1, MANIFEST_FILENAME);
		File manifest17 = new File(directoryWithManifest17, MANIFEST_FILENAME);
		File otherFile = new File(directoryWithoutManifest, "OtherFile");
		
		String manifestContents1 = "Manifest1\n";
		String manifestContents17 = "Another Manifest\n";
		String otherContents = "This is not a manifest file\n";
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			currentFilingSystem.writeFile(projectName, manifest1, manifestContents1);
			currentFilingSystem.writeFile(projectName, manifest17, manifestContents17);
			currentFilingSystem.writeFile(projectName, otherFile, otherContents);
			
			Map<Integer, String> manifests = currentFilingSystem.readAllManifestFiles(projectName);
			assertEquals(currentFilingSystem.getClass().getSimpleName(), 2, manifests.size());
			assertContains(1, manifests.keySet());
			assertContains(17, manifests.keySet());
			assertEquals(currentFilingSystem.getClass().getSimpleName(), manifestContents1, manifests.get(1));
			assertEquals(currentFilingSystem.getClass().getSimpleName(), manifestContents17, manifests.get(17));
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
	}

	private void verifyReadMultipleFiles() throws Exception
	{
		String projectName = "TestingReadMultiple";
		File directoryWithObjects1 = new File("json/objects-1");
		File directoryWithObjects17 = new File("json/objects-17");

		String name1 = "1";
		String contents1 = "first file\n";
		String name2 = "2";
		String contents2 = "second file\n";
		String name3 = "3";
		String contents3 = "this is the\nthird file\n";

		File object1 = new File(directoryWithObjects1, name1);
		File object2 = new File(directoryWithObjects17, name2);
		File object3 = new File(directoryWithObjects17, name3);
		
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			currentFilingSystem.writeFile(projectName, object1, contents1);
			currentFilingSystem.writeFile(projectName, object2, contents2);
			currentFilingSystem.writeFile(projectName, object3, contents3);
			
			Vector<File> filePathSet = new Vector<File>();
			filePathSet.add(object1);
			filePathSet.add(object2);
			filePathSet.add(object3);
			
			Map<File, String> data = currentFilingSystem.readMultipleFiles(projectName, filePathSet);
			assertEquals(currentFilingSystem.getClass().getSimpleName(), 3, data.size());
			assertContains(object1, data.keySet());
			assertContains(object2, data.keySet());
			assertContains(object3, data.keySet());
			assertEquals(currentFilingSystem.getClass().getSimpleName(), contents1, data.get(object1));
			assertEquals(currentFilingSystem.getClass().getSimpleName(), contents2, data.get(object2));
			assertEquals(currentFilingSystem.getClass().getSimpleName(), contents3, data.get(object3));
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
	}

	private void verifyTransactions() throws Exception
	{
		String projectName = "TestingTransactions";
		File file1 = new File("/json/objects-1/1");
		File file2 = new File("/json/objects-2/2");
		File file3 = new File("/json/objects-3/3");
		File file4 = new File("/json/objects-4/4");
		
		String contents1 = "This is file 1\n";
		String contents2 = "This is file 2\n";
		String contents3 = "This file will be deleted";
		String contents4 = "Anotherfile to delete";

		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		currentFilingSystem.createProject(projectName);
		try
		{
			currentFilingSystem.writeFile(projectName, file1, contents1);
			currentFilingSystem.writeFile(projectName, file4, contents4);
			currentFilingSystem.writeFile(projectName, file3, contents3);

			currentFilingSystem.beginTransaction(projectName);
			{
				currentFilingSystem.writeFile(projectName, file2, "Bogus will be overwritten");
				currentFilingSystem.writeFile(projectName, file2, contents2);
				currentFilingSystem.deleteFile(projectName, file3);
				currentFilingSystem.deleteFile(projectName, file4);
				try
				{
					currentFilingSystem.deleteFile("OtherProjectName", file1);
					fail("Should have thrown for deleting from wrong project in transaction");
				}
				catch(Exception ignoreExpected)
				{
				}
				try
				{
					currentFilingSystem.writeFile("OtherProjectName", file1, contents1);
					fail("Should have thrown for writing to wrong project in transaction");
				}
				catch(Exception ignoreExpected)
				{
				}
			}
			currentFilingSystem.endTransaction();
			
			assertTrue(currentFilingSystem.getClass().getSimpleName(), currentFilingSystem.doesFileExist(projectName, file1));
			assertEquals(currentFilingSystem.getClass().getSimpleName(), contents1, currentFilingSystem.readFile(projectName, file1));
			assertTrue(currentFilingSystem.getClass().getSimpleName(), currentFilingSystem.doesFileExist(projectName, file2));
			assertEquals(currentFilingSystem.getClass().getSimpleName(), contents2, currentFilingSystem.readFile(projectName, file2));
			assertFalse(currentFilingSystem.getClass().getSimpleName(), currentFilingSystem.doesFileExist(projectName, file3));
			assertFalse(currentFilingSystem.getClass().getSimpleName(), currentFilingSystem.doesFileExist(projectName, file4));
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
	}

	private void verifyDotDot()
	{
		// TODO: Write this test
		//fail("Test not implemented yet");
	}

	public void testLockAndUnlock() throws Exception
	{
		// NOTE: Local file system allows the same process to lock a project more than once
		currentFilingSystem = new MiradiRemoteFileSystem();
		currentFilingSystem.setDataLocation(SERVER_URL_STRING);
		
		String projectName = "TestingLocks";
		if(currentFilingSystem.doesProjectDirectoryExist(projectName))
			currentFilingSystem.deleteProject(projectName);
		try
		{
			currentFilingSystem.lockProject(projectName);
			fail("Should have thrown for locking non-existant project: " + currentFilingSystem.getClass());
		}
		catch(FileNotFoundException ignoreExpected)
		{
		}

		currentFilingSystem.createProject(projectName);
		try
		{
			currentFilingSystem.lockProject(projectName);
			try
			{
				currentFilingSystem.lockProject(projectName);
				fail("Should have thrown for locking twice: " + currentFilingSystem.getClass());
			}
			catch(AlreadyLockedException ignoreExpected)
			{
			}
			currentFilingSystem.unlockProject(projectName);
			currentFilingSystem.unlockProject(projectName);
			currentFilingSystem.lockProject(projectName);
			currentFilingSystem.unlockProject(projectName);
		}
		finally
		{
			currentFilingSystem.deleteProject(projectName);
		}
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
		
		
		if(remoteFileSystem.doesProjectDirectoryExist(projectName))
			remoteFileSystem.deleteProject(projectName);
		{
			Set<String> projectNames = remoteFileSystem.getProjectList();
			assertNotContains(projectName, projectNames);
		}
		remoteFileSystem.createProject(projectName);
		try
		{
			Set<String> projectNames = remoteFileSystem.getProjectList();
			assertContains(projectName, projectNames);
		}
		finally
		{
			remoteFileSystem.deleteProject(projectName);
		}
		
	}

	private static final String SERVER_URL_STRING = "http://localhost:7000/MiradiServer/projects/";

	private MiradiLocalFileSystem localFileSystem;
	private MiradiRemoteFileSystem remoteFileSystem;
	private MiradiMemoryFileSystem memoryFileSystem;
	
	private MiradiFileSystem[] filingSystems;
	private MiradiFileSystem currentFilingSystem;
	private File tempDirectory;
}
