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
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.utils.CpmzFileChooser;
import org.miradi.views.MainWindowDoer;
import org.miradi.xml.conpro.exporter.ConProMiradiXmlValidator;
import org.miradi.xml.conpro.exporter.ConproXmlExporter;

public class CpmzExporter extends MainWindowDoer
{
	@Override
	public boolean isAvailable()
	{
		return (getProject().isOpen());
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		CpmzFileChooser fileChooser = new CpmzFileChooser(getMainWindow());
		File chosen = fileChooser.displayChooser();
		if (chosen == null) 
			return;
		
		try 
		{
			createCpmzFile(chosen);
		} 
		catch (Exception e) 
		{
			EAM.logException(e);
			throw new CommandFailedException(EAM.text("Error Export To Miradi Zip: Possible Write Protected: ") + e);
		}
	}


	private void createCpmzFile(File chosen) throws Exception
	{
		File projectXmlFile = createProjectXmlFileInSystemTemp();
		ZipOutputStream out = new ZipOutputStream(new FileOutputStream(chosen));
		try
		{
			new ConproXmlExporter(getProject()).export(projectXmlFile);
			if (!new ConProMiradiXmlValidator().isValid(new FileInputStream(projectXmlFile)))
				throw new Exception("Exported file does not validate.");
			
			ZipEntry entry = new ZipEntry(projectXmlFile.getName());
			int size = (int) projectXmlFile.length();
			entry.setSize(size);
			
			byte[] contents = new byte[size];
			FileInputStream in = new FileInputStream(projectXmlFile);
			in.read(contents);
			in.close();
			out.putNextEntry(entry);
			out.write(contents);
		}
		finally
		{
			projectXmlFile.delete();
			out.closeEntry();
			out.close();
		}
	}
	
	public static File createProjectXmlFileInSystemTemp()
	{
		return new File(System.getProperty("java.io.tmpdir"), PROJECT_XML_FILE_NAME);
	}

	public static final String PROJECT_XML_FILE_NAME = "project.xml";
}
