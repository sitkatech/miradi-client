/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.utils;

import java.io.File;
import java.io.IOException;

import org.martus.util.DirectoryUtils;
import org.miradi.main.MiradiTestCase;
import org.miradi.project.Project;

public class TestFileUtilities extends MiradiTestCase
{
	public TestFileUtilities(String name)
	{
		super(name);
	}
	
	public void testCreateFileNameWithExtension()
	{
		verifyExtension("filename.html", "filename", "html");
		verifyExtension("filename.html", "filename", ".html");
	}
	
	private void verifyExtension(String expectedValue, String fileName, String extension)
	{
		assertEquals("Incorrect extension appended?", expectedValue, FileUtilities.createFileNameWithExtension(fileName, extension));
	}

	public void testJoin() throws Exception
	{
		final String separator = "/";
		verifyJoinedPath(separator, "", "");
		final String a = "a";
		final String b = "b";
		final String expectedJoinedPath = a + separator + b;
		verifyJoinedPath(expectedJoinedPath, a, b);
		verifyJoinedPath(expectedJoinedPath, a + separator, b);
		verifyJoinedPath(expectedJoinedPath, a,  separator + b);
		verifyJoinedPath(expectedJoinedPath, a + separator,  separator + b);
		verifyJoinedPath(separator + a + separator + b + separator, separator + a,  b + separator);
	}

	private void verifyJoinedPath(String expectedJoinedValue, String parentPath, String childPath) throws Exception
	{
		assertEquals("incorrect join?", expectedJoinedValue, FileUtilities.join(parentPath, childPath));
	}

	public void testWithoutProjectSuffix() throws Exception
	{
		verifyFileExtensionRemoval("marineExample.Miradi", "marineExample");
		verifyFileExtensionRemoval("marineExample.Miradi.Miradi", "marineExample.Miradi");
		verifyFileExtensionRemoval("marineExample.1.2.Miradi", "marineExample.1.2");
		
		try
		{
			verifyFileExtensionRemoval("marineExample", "marineExample");
			fail("Project file name should ways have .Miradi extension");
		}
		catch (Exception ignoreExpectedException)
		{
		}
	}
	
	private void verifyFileExtensionRemoval(String actualValue, String expectedValue) throws Exception
	{
		assertEquals("Incorrect file extension chunk removed?", expectedValue, Project.withoutMpfProjectSuffix(actualValue));
	}

	public void testCompareDirectoriesBasedOnFileNames() throws Exception
	{
		File tempDir1 = createTempParentChildrenDir("temp1");
		File tempDir2 = createTempParentChildrenDir("temp2");
		File tempDir3 = createTempDirStructure3();
		try
		{
			verifySameDirectories(tempDir2, tempDir1);
			verifyDifferentDirectories(tempDir2, tempDir3);
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDir1);
			DirectoryUtils.deleteEntireDirectoryTree(tempDir2);
			DirectoryUtils.deleteEntireDirectoryTree(tempDir3);
		}
	}
	
	private void verifySameDirectories(File dir1, File dir2)
	{
		assertTrue("Files should be the same?", compareDirectories(dir1, dir2));
		assertTrue("Files should be the same?", compareDirectories(dir2, dir1));
	}

	private void verifyDifferentDirectories(File dir1, File dir2)
	{
		assertFalse("Files should be different?", compareDirectories(dir1, dir2));
		assertFalse("Files should be different?", compareDirectories(dir2, dir1));
	}
	
	private boolean compareDirectories(File dir1, File dir2)
	{
		return FileUtilities.compareDirectoriesBasedOnFileNames(dir1, dir2);
	}
	
	private File createTempParentChildrenDir(final String tempDirName) throws Exception
	{
		File tempDir = FileUtilities.createTempDirectory(tempDirName);
		File parentDir = new File(tempDir, "parent");
		parentDir.mkdir();
		new File(parentDir, "child1").createNewFile();
		new File(parentDir, "child2").createNewFile();
		
		return tempDir;
	}

	private File createTempDirStructure3() throws IOException
	{
		File tempDir = FileUtilities.createTempDirectory("temp3");
		new File(tempDir, "json").createNewFile();
		new File(tempDir, "settings").createNewFile();

		return tempDir;
	}
}
