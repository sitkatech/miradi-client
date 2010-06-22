/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.views.umbrella;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipFile;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;

abstract public class AbstractZippedXmlImporter extends AbstractProjectImporter
{
	public AbstractZippedXmlImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	protected void importProjectFromXmlEntry(ZipFile zipFile, File newProjectDir) throws Exception, IOException
	{
		Project projectToFill = new Project();
		projectToFill.setLocalDataLocation(newProjectDir.getParentFile());
		createOrOpenProject(projectToFill, newProjectDir.getName());
		try
		{
			InputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
			try
			{
				importProjectXml(projectToFill, zipFile, projectAsInputStream);
			}
			finally
			{
				projectAsInputStream.close();
			}

			projectToFill.close();
		}
		catch(Exception e)
		{
			deleteIncompleteProject(projectToFill);
			throw e;
		}

	}
	
	abstract protected void createOrOpenProject(Project projectToFill, String projectName) throws Exception;

	abstract protected void importProjectXml(Project projectToFill, ZipFile zipFile, InputStreamWithSeek projectAsInputStream) throws Exception;

}
