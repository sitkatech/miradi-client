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
package org.miradi.project;

import java.io.File;

import org.martus.util.DirectoryUtils;
import org.miradi.main.EAMTestCase;
import org.miradi.project.Project;

public class TestRealProject extends EAMTestCase
{
	public TestRealProject(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		projectDirectory = createTempDirectory();
		projectDirectory.delete();
	}
	
	public void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(projectDirectory);
		super.tearDown();
	}

	public void testIsOpen() throws Exception
	{
		Project project = new Project();
		assertFalse("already open?", project.isOpen());
		project.createOrOpen(projectDirectory);
		assertTrue("not open?", project.isOpen());
		project.close();
		assertFalse("still open?", project.isOpen());
	}
	
	public void testInsertNode() throws Exception
	{
	}
	
	File projectDirectory;
}
