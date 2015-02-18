/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
		final byte[] mpzBytes = TestMpzToMpfConverter.readSampleMpz("/Sample-v61.mpz");
		final File mpzFile = TestMpzToMpfConverter.writeToTemporaryFile(mpzBytes);
		final MiradiZipFile mpzZipFile = new MiradiZipFile(mpzFile);
		File tempDirectory = ZipUtilities.extractAll(mpzZipFile);
		try
		{
			File projectDir = getProjectDir(tempDirectory);
			assertTrue("zip file does not match directory content?", ZipUtilities.doesProjectZipContainAllProjectFiles(mpzZipFile, projectDir));
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
			mpzFile.delete();
		}
	}
	
	public void testIsMacResourceForkPath()
	{
		verifyIsMacResourceForkPath("__MACOSX");
		verifyIsMacResourceForkPath("/users/me/__MACOSX/");
		verifyIsMacResourceForkPath("/users/me/__MACOSX/someDir/file.txt");
		
		verifyIsNotMacResourceForkPath("/users/me/MACOSX");
		verifyIsNotMacResourceForkPath("/users/me/__MACOSX_V2");
		verifyIsNotMacResourceForkPath("/users/me/__MACOSX_V2/someDir/");
	}

	private void verifyIsNotMacResourceForkPath(String string)
	{
		assertFalse("Path is not Mac OS resource fork path", ZipUtilities.isMacResourceForkPath(new File(string)));
		
	}

	private void verifyIsMacResourceForkPath(String string)
	{
		assertTrue("Path is not Mac OS resource fork path", ZipUtilities.isMacResourceForkPath(new File(string)));
	}

	private File getProjectDir(File tempDirectory) throws Exception
	{
		final File[] childrenFiles = tempDirectory.listFiles();
		if (childrenFiles.length != 1)
			throw new Exception("temp directory should only contain project dir");
		
		return childrenFiles[0];
	}
	
	public void testNormalizeToForwardSlashes()
	{
		verifyNormalizeToForwardSlashes("project", "project");
		
		verifyNormalizeToForwardSlashes("/project", "/project");
		verifyNormalizeToForwardSlashes("project/", "project/");
		verifyNormalizeToForwardSlashes("/project/", "/project/");
		
		verifyNormalizeToForwardSlashes("/project", "\\project");
		verifyNormalizeToForwardSlashes("project/", "project\\");
		verifyNormalizeToForwardSlashes("/project/", "\\project\\");
	}
	
	public void testNormalizeToBackwardSlashes()
	{
		verifyNormalizeToBackwardSlashes("project", "project");
		verifyNormalizeToBackwardSlashes("\\project", "/project");
		verifyNormalizeToBackwardSlashes("project\\", "project/");
		verifyNormalizeToBackwardSlashes("\\project\\", "/project/");
		verifyNormalizeToBackwardSlashes("\\project", "\\project");
		verifyNormalizeToBackwardSlashes("project\\", "project\\");
		verifyNormalizeToBackwardSlashes("\\project\\", "\\project\\");
	}
	
	private void verifyNormalizeToBackwardSlashes(String expected, String actual)
	{
		assertEquals("Slashes not normalized to backward slashes?", expected, ZipUtilities.normalizeSlashes(actual, new ToBackslashReplacement()));
	}

	private void verifyNormalizeToForwardSlashes(String expected, String actual)
	{
		assertEquals("Slashes were not normalized to forward slashes?", expected, ZipUtilities.normalizeSlashes(actual, new ToForwardSlashReplacement()));
	}
	
	public void testGetFullyNormalized() throws Exception
	{
		verifyNormalizedPath("a", "a");
		verifyNormalizedPath("a", "/a");
		verifyNormalizedPath("a", "\\a");
		verifyNormalizedPath("a/b/c", "a\\b/c");
		verifyNormalizedPath("a/b", "/a/b");
		verifyNormalizedPath("a/b", "\\a\\b");
	}

	private void verifyNormalizedPath(final String expected, final String actual)
	{
		assertEquals(expected, ZipUtilities.getNormalizedWithoutLeadingSlash(actual));
	}
	
	public void testRemoveLeadingSlash()
	{
		verifyRemoveLeadingSlash("project", "project");
		verifyRemoveLeadingSlash("project", "/project");
		verifyRemoveLeadingSlash("project/", "project/");
		verifyRemoveLeadingSlash("project/", "/project/");
		
		verifyRemoveLeadingBackSlash("project", "\\project");
		verifyRemoveLeadingBackSlash("project\\", "project\\");
		verifyRemoveLeadingBackSlash("project\\", "\\project\\");
		verifyRemoveLeadingBackSlash("project\\json", "\\project\\json");
	}
	
	private void verifyRemoveLeadingSlash(String expected, String actual)
	{
		verifyRemoveLeadingSlash(expected, actual, FileUtilities.SEPARATOR);
	}
	
	private void verifyRemoveLeadingBackSlash(String expected, String actual)
	{
		verifyRemoveLeadingSlash(expected, actual, FileUtilities.REGULAR_EXPRESSION_BACKSLASH);
	}

	private void verifyRemoveLeadingSlash(String expected, String actual, final String separator)
	{
		assertEquals("leading slash not removed?", expected, ZipUtilities.removeLeadingSlash(actual, separator));
	}
}
