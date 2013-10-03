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
import java.io.FileNotFoundException;
import java.util.zip.ZipEntry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.martus.swing.UiFileChooser;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.ZipEntryInputStreamWithSeek;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.CorruptSimpleThreatRatingDataException;
import org.miradi.exceptions.CpmzVersionTooOldException;
import org.miradi.exceptions.FutureSchemaVersionException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.exceptions.ValidationException;
import org.miradi.exceptions.XmpzVersionTooOldException;
import org.miradi.files.AbstractMpfFileFilter;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.AbstractFileChooser;
import org.miradi.utils.GenericMiradiFileFilter;
import org.miradi.utils.MiradiBackgroundWorkerThread;
import org.miradi.utils.MiradiFileSaveChooser;
import org.miradi.utils.MiradiZipFile;
import org.miradi.utils.ProgressInterface;
import org.miradi.views.noproject.NoProjectView;

public abstract class AbstractProjectImporter
{	
	public AbstractProjectImporter(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public void importProject() throws Exception 
	{
		try
		{
			JFileChooser fileChooser = new JFileChooser(currentDirectory);
			fileChooser.setDialogTitle(EAM.text("Import Project"));
			addFileFilters(fileChooser);
			
			fileChooser.setDialogType(JFileChooser.CUSTOM_DIALOG);
			fileChooser.setApproveButtonToolTipText(EAM.text(getApproveButtonToolTipText()));
			if (fileChooser.showDialog(getMainWindow(), getDialogApprovelButtonText()) != JFileChooser.APPROVE_OPTION)
				return;
			
			File fileToImport = fileChooser.getSelectedFile();
			fileToImport = MiradiFileSaveChooser.getFileWithExtension(fileChooser, fileToImport);
			
			File importedFile = importProject(fileToImport);
			if (importedFile != null)
				userConfirmOpenImportedProject(importedFile);
		}
		catch (CorruptSimpleThreatRatingDataException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("This project cannot be imported because its Threat Rating data is missing or damaged. " +
									  "Please contact Miradi support for recovery options."));
		}
		catch (UserCanceledException e)
		{
			EAM.notifyDialog(EAM.text("Import was canceled!"));
		}
		catch (UnsupportedNewVersionSchemaException e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(IMPORT_FAILED_MESSAGE);
		}
		catch (CpmzVersionTooOldException e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("This project cannot be imported because it is in an old data format. \n" +
									 "Please re-export a new copy from ConPro, and import that instead."));
		}
		catch (XmpzVersionTooOldException e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		catch (ValidationException e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(e.getMessage());
		}
		catch (FutureSchemaVersionException e)
		{
			EAM.logException(e);
			showImportFailedErrorDialog("This project cannot be imported by this version of Miradi because it <BR>" +
										"is in a newer data format. Please upgrade to the latest version of Miradi.");
		}
		catch(Exception e)
		{
			EAM.logException(e);
			String message = e.getMessage();
			if(message == null)
				message = "";
			showImportFailedErrorDialog(message);
		}
	}

	private String getNameWithoutExtension(File fileToImport)
	{
		GenericMiradiFileFilter[] fileFilters = getFileFilters();
		final String fileName = fileToImport.getName();
		for (int index = 0; index < fileFilters.length; ++index)
		{
			final String fileExtension = fileFilters[index].getFileExtension();
			if (fileName.endsWith(fileExtension))
			{
				final int indexOfExtension = fileName.lastIndexOf(fileExtension);
				return fileName.substring(0, indexOfExtension);
			}
		}
		
		return fileName;
	}

	public File importProject(File fileToImport) throws Exception
	{
		final String fileNameWithoutExtension = getNameWithoutExtension(fileToImport);
		final File proposedMpfProjectFile = new File(AbstractMpfFileFilter.createNameWithExtension(fileNameWithoutExtension));
		
		return importProject(EAM.getHomeDirectory(), fileToImport, proposedMpfProjectFile);
	}
	
