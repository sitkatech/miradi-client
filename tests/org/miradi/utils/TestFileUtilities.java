/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.io.File;
import java.io.IOException;

import org.miradi.main.MiradiTestCase;

public class TestFileUtilities extends MiradiTestCase
{
	public TestFileUtilities(String name)
	{
		super(name);
	}
	
	public void testCompareDirectoriesBasedOnFileNames() throws Exception
	{
		File tempDir1 = createTempParentChildrenDir("temp1");
		File tempDir2 = createTempParentChildrenDir("temp2");
		File tempDir3 = createTempDirStructure3();
		
		verifySameDirectories(tempDir2, tempDir1);
		verifyDifferentDirectories(tempDir2, tempDir3);
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
	
	private File createTempParentChildrenDir(final String parentName) throws Exception
	{
		File tempFile = FileUtilities.createTempDirectory(parentName);
		File parent = new File(tempFile, "parent1");
		parent.mkdir();
		new File(parent, "child1").createNewFile();
		new File(parent, "child2").createNewFile();
		
		return tempFile;
	}

	private File createTempDirStructure3() throws IOException
	{
		File tempFile2 = FileUtilities.createTempDirectory("temp3");
		new File(tempFile2, "json").createNewFile();
		new File(tempFile2, "settings").createNewFile();

		return tempFile2;
	}
}
