/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.project;

import java.io.File;
import java.util.Calendar;

import org.martus.util.DirectoryUtils;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.MpzFileFilter;
import org.miradi.utils.ZipUtilities;
import org.miradi.views.umbrella.MpzProjectImporter;
import org.miradi.wizard.noproject.FileSystemTreeNode;

public class OldProjectDirToMpfConverter
{
	public static File convert(MainWindow mainWindow, File oldProjectDirectory) throws Exception
	{
		File backupFolder = new File(EAM.getHomeDirectory(), FileSystemTreeNode.BACKUP_FOLDER_NAME);
		String body = EAM.text(
			"The selected project was created with an older version of Miradi.<br/>" + 
			"In order to use it with this version of Miradi, the data will have <br/>" +
			"to be migrated. This is safe and automatic, but after the conversion <br/>" +
			"has been done, Miradi 3.X will no longer be able to use this file.<br/>" +
			"<br/>" +
			"If you continue, you will still be able to export the project as <br/>" +
			"a Miradi Project Zip (MPZ) file, which can be imported by Miradi 3.3. <br/>" +
			"<br/>" +
			"In the unlikely event that you need to revert to Miradi 3.3, a <br/>" +
			"backup copy of the project, in MPZ format, will be created in a <br/>" +
			"directory named: %DIRECTORY%<br/>" +
			"<br/>" +
			"Do you want to go ahead and migrate and open this project?"
		);
		body = EAM.substitute(body, "%DIRECTORY%", backupFolder.getAbsolutePath());
		String[] buttons = new String[] { EAM.text("Button|Migrate"), EAM.text("Button|Cancel")};
		int result = EAM.confirmDialog(EAM.text("Title|Migrate Project"), "<html>" + body, buttons);
		if(result != 0)
			return null;
		
		backupFolder.mkdirs();
		File oldProjectZippedAsBackup = new File(backupFolder, "v3-Backup-" + oldProjectDirectory.getName() + "-" + Calendar.getInstance().getTimeInMillis() + MpzFileFilter.EXTENSION);
		if (oldProjectZippedAsBackup.exists())
			throw new Exception("Attempted to override an existing backup file when converting old project dir to new project format:" + oldProjectZippedAsBackup.getAbsolutePath());
		
		ZipUtilities.createZipFromDirectory(oldProjectDirectory, oldProjectZippedAsBackup);
		if (!ZipUtilities.doesProjectZipContainAllProjectFiles(new MiradiZipFile(oldProjectZippedAsBackup), oldProjectDirectory))
			throw new Exception("Mpz to Mpf data conversion failed");
		
		MpzProjectImporter importer = new MpzProjectImporter(mainWindow);
		File proposedProjectFile = new File(AbstractMpfFileFilter.createNameWithExtension(oldProjectDirectory.getName()));
		File importedFile = importer.importProject(oldProjectDirectory.getParentFile(), oldProjectZippedAsBackup, proposedProjectFile);
		if (importedFile != null)
			DirectoryUtils.deleteEntireDirectoryTree(oldProjectDirectory);
		
		return importedFile;
	}
}
