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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

public class MiradiMemoryFileSystem extends AbstractNonRemoteMiradiFileSystem
{
	public MiradiMemoryFileSystem()
	{
		projects = new HashMap<String, StoredProject>();
	}

	public void createProject(String projectName) throws Exception
	{
		if(doesProjectDirectoryExist(projectName))
			throw new Exception();
		
		StoredProject project = new StoredProject();
		projects.put(projectName, project);
	}

	public void deleteFile(String projectName, File file) throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			throw new IOException();
		
		if(!doesFileExist(projectName, file))
			throw new IOException();
		
		StoredProject project = getProject(projectName);
		project.remove(file);
	}

	private StoredProject getProject(String projectName)
	{
		StoredProject project = projects.get(projectName);
		return project;
	}

	public void deleteMultipleFiles(String projectName,
			HashSet<File> pendingDeletes) throws Exception
	{
		for(File file : pendingDeletes)
		{
			deleteFile(projectName, file);
		}
	}

	public void deleteProject(String projectName) throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			throw new FileNotFoundException();
		
		projects.remove(projectName);
	}

	public boolean doesFileExist(String projectName, File file)
			throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			return false;
		
		return getProject(projectName).keySet().contains(file);
	}

	public boolean doesProjectDirectoryExist(String projectName)
			throws Exception
	{
		return projects.keySet().contains(projectName);
	}

	public String getDataLocation()
	{
		return fakeDataLocation;
	}

	public void lockProject(String projectName) throws Exception
	{
		// TODO Auto-generated method stub

	}

	public Map<Integer, String> readAllManifestFiles(String projectName) throws Exception
	{
		HashMap<Integer, String> results = new HashMap<Integer, String>();
		StoredProject project = getProject(projectName);
		for(File file : project.keySet())
		{
			if(file.getName().equals("manifest"))
			{
				File directory = file.getParentFile();
				int type = getTypeOfObjectDirectory(directory);
				results.put(type, readFile(projectName, file));
			}
		}
		
		return results;
	}

	public String readFile(String projectName, File file) throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			throw new IOException();
		return getProject(projectName).get(file);
	}

	public Map<File, String> readMultipleFiles(String projectName,
			Vector<File> filePathSet) throws Exception
	{
		HashMap<File, String> results = new HashMap<File, String>();
		StoredProject project = getProject(projectName);
		for(File file : filePathSet)
		{
			results.put(file, project.get(file));
		}
		return results;
	}

	public void setDataLocation(String dataLocation) throws Exception
	{
		fakeDataLocation = dataLocation;
	}

	public void unlockProject(String projectName) throws Exception
	{
		// TODO Auto-generated method stub

	}

	public void writeFile(String projectName, File file, String contents)
			throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			throw new IOException();
		getProject(projectName).put(file, contents);
	}

	public void writeMultipleFiles(String projectName,
			HashMap<File, String> pendingWrites) throws Exception
	{
		for(File file : pendingWrites.keySet())
		{
			writeFile(projectName, file, pendingWrites.get(file));
		}
	}
	
	class StoredProject extends HashMap<File, String>
	{
	}

	private String fakeDataLocation;
	private HashMap<String, StoredProject> projects;
}
