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
	
	public void testAttemptUsingReversedPathSeparator()
	{
		verifyAttemptUsingReversedPathSeparator("/", "\\");
		verifyAttemptUsingReversedPathSeparator("\\", "/");
	}
	
	private void verifyAttemptUsingReversedPathSeparator(String expected, String actual)
	{
		assertEquals("incorrect reversed path seperator", expected, MiradiZipFile.attemptUsingReversedPathSeparator(actual));
	}

	public void testReplaceWithOtherPossibleLeadingChar()
	{
		verifyReplaceWithOtherPossibleLeadingChar("/", FileUtilities.SEPARATOR, "");
		verifyReplaceWithOtherPossibleLeadingChar("", FileUtilities.SEPARATOR, "/");
	}

	private void verifyReplaceWithOtherPossibleLeadingChar(String otherExpectedLeadingChar, String separatorToReplace, String leadingChar)
	{
		assertEquals("leading char was not replaced with other option?", otherExpectedLeadingChar, MiradiZipFile.replaceWithOtherPossibleLeadingChar(leadingChar, separatorToReplace));
	}
}
