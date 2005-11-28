/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.project;

import java.io.File;
import java.io.IOException;

import org.conservationmeasures.eam.database.ProjectServer;
import org.martus.util.DirectoryUtils;
import org.martus.util.TestCaseEnhanced;

public class ProjectServerForTesting extends ProjectServer
{
	public ProjectServerForTesting() throws IOException
	{
		super();
	}

	public void openMemoryDatabase(String nameToUse) throws IOException
	{
		final String tempFileName = "$$$" + TestCaseEnhanced.getCallingTestClass();
		eamDir = File.createTempFile(tempFileName, null);
		eamDir.delete();
		eamDir.mkdir();
		
		File projectDir = new File(eamDir, nameToUse);

		openNonDatabaseStore(projectDir);
		String databaseName = eamDir.getName() + "-" + nameToUse;
		db.openMemoryDatabase(databaseName);
		dropAllTables();
		createCommandsTable();
	}
	
	public void close() throws IOException
	{
		super.close();
		if(eamDir != null)
			DirectoryUtils.deleteEntireDirectoryTree(eamDir);
		eamDir = null;
	}

	File eamDir;
}
