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
import org.miradi.objecthelpers.FileSystemProjectSorter;

public class FileSystemDirectoryNode extends FileSystemTreeNode
{
	public FileSystemDirectoryNode(File file, FileSystemProjectSorter sorterToUse) throws Exception
	{
		super(file, sorterToUse);
	}
	
	@Override
	protected FileSystemTreeNode createNode(File file, FileSystemProjectSorter sorterToUse) throws Exception
	{
		return new FileSystemDirectoryNode(file, sorterToUse);
	}
	
	@Override
	protected boolean isVisibleDirectoryOrProject(File file)
	{
		if (ProjectServer.isExistingProject(file))
			return false;
		
		if (isExternalReportsDirectory(file))
			return false;
		
		if (isCustomReportsDirectory(file))
			return false;
		
		if (isExternalResourceDirectory(file))
			return false;
		
		return file.isDirectory();			
	}
}
