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

import org.miradi.views.umbrella.AbstractProjectImporter;
import org.miradi.views.umbrella.CpmzProjectImporter;
import org.miradi.views.umbrella.ZippedProjectImporter;

public class CommandLineProjectFileImporterHelper
{
	public CommandLineProjectFileImporterHelper(AbstractProjectImporter importerToUse, String commandLineArg)
	{
		importer = importerToUse;
		projectFileToImport = new File(extractProjectFileName(commandLineArg));
	}
	
	public static void importIfRequested(MainWindow mainWindowToUse, String[] commandLineArgs) throws Exception
	{
		CommandLineProjectFileImporterHelper importHelper = createImportHelper(mainWindowToUse, commandLineArgs);
		if (importHelper != null && importHelper.isImportableProjectFile())
			importHelper.userComfirmImport();
	}
	
	private String extractProjectFileName(final String commandLineArg)
	{
		int endDelimiter = commandLineArg.indexOf(TAG_END_DELIMITER);
		
		final String projectFileName = commandLineArg.substring(endDelimiter + 1);
		return projectFileName;
	}
	
	private boolean isImportableProjectFile()
	{
		if (getProjectFileToImport() == null)
		{
			return false;
		}
		
		if (!getProjectFileToImport().exists())
		{
			String message = EAM.substitute(EAM.text("Importing File (%s) from command line does not exist"), getFileName());
			EAM.errorDialog(message);
			return false;
		}
		
		if (getProjectFileToImport().isDirectory())
		{
			String message = EAM.substitute(EAM.text("Importing File (%s) from command line is a directory"), getFileName());
			EAM.errorDialog(message);
			return false;
		}
		
		return true;
	}	
	
	private void userComfirmImport() throws Exception
	{
		String message = EAM.substitute(EAM.text("Do you want to attempt to import %s into Miradi?"), getFileName());
		int userComfirmationChoice = confirmImportDialog(EAM.text("Import"), message);
		if (userComfirmationChoice == IMPORT_CHOICE)
			importProjectFromCommandLine();
		
		if (userComfirmationChoice == EXIT_CHOICE)
			getMainWindow().exitNormally();
	}
	
	private static CommandLineProjectFileImporterHelper createImportHelper(MainWindow mainWindowToUse, String[] commandLineArgsToUse) throws Exception
	{
		for (int index = 0; index < commandLineArgsToUse.length; ++index)
		{
			String commandLineArg = commandLineArgsToUse[index];
			if (isImportTagArgument(commandLineArg, CommandLineProjectFileImporterHelper.COMMANDLINE_TAG_IMPORT_MPZ))
				return new CommandLineProjectFileImporterHelper(new ZippedProjectImporter(mainWindowToUse), commandLineArg);
			
			if (isImportTagArgument(commandLineArg, CommandLineProjectFileImporterHelper.COMMANDLINE_TAG_IMPORT_CPMZ))
				return new CommandLineProjectFileImporterHelper(new CpmzProjectImporter(mainWindowToUse), commandLineArg);
		}
		
		return null;
	}

	private MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
	
	private static int confirmImportDialog(String title, String body)
	{
		String[] buttons = new String[3];
		buttons[IMPORT_CHOICE] = EAM.text("Button|Import");
		buttons[DO_NOT_IMPORT_CHOICE] = EAM.text("Button|Don't Import");
		buttons[EXIT_CHOICE] = EAM.text("Button|Exit");
		
		return EAM.confirmDialog(title, body, buttons);
	}
	
	private static boolean isImportTagArgument(String commandLineArg, String commandlineImportTag)
	{
		return commandLineArg.toLowerCase().startsWith(commandlineImportTag);
	}

	private void importProjectFromCommandLine() throws Exception
	{
		importer.importProject(getProjectFileToImport());
	}

	private File getProjectFileToImport()
	{
		return projectFileToImport;
	}

	private String getFileName()
	{
		return getProjectFileToImport().getName();
	}
	
	private AbstractProjectImporter importer;
	private File projectFileToImport;
	private static final String TAG_END_DELIMITER = "=";
	public static final String COMMANDLINE_TAG_IMPORT_MPZ = "--importmpz" + TAG_END_DELIMITER;
	public static final String COMMANDLINE_TAG_IMPORT_CPMZ = "--importcpmz" + TAG_END_DELIMITER;
	private static final int IMPORT_CHOICE = 0;
	private static final int DO_NOT_IMPORT_CHOICE = 1;
	private static final int EXIT_CHOICE = 2;
}
