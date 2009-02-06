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
package org.miradi.xml;

import java.io.File;

import org.martus.util.UnicodeWriter;
import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.Translation;

public abstract class XmlExporter
{
	public XmlExporter(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void export(File destination) throws Exception
	{
		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			exportProject(out);
		}
		finally
		{
			out.close();
		}
	}
	
	protected static Project getOpenedProject(String[] commandLineArguments) throws Exception
	{
		if (incorrectArgumentCount(commandLineArguments))
			throw new RuntimeException("Incorrect number of arguments " + commandLineArguments.length);

		Project newProject = new Project();
		File projectDirectory = getProjectDirectory(commandLineArguments);
		if(!ProjectServer.isExistingProject(projectDirectory))
			throw new RuntimeException("Project does not exist:" + projectDirectory);

		newProject.createOrOpen(projectDirectory);
		Translation.initialize();
		
		return newProject;
	}	 
	
	public static File getProjectDirectory(String[] commandLineArguments) throws Exception
	{
		return new File(EAM.getHomeDirectory(), commandLineArguments[0]);
	}
	
	public static File getXmlDestination(String[] commandLineArguments) throws Exception
	{
		return new File(commandLineArguments[1]);
	}

	public static boolean incorrectArgumentCount(String[] commandLineArguments)
	{
		return commandLineArguments.length != 2;
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	abstract protected void exportProject(UnicodeWriter out) throws Exception;
	
	private Project project;
}