	public File importProject(File projectDirectory, File fileToImport, final File proposedProjectFile) throws Exception
	{
		String projectFileName = getMainWindow().askForDestinationProjectName(proposedProjectFile);
		if (projectFileName == null)
			return null;
		
		possiblyNotifyUserOfAutomaticMigration(fileToImport);
		ProgressDialog progressDialog = new ProgressDialog(getMainWindow(), EAM.text("Importing..."));
		Worker worker = new Worker(progressDialog, projectDirectory, fileToImport, projectFileName);
		progressDialog.doWorkInBackgroundWhileShowingProgress(worker);
		
		refreshNoProjectPanel();
		currentDirectory = fileToImport.getParent();
		
		return worker.getImportedFile();
	}
	
	private class Worker extends MiradiBackgroundWorkerThread
	{
		public Worker(ProgressInterface progressInterfaceToUse, File projectDirectory, File fileToImportToUse, String projectFileNameToUse) throws Exception
		{
			super(progressInterfaceToUse);
			
			fileToImport = fileToImportToUse;
			projectFileName = projectFileNameToUse;
			newProjectFile = new File(projectDirectory, projectFileName);

			if(!Project.isValidMpfProjectFilename(projectFileName))
				throw new Exception("Illegal project name: " + projectFileName);
		}
		
		public File getImportedFile()
		{
			return newProjectFile;
		}

		@Override
		protected void doRealWork() throws Exception
		{
			createProject(fileToImport, newProjectFile, getProgressIndicator());
			getProgressIndicator().finished();
		}
		
		private File fileToImport;
		private String projectFileName;
		private File newProjectFile;
	}

	private void userConfirmOpenImportedProject(File projectFile) throws Exception
	{
		String openProjectMessage = EAM.substitute(EAM.text("Import Completed.  Would you like to open %s?"), projectFile.getName());
		boolean shouldOpenProjectAfterImport = EAM.confirmOpenDialog(EAM.text("Open Project"), openProjectMessage);
		if (shouldOpenProjectAfterImport)
		{
			getMainWindow().createOrOpenProject(projectFile);
		}
	}

	private void addFileFilters(JFileChooser fileChooser)
	{
		FileFilter[] filters = getFileFilters();
		AbstractFileChooser.addFileFilters(fileChooser, filters);
	}

	private void showImportFailedErrorDialog(String message)
	{
		if(message == null)
			message = EAM.text("Unexpected error");
		String safeMessage = EAM.substitute(EAM.text("<html>Import failed: <br><p> %s </p></html>"), message);
		EAM.errorDialog(safeMessage);
	}

	private String getApproveButtonToolTipText()
	{
		return "Import";
	}
	
	private String getDialogApprovelButtonText()
	{
		return "Import";
	}
	
	private void refreshNoProjectPanel() throws Exception
	{
		NoProjectView noProjectView = (NoProjectView) getMainWindow().getView(NoProjectView.getViewName());
		noProjectView.refreshText();
	}
	
	protected InputStreamWithSeek getProjectAsInputStream(MiradiZipFile zipFile) throws Exception
	{
		String xmlFileName = XmlExporterDoer.PROJECT_XML_FILE_NAME;
		ZipEntry zipEntry = zipFile.getEntry(xmlFileName);
		if(zipEntry == null)
			throw new FileNotFoundException("Invalid XMPZ file: Could not find " + xmlFileName);
		
		return new ZipEntryInputStreamWithSeek(zipFile, zipEntry);
	}
	
	public static void notifyUserOfAutoMigration() throws Exception
	{
		EAM.showHtmlInfoMessageOkDialog(AUTO_MIGRATION_MESSAGE_FILE_NAME);
	}

	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private static final String AUTO_MIGRATION_MESSAGE_FILE_NAME = "AutoMigrationMessage.html";

	private static String currentDirectory = UiFileChooser.getHomeDirectoryFile().getPath();
	private static final String IMPORT_FAILED_MESSAGE = EAM.text("This file cannot be imported because it is a newer format than this version of Miradi supports. <br>" +
			  "Please make sure you are running the latest version of Miradi. If you are already <br>" +
			  "running the latest Miradi, either wait for a newer version that supports this format, <br>" +
			  "or re-export the project to an older (supported) format.");
	
	protected abstract void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator)  throws Exception;
	
	protected abstract GenericMiradiFileFilter[] getFileFilters();
	
	abstract protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception;
	
	private MainWindow mainWindow;
}
