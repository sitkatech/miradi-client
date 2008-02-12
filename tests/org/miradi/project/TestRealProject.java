/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
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
