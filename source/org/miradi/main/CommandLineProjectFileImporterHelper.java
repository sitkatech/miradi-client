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

public class CommandLineProjectFileImporterHelper
{
	public CommandLineProjectFileImporterHelper(AbstractProjectImporter importerToUse, String commandLineArg)
	{
		importer = importerToUse;
		projectFileToImport = new File(extractProjectFileName(commandLineArg));
	}
	
	private String extractProjectFileName(final String commandLineArg)
	{
		int endDelimiter = commandLineArg.indexOf(TAG_END_DELIMITER);
		int endOfString = commandLineArg.length();
		
		final String projectFileName = commandLineArg.substring(endDelimiter + 1, endOfString);
		return projectFileName;
	}
	
	public boolean isImportableProjectFile()
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
	
	public void importProjectFromCommandLine() throws Exception
	{
		importer.importProject(getProjectFileToImport());
	}

	private File getProjectFileToImport()
	{
		return projectFileToImport;
	}

	public String getFileName()
	{
		return getProjectFileToImport().getName();
	}
	
	private AbstractProjectImporter importer;
	private File projectFileToImport;
	private static final String TAG_END_DELIMITER = "=";
	public static final String COMMANDLINE_TAG_IMPORT_MPZ = "--importmpz" + TAG_END_DELIMITER;
	public static final String COMMANDLINE_TAG_IMPORT_CPMZ = "--importcpmz" + TAG_END_DELIMITER;
}
