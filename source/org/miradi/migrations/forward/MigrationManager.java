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

package org.miradi.migrations.forward;

import java.io.File;
import java.io.IOException;
import java.util.Vector;

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.ProjectFileTooNewException;
import org.miradi.exceptions.ProjectFileTooOldException;
import org.miradi.main.EAM;
import org.miradi.migrations.AbstractMigration;
import org.miradi.migrations.AbstractMigrationManager;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.migrations.VersionRange;
import org.miradi.project.Project;
import org.miradi.utils.FileUtilities;

public class MigrationManager extends AbstractMigrationManager
{
	public MigrationManager()
	{
	}
	
	public void safelyMigrateForward(File projectFile) throws Exception
	{
		createBackup(projectFile);
		String contents = UnicodeReader.getFileContents(projectFile);
		contents = migrateForward(contents);
		UnicodeWriter fileWriter = new UnicodeWriter(projectFile);
		fileWriter.write(contents);
		fileWriter.close();
	}

	public String migrateForward(String mpfAsString) throws Exception
	{
		RawProject rawProject = createRawProject(mpfAsString);
		Vector<AbstractMigration> migrations = createEmptyMigrations(rawProject);
		for(AbstractMigration abstractMigration : migrations)
		{
			abstractMigration.forwardMigrateIfPossible();	
		}

		return convertToMpfString(rawProject);
	}

	//FIXME, migrate forward and reverse need to be refactored to eliminate dupe code
	public String migrateReverse(String mpfAsString) throws Exception
	{
		RawProject rawProject = createRawProject(mpfAsString);
		Vector<AbstractMigration> migrations = createEmptyMigrations(rawProject);
		for(int index = migrations.size() - 1; index >= 0; --index)
		{
			AbstractMigration migration = migrations.get(index);
			migration.reverseMigrateIfPossible();	
		}
		
		return convertToMpfString(rawProject);
	}

	private RawProject createRawProject(String mpfAsString) throws Exception, IOException
	{
		VersionRange versionRange = RawProjectLoader.loadVersionRange(new UnicodeStringReader(mpfAsString));
		RawProject rawProject = RawProjectLoader.loadProject(new UnicodeStringReader(mpfAsString));
		rawProject.setCurrentVersionRange(versionRange);
		return rawProject;
	}

	private Vector<AbstractMigration> createEmptyMigrations(RawProject rawProject)
	{
		Vector<AbstractMigration> migrations = new Vector<AbstractMigration>();
		migrations.add(new MigrationTo4(rawProject));
		migrations.add(new MigrationTo5(rawProject));
		migrations.add(new MigrationTo6(rawProject));
		
		return migrations;
	}
	
	private void createBackup(File projectFile) throws Exception
	{
		FileUtilities.createMpfBackup(projectFile, getBackupFolderTranslatedName());
	}

	private String getBackupFolderTranslatedName()
	{
		return EAM.substitute(EAM.text("(%s)"), "Automated-Migration-Backups");
	}

	public boolean needsMigration(final File projectFile) throws Exception
	{
		String contents = UnicodeReader.getFileContents(projectFile);
		VersionRange mpfVersionRange = RawProjectLoader.loadVersionRange(new UnicodeStringReader(contents));
		final int migrationType = getMigrationType(Project.getMiradiVersionRange(), mpfVersionRange);
		
		return migrationType == MIGRATION;
	}
	
	public void validateProjectVersion(final File projectFile) throws Exception
	{
		String contents = UnicodeReader.getFileContents(projectFile);
		VersionRange mpfVersionRange = RawProjectLoader.loadVersionRange(new UnicodeStringReader(contents));
		final VersionRange miradiVersionRange = Project.getMiradiVersionRange();
		final int migrationType = getMigrationType(miradiVersionRange, mpfVersionRange);

		if (migrationType == TOO_OLD_TO_MIGRATE)
			throw new ProjectFileTooOldException(mpfVersionRange.getHighVersion(), miradiVersionRange.getLowVersion());
		
		if (migrationType == TOO_NEW_TO_MIGRATE)
			throw new ProjectFileTooNewException(mpfVersionRange.getLowVersion(), miradiVersionRange.getHighVersion());
	}
	
	public static int getMigrationType(VersionRange miradiVersionRange, VersionRange mpfVersionRange) throws Exception
	{
		if (mpfVersionRange.getHighVersion() < OLDEST_VERSION_TO_HANDLE)
			return TOO_OLD_TO_MIGRATE;
		
		if (mpfVersionRange.isEntirelyOlderThan(miradiVersionRange))
			return MIGRATION;
		
		if (mpfVersionRange.isEntirelyNewerThan(miradiVersionRange))
			return TOO_NEW_TO_MIGRATE;
		
		return NO_MIGRATION;
	}

	public static final int NO_MIGRATION = 0;
	public static final int MIGRATION = 1;
	public static final int TOO_NEW_TO_MIGRATE = 2;
	public static final int TOO_OLD_TO_MIGRATE = 3;
	
	private static final int OLDEST_VERSION_TO_HANDLE = 3;
}
