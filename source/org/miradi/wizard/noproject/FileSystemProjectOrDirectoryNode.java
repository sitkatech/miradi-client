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
package org.miradi.wizard.noproject;

import java.io.File;

import org.miradi.database.ProjectServer;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FileSystemProjectSorter;

public class FileSystemProjectOrDirectoryNode extends FileSystemTreeNode
{
	public FileSystemProjectOrDirectoryNode(ProjectServer databaseToUse, File file, 	FileSystemProjectSorter sorterToUse) throws Exception
	{
		super(databaseToUse, file, sorterToUse);
	}

	@Override
	protected FileSystemTreeNode createNode(ProjectServer databaseToUse, File file, FileSystemProjectSorter sorterToUse) throws Exception
	{
		return new FileSystemProjectOrDirectoryNode(databaseToUse, file, sorterToUse);
	}
	
	@Override
	protected boolean shouldBeIncluded(File file)
	{
		try
		{
			if (!file.isDirectory())
				return false;

			//NOTE: Must check if project first, to allow users to have projects
			// that happen to have names the same as our old special directory names
			if (ProjectServer.isExistingLocalProject(file))
				return true;
			
			if (isExternalReportsDirectory(file))
				return false;
			
			if (isCustomReportsDirectory(file))
				return false;
			
			if (isExternalResourceDirectory(file))
				return false;
			
			return true;
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return false;
		}
	}
}
