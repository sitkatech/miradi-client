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
import java.util.zip.ZipException;

import org.miradi.exceptions.UnrecognizedFileToImportException;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.Xmpz2FileFilter;
import org.miradi.views.umbrella.AbstractProjectImporter;
import org.miradi.views.umbrella.CpmzProjectImporter;
import org.miradi.views.umbrella.ExportCpmzDoer;
import org.miradi.views.umbrella.MpzProjectImporter;
import org.miradi.views.umbrella.XmlExporterDoer;
import org.miradi.views.umbrella.Xmpz2ProjectImporter;
import org.miradi.views.umbrella.doers.AbstractExportProjectXmlZipDoer;
import org.miradi.wizard.noproject.projectlist.ProjectListTreeTable;

public class CommandLineProjectFileImporterHelper
{
	private CommandLineProjectFileImporterHelper(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}

	public static void importIfRequested(MainWindow mainWindowToUse, String[] commandLineArgs) throws Exception
	{
		try
		{
			CommandLineProjectFileImporterHelper helper = new CommandLineProjectFileImporterHelper(mainWindowToUse);
			File importedFile = helper.importIfRequested(commandLineArgs);
			if (importedFile != null)
				ProjectListTreeTable.doProjectOpen(mainWindowToUse, importedFile);
		}
		catch (ZipException e)
		{
			EAM.errorDialog(e.getMessage());
			EAM.logException(e);
		}
		catch (UnrecognizedFileToImportException e)
		{
			EAM.errorDialog(e.getMessage());
			EAM.logException(e);
		}
	}
	
	private File importIfRequested(String[] commandLineArgs) throws Exception
	{
		Vector<File> filesToImport = extractProjectFilesToImport(commandLineArgs);
		if (filesToImport.isEmpty())
			return null;
		
		if (filesToImport.size() > 1)
			EAM.okDialog(EAM.text("Import"), new String[]{EAM.text("Currently, Miradi can only import one project file at a time.  Importing first file only.")});
		
		File projectFileToImport = filesToImport.firstElement();
		if (!isImportableProjectFile(projectFileToImport))
		{
			EAM.errorDialog(EAM.substituteString(EAM.text("Miradi does not recognize %s as a project file"), projectFileToImport.getName()));
			return null;
		}

		return importProject(projectFileToImport);
	}

	private File importProject(File projectFileToImport) throws Exception
	{
		if (ProjectListTreeTable.isProject(projectFileToImport))
		{
			if (isOutsideOfHomeDir(projectFileToImport))
				return importMpfFile(projectFileToImport);
		}
		else
		{
			if (getUserImportConfirmation(projectFileToImport.getName()))
				return importZippedProject(projectFileToImport);
		}
		
		return projectFileToImport;
	}

	private File importZippedProject(File projectFileToImport)	throws Exception
	{
		AbstractProjectImporter importer = createImporter(projectFileToImport);
		return importer.importProject(projectFileToImport);
	}

	private File importMpfFile(File projectFileToImport) throws Exception
	{
		if (!getUserImportConfirmation(projectFileToImport.getName()))
			return null;
		
		String projectName = getMainWindow().askForDestinationProjectName(projectFileToImport);
		if (projectName == null)
			return null;

		File newProjectFile = new File(EAM.getHomeDirectory(), projectName);
		FileUtilities.copyFile(projectFileToImport, newProjectFile);
		
		return newProjectFile;
	}

	private boolean isOutsideOfHomeDir(File projectFileToImport)
	{
		File projectDirectory = EAM.getHomeDirectory();
		File parent = projectFileToImport.getParentFile();
		while (parent != null)
		{
			if (projectDirectory.equals(parent))
				return false;
			
			parent = parent.getParentFile();
		}
		
		return true;
	}
	
	private Vector<File> extractProjectFilesToImport(String[] commandLineArgs)
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
			String message = EAM.substituteString(EAM.text("Cannot import project file %s because it does not exist"), projectFileToImport.getName());
			EAM.errorDialog(message);
			return false;
		}
		
		if (projectFileToImport.isDirectory())
		{
			String message = EAM.substituteString(EAM.text("Cannot import project file %s because it is a directory, not a file"), projectFileToImport.getName());
			EAM.errorDialog(message);
			return false;
		}
		
		return true;
	}	
	
	private boolean getUserImportConfirmation(String fileNameToImport) throws Exception
	{
		String message = EAM.substituteString(EAM.text("Do you want to attempt to import %s into Miradi?"), fileNameToImport);
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
	
	private AbstractProjectImporter createImporter(File projectFile) throws ZipException, Exception
	{
		try
		{
			MiradiZipFile zipFile = new MiradiZipFile(projectFile);
			if (isXmpz2(projectFile))
				return new Xmpz2ProjectImporter(getMainWindow());
			
			if (CpmzProjectImporter.zipContainsMpfProject(zipFile) || CpmzProjectImporter.containsEntry(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME))
				return new CpmzProjectImporter(getMainWindow());
			
			if (isMpz(zipFile))
				return new MpzProjectImporter(getMainWindow());
		}
		catch(ZipException e)
		{
			throw new ZipException(EAM.substituteString(EAM.text("Error reading %s. Perhaps it is not a valid zip file."), projectFile.getName()));
		}
		
		throw new UnrecognizedFileToImportException(EAM.substituteString(EAM.text("Miradi did not recognize the file: %s as importable."), projectFile.getName()));
	}
	
	private boolean isXmpz2(File projectFile) throws Exception
	{
		if (!projectFile.getName().endsWith(new Xmpz2FileFilter().getFileExtension()))
			return false;
		
		if (!containsFile(new MiradiZipFile(projectFile), AbstractExportProjectXmlZipDoer.SCHEMA_FILE_NAME))
			return false;
		
		return containsFile(new MiradiZipFile(projectFile), XmlExporterDoer.PROJECT_XML_FILE_NAME);
	}

	private boolean isMpz(MiradiZipFile zipFile)
	{
		return containsFile(zipFile, CommandLineProjectFileImporterHelper.PROJECTINFO_FILE);
	}

	private boolean containsFile(MiradiZipFile zipFile, final String fileToFind)
	{
		Enumeration<? extends ZipEntry> entries = zipFile.entries();
		while (entries.hasMoreElements())
		{
			ZipEntry entry = entries.nextElement();
			if (entry.isDirectory())
				continue;
			
			if (entry.getName().toLowerCase().endsWith(fileToFind))
				return true;
		}
		
		return false;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	public static String PROJECTINFO_FILE = "project";
	private static final String TAG_END_DELIMITER = "=";
	public static final String COMMANDLINE_TAG_IMPORT_MPZ = "--importmpz" + TAG_END_DELIMITER;
	public static final String COMMANDLINE_TAG_IMPORT_CPMZ = "--importcpmz" + TAG_END_DELIMITER;
	private static final int IMPORT_CHOICE = 0;
	private static final int DO_NOT_IMPORT_CHOICE = 1;
	private static final int EXIT_CHOICE = 2;
}
