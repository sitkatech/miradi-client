/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations;

import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;
import org.miradi.project.ProjectSaver;
import org.miradi.project.RawProjectSaver;

public class TestRawProject extends TestCaseWithProject
{
	public TestRawProject(String name)
	{
		super(name);
	}
	
	public void testProjectLoaderSaverCycle() throws Exception
	{
		getProject().populateEverything();
		String projectMpfSnapShot = ProjectSaver.createSnapShot(getProject());
		RawProject rawProject = RawProjectLoader.loadProject(projectMpfSnapShot);
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		RawProjectSaver.saveProject(rawProject, stringWriter);
		
		assertEquals("Project load and save as string is broken?", projectMpfSnapShot, stringWriter.toString());
	}
}
