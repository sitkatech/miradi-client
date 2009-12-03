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

package org.miradi.database;

import java.io.File;

import org.miradi.exceptions.FutureVersionException;
import org.miradi.exceptions.OldVersionException;
import org.miradi.main.EAM;
import org.miradi.views.umbrella.CreateProjectDialog;

public class AllProjectCommandLineMigrator
{
	public static void main(String[] args)
	{
		try
		{
			migrateAllProjects();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void migrateAllProjects() throws Exception
	{
		File[] projectDirectories = getProjectDirectories();
		for(int index = 0; index < projectDirectories.length; ++index)
		{
			File projectFile = projectDirectories[index];
			String projectName = projectFile.getName();
			ProjectServer database = new ProjectServer();
			database.setLocalDataLocation(EAM.getHomeDirectory());		
			migrateProject(database, projectName);
		}
	}
	
	private static void migrateProject(ProjectServer database, String projectName) throws Exception
	{
		int existingVersion = database.readProjectDataVersion(projectName); 
		if(existingVersion > ProjectServer.DATA_VERSION)
			throw new FutureVersionException();

		if(existingVersion < ProjectServer.DATA_VERSION)
			upgrade(database, projectName);
		
		int updatedVersion = database.readProjectDataVersion(projectName); 
		if(updatedVersion < ProjectServer.DATA_VERSION)
			throw new OldVersionException();
	}

	private static void upgrade(ProjectServer database, String projectName) throws Exception
	{
		DataUpgrader.initializeStaticDirectory(new File(database.getDataLocation(), projectName));
		DataUpgrader.upgrade();
	}

	private static File[] getProjectDirectories()
	{
		File home = EAM.getHomeDirectory();
		home.mkdirs();
		return home.listFiles(new CreateProjectDialog.DirectoryFilter());
	}
}
