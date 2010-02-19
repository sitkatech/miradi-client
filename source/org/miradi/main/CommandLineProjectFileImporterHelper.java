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

package org.miradi.main;

import java.io.File;
import java.util.Enumeration;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.miradi.database.ProjectServer;
import org.miradi.views.umbrella.AbstractProjectImporter;
import org.miradi.views.umbrella.CpmzProjectImporter;
import org.miradi.views.umbrella.ExportCpmzDoer;
import org.miradi.views.umbrella.ZippedProjectImporter;

public class CommandLineProjectFileImporterHelper
{
	public static void importIfRequested(MainWindow mainWindowToUse, String[] commandLineArgs) throws Exception
	{
		CommandLineProjectFileImporterHelper helper = new CommandLineProjectFileImporterHelper(mainWindowToUse);
		helper.importIfRequested(commandLineArgs);
	}
	
	private CommandLineProjectFileImporterHelper(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	private void importIfRequested(String[] commandLineArgs) throws Exception
	{
		Vector<File> filesToImport = extractProjectFileToImport(commandLineArgs);
		if (filesToImport.isEmpty())
			return;
		
		if (filesToImport.size() > 1)
			EAM.okDialog(EAM.text("Import"), new String[]{EAM.text("Currently, Miradi can only import one project file at a time")});
		
		File projectFileToImport = filesToImport.firstElement();
		if (!isImportableProjectFile(projectFileToImport))
		{
			EAM.errorDialog(EAM.substitute(EAM.text("Miradi does not recognize %s as a project file"), projectFileToImport.getName()));
			return;
		}

		AbstractProjectImporter importer = createImporter(projectFileToImport);
		if (importer == null)
		{
			EAM.errorDialog(EAM.substitute(EAM.text("Miradi did not recognize the file: %s as importable."), projectFileToImport.getName()));
			return;
		}
		
		if (userComfirmImport(projectFileToImport.getName()))
			importer.importProject(projectFileToImport);
	}
	
	private Vector<File> extractProjectFileToImport(String[] commandLineArgs)
	{
		Vector<File> filesToImport = new Vector<File>();
		for (int index = 0; index < commandLineArgs.length; ++index)
		{
			String commandLineArg = commandLineArgs[index];
			if (!commandLineArg.startsWith("-"))
				filesToImport.add(new File(commandLineArg));
		}
		
		return filesToImport;
	}
	
	private boolean isImportableProjectFile(File projectFileToImport)
	{
		if (!projectFileToImport.exists())
		{
			String message = EAM.substitute(EAM.text("Importing File (%s) from command line does not exist"), projectFileToImport.getName());
			EAM.errorDialog(message);
			return false;
		}
		
		if (projectFileToImport.isDirectory())
		{
			String message = EAM.substitute(EAM.text("Importing File (%s) from command line is a directory"), projectFileToImport.getName());
			EAM.errorDialog(message);
			return false;
		}
		
		return true;
	}	
	
	private boolean userComfirmImport(String fileNameToImport) throws Exception
	{
		String message = EAM.substitute(EAM.text("Do you want to attempt to import %s into Miradi?"), fileNameToImport);
		int userComfirmationChoice = confirmImportDialog(EAM.text("Import"), message);
		if (userComfirmationChoice == IMPORT_CHOICE)
			return true;
		
		if (userComfirmationChoice == EXIT_CHOICE)
			getMainWindow().exitNormally();
		
		return false;
	}
	
	private static int confirmImportDialog(String title, String body)
	{
		String[] buttons = new String[3];
		buttons[IMPORT_CHOICE] = EAM.text("Button|Import");
		buttons[DO_NOT_IMPORT_CHOICE] = EAM.text("Button|Don't Import");
		buttons[EXIT_CHOICE] = EAM.text("Button|Exit");
		
		return EAM.confirmDialog(title, body, buttons);
	}
	
	private AbstractProjectImporter createImporter(File projectFile) throws Exception
	{
		ZipFile zipFile = new ZipFile(projectFile);
		if (CpmzProjectImporter.zipContainsMpzProject(zipFile) || CpmzProjectImporter.containsEntry(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME))
			return new CpmzProjectImporter(getMainWindow());
		
		if (isMpz(zipFile))
			return new ZippedProjectImporter(getMainWindow());
		
		return null;
	}
	
	private boolean isMpz(ZipFile zipFile)
	{
		Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory())
				continue;
			
			if (entry.getName().toLowerCase().endsWith(ProjectServer.PROJECTINFO_FILE))
				return true;
		}
		
		return false;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	private static final String TAG_END_DELIMITER = "=";
	public static final String COMMANDLINE_TAG_IMPORT_MPZ = "--importmpz" + TAG_END_DELIMITER;
	public static final String COMMANDLINE_TAG_IMPORT_CPMZ = "--importcpmz" + TAG_END_DELIMITER;
	private static final int IMPORT_CHOICE = 0;
	private static final int DO_NOT_IMPORT_CHOICE = 1;
	private static final int EXIT_CHOICE = 2;
}
