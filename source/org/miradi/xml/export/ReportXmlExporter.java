/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.xml.export;

import java.io.File;

import org.miradi.main.EAM;
import org.miradi.project.Project;


public class ReportXmlExporter
{
	public ReportXmlExporter(Project project)
	{
		//EXPORT FROM HERE
	}
	
	public static void main(String[] commandLineArguments)
	{	
		try
		{
			Project project = new Project();
			project.openProject(getProjectDirectory(commandLineArguments));
			new ReportXmlExporter(project);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}	
	
	public static File getProjectDirectory(String[] commandLineArguments) throws Exception
	{
		File projectDirectory = new File(EAM.getHomeDirectory(), commandLineArguments[0]);
		if (commandLineArguments.length != 1)
			throw new RuntimeException("Incorrect number of arguments");

		return projectDirectory;
	}
}
