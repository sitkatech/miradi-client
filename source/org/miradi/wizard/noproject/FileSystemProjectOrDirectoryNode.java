/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.legacyprojects.LegacyProjectUtilities;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.FileSystemProjectSorter;
import org.miradi.project.Project;
import org.miradi.utils.Translation;

public class FileSystemProjectOrDirectoryNode extends FileSystemTreeNode
{
	public FileSystemProjectOrDirectoryNode(File file, FileSystemProjectSorter sorterToUse) throws Exception
	{
		super(file, sorterToUse);
	}

	@Override
	protected FileSystemTreeNode createNode(File file, FileSystemProjectSorter sorterToUse) throws Exception
	{
		return new FileSystemProjectOrDirectoryNode(file, sorterToUse);
	}
	
	@Override
	protected boolean shouldBeIncluded(File file)
	{
		try
		{
			if(AbstractMpfFileFilter.isMpfFile(file))
				return true;
			
			if (!file.isDirectory())
				return false;

			//NOTE: Must check if project first, to allow users to have projects
			// that happen to have names the same as our old special directory names
			if (LegacyProjectUtilities.isExistingLocalProject(file))
				return true;
			
			if (isMiradi3BackupsDirectory(file))
				return false;
			
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
	
	@Override
	public String getNodeLabel()
	{
		try
		{
			final File possibleProjectFile = getFile();
			if(AbstractMpfFileFilter.isMpfFile(possibleProjectFile))
				return Project.withoutMpfProjectSuffix(possibleProjectFile.getName());
			
			if (LegacyProjectUtilities.isExistingLocalProject(possibleProjectFile))
				return EAM.substituteSingleString(EAM.text("%s (old project)"), possibleProjectFile.getName());
			
			return super.getNodeLabel();
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
			
			return Translation.getCellTextWhenException();
		}
	}
}
