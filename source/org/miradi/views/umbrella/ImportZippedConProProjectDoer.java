/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.CpmzFileFilter;
import org.miradi.xml.conpro.importer.ConProXmlImporter;

public class ImportZippedConProProjectDoer extends ImportProjectDoer
{
	@Override
	public boolean isAvailable()
	{
		//FIXME temporarly disabled, since class does nothing
		return false;
	}
	
	@Override
	public void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);
		
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(importFile));
		ZipEntry entry = zipIn.getNextEntry();
		if (entry == null)
		{
			zipIn.close();
			return;
		}
		
		Project projectToFill = new Project();
		try
		{
			ProjectUnzipper.extractOneFile(zipIn, new File(homeDirectory, "project.xml"), entry);	
			projectToFill.createOrOpen(new File(EAM.getHomeDirectory(), newProjectFilename));	
			new ConProXmlImporter(projectToFill).populateProjectFromFile(new File(CpmzExporter.PROJECT_XML_FILE_NAME));
		}
		finally
		{
			zipIn.close();
			projectToFill.close();			
		}
	}

	@Override
	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new CpmzFileFilter()};
	}
}
