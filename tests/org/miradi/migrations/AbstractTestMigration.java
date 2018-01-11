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

package org.miradi.migrations;

import org.martus.util.UnicodeStringReader;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.forward.MigrationManager;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectLoader;
import org.miradi.project.ProjectSaver;
import org.miradi.project.ProjectSaverForTesting;
import org.miradi.project.RawProjectSaver;

abstract public class AbstractTestMigration extends TestCaseWithProject
{
	public AbstractTestMigration(String name)
	{
		super(name);
	}
	
	public void testEmptyProject() throws Exception
	{
		verifyFullCircleMigrations(new VersionRange(getFromVersion(), getToVersion()));
	}

	protected MigrationResult migrateProject(Project projectToUse, VersionRange FromVersionRange, VersionRange toVersionRange) throws Exception
	{
		String projectAsString = ProjectSaverForTesting.createSnapShot(projectToUse, FromVersionRange);
		final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
		return migrateProject(projectToMigrate, toVersionRange);
	}

	protected MigrationResult migrateProject(final RawProject projectToMigrate, VersionRange toVersionRange) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		
		return migrationManager.migrate(projectToMigrate, toVersionRange);
	}

	protected ProjectForTesting migrateProject(final VersionRange versionRangeToUse) throws Exception
	{
		String projectAsString = ProjectSaverForTesting.createSnapShot(getProject(), versionRangeToUse);
		return migrateProject(projectAsString);
	}

	protected ProjectForTesting migrateProject(String projectAsString) throws Exception
	{
		final RawProject projectToMigrate = migrateProjectAndReturnRawProject(projectAsString);
		
		String migratedMpfFile = RawProjectSaver.saveProject(projectToMigrate);
		ProjectForTesting migratedProject = ProjectForTesting.createProjectWithoutDefaultObjects("MigratedProject");
		ProjectLoader.loadProject(new UnicodeStringReader(migratedMpfFile), migratedProject);

		return migratedProject;
	}

	protected RawProject migrateProjectAndReturnRawProject(String projectAsString) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		final RawProject projectToMigrate = RawProjectLoader.loadProject(projectAsString);
		migrationManager.migrate(projectToMigrate, Project.getMiradiVersionRange());
		return projectToMigrate;
	}	
	
	protected RawProject reverseMigrate(final VersionRange versionRangeToUse) throws Exception
	{
		return reverseMigrate(getProject(), versionRangeToUse);
	}

	protected RawProject reverseMigrate(final ProjectForTesting projectToUse,final VersionRange versionRangeToUse) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(projectToUse, versionRangeToUse);
		RawProject rawProjectToMigrate = RawProjectLoader.loadProject(projectAsString);
		migrationManager.migrate(rawProjectToMigrate, new VersionRange(MigrationManager.OLDEST_VERSION_TO_HANDLE));
		
		return rawProjectToMigrate;
	}

	protected MigrationResult reverseMigrateReturnMigrationResult(final VersionRange versionRangeToUse) throws Exception
	{
		return reverseMigrateReturnMigrationResult(getProject(), versionRangeToUse);
	}

	protected MigrationResult reverseMigrateReturnMigrationResult(final ProjectForTesting projectToUse, final VersionRange versionRangeToUse) throws Exception
	{
		MigrationManager migrationManager = new MigrationManager();
		String projectAsString = ProjectSaverForTesting.createSnapShot(projectToUse, versionRangeToUse);
		RawProject rawProjectToMigrate = RawProjectLoader.loadProject(projectAsString);
		return migrationManager.migrate(rawProjectToMigrate, new VersionRange(MigrationManager.OLDEST_VERSION_TO_HANDLE));
	}

	protected ProjectForTesting verifyFullCircleMigrations(final VersionRange versionRangeToUse) throws Exception
	{
		ProjectForTesting projectAfterFirstMigration = migrateProject(versionRangeToUse);
		String afterFirstForwardMigration = ProjectSaver.createSnapShot(projectAfterFirstMigration);

		RawProject reverseMigratedProject = reverseMigrate(projectAfterFirstMigration,  versionRangeToUse);
		String afterReverseMigration = RawProjectSaver.saveProject(reverseMigratedProject);
		
		ProjectForTesting fullCircleBackProject = migrateProject(afterReverseMigration);
		String fullCircleBackProjectAsString = ProjectSaver.createSnapShot(fullCircleBackProject);
		
		assertEquals("Full circle migration results are not the same?", stripHighestIdLine(afterFirstForwardMigration), stripHighestIdLine(fullCircleBackProjectAsString));
		
		return fullCircleBackProject;
	}
	
	private String stripHighestIdLine(String mpf)
	{
		int indexOfLastLine = mpf.indexOf("UP	HighestUsedNodeId=");
		return mpf.substring(0, indexOfLastLine);
	}
	
	abstract protected int getToVersion();

	abstract protected int getFromVersion();
}
