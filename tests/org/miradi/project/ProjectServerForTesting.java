/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;

import org.martus.util.DirectoryUtils;
import org.martus.util.TestCaseEnhanced;
import org.martus.util.DirectoryLock.AlreadyLockedException;
import org.miradi.database.HybridFileBasedProjectServer;
import org.miradi.objects.BaseObject;
import org.miradi.project.SimpleThreatRatingFramework;

public class ProjectServerForTesting extends HybridFileBasedProjectServer
{
	public ProjectServerForTesting() throws IOException
	{
		super();
	}

	public void openMemoryDatabase(String nameToUse) throws IOException, AlreadyLockedException
	{
		final String tempFileName = "$$$" + TestCaseEnhanced.getCallingTestClass();
		eamDir = File.createTempFile(tempFileName, null);
		eamDir.delete();
		eamDir.mkdir();
		
		File projectDir = new File(eamDir, nameToUse);

		openNonDatabaseStore(projectDir);
	}
	
	public void writeObject(BaseObject object) throws IOException, ParseException
	{
		super.writeObject(object);
		++callsToWriteObject;
	}
	
	

	public void writeThreatRatingFramework(SimpleThreatRatingFramework framework) throws IOException
	{
		super.writeThreatRatingFramework(framework);
		++callsToWriteThreatRatingFramework;
	}
	
	public void closeAndDontDelete() throws IOException
	{
		super.close();
	}
	
	public void close() throws IOException
	{
		closeAndDontDelete();
		if(eamDir != null)
			DirectoryUtils.deleteEntireDirectoryTree(eamDir);
		eamDir = null;
	}

	File eamDir;
	public int callsToWriteObject;
	public int callsToWriteThreatRatingFramework;
}
