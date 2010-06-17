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
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.martus.util.inputstreamwithseek.ByteArrayInputStreamWithSeek;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.XmpzFileFilter;
import org.miradi.xml.xmpz.XmpzXmlImporter;

public class XmpzProjectImporter extends AbstractProjectImporter
{
	public XmpzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);

		File newProjectDir = new File(EAM.getHomeDirectory(), newProjectFilename);
		importProject(importFile, newProjectDir);		
	}
	
	private void importProject(File zipFileToImport, File newProjectDir) throws ZipException, IOException, Exception, ValidationException
	{
		ZipFile zipFile = new ZipFile(zipFileToImport);
		try
		{
			byte[] extractXmlBytes = readZipEntryFile(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME);
			if (extractXmlBytes.length == 0)
				throw new Exception(ExportCpmzDoer.PROJECT_XML_FILE_NAME + EAM.text(" was empty"));
			
			importProjectFromXmlEntry(zipFile, newProjectDir);
		}
		finally
		{
			zipFile.close();
		}
	}
	
	private void importProjectFromXmlEntry(ZipFile zipFile, File newProjectDir) throws Exception, IOException
	{
		Project projectToFill = new Project();
		projectToFill.setLocalDataLocation(newProjectDir.getParentFile());
		projectToFill.createorOpenForImport(newProjectDir.getName());
		try
		{
			ByteArrayInputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
			try
			{
				XmpzXmlImporter xmpzImporter = new XmpzXmlImporter(projectToFill);
				xmpzImporter.importProject(projectAsInputStream);
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
		
	@Override
	public FileFilter[] getFileFilters()
	{
		return new FileFilter[] {new XmpzFileFilter()};
	}
}
