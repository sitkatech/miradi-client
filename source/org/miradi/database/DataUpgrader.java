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
import java.io.IOException;

import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryLock.AlreadyLockedException;
import org.martus.util.UnicodeWriter;
import org.miradi.database.migrations.MigrationsForMiradi3;
import org.miradi.database.migrations.MigrationsOlderThanMiradiVersion2;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.main.EAM;
import org.miradi.project.ProjectMpzWriter;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.MiradiBackgroundWorkerThread;
import org.miradi.utils.ProgressInterface;

public class DataUpgrader
{
	public static class MigrationTooOldException extends Exception
	{
	}

	public static void attemptUpgrade(File projectDirectory, int oldVersion) throws AlreadyLockedException
	{
		String[] migrationText = {
				EAM.text("This project was created with an older version of Miradi, " +
				"so it needs to be migrated to the current data format before it can be opened. " +
				"A backup will be saved first in case anything goes wrong. " +
				"Perform the automatic migration?")
				};
		String[] buttons = {EAM.text("Button|Migrate"), EAM.text("Button|Cancel"),};
		if(!EAM.confirmDialog(EAM.text("Project Migration Required"), migrationText, buttons))
			return;
		
		File zipFile = new File(projectDirectory.getParent(), "backup-" + projectDirectory.getName() + "-" + oldVersion + ".mpz");
		if(zipFile.exists())
		{
			String[] backupExistsText = {
					EAM.text("A backup archive for this project already exists." +
					"Continuing with this migration will replace the existing backup with a new copy." +
					"It is probably safe to do this, unless an earlier migration attempt failed.")
					};
			String[] replaceButtons = {EAM.text("Button|Replace Backup"), EAM.text("Button|Cancel"), };
			if(!EAM.confirmDialog(EAM.text("WARNING"), backupExistsText, replaceButtons))
				return;
		}
		
		int versionAfterUpgrading = -1;
		try
		{
			ProjectMpzWriter.createProjectZipFile(projectDirectory, zipFile);
			
			initializeStaticDirectory(projectDirectory);
			ProgressDialog progressDialog = new ProgressDialog(EAM.getMainWindow(), EAM.text("Migrating Project Data"));

			Worker worker = new Worker(progressDialog);
			progressDialog.work(worker);

			versionAfterUpgrading = DataUpgrader.readDataVersion(projectDirectory);			
		}
		catch (DataUpgrader.MigrationTooOldException e)
		{
			EAM.errorDialog(EAM.text("That project is too old to be migrated by this version of Miradi. " +
					"You can use Miradi 1.0 to migrate it to a modern data format, " +
					"and after that it can be opened and migrated by this version."));
			return;
		}
		catch (DirectoryLock.AlreadyLockedException e)
		{
			EAM.logException(e);
			throw e;
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
		if(versionAfterUpgrading == ProjectServer.DATA_VERSION)
			EAM.notifyDialog(EAM.text("Project was migrated to the current data format"));
		else
			EAM.errorDialog(EAM.text("Attempt to migrate project to the current data format FAILED\n" +
				"The pre-migration project was archived in: " + zipFile + "\n" +
				"WARNING: Attempting to open this project again before repairing the problem " +
				"may result in losing data. \n" +
				"Please seek technical help from the Miradi team."));
	}

	static class Worker extends MiradiBackgroundWorkerThread
	{
		protected Worker(ProgressInterface progressToNotify)
		{
			super(progressToNotify);
		}

		@Override
		protected void doRealWork() throws Exception
		{
			upgrade(getProgressIndicator());
		}
		
	}

	public static void initializeStaticDirectory(File projectDirectory) throws IOException
	{
		topDirectory = projectDirectory;
	}

	public static void upgrade(ProgressInterface progressIndicator) throws Exception
	{
		int initialVersion = DataUpgrader.readDataVersion(getTopDirectory());
		if(DataUpgrader.readDataVersion(getTopDirectory()) < 15)
			throw new MigrationTooOldException();

		int numberOfMigrations = ProjectServer.DATA_VERSION - initialVersion;
		progressIndicator.setStatusMessage(EAM.text("Migrating..."), numberOfMigrations);

		DirectoryLock migrationLock = new DirectoryLock();
		migrationLock.lock(getTopDirectory());
		try
		{
			if(DataUpgrader.readDataVersion(getTopDirectory()) == 15)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion16();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 16)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion17();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 17)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion18();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 18)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion19();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 19)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion20();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 20)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion21();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 21)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion22();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 22)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion23();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 23)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion24();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 24)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion25();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 25)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion26();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 26)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion27();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 27)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion28();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 28)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion29();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 29)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion30();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 30)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion31();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 31)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion32();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 32)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion33();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 33)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion34();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 34)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion35();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 35)
			{
				MigrationsOlderThanMiradiVersion2.upgradeToVersion36();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 36)
			{
				MigrationsForMiradi3.upgradeToVersion37();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 37)
			{
				MigrationsForMiradi3.upgradeToVersion38();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 38)
			{
				MigrationsForMiradi3.upgradeToVersion39();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 39)
			{
				MigrationsForMiradi3.upgradeToVersion40();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 40)
			{
				MigrationsForMiradi3.upgradeToVersion41();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 41)
			{
				MigrationsForMiradi3.upgradeToVersion42();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 42)
			{
				MigrationsForMiradi3.upgradeToVersion43();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 43)
			{
				MigrationsForMiradi3.upgradeToVersion44();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 44)
			{
				MigrationsForMiradi3.upgradeToVersion45();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 45)
			{
				MigrationsForMiradi3.upgradeToVersion46();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 46)
			{
				MigrationsForMiradi3.upgradeToVersion47();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 47)
			{
				MigrationsForMiradi3.upgradeToVersion48();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 48)
			{
				MigrationsForMiradi3.upgradeToVersion49();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 49)
			{
				MigrationsForMiradi3.upgradeToVersion50();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 50)
			{
				MigrationsForMiradi3.upgradeToVersion51();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 51)
			{
				MigrationsForMiradi3.upgradeToVersion52();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 52)
			{
				MigrationsForMiradi3.upgradeToVersion53();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 53)
			{
				MigrationsForMiradi3.upgradeToVersion54();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 54)
			{
				MigrationsForMiradi3.upgradeToVersion55();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 55)
			{
				MigrationsForMiradi3.upgradeToVersion56();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 56)
			{
				MigrationsForMiradi3.upgradeToVersion57();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 57)
			{
				MigrationsForMiradi3.upgradeToVersion58();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 58)
			{
				MigrationsForMiradi3.upgradeToVersion59();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 59)
			{
				MigrationsForMiradi3.upgradeToVersion60();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}

			if(DataUpgrader.readDataVersion(getTopDirectory()) == 60)
			{
				MigrationsForMiradi3.upgradeToVersion61();
				progressIndicator.incrementProgress();
				if(progressIndicator.shouldExit())
					return;
			}
		}
		finally
		{
			migrationLock.close();
		}
	}
	
	public static int readDataVersion(File projectDirectory) throws Exception
	{
		return new ProjectServer().readLocalDataVersion(projectDirectory);
	}
	
	public static void writeLocalVersion(File projectDirectory, int versionToWrite) throws Exception
	{
		new ProjectServer().writeLocalDataVersion(projectDirectory, versionToWrite);
	}

	public static int readHighestIdInProjectFile(File dirToUse) throws Exception
	{
		File projectFile = new File(dirToUse, "project");
		EnhancedJsonObject readIn = readFile(projectFile);
		int gotId = readIn.getInt("HighestUsedNodeId");
		
		return gotId;
	}
	
	public static void writeHighestIdToProjectFile(File dirToUse, int highestIdToWrite) throws Exception
	{
		File projectFile = new File(dirToUse, "project");
		EnhancedJsonObject readIn = readFile(projectFile);
		readIn.put("HighestUsedNodeId", highestIdToWrite);
		writeJson(projectFile, readIn);
	}

	public static EnhancedJsonObject readFile(File file) throws Exception
	{
		EnhancedJsonObject objectRead = JSONFile.read(file);
		return objectRead;
	}

	public static void writeJson(File file, EnhancedJsonObject jsonToWrite) throws Exception
	{
		JSONFile.write(file, jsonToWrite);
	}
	
	public static File createManifestFile(File parent, int[] ids) throws Exception
	{
		File manifestFile = new File(parent, "manifest");
		createFile(manifestFile, buildManifestContents(ids));
		return manifestFile;
	}
	
	public static String buildManifestContents(int[] ids)
	{
		String contents = "{\"Type\":\"ObjectManifest\"";
		for(int i = 0; i < ids.length; ++i)
		{
			contents += ",\"" + ids[i] + "\":true";
		}
		contents += "}";
		return contents;
	}
	
	public static void createFile(File file, String contents) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(file);
		writer.writeln(contents);
		writer.close();
	}
	
	public static File createObjectsDir(File jsonDir, int type)
	{
		File objectsDir = getObjectsDir(jsonDir, type);
		objectsDir.mkdirs();
		
		return objectsDir;
	}
	
	public static File getObjectsDir(File jsonDir, int type)
	{
		return new File(jsonDir, "objects-" + type);
	}
	
	public static File getTopJsonDir()
	{
		return new File(getTopDirectory(), "json");
	}
	
	public static File getTopDirectory()
	{
		return topDirectory;
	}

	private static File topDirectory;
}
