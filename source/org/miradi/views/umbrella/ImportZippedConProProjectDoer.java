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

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipInputStream;

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
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFileToImport));
		ByteArrayInputStream projectAsInputStream = extractEntry(zipIn);
		try 
		{
			projectToFill.createOrOpen(new File(EAM.getHomeDirectory(), newProjectFilename));	
			new ConProXmlImporter(projectToFill).importConProProject(new InputSource(projectAsInputStream));
		}
		finally
		{
			projectToFill.close();
			zipIn.close();
			projectAsInputStream.close();
		}
	}

	//FIXME this method needs to be updated to extract all the contents of the zip
	public static ByteArrayInputStream extractEntry(ZipInputStream  in) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		BufferedOutputStream bufferOut = new BufferedOutputStream(byteOut);
		try
		{
			//FIXME get rid of this 1000
			final int ARRAY_LENGHT = 1000;
			while((in.getNextEntry()) != null)
			{
				int count;
				byte data[] = new byte[ARRAY_LENGHT];
				while ((count = in.read(data, 0, ARRAY_LENGHT)) != -1)
				{
					bufferOut.write(data, 0, count);
				}
			}
		}
		finally
		{
			bufferOut.flush();
			bufferOut.close();
		}

		return new ByteArrayInputStream(byteOut.toByteArray()); 
	}
	
	@Override
	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new CpmzFileFilter()};
	}
}
