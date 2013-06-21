/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.mpfMigrations;

import org.miradi.main.TestCaseWithProject;

public class TestVersionRange extends TestCaseWithProject
{
	public TestVersionRange(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		verifyLegalVersionRange(0, 0);
		verifyLegalVersionRange(0, 1);
		verifyIllegalVersionRange(1, 0);
	}
	
	public void testIsRangeLessThan() throws Exception
	{
		VersionRange versionRange1 = createVersionRange(0, 0);
		VersionRange versionRange2 = createVersionRange(1, 1);
		assertTrue("Version range1 is not entirely older than range2?", versionRange1.isEntirelyOlderThan(versionRange2));
		assertFalse("Version range2 is not entirely older than range1?", versionRange2.isEntirelyOlderThan(versionRange1));
	}
	
	public void testIsGreaterThan() throws Exception
	{
		VersionRange versionRange1 = createVersionRange(10, 10);
		VersionRange versionRange2 = createVersionRange(1, 1);
		assertTrue("Version range1 is not entirely newer version range2?", versionRange1.isEntirelyNewerThan(versionRange2));
		assertFalse("Version range2 is not entirely newer version range1?", versionRange2.isEntirelyNewerThan(versionRange1));
	}
	
	public void testUpperOverlap() throws Exception
	{
		VersionRange versionRange1 = createVersionRange(15, 25);
		VersionRange versionRange2 = createVersionRange(10, 20);
		assertTrue("Version range1 overlaps version rang2's high end?", versionRange1.doesRangeOverlap(versionRange2));
		assertFalse("Version range2 overlaps version rang1's high end?", versionRange2.doesRangeOverlap(versionRange1));
	}

	private void verifyLegalVersionRange(int lowVersion, int highVersion) throws Exception
	{
		createVersionRange(lowVersion, highVersion);
	}

	private void verifyIllegalVersionRange(int lowVersion, int highVersion)
	{
		try
		{
			createVersionRange(lowVersion, highVersion);
			fail("High version lower than low version should have caused exception?");
		}
		catch (Exception eignoreExpectedException)
		{
		}
	}

	private VersionRange createVersionRange(int lowVersion, int highVersion)	throws Exception
	{
		return new VersionRange(lowVersion, highVersion);
	}
}
