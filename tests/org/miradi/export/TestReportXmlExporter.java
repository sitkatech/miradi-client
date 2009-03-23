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
package org.miradi.export;

import java.io.File;

import org.miradi.main.TestCaseWithProject;
import org.miradi.xml.reports.export.ProjectAsXmlExporter;

public class TestReportXmlExporter extends TestCaseWithProject
{
	public TestReportXmlExporter(String name)
	{
		super(name);
	}
	
	public void testGetProjectDirectory() throws Exception
	{
		String[] commandLineArguments1 = {"TestProject", "someDestination"};
		File projectFile = ProjectAsXmlExporter.getProjectDirectory(commandLineArguments1);
		assertEquals("wrong project file name?", "TestProject", projectFile.getName());
		
		File xmlDestination = ProjectAsXmlExporter.getXmlDestination(commandLineArguments1);
		assertEquals("wrong xml destination?", "someDestination", xmlDestination.getName());
	}
	
	public void testIncorrectArgumentCount()
	{
		String[] commandLineArguments1 = {"TestProject", "xml destination", "thirdArgument", };
		assertTrue(ProjectAsXmlExporter.incorrectArgumentCount(commandLineArguments1));
		
		String[] commandLineArguments2 = {"TestProject", "xml destination", };
		assertFalse(ProjectAsXmlExporter.incorrectArgumentCount(commandLineArguments2));
	}
}
