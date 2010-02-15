/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.martus.util.DirectoryUtils;
import org.martus.util.inputstreamwithseek.ByteArrayInputStreamWithSeek;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ProjectUnzipper;
import org.miradi.utils.ConceptualModelByTargetSplitter;
import org.miradi.utils.CpmzFileFilterForChooserDialog;
import org.miradi.utils.GroupBoxHelper;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.views.diagram.DiagramView;
import org.miradi.xml.conpro.importer.ConproXmlImporter;

public class CpmzProjectImporter extends AbstractProjectImporter
{	
	public CpmzProjectImporter(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public static void doImport(MainWindow mainWindow) throws Exception
	{
		new CpmzProjectImporter(mainWindow).importProject();
	}
	
	@Override
	public void createProject(File importFile, File homeDirectory, String newProjectFilename) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);
			
		File newProjectDir = new File(EAM.getHomeDirectory(), newProjectFilename);
		importProject(importFile, newProjectDir);
	}

	private void deleteIncompleteProject(Project projectToFill)	throws Exception
	{
		File projectDirectory = projectToFill.createProjectDirectory();
		projectToFill.close();
		DirectoryUtils.deleteEntireDirectoryTree(projectDirectory);
	}

	private void importProject(File zipFileToImport, File newProjectDir) throws ZipException, IOException, Exception, ValidationException
	{
		ZipFile zipFile = new ZipFile(zipFileToImport);
		try
		{
			byte[] extractXmlBytes = extractXmlBytes(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME);
			if (extractXmlBytes.length == 0)
				throw new Exception(ExportCpmzDoer.PROJECT_XML_FILE_NAME + EAM.text(" was empty"));
			
			if (zipContainsMpzProject(zipFile))
				importProjectFromMpzEntry(zipFile, newProjectDir);
			else
				importProjectFromXmlEntry(zipFile, newProjectDir);
		}
		finally
		{
			zipFile.close();
		}
	}

	private void importProjectFromMpzEntry(ZipFile zipFile, File newProjectDir) throws Exception
	{
		ZipEntry mpzEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
		InputStream inputStream = zipFile.getInputStream(mpzEntry);
		try
		{
			ProjectUnzipper.unzipToProjectDirectory(newProjectDir.getParentFile(), newProjectDir.getName(), inputStream);
		}
		finally
		{
			inputStream.close();
		}
		
		importConproProjectNumbers(zipFile, newProjectDir);
	}

	private void importConproProjectNumbers(ZipFile zipFile, File newProjectDir) throws Exception
	{
		Project projectToFill = new Project();
		projectToFill.setLocalDataLocation(newProjectDir.getParentFile());
		projectToFill.openProject(newProjectDir.getName());
		try
		{
			ByteArrayInputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
			try
			{
				new ConproXmlImporter(projectToFill).importConProjectNumbers(projectAsInputStream);
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

	private void importProjectFromXmlEntry(ZipFile zipFile, File newProjectDir) throws Exception, IOException
	{
		Project projectToFill = new Project();
		projectToFill.setLocalDataLocation(newProjectDir.getParentFile());
		projectToFill.createOrOpen(newProjectDir.getName());
		try
		{
			ByteArrayInputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
			try
			{
				ConproXmlImporter conProXmlImporter = new ConproXmlImporter(projectToFill);
				conProXmlImporter.importConProProject(projectAsInputStream);
				ORef highOrAboveRankedThreatsTag = conProXmlImporter.getHighOrAboveRankedThreatsTag();
				splitMainDiagramByTargets(projectToFill, highOrAboveRankedThreatsTag);
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
	
	private ByteArrayInputStreamWithSeek getProjectAsInputStream(ZipFile zipFile) throws Exception
	{
		byte[] extractXmlBytes = extractXmlBytes(zipFile, ExportCpmzDoer.PROJECT_XML_FILE_NAME);
		return new ByteArrayInputStreamWithSeek(extractXmlBytes);
	}

	private void splitMainDiagramByTargets(Project filledProject, ORef highOrAboveRankedThreatsTag) throws Exception
	{
		ORefList conceptualModelRefs = filledProject.getConceptualModelDiagramPool().getRefList();
		ORef conceptualModelRef = conceptualModelRefs.getRefForType(ConceptualModelDiagram.getObjectType());
		ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(filledProject, conceptualModelRef);
		new ConceptualModelByTargetSplitter(filledProject).splitByTarget(conceptualModel, highOrAboveRankedThreatsTag);
		
		invokeMeglerArranger(filledProject, conceptualModelRefs);
		selectFirstDiagramInAlphabeticallySortedList(filledProject);
		new GroupBoxHelper(filledProject).setGroupBoxTagsToMatchChildren();
	}

	private void invokeMeglerArranger(Project filledProject, ORefList conceptualModelRefs) throws Exception
	{
		for(int index = 0; index < conceptualModelRefs.size(); ++index)
		{
			ConceptualModelDiagram diagramToArrange = ConceptualModelDiagram.find(filledProject, conceptualModelRefs.get(index));
			MeglerArranger meglerArranger = new MeglerArranger(diagramToArrange);
			meglerArranger.arrange();
		}
	}

	private void selectFirstDiagramInAlphabeticallySortedList(Project filledProject) throws Exception
	{
		ORefList sortedConceptualModelRefs = filledProject.getConceptualModelDiagramPool().getSortedRefList();
		final int FIRST_REF_INDEX = 0;
		ORef firstRefInAlphabeticallySortedList = sortedConceptualModelRefs.get(FIRST_REF_INDEX);
		ViewData viewData = filledProject.getViewData(DiagramView.getViewName());
		CommandSetObjectData setCurrentDiagramCommand = new CommandSetObjectData(viewData, ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF, firstRefInAlphabeticallySortedList.toString());
		filledProject.executeCommand(setCurrentDiagramCommand);
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
		return new FileFilter[] {new CpmzFileFilterForChooserDialog()};
	}

	protected void showImportCompletedDialog() throws Exception
	{
		HtmlViewPanelWithMargins.createFromHtmlFileName(getMainWindow(), EAM.text("Import"), "NextStepAfterCpmzImport.html").showAsOkDialog();
	}
}
