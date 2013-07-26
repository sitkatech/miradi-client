/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.migrations;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaver;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.project.RawProjectSaver;

public class AbstractTestMigration extends TestCaseWithProject
{
	public AbstractTestMigration(String name)
	{
		super(name);
	}
	
	protected ProjectForTesting migrateProject(final VersionRange versionRangeToUse) throws Exception
	{
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), versionRangeToUse);
		return migrateProject(projectAsString);
	}

	protected ProjectForTesting migrateProject(String projectAsString) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		String migratedMpfFile = migrationManager.migrateForward(projectAsString);
		
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);
		
		return migratedProject;
	}	
	
	protected RawProject reverseMigrate(final VersionRange versionRangeToUse) throws Exception
	{
		return reverseMigrate(getProject(), versionRangeToUse);
	}

	protected RawProject reverseMigrate(final ProjectForTesting projectToUse,final VersionRange versionRangeToUse) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(projectToUse, versionRangeToUse);
		
		String migratedMpfFile = migrationManager.migrateReverse(projectAsString);

		return RawProjectLoader.loadProject(migratedMpfFile);
	}
	
	protected void verifyFullCircleMigrations(final VersionRange versionRangeToUse) throws Exception
	{
		ProjectForTesting projectAfterFirstMigration = migrateProject(versionRangeToUse);
		String beforeForwardMigration = ProjectSaver.createSnapShot(projectAfterFirstMigration);
		
		RawProject reverseMigratedProject = reverseMigrate(projectAfterFirstMigration,  versionRangeToUse);
		String afterReverseMigration = RawProjectSaver.saveProject(reverseMigratedProject);
		
		ProjectForTesting fullCircleBackProject = migrateProject(afterReverseMigration);
		String fullCircleBackProjectAsString = ProjectSaver.createSnapShot(fullCircleBackProject);
		
		assertEquals("Full circle migration results are not the same?", beforeForwardMigration, fullCircleBackProjectAsString);
	}
}
