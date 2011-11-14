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
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.martus.util.UnicodeReader;

public class MiradiLocalFileSystem
{
	public void setDataLocation(String dataLocation) throws Exception
	{
		dataDirectory = new File(dataLocation);
	}

	public Set<String> getListOfProjectsIn(String directory)
	{
		File directoryFile = new File(dataDirectory, directory);
		String[] projectNames = directoryFile.list();
		if(projectNames == null)
			projectNames = new String[0];
		return new HashSet<String>(Arrays.asList(projectNames));
	}

	public boolean doesFileExist(String projectName, File file)
			throws Exception
	{
		return filePath(projectName, file).exists();
	}

	public String readFile(String projectName, File file) throws Exception
	{
		File path = filePath(projectName, file);
		UnicodeReader reader = new UnicodeReader(path);
		String contents = reader.readAll();
		reader.close();
		return contents;
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
