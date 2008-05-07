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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.CpmzFileFilter;
import org.miradi.xml.conpro.importer.ConProXmlImporter;
import org.xml.sax.InputSource;

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
		
		importProject(importFile, newProjectFilename);
	}

	private void importProject(File zipFileToImport, String newProjectFilename) throws Exception
	{
		Project projectToFill = new Project();
		ZipFile zipFile = new ZipFile(zipFileToImport);
		ByteArrayInputStream projectAsInputStream = extractEntry(zipFile);
		try 
		{
			projectToFill.createOrOpen(new File(EAM.getHomeDirectory(), newProjectFilename));	
			new ConProXmlImporter(projectToFill).importConProProject(new InputSource(projectAsInputStream));
		}
		finally
		{
			//FIXME extract into nesterd try finallys
			projectToFill.close();
			zipFile.close();
			projectAsInputStream.close();
		}
	}

	//FIXME this method needs to be updated to extract all the contents of the zip
	public static ByteArrayInputStream extractEntry(ZipFile  zipFile) throws Exception
	{
		ZipEntry zipEntry = zipFile.getEntry(CpmzExporter.PROJECT_XML_FILE_NAME);
		if (zipEntry == null)
			return new ByteArrayInputStream(new byte[0]);
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try
		{
			byte[] data = new byte[(int) zipEntry.getSize()];
			InputStream inputStream = zipFile.getInputStream(zipEntry);
			inputStream.read(data, 0, data.length);
			byteOut.write(data);
		}
		finally
		{
			byteOut.close();
		}

		return new ByteArrayInputStream(byteOut.toByteArray()); 
	}
	
	@Override
	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new CpmzFileFilter()};
	}
}
