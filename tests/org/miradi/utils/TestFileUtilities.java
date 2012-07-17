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
		File tempFile1 = createTempDirStructure1();
		File tempFile2 = createTempDirStructure2();
		
		verifySameDirectories(tempFile1, tempFile1);
		verifySameDirectories(tempFile2, tempFile2);
		verifyDifferentDirectories(tempFile1, tempFile2);
	}
	
	private void verifySameDirectories(File file1, File file2)
	{
		assertTrue("Files should be the same?", compareDirectories(file1, file2));
		assertTrue("Files should be the same?", compareDirectories(file2, file1));
	}

	private void verifyDifferentDirectories(File file1, File file2)
	{
		assertFalse("Files should be different?", compareDirectories(file1, file2));
		assertFalse("Files should be different?", compareDirectories(file2, file1));
	}
	
	private boolean compareDirectories(File file1, File file2)
	{
		return FileUtilities.compareDirectoriesBasedOnFileNames(file1, file2);
	}
	
	private File createTempDirStructure1() throws IOException
	{
		File tempFile = FileUtilities.createTempDirectory("temp1");
		new File(tempFile, "child1").createNewFile();
		new File(tempFile, "child2").createNewFile();
		
		return tempFile;
	}

	private File createTempDirStructure2() throws IOException
	{
		File tempFile2 = FileUtilities.createTempDirectory("temp2");
		new File(tempFile2, "json").createNewFile();
		new File(tempFile2, "settings").createNewFile();

		return tempFile2;
	}
}
