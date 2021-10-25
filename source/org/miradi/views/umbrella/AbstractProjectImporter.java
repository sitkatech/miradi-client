/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import java.io.FileNotFoundException;
import java.util.zip.ZipEntry;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.martus.swing.UiFileChooser;
import org.martus.util.Stopwatch;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.ZipEntryInputStreamWithSeek;
import org.miradi.dialogs.base.ProgressDialog;
import org.miradi.exceptions.CorruptSimpleThreatRatingDataException;
import org.miradi.exceptions.FutureSchemaVersionException;
import org.miradi.exceptions.UnsupportedNewVersionSchemaException;
import org.miradi.exceptions.UserCanceledException;
import org.miradi.exceptions.XmlValidationException;
import org.miradi.exceptions.XmlVersionTooOldException;
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
			if (fileChooser.showDialog(getMainWindow(), getDialogApproveButtonText()) != JFileChooser.APPROVE_OPTION)
				return;
			
			File fileToImport = fileChooser.getSelectedFile();
			fileToImport = MiradiFileSaveChooser.getFileWithExtension(fileChooser, fileToImport);
			
			Stopwatch sw = new Stopwatch();
			File importedFile = importProject(fileToImport);
			long elapsedInSeconds = sw.elapsedInSeconds();
			EAM.logDebug("xmpz2 import took: " + elapsedInSeconds + " seconds");
			if (importedFile != null)
				getMainWindow().createOrOpenProject(importedFile);
		}
		catch (Exception e)
		{
			handleImportException(e);
		}
	}

	public static void handleImportException(Exception e)
	{
		if (e instanceof CorruptSimpleThreatRatingDataException)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("This project cannot be imported because its Threat Rating data is missing or damaged. " +
					"Please contact Miradi support for recovery options."));
			return;
		}

		if (e instanceof UserCanceledException)
		{
			EAM.notifyDialog(EAM.text("Import was canceled!"));
			return;
		}

		if (e instanceof UnsupportedNewVersionSchemaException)
		{
			logTooNewVersionException(e);
			return;
		}

		if (e instanceof XmlVersionTooOldException)
		{
			EAM.alertUserOfNonFatalException(e);
			return;
		}

		if (e instanceof XmlValidationException)
		{
			EAM.logException(e);
			showImportFailedErrorDialog(e.getMessage());
			return;
		}

		if (e instanceof FutureSchemaVersionException)
		{
			EAM.logException(e);
			showImportFailedErrorDialog("This project cannot be imported by this version of Miradi because it <BR>" +
					"is in a newer data format. Please upgrade to the latest version of Miradi.");
			return;
		}

		EAM.logException(e);
		String message = e.getMessage();
		if(message == null)
			message = "";
		showImportFailedErrorDialog(message);
	}

	public static void logTooNewVersionException(Exception e)
	{
		EAM.logException(e);
		showImportFailedErrorDialog(IMPORT_FAILED_MESSAGE);
	}

	public File importProject(File fileToImport) throws Exception
    {
        return importProject(EAM.getHomeDirectory(), fileToImport, fileToImport);
    }

	public File importProject(File projectDirectory, File fileToImport, final File proposedProjectFile) throws Exception
	{
		String proposedProjectFileName = getMainWindow().getDestinationProjectFileName(proposedProjectFile);
		if (proposedProjectFileName == null)
			return null;
		
		possiblyNotifyUserOfAutomaticMigration(fileToImport);
		ProgressDialog progressDialog = new ProgressDialog(getMainWindow(), EAM.text("Importing..."));
		Worker worker = new Worker(progressDialog, projectDirectory, fileToImport, proposedProjectFileName);
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
		}
		
		private File fileToImport;
		private String projectFileName;
		private File newProjectFile;
	}

	private void addFileFilters(JFileChooser fileChooser)
	{
		FileFilter[] filters = getFileFilters();
		AbstractFileChooser.addFileFilters(fileChooser, filters);
	}

	private static void showImportFailedErrorDialog(String message)
	{
		if(message == null)
			message = EAM.text("Unexpected error");
		String safeMessage = EAM.substituteSingleString(EAM.text("<html>Import failed: <br><p> %s </p></html>"), message);
		EAM.errorDialog(safeMessage);
	}

	private String getApproveButtonToolTipText()
	{
		return "Import";
	}
	
	private String getDialogApproveButtonText()
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
			  "Please make sure you are running the latest version of Miradi. <br>" +
			  "To download the latest version of Miradi, go to www.miradi.org.");
	
	protected abstract void createProject(File importFile, File newProjectFile, ProgressInterface progressIndicator)  throws Exception;
	
	protected abstract GenericMiradiFileFilter[] getFileFilters();
	
	abstract protected void possiblyNotifyUserOfAutomaticMigration(File file) throws Exception;
	
	private MainWindow mainWindow;
}
