/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.export;

import java.io.File;

import org.miradi.main.TestCaseWithProject;
import org.miradi.xml.export.ReportXmlExporter;

public class TestReportXmlExporter extends TestCaseWithProject
{
	public TestReportXmlExporter(String name)
	{
		super(name);
	}
	
	public void testGetProjectDirectory() throws Exception
	{
		String[] commandLineArguments1 = {"TestProject", "someDestination"};
		File projectFile = ReportXmlExporter.getProjectDirectory(commandLineArguments1);
		assertEquals("wrong project file name?", "TestProject", projectFile.getName());
		
		File xmlDestination = ReportXmlExporter.getXmlDestination(commandLineArguments1);
		assertEquals("wrong xml destination?", "someDestination", xmlDestination.getName());
	}
	
	public void testIncorrectArgumentCount()
	{
		String[] commandLineArguments1 = {"TestProject", "xml destination", "thirdArgument", };
		assertTrue(ReportXmlExporter.incorrectArgumentCount(commandLineArguments1));
		
		String[] commandLineArguments2 = {"TestProject", "xml destination", };
		assertFalse(ReportXmlExporter.incorrectArgumentCount(commandLineArguments2));
	}
}
