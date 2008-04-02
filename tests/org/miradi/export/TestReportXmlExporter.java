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
		String[] commandLineArguments1 = {"TestProject"};
		File projectFile1 = ReportXmlExporter.getProjectDirectory(commandLineArguments1);
		assertEquals("wrong project file name?", "TestProject", projectFile1.getName());
		
		try
		{
			String[] commandLineArguments2 = {"TestProject", "SecondArgument"};
			ReportXmlExporter.getProjectDirectory(commandLineArguments2);
			fail("should not accecpt anthing but one command line argument");
		}
		catch (Exception e)
		{
		}
	}
}
