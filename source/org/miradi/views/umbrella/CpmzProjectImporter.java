/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.diagram.arranger.MeglerArranger;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.TncProjectData;
import org.miradi.objects.ViewData;
import org.miradi.project.MpzToMpfConverter;
import org.miradi.project.Project;
import org.miradi.project.ProjectSaver;
import org.miradi.project.RawProjectSaver;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.TncProjectDataSchema;
import org.miradi.utils.ConceptualModelByTargetSplitter;
import org.miradi.utils.CpmzFileFilterForChooserDialog;
import org.miradi.utils.FileUtilities;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.GroupBoxHelper;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.NullProgressMeter;
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
	public void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator) throws Exception
	{
		importProject(importFile, newProjectFile, progressIndicator);
	}

	@Override
	protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception
	{
		MiradiZipFile zipFile = new MiradiZipFile(file);
		try
		{
			if(!zipContainsMpzProject(zipFile))
				return;
			
			ZipEntry mpzEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
			if (mpzEntry == null)
				return;

			final InputStream mpzInputStream = zipFile.getInputStream(mpzEntry);
			File tempMpzFile = FileUtilities.createTempFileCopyOf(mpzInputStream);
			possiblyNotifyUserOfAutoMigration(tempMpzFile);
		}
		finally
		{
			zipFile.close();
		}
	}

	public static void possiblyNotifyUserOfAutoMigration(File mpzFile)	throws Exception
	{
		final MiradiZipFile mpzZipFile = new MiradiZipFile(mpzFile);
		try
		{
			if(MpzToMpfConverter.needsMigration(mpzZipFile))
				notifyUserOfAutoMigration();
		}
		finally 
		{
			mpzZipFile.close();
		}
	}

	private void importProject(File zipFileToImport, File newProjectFile, ProgressInterface progressIndicator) throws ZipException, IOException, Exception, ValidationException
	{
		MiradiZipFile zipFile = new MiradiZipFile(zipFileToImport);
		try
		{
			if (zipContainsMpfProject(zipFile))
			{
				final RawProject project = importProjectFromMpfEntry(zipFile, progressIndicator);
				RawProjectSaver.saveProject(project, new UnicodeWriter(newProjectFile));
			}
			else if(zipContainsMpzProject(zipFile))
			{
				final RawProject project = importProjectFromMpzEntry(zipFile, progressIndicator);
				RawProjectSaver.saveProject(project, new UnicodeWriter(newProjectFile));
			}
			else
			{
				final Project project = importProjectFromXmlEntry(zipFile, progressIndicator);
				ProjectSaver.saveProject(project, newProjectFile);
			}
		}
		finally
		{
			zipFile.close();
		}
	}

	private RawProject importProjectFromMpfEntry(MiradiZipFile zipFile, ProgressInterface progressIndicator) throws Exception
	{
		ZipEntry mpfEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_MPF_NAME);
		InputStream inputStream = zipFile.getInputStream(mpfEntry);
		try
		{
			String mpfString = toString(inputStream);
			RawProject rawProject = RawProjectLoader.loadProject(mpfString);
			progressIndicator.setStatusMessage(EAM.text("Importing Miradi Data..."), 1);
			progressIndicator.incrementProgress();
			
			progressIndicator.setStatusMessage(EAM.text("Updating ConPro Project Number..."), 1);
			importConproProjectNumbers(zipFile, rawProject, progressIndicator);
			
			return rawProject;
		}
		finally
		{
			inputStream.close();
		}
	}

	private String toString(InputStream inputStream) throws Exception
	{
		UnicodeReader reader = new UnicodeReader(inputStream);
		try
		{
			return reader.readAll();
		}
		finally
		{
			reader.close();
		}
	}

	private RawProject importProjectFromMpzEntry(MiradiZipFile zipFile, ProgressInterface progressIndicator) throws Exception
	{
		ZipEntry mpzEntry = zipFile.getEntry(ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
		InputStream inputStream = zipFile.getInputStream(mpzEntry);
		try
		{
			RawProject project = importProjectFromMpzStream(inputStream, progressIndicator);
			
			progressIndicator.setStatusMessage(EAM.text("Updating ConPro Project Number..."), 1);
			importConproProjectNumbers(zipFile, project, progressIndicator);
			
			return project;
		}
		finally
		{
			inputStream.close();
		}
		
	}

	private RawProject importProjectFromMpzStream(InputStream inputStream, ProgressInterface progressIndicator) throws Exception
	{
		File mpzFile = extractStreamToFile(inputStream, progressIndicator);
		try
		{
			String mpfAsString = MpzToMpfConverter.convert(mpzFile, progressIndicator);
			return RawProjectLoader.loadProject(mpfAsString);
		}
		finally
		{
			FileUtilities.deleteExistingWithRetries(mpzFile);
		}
	}

	private void importConproProjectNumbers(MiradiZipFile zipFile, RawProject projectToFill, ProgressInterface progressIndicator) throws Exception
	{
		InputStreamWithSeek projectAsInputStream = getProjectAsInputStream(zipFile);
		try
		{
			new ConproXmlImporter(null, progressIndicator).importConProProjectNumbers(projectAsInputStream, projectToFill);
		}
		finally
		{
			projectAsInputStream.close();
		}
	}

	@Override
	protected void createOrOpenProject(Project projectToFill, File projectFile)	throws Exception
	{
		projectToFill.createOrOpenWithDefaultObjects(projectFile, new NullProgressMeter());
	}

	@Override
	protected void importProjectXml(Project projectToFill, MiradiZipFile zipFile, InputStreamWithSeek projectAsInputStream, ProgressInterface progressIndicator) throws Exception
	{
		progressIndicator.setStatusMessage(EAM.text("Importing ConPro Data..."), 16);
		ConproXmlImporter conProXmlImporter = new ConproXmlImporter(projectToFill, progressIndicator);
		conProXmlImporter.importConProProject(projectAsInputStream);
		ORef highOrAboveRankedThreatsTag = conProXmlImporter.getHighOrAboveRankedThreatsTag();
		splitMainDiagramByTargets(projectToFill, highOrAboveRankedThreatsTag);
		progressIndicator.incrementProgress();
		
		importAdditionalFieldsFromTextFiles(projectToFill, zipFile);
		progressIndicator.incrementProgress();
	}
	
	@Override
	protected Project createProjectToFill() throws Exception
	{
		Project project = new Project();
		project.createOrOpenWithDefaultObjects(new File("[Imported]"), new NullProgressMeter());
		return project;
	}
	
	private void importAdditionalFieldsFromTextFiles(Project projectToFill, MiradiZipFile zipFile) throws Exception
	{
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_RESOURCES_SCORECARD, zipFile, "ProjectResourcesScorecard.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_LEVEL_COMMENTS, zipFile, "ProjectLevelComments.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_PROJECT_CITATIONS, zipFile, "ProjectCitations.txt");
		importIfPresent(projectToFill, TncProjectData.TAG_CAP_STANDARDS_SCORECARD, zipFile, "ProjectCapStandards.txt");
	}

	private void importIfPresent(Project projectToFill, String fieldTag, MiradiZipFile zipFile, String entryFilename) throws Exception
	{
		ORef tncDataRef = projectToFill.getSafeSingleObjectRef(TncProjectDataSchema.getObjectType());
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
		ORef conceptualModelRef = conceptualModelRefs.getRefForType(ConceptualModelDiagramSchema.getObjectType());
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
		ORef firstRefInAlphabeticallySortedList = sortedConceptualModelRefs.getFirstElement();
		ViewData viewData = filledProject.getViewData(DiagramView.getViewName());
		CommandSetObjectData setCurrentDiagramCommand = new CommandSetObjectData(viewData, ViewData.TAG_CURRENT_CONCEPTUAL_MODEL_REF, firstRefInAlphabeticallySortedList.toString());
		filledProject.executeCommand(setCurrentDiagramCommand);
	}

	public static boolean zipContainsMpzProject(MiradiZipFile zipFile)
	{
		return containsEntry(zipFile, ExportCpmzDoer.PROJECT_ZIP_FILE_NAME);
	}

	public static boolean zipContainsMpfProject(MiradiZipFile zipFile)
	{
		return containsEntry(zipFile, ExportCpmzDoer.PROJECT_MPF_NAME);
	}

	public static boolean containsEntry(MiradiZipFile zipFile, final String entry)
	{
		ZipEntry zipEntry = zipFile.getEntry(entry);
		if (zipEntry == null)
			return false;

		return zipEntry.getSize() > 0;
	}
	
	@Override
	protected GenericMiradiFileFilter createFileFilter()
	{
		return new CpmzFileFilterForChooserDialog();
	}

	public static File extractStreamToFile(InputStream mpzInputStream, ProgressInterface progressIndicator) throws Exception
	{
		return FileUtilities.createTempFileCopyOf(mpzInputStream);
	}
}
