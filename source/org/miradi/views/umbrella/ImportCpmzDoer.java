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
import org.miradi.diagram.DiagramModel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.FactorLink;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.CodeList;
import org.miradi.utils.ConceptualModelByTargetSplitter;
import org.miradi.utils.CpmzFileFilter;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.xml.conpro.importer.ConProXmlImporter;

public class ImportCpmzDoer extends ImportProjectDoer
{
	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		if (!MainWindow.ALLOW_CONPRO_IMPORT_EXPORT)
		{
			EAM.notifyDialog(MainWindow.DISABLED_CONPRO_IMPORT_EXPORT_MESSAGE);
			return;
		}
		
		super.doIt();
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
			hideLinkLayer(projectToFill);
			splitMainDiagramByTargets(projectToFill);
		}
		finally
		{
			projectAsInputStream.close();
		}
	}

	private void splitMainDiagramByTargets(Project filledProject) throws Exception
	{
		ORefList conceptualModelRefs = filledProject.getConceptualModelDiagramPool().getRefList();
		ORef conceptualModelRef = conceptualModelRefs.getRefForType(ConceptualModelDiagram.getObjectType());
		ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(filledProject, conceptualModelRef);
		new ConceptualModelByTargetSplitter(filledProject).splitByTarget(conceptualModel);
	}

	//FIXME this is not working due to DiagramModel being always null.  
	private void hideLinkLayer(Project projectToFill) throws Exception
	{
		CodeList codeListWithHiddenLinkLayer = new CodeList();
		codeListWithHiddenLinkLayer.add(FactorLink.OBJECT_NAME);
		DiagramModel diagramModel = getMainWindow().getDiagramModel();
		if (diagramModel == null)
			return;
		
		DiagramObject diagramObject = diagramModel.getDiagramObject();
		CommandSetObjectData setLegendSettingsCommand = new CommandSetObjectData(diagramObject.getRef(), DiagramObject.TAG_HIDDEN_TYPES, codeListWithHiddenLinkLayer.toString());
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
			byte[] data = new byte[(int) zipEntry.getSize()];
			InputStream inputStream = zipFile.getInputStream(zipEntry);
			int offset = 0;
			while(true)
			{
				if(offset >= data.length)
					break;
				
				int got = inputStream.read(data, 0, data.length - offset);
				offset += got;
				byteOut.write(data, 0, got);
			}
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
	public FileFilter[] getFileFilters()
	{
		return new FileFilter[] {new CpmzFileFilter()};
	}

	protected void showImportCompletedDialog() throws Exception
	{
		HtmlViewPanelWithMargins.createFromHtmlFileName(getMainWindow(), EAM.text("Import"), "NextStepAfterCpmzImport.html").showAsOkDialog();
	}
}
