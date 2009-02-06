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
import java.text.ParseException;

import org.json.JSONObject;
import org.martus.util.DirectoryLock;
import org.martus.util.Stopwatch;
import org.martus.util.DirectoryLock.AlreadyLockedException;
import org.miradi.main.EAM;
import org.miradi.utils.EnhancedJsonObject;

public class FileBasedProjectServer extends ProjectServer
{
	public FileBasedProjectServer() throws IOException
	{
		super();
		lock = new DirectoryLock();
	}

	public void create(File directory) throws Exception
	{
		if(!isEmpty(directory))
			throw new RuntimeException("Can't create project in non-empty directory");
		
		openNonDatabaseStore(directory);
		writeVersion();
	}

	public void open(File directory) throws IOException, AlreadyLockedException
	{
		if(!isExistingProject(directory))
			throw new IOException("Can't open non-project, non-empty directory");

		openNonDatabaseStore(directory);
	}
	
	public void close() throws IOException
	{
		topDirectory = null;
		name = null;
		lock.close();
		
		if(jsonFileWriteCount == 0 || jsonFileWriteMillis == 0)
			return;
		EAM.logVerbose("Wrote " + jsonFileWriteCount + " files in " + jsonFileWriteMillis + 
				"ms which is " + jsonFileWriteCount*1000/jsonFileWriteMillis + " files/second");
	}

	public boolean isOpen()
	{
		return lock.isLocked();
	}

	public boolean doesFileExist(File infoFile)
	{
		return infoFile.exists();
	}


	
	
	void writeJsonFile(File file, JSONObject json) throws IOException
	{
		file.getParentFile().mkdirs();
		Stopwatch sw = new Stopwatch();
		JSONFile.write(file, json);
		jsonFileWriteMillis += sw.elapsed();
		++jsonFileWriteCount;
	}
	
	EnhancedJsonObject readJsonFile(File file) throws IOException, ParseException
	{
		return JSONFile.read(file);
	}
	
	boolean deleteJsonFile(File objectFile)
	{
		return objectFile.delete();
	}
	

	
	private boolean isEmpty(File directory)
	{
		String[] files = directory.list();
		if(files == null)
			return true;
		return (files.length == 0);
	}
	
	protected void openNonDatabaseStore(File directory) throws IOException, AlreadyLockedException
	{
		directory.mkdirs();
		lock.lock(directory);
		setTopDirectory(directory);
		createJsonDirectories();
		name = topDirectory.getName();
	}

	private void createJsonDirectories()
	{
		getJsonDirectory().mkdirs();
	}
	
	public void updateLastModifiedTime() throws Exception
	{
		modifiedDateWriter.attemptToWriteCurrentTime(getTopDirectory());
	}

	private DirectoryLock lock;
	private int jsonFileWriteCount;
	private long jsonFileWriteMillis;
}
