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

import org.miradi.main.TestCaseWithProject;

public class TestFileUtilities extends TestCaseWithProject
{
	public TestFileUtilities(String name)
	{
		super(name);
	}
	
	public void testGetRelativePathToParent()
	{
		verifyRelativePath("/users/miradi/someProject", "/someProject");
	}

	private void verifyRelativePath(final String pathname, String expectedRelativePath)
	{
		File file = new File(pathname);
		String relativePath = FileUtilities.getRelativePathToParent(file);
		assertEquals("Incorrect relative path?", expectedRelativePath, relativePath);
	}
}
