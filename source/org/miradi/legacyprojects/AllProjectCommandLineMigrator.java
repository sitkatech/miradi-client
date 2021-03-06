/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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

package org.miradi.legacyprojects;


public class AllProjectCommandLineMigrator
{
	public static void main(String[] args)
	{
		// Disabling this class for now; can re-write for the new file format if/when needed
		System.out.println("This utility needs to be rewritten to handle the new file format");
		System.exit(1);
		try
		{
//			migrateAllProjects();
//			System.out.println("All Projects Migrated");
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
//	private static void migrateAllProjects() throws Exception
//	{
//		File[] projectDirectories = getProjectDirectories();
//		for(int index = 0; index < projectDirectories.length; ++index)
//		{
//			File projectFile = projectDirectories[index];
//			String projectName = projectFile.getName();
//			ProjectServer database = new ProjectServer();
//			database.setLocalDataLocation(EAM.getHomeDirectory());		
//			migrateProject(database, projectName);
//		}
//	}
//	
//	private static void migrateProject(ProjectServer database, String projectName) throws Exception
//	{
//		int existingVersion = database.readProjectDataVersion(projectName); 
//		if(existingVersion > ProjectServer.DATA_VERSION)
//			throw new FutureSchemaVersionException();
//
//		if(existingVersion < ProjectServer.DATA_VERSION)
//			upgrade(database, projectName);
//		
//		int updatedVersion = database.readProjectDataVersion(projectName); 
//		if(updatedVersion < ProjectServer.DATA_VERSION)
//			throw new OldSchemaVersionException();
//	}
//
//	private static void upgrade(ProjectServer database, String projectName) throws Exception
//	{
//		DataUpgrader.initializeStaticDirectory(new File(database.getDataLocation(), projectName));
//		DataUpgrader.upgrade(new NullProgressMeter());
//	}
//
//	private static File[] getProjectDirectories()
//	{
//		File home = EAM.getHomeDirectory();
//		home.mkdirs();
//		return home.listFiles(new CreateProjectDialog.ProjectFilter());
//	}
}
