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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.martus.util.UnicodeStringReader;
import org.martus.util.DirectoryLock.AlreadyLockedException;

public class MiradiRemoteFileSystem extends MiradiFileSystemWithTransactions
{
	public MiradiRemoteFileSystem()
	{
	}
	
	public void setDataLocation(String dataLocation) throws Exception
	{
		serverURL = new URL(dataLocation);
	}

	public String getDataLocation()
	{
		return serverURL.toString();
	}
	
	public Set<String> getProjectList() throws Exception
	{
		HttpTransaction get = new HttpGet(serverURL, null, null, null);
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		
		HashSet<String> projectList = new HashSet<String>();
		BufferedReader reader = new BufferedReader(new StringReader(get.getResultData()));
		while(true)
		{
			String projectName = reader.readLine();
			if(projectName == null)
				break;
			projectList.add(projectName);
		}
		return projectList;
	}
	
	public boolean doesProjectDirectoryExist(String projectName) throws Exception
	{
		HttpTransaction get = new HttpGet(serverURL, projectName, new String[] {EXISTS});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return (get.getResultData().startsWith(EXISTS));
	}
	
	public void createProject(String projectName) throws Exception
	{
		HttpPost post = HttpPost.createProject(serverURL, projectName);
		if(post.getResultCode() != HTTP_SUCCESS)
			throw new IOException(post.getResultMessage());
	}
	
	public void deleteProject(String projectName) throws Exception
	{
		HttpTransaction delete = HttpDelete.deleteProject(serverURL, projectName);
		if(delete.getResultCode() != HTTP_SUCCESS)
			throw new IOException(delete.getResultMessage());
	}
	
	public void lockProject(String projectName) throws Exception
	{
		File file = new File(LOCK_FILE_NAME);
		try
		{
			HttpTransaction lock = HttpPost.lockFile(serverURL, projectName, file);
			if(lock.getResultCode() != HTTP_SUCCESS)
				throw new AlreadyLockedException();
		}
		catch(FileNotFoundException e)
		{
			throw e;
		}
		catch(IOException e)
		{
			throw new AlreadyLockedException();
		}
	}

	public void unlockProject(String projectName) throws Exception
	{
		File file = new File(LOCK_FILE_NAME);
		if(!doesFileExist(projectName, file))
			return;
		
		HttpTransaction unlock = HttpDelete.unlockFile(serverURL, projectName, file);
		if(unlock.getResultCode() != HTTP_SUCCESS)
			throw new RuntimeException("Unlock failed");
	}
	
	public boolean doesFileExist(String projectName, File file) throws Exception
	{
		HttpTransaction get = new HttpGet(serverURL, projectName, file, new String[] {EXISTS});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return (get.getResultData().startsWith(EXISTS));
	}
	
	public String readFile(String projectName, File file) throws Exception
	{
		HttpTransaction get = new HttpGet(serverURL, projectName, file);
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		return get.getResultData();
	}
	
	public Map<Integer, String> readAllManifestFiles(String projectName) throws Exception
	{
		HashMap map = new HashMap();
		HttpTransaction get = new HttpGet(serverURL, projectName, new String[] {MANIFESTS});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		
		String results = get.getResultData();
		UnicodeStringReader reader = new UnicodeStringReader(results);
		while(true)
		{
			String typeString = reader.readLine();
			if(typeString == null)
				break;
			if(typeString.length() == 0)
				continue;
			String manifestContents = reader.readLine();
			map.put(Integer.parseInt(typeString), manifestContents + "\n");
		}
		return map;
	}

	public Map<File, String> readMultipleFiles(String projectName, Vector<File> filePathSet) throws Exception
	{
		HashMap<File, String> map = new HashMap<File, String>();
		
		StringBuffer files = new StringBuffer();
		for(File file : filePathSet)
		{
			if(files.length() != 0)
				files.append(",");
			files.append(file.getPath());
		}
		HttpTransaction get = new HttpGet(serverURL, projectName, new String[] {"files=" + files});
		if(get.getResultCode() != HTTP_SUCCESS)
			throw new IOException(get.getResultMessage());
		String results = get.getResultData();

		UnicodeStringReader reader = new UnicodeStringReader(results);
		while(true)
		{
			String path = reader.readLine();
			if(path == null)
				break;
			if(path.length() == 0)
				continue;
			StringBuffer contents = new StringBuffer();
			while(true)
			{
				String line = reader.readLine();
				if(line == null || line.length() == 0)
					break;
				contents.append(line);
				contents.append("\n");
			}
			map.put(new File(path), contents.toString());
		}
		return map;
		
	}

	public void writeFile(String projectName, File file, String contents) throws Exception
	{
		if(wasWriteHandledByTransaction(projectName, file, contents))
			return;
		
		HttpTransaction post = HttpPost.writeFile(serverURL, projectName, file, contents);
		if(post.getResultCode() != HTTP_SUCCESS)
			throw new IOException(post.getResultMessage());
	}
	
	public void writeMultipleFiles(String projectName, HashMap<File, String> fileContentsMap) throws Exception
	{
		HttpPost post = HttpPost.writeMultiple(serverURL, projectName, fileContentsMap);
		if(post.getResultCode() != HTTP_SUCCESS)
			throw new IOException(post.getResultMessage());
	}

	public void deleteFile(String projectName, File file) throws Exception
	{
		if(wasDeleteHandledByTransaction(projectName, file))
			return;
		
		HttpTransaction delete = HttpDelete.deleteFile(serverURL, projectName, file);
		if(delete.getResultCode() != HTTP_SUCCESS)
			throw new IOException(delete.getResultMessage());
	}
	
	public void deleteMultipleFiles(String projectName, HashSet<File> filesToDelete) throws Exception
	{
		HttpTransaction delete = HttpDelete.deleteFiles(serverURL, projectName, filesToDelete);
		if(delete.getResultCode() != HTTP_SUCCESS)
			throw new IOException(delete.getResultMessage());
	}

	private static final int HTTP_SUCCESS = 200;
	private static final String EXISTS = "Exists";
	private static final String MANIFESTS = "Manifests";
	private static final String LOCK_FILE_NAME = "/lock";

	private URL serverURL;
}
