/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JScrollPane;

import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.database.ProjectServer;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.project.ProjectUnzipper;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiFileChooser;

public class NoProjectView extends UmbrellaView implements HyperlinkHandler
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		
		htmlViewer = new HtmlViewer("", this);
		setToolBar(new NoProjectToolBar(getActions()));
	}
	
	public void linkClicked(String linkDescription)
	{
		if(linkDescription.equals(NoProjectHtmlText.NEW_PROJECT))
		{
			Action action = new ActionNewProject(getMainWindow());
			action.actionPerformed(null);
		}
		else if(linkDescription.startsWith(NoProjectHtmlText.OPEN_PREFIX))
		{
			String projectName = linkDescription.substring(NoProjectHtmlText.OPEN_PREFIX.length());
			File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
			getMainWindow().createOrOpenProject(projectDirectory);
		}
		else if(linkDescription.equals(NoProjectHtmlText.IMPORT_ZIP))
		{
			doImportZip();
		}
		else if(linkDescription.equals("Definition:Project"))
		{
			EAM.okDialog("Definition: Project", new String[] {"A project is..."});
		}
		else if(linkDescription.equals("Definition:EAM"))
		{
			EAM.okDialog("Definition: e-Adaptive Management", new String[] {"e-Adaptive Management is..."});
		}
		else
		{
			EAM.okDialog("Not implemented yet", new String[] {"Not implemented yet"});
		}
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();
		refreshText();
		
		JScrollPane scrollPane = new JScrollPane(htmlViewer);
		
		setLayout(new BorderLayout());
		add(scrollPane, BorderLayout.CENTER);
	}

	private void refreshText()
	{
		String htmlText = new NoProjectHtmlText().getText();
		htmlViewer.setText(htmlText);
	}
	
	public void becomeInactive() throws Exception
	{
		// nothing to do...would clear all view data
		super.becomeInactive();
	}

	public void valueChanged(String widget, String newValue)
	{
	}

	public void doImportZip()
	{
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Zipped Project");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory);
		if (results.wasCancelChoosen())
			return;
		File zipToImport = results.getChosenFile();
		String projectName = withoutExtension(zipToImport.getName());
		File finalProjectDirectory = new File(EAM.getHomeDirectory(), projectName);
		if(ProjectServer.isExistingProject(finalProjectDirectory))
		{
			EAM.notifyDialog("Cannot import a project that already exists: " + projectName);
			return;
		}
		if(finalProjectDirectory.exists())
		{
			EAM.notifyDialog("Cannot import over an existing file or directory: " + 
					finalProjectDirectory.getAbsolutePath());
			return;
		}
		
		try
		{
			if(!ProjectUnzipper.isZipFileImportable(zipToImport))
			{
				EAM.notifyDialog("Not a valid project zip file");
				return;
			}
			
			ProjectUnzipper.unzipToProjectDirectory(zipToImport, finalProjectDirectory);
			
			refreshText();
			EAM.notifyDialog("Import complete");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			EAM.errorDialog("Import failed");
		}
		
	}
	
	private String withoutExtension(String fileName)
	{
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		return fileName.substring(0, lastDotAt);
	}
	
	public void buttonPressed(String buttonName)
	{
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.NO_PROJECT_VIEW_NAME;
	}

	HtmlViewer htmlViewer;
}

