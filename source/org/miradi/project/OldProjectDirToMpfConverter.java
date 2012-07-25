/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.project;

import java.io.File;
import java.util.Calendar;
import java.util.zip.ZipFile;

import org.martus.util.DirectoryUtils;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MpfFileFilter;
import org.miradi.utils.MpzFileFilter;
import org.miradi.utils.ZipUtilities;
import org.miradi.views.umbrella.MpzProjectImporter;

public class OldProjectDirToMpfConverter
{
	public static File convert(MainWindow mainWindow, File oldProjectDirectory) throws Exception
	{
		File oldProjectZippedAsBackup = new File(EAM.getHomeDirectory(), "Backup-" + oldProjectDirectory.getName() + "-" + Calendar.getInstance().getTimeInMillis() + MpzFileFilter.EXTENSION);
		if (oldProjectZippedAsBackup.exists())
			throw new Exception("Attempted to override an existing backup file when converting old project dir to new project format:" + oldProjectZippedAsBackup.getAbsolutePath());
		
		ZipUtilities.createZipFromDirectory(oldProjectDirectory, oldProjectZippedAsBackup);
		if (!ZipUtilities.doesProjectZipContainAllProjectFiles(new ZipFile(oldProjectZippedAsBackup), oldProjectDirectory))
			throw new Exception("Mpz to Mpf data conversion failed");
		
		MpzProjectImporter importer = new MpzProjectImporter(mainWindow);
		File proposedProjectFile = new File(oldProjectDirectory.getName() + MpfFileFilter.EXTENSION);
		File importedFile = importer.importProject(oldProjectZippedAsBackup, proposedProjectFile);
		if (importedFile != null)
			DirectoryUtils.deleteEntireDirectoryTree(oldProjectDirectory);
		
		return importedFile;
	}
}
