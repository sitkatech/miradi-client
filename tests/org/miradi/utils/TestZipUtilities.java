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
import java.util.zip.ZipFile;

import org.martus.util.DirectoryUtils;
import org.miradi.main.TestCaseWithProject;
import org.miradi.project.TestMpzToMpfConverter;

public class TestZipUtilities extends TestCaseWithProject
{
	public TestZipUtilities(String name)
	{
		super(name);
	}
	
	public void testCompareZipToDirectory() throws Exception
	{
		final File tempDirectory = FileUtilities.createTempDirectory("tempZipComarison");
		final byte[] mpzBytes = TestMpzToMpfConverter.readSampleMpz("/Sample-v61.mpz");
		final File mpzFile = TestMpzToMpfConverter.writeToTemporaryFile(mpzBytes);
		try
		{
			final ZipFile mpzZipFile = new ZipFile(mpzFile);
			ZipUtilities.extractAll(mpzZipFile, tempDirectory);
			assertTrue("zip file does not match directory content?", ZipUtilities.compare(mpzZipFile, tempDirectory, "Marine Example 3.3.2"));
		}
		finally
		{
			DirectoryUtils.deleteAllFilesOnlyInDirectory(tempDirectory);
			mpzFile.delete();
		}
	}
}
