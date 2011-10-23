/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.main;

import java.io.File;

import org.martus.util.DirectoryUtils;
import org.martus.util.TestCaseEnhanced;
import org.miradi.project.Project;

public class TestAutomaticProjectSaver extends TestCaseEnhanced
{
	public TestAutomaticProjectSaver(String name)
	{
		super(name);
	}
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		project = new Project();
		saver = new AutomaticProjectSaver(project);

		tempDirectory = createTempDirectory();
		projectFile = new File(tempDirectory, getName() + ".Miradi");
	}
	
	@Override
	protected void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		
		super.tearDown();
	}
	
	public void testSessionFileCreated() throws Exception
	{
		final File sessionFile = new File(projectFile.getAbsolutePath() + AutomaticProjectSaver.SESSION_EXTENTION);
		assertFalse("There should not be a session file?", sessionFile.exists());
		saver.startSaving(projectFile);
		assertTrue("session file was not created?", sessionFile.exists());
	}
	
	public void testStartSavingCreatesNewlyCreatedProject() throws Exception
	{
		saver.startSaving(projectFile);
		assertTrue("project file was not created for the first time?", projectFile.exists());
	}
	
	public void testSafeSave() throws Exception
	{
		File oldFile = saver.getOldFile(projectFile);
		File newFile = saver.getNewFile(projectFile);

		saver.safeSave(projectFile);
		assertTrue(projectFile.exists());
		assertFalse(oldFile.exists());
		assertFalse(newFile.exists());

		saver.safeSave(projectFile);
		assertTrue(projectFile.exists());
		assertTrue(oldFile.exists());
		assertFalse(newFile.exists());

		saver.safeSave(projectFile);
		assertTrue(projectFile.exists());
		assertTrue(oldFile.exists());
		assertFalse(newFile.exists());
	}
	
	private Project project;
	private AutomaticProjectSaver saver;
	private File tempDirectory;
	private File projectFile;

}
