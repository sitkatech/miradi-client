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

import org.miradi.main.MiradiTestCase;

public class TestMiradiZipFile extends MiradiTestCase
{
	public TestMiradiZipFile(String name)
	{
		super(name);
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
		assertEquals("Slashes not normalized to backward slashes?", expected, MiradiZipFile.normalizeSlashes(actual, new ToBackslashReplacement()));
	}

	private void verifyNormalizeToForwardSlashes(String expected, String actual)
	{
		assertEquals("Slashes were not normalized to forward slashes?", expected, MiradiZipFile.normalizeSlashes(actual, new ToForwardSlashReplacement()));
	}
	
	public void testGetFullyNormalized() throws Exception
	{
		assertEquals("a/b/c", MiradiZipFile.getFullyNormalized("a\\b/c"));
		assertEquals("a/b", MiradiZipFile.getFullyNormalized("/a/b"));
		assertEquals("a/b", MiradiZipFile.getFullyNormalized("\\a\\b"));
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
		assertEquals("leading slash not removed?", expected, MiradiZipFile.removeLeadingSlash(actual, separator));
	}
}
