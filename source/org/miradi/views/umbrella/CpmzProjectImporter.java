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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.filechooser.FileFilter;

import org.martus.util.UnicodeReader;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.ViewData;
import org.miradi.project.Project;
import org.miradi.project.ProjectMpzImporter;
import org.miradi.utils.ConceptualModelByTargetSplitter;
import org.miradi.utils.CpmzFileFilterForChooserDialog;
import org.miradi.utils.GroupBoxHelper;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.diagram.DiagramView;
import org.miradi.xml.conpro.importer.ConproXmlImporter;

public class CpmzProjectImporter extends AbstractZippedXmlImporter
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
	public void createProject(File importFile, File homeDirectory, String newProjectFilename, ProgressInterface progressIndicator) throws Exception
	{
		if(!Project.isValidProjectFilename(newProjectFilename))
			throw new Exception("Illegal project name: " + newProjectFilename);
			
		File newProjectDir = new File(EAM.getHomeDirectory(), newProjectFilename);
		importProject(importFile, newProjectDir, progressIndicator);
	}

	private void importProject(File zipFileToImport, File newProjectDir, ProgressInterface progressIndicator) throws ZipException, IOException, Exception, ValidationException
	{
		progressIndicator.setStatusMessage(EAM.text("Importing..."), 10);
		ZipFile zipFile = new ZipFile(zipFileToImport);
		try
		{
			if (zipContainsMpzProject(zipFile))
				importProjectFromMpzEntry(zipFile, newProjectDir, progressIndicator);
			else
				importProjectFromXmlEntry(zipFile, newProjectDir, progressIndicator);
		}
		finally
		{
			zipFile.close();
		}
	}

	private void importProjectFromMpzEntry(ZipFile zipFile, File newProjectDir, ProgressInterface progressIndicator) throws Exception
	{
		ZipEntry mpzEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
		InputStream inputStream = zipFile.getInputStream(mpzEntry);
		try
		{
			ProjectMpzImporter.unzipToProjectDirectory(newProjectDir.getParentFile(), newProjectDir.getName(), inputStream);
			progressIndicator.incrementProgress();
		}
		finally
		{
			inputStream.close();
		}
		
		importConproProjectNumbers(zipFile, newProjectDir);
		progressIndicator.incrementProgress();
	}

	private void importConproProjectNumbers(ZipFile zipFile, File newProjectDir) throws Exception
	{
		Project projectToFill = new Project();
		projectToFill.setLocalDataLocation(newProjectDir.getParentFile());
		projectToFill.openProject(newProjectDir.getName());
		try
		{
			InputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
			try
			{
				new ConproXmlImporter(projectToFill).importConProProjectNumbers(projectAsInputStream);
			}
			finally
			{
				projectAsInputStream.close();
			}

			projectToFill.close();
		}
		catch(Exception e)
		{
			projectToFill.closeAndDeleteProject();
			throw e;
		}
	}

	@Override
	protected void createOrOpenProject(Project projectToFill, String projectName)	throws Exception
	{
		projectToFill.createOrOpenWithDefaultObjects(projectName);
	}

	@Override
	protected void importProjectXml(Project projectToFill, ZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		ConproXmlImporter conProXmlImporter = new ConproXmlImporter(projectToFill);
		conProXmlImporter.importConProProject(projectAsInputStream);
		ORef highOrAboveRankedThreatsTag = conProXmlImporter.getHighOrAboveRankedThreatsTag();
		splitMainDiagramByTargets(projectToFill, highOrAboveRankedThreatsTag);
		
		importAdditionalFieldsFromTextFiles(projectToFill, zipFile);
	}
	
	private void importAdditionalFieldsFromTextFiles(Project projectToFill, ZipFile zipFile) throws Exception
	{
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD, zipFile, "ProjectResourcesScorecard.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_LEVEL_COMMENTS, zipFile, "ProjectLevelComments.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_CITATIONS, zipFile, "ProjectCitations.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_CAP_STANDARDS_SCORECARD, zipFile, "ProjectCapStandards.txt");
	}

	private void importIfPresent(Project projectToFill, String fieldTag, ZipFile zipFile, String entryFilename) throws Exception
	{
		ORef tncDataRef = projectToFill.getSafeSingleObjectRef(TncProjectData.getObjectType());
		ZipEntry entry = zipFile.getEntry(entryFilename);
		if(entry == null)
			return;
		
		UnicodeReader reader = new UnicodeReader(zipFile.getInputStream(entry));
		try
		{
			String contents = reader.readAll();
			projectToFill.setObjectData(tncDataRef, fieldTag, contents);
		}
		finally
		{
			reader.close();
		}
	}

	private void splitMainDiagramByTargets(Project filledProject, ORef highOrAboveRankedThreatsTag) throws Exception
	{
		ORefList conceptualModelRefs = filledProject.getConceptualModelDiagramPool().getRefList();
		ORef conceptualModelRef = conceptualModelRefs.getRefForType(ConceptualModelDiagram.getObjectType());
		ConceptualModelDiagram conceptualModel = ConceptualModelDiagram.find(filledProject, conceptualModelRef);
		new ConceptualModelByTargetSplitter(filledProject).splitByTarget(conceptualModel, highOrAboveRankedThreatsTag);
		
		invokeMeglerArrangerOnAllConceptualModelPages(filledProject);
		selectFirstDiagramInAlphabeticallySortedList(filledProject);
		new GroupBoxHelper(filledProject).setGroupBoxTagsToMatchChildren();
	}

	private void invokeMeglerArrangerOnAllConceptualModelPages(Project filledProject) throws Exception
	{
		ORefList conceptualModelRefs = filledProject.getConceptualModelDiagramPool().getRefList();
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

	public static boolean zipContainsMpzProject(ZipFile zipFile)
	{
		return containsEntry(zipFile, ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
	}

	public static boolean containsEntry(ZipFile zipFile, final String entry)
	{
		ZipEntry zipEntry = zipFile.getEntry(entry);
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
