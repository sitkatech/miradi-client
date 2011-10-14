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

package org.miradi.project;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;

public class TestProjectSaver extends TestCaseWithProject
{
	public TestProjectSaver(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		getProject().populateEverything();
		getProject().populateSimpleThreatRatingValues();
	}
	
	public void testBasics() throws Exception
	{
		saveProjectToString();
	}

	public void testSaveAndLoad() throws Exception
	{
		String contents = saveProjectToString();

		Project project2 = new Project();
		UnicodeStringReader reader = new UnicodeStringReader(contents);
		ProjectLoader.loadProject(reader, project2);
	}

	private String saveProjectToString() throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		ProjectSaver.saveProject(getProject(), writer);
		writer.close();
		String result = writer.toString();
		return result;
	}
	
}
