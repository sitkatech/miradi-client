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

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.ProgressInterface;
import org.miradi.utils.XmpzFileFilter;
import org.miradi.xml.xmpz.XmpzXmlImporter;

public class XmpzProjectImporter extends AbstractZippedXmlImporter
{
	public XmpzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected void createProject(File importFile, File homeDirectory, String newProjectFilename, ProgressInterface progressIndicator) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);

		File newProjectDir = new File(EAM.getHomeDirectory(), newProjectFilename);
		importProject(importFile, newProjectDir, progressIndicator);		
	}
	
	private void importProject(File zipFileToImport, File newProjectDir, ProgressInterface progressIndicator) throws ZipException, IOException, Exception, ValidationException
	{
		ZipFile zipFile = new ZipFile(zipFileToImport);
		try
		{
			importProjectFromXmlEntry(zipFile, newProjectDir, progressIndicator);
		}
		finally
		{
			zipFile.close();
		}
	}
	
	@Override
	protected void importProjectXml(Project projectToFill, ZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		XmpzXmlImporter xmpzImporter = new XmpzXmlImporter(projectToFill, progressIndicator);
		xmpzImporter.importProject(projectAsInputStream);
	}

	@Override
	protected void createOrOpenProject(Project projectToFill, String projectName) throws Exception
	{
		projectToFill.rawCreateorOpen(projectName);
	}
		
	@Override
	public FileFilter[] getFileFilters()
	{
		return new FileFilter[] {new XmpzFileFilter()};
	}
}
