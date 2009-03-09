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

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;

public class MiradiLocalFileSystem implements MiradiFileSystem
{
	public MiradiLocalFileSystem()
	{
	}
	
	public void setDataDirectory(File miradiDataDirectory)
	{
		dataDirectory = miradiDataDirectory;
	}

	public void createProject(String projectName) throws Exception
	{
		projectPath(projectName).mkdir();
	}

	public void deleteFile(String projectName, File file) throws Exception
	{
		File path = filePath(projectName, file);
		if(!path.exists())
			throw new FileNotFoundException();
		path.delete();
	}

	public void deleteProject(String projectName) throws Exception
	{
		File path = projectPath(projectName);
		if(!path.exists())
			throw new FileNotFoundException();
		
		DirectoryUtils.deleteEntireDirectoryTree(path);
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

	public void writeFile(String projectName, File file, String contents)
			throws Exception
	{
		if(!doesProjectDirectoryExist(projectName))
			throw new FileNotFoundException();
		
		File path = filePath(projectName, file);
		path.getParentFile().mkdirs();
		UnicodeWriter writer = new UnicodeWriter(path);
		writer.write(contents);
		writer.close();
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
}
