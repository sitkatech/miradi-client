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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.martus.util.inputstreamwithseek.ByteArrayInputStreamWithSeek;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.ResourcesHandler;
import org.miradi.objects.FactorLink;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.CodeList;
import org.miradi.utils.CpmzFileFilter;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.views.diagram.DiagramView;
import org.miradi.xml.conpro.importer.ConProXmlImporter;

public class ImportCpmzDoer extends ImportProjectDoer
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
		
		Project projectToFill = new Project();
		projectToFill.createOrOpen(new File(EAM.getHomeDirectory(), newProjectFilename));
		try 
		{
			importProject(importFile, projectToFill);
		}
		finally
		{
			projectToFill.close();
		}
	}

	private void importProject(File zipFileToImport, Project projectToFill) throws ZipException, IOException, Exception, ValidationException
	{
		ZipFile zipFile = new ZipFile(zipFileToImport);
		try
		{
			if (zipContainsMpzProject(zipFile))
				importProjectFromMpzEntry(projectToFill, zipFile);
			else
				importProjectFromXmlEntry(projectToFill, zipFile);
		}
		finally
		{
			zipFile.close();
		}
	}

	private void importProjectFromMpzEntry(Project projectToFill, ZipFile zipFile) throws Exception
	{
		ZipEntry mpzEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
		InputStream inputStream = zipFile.getInputStream(mpzEntry);
		try
		{
			ProjectUnzipper.unzipToProjectDirectory(EAM.getHomeDirectory(), projectToFill.getFilename(), inputStream);
		}
		finally
		{
			inputStream.close();
		}
	}

	private void importProjectFromXmlEntry(Project projectToFill, ZipFile zipFile) throws Exception, IOException
	{
		byte[] extractXmlBytes = extractXmlBytes(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME);
		ByteArrayInputStreamWithSeek projectAsInputStream = new ByteArrayInputStreamWithSeek(extractXmlBytes);
		try
		{
			new ConProXmlImporter(projectToFill).importConProProject(projectAsInputStream);
			showDialogWithCoachText();
			hideLinkLayer(projectToFill);
		}
		finally
		{
			projectAsInputStream.close();
		}
	}

	private void hideLinkLayer(Project projectToFill) throws Exception
	{
		CodeList codeListWithHiddenLinkLayer = new CodeList();
		codeListWithHiddenLinkLayer.add(FactorLink.OBJECT_NAME);
		ViewData viewData = projectToFill.getViewData(DiagramView.getViewName());
		
		CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(viewData.getRef(), ViewData.TAG_DIAGRAM_HIDDEN_TYPES, codeListWithHiddenLinkLayer.toString());
		projectToFill.executeCommand(setLegendSettingsCommand);
	}

	public static byte[] extractXmlBytes(ZipFile zipFile, String entryName) throws Exception
	{
		ZipEntry zipEntry = zipFile.getEntry(entryName);
		if (zipEntry == null)
			return new byte[0];
		
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		try
		{
			//FIXME Is this really really correct? It might be, but must know there are two 
			//lengths for an entry (the compressed size and the uncompressed size), and not sure 
			//which one read wants, nor which one should be used to set the size of the byte array.
			//research and make sure it is correct.
			byte[] data = new byte[(int) zipEntry.getSize()];
			InputStream inputStream = zipFile.getInputStream(zipEntry);
			inputStream.read(data, 0, data.length);
			byteOut.write(data);
		}
		finally
		{
			byteOut.close();
		}

		return byteOut.toByteArray(); 
	}
	
	private boolean zipContainsMpzProject(ZipFile zipFile)
	{
		ZipEntry zipEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
		if (zipEntry == null)
			return false;

		return zipEntry.getSize() > 0;
	}
	
	@Override
	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new CpmzFileFilter()};
	}
	
	private void showDialogWithCoachText() throws Exception
	{
		String html = ResourcesHandler.loadResourceFile("NextStepAfterCpmzImport.html");
		HtmlViewPanelWithMargins.createFromTextString(getMainWindow(), EAM.text("Import"), html).showAsOkDialog();
	}
}
