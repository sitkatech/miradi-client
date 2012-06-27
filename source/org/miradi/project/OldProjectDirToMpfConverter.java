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
import java.util.zip.ZipFile;

import org.martus.util.DirectoryUtils;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.ZipUtilities;
import org.miradi.views.umbrella.MpzProjectImporter;

public class OldProjectDirToMpfConverter
{
	public static File convert(MainWindow mainWindow, File oldProjectDirectory) throws Exception
	{
		File oldProjectZipped = ZipUtilities.createZipFromDirectory(oldProjectDirectory);
		MpzProjectImporter importer = new MpzProjectImporter(mainWindow);
		File importedFile = importer.importProject(oldProjectZipped, oldProjectDirectory.getName());

		if (!ZipUtilities.compare(new ZipFile(oldProjectZipped), oldProjectDirectory, oldProjectDirectory.getName()))
		{
			final String errorMessage = EAM.text("Mpz to Mpf data conversion failed");
			EAM.errorDialog(errorMessage);
			throw new RuntimeException(errorMessage);
		}

		DirectoryUtils.deleteEntireDirectoryTree(oldProjectDirectory);
		return importedFile;
	}
}
