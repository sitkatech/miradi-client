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
package org.miradi.network;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.channels.OverlappingFileLockException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import org.martus.util.DirectoryLock;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class MiradiLocalFileSystem extends MiradiFileSystemWithTransactions
{
	public MiradiLocalFileSystem()
	{
		locks = new HashMap<String, DirectoryLock>();
	}
	
	public void setDataLocation(String dataLocation) throws Exception
	{
		dataDirectory = new File(dataLocation);
	}

	public String getDataLocation()
	{
		return dataDirectory.getAbsolutePath();
	}

	public void createProject(String projectName) throws Exception
	{
		projectPath(projectName).mkdir();
	}

	public void deleteProject(String projectName) throws Exception
	{
		File path = projectPath(projectName);
		if(!path.exists())
			throw new FileNotFoundException();
		
		DirectoryUtils.deleteEntireDirectoryTree(path);
	}

	public void lockProject(String projectName) throws Exception
	{
		File path = projectPath(projectName);
		DirectoryLock lock = new DirectoryLock();
		try
		{
			lock.lock(path);
		}
		catch(OverlappingFileLockException e)
		{
			throw new AlreadyLockedException();
		}
		locks.put(projectName, lock);
	}

	public void unlockProject(String projectName) throws Exception
	{
		DirectoryLock lock = locks.get(projectName);
		if(lock == null)
			return;
		
		lock.close();
		locks.remove(projectName);
	}

	public boolean doesFileExist(String projectName, File file)
			throws Exception
	{
		return filePath(projectName, file).exists();
	}

	public boolean doesProjectDirectoryExist(String projectName) throws Exception
	{
		File path = projectPath(projectName);
		if(!path.exists())
			return false;
		if(!path.isDirectory())
			return false;
		return true;
	}

	public String readFile(String projectName, File file) throws Exception
	{
		File path = filePath(projectName, file);
		UnicodeReader reader = new UnicodeReader(path);
		String contents = reader.readAll();
		reader.close();
		return contents;
	}

	public Map<Integer, String> readAllManifestFiles(String projectName) throws Exception
	{
		HashMap<Integer, String> map = new HashMap<Integer, String>();
		
		File jsonPath = new File(projectPath(projectName), "json");
		File[] filesInJson = jsonPath.listFiles();
		if(filesInJson == null)
			return map;
		
		for(File file : filesInJson)
		{
			if(!file.isDirectory())
				continue;
			String name = file.getName();
			File relativeObjectsDir = new File("json", name);
			File relativeManifestFile = new File(relativeObjectsDir, "manifest");
			File absoluteManifestFile = filePath(projectName, relativeManifestFile);
			if(!absoluteManifestFile.exists())
				continue;
			
			final String PREFIX = "objects-";
			final int PREFIX_LENGTH = PREFIX.length();
			if(!name.startsWith(PREFIX))
				continue;
			int type = Integer.parseInt(name.substring(PREFIX_LENGTH));
			String contents = readFile(projectName, relativeManifestFile);
			map.put(type, contents);
		}
		
		return map;
	}

	public Map<File, String> readMultipleFiles(String projectName, Vector<File> filePathSet) throws Exception
	{
		HashMap<File, String> map = new HashMap<File, String>();
		for(File filePath : filePathSet)
		{
			String contents = readFile(projectName, filePath);
			map.put(filePath, contents);
		}
		return map;
	}

	public void writeFile(String projectName, File file, String contents)
			throws Exception
	{
		if(wasWriteHandledByTransaction(projectName, file, contents))
			return;
		
		if(!doesProjectDirectoryExist(projectName))
			throw new FileNotFoundException();
		
		File path = filePath(projectName, file);
		path.getParentFile().mkdirs();
		UnicodeWriter writer = new UnicodeWriter(path);
		writer.write(contents);
		writer.close();
	}
	
	public void writeMultipleFiles(String projectName, HashMap<File, String> fileContentsMap) throws Exception
	{
		for(File file : fileContentsMap.keySet())
		{
			writeFile(projectName, file, fileContentsMap.get(file));
		}
	}

	public void deleteFile(String projectName, File file) throws Exception
	{
		if(wasDeleteHandledByTransaction(projectName, file))
			return;
		
		File path = filePath(projectName, file);
		if(!path.exists())
			throw new FileNotFoundException();
		path.delete();
	}

	public void deleteMultipleFiles(String projectName, HashSet<File> pendingDeletes) throws Exception
	{
		for(File file : pendingDeletes)
		{
			deleteFile(projectName, file);
		}
	}

	private File projectPath(String projectName)
	{
		return new File(dataDirectory, projectName);
	}

	private File filePath(String projectName, File file)
	{
		return new File(projectPath(projectName), file.toString());
	}
	
	private File dataDirectory;
	private HashMap<String, DirectoryLock> locks;
}
