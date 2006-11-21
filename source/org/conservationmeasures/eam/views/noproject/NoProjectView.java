/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JSplitPane;

import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZipFile;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.noproject.wizard.NoProjectWizardPanel;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.ViewSplitPane;
import org.martus.swing.HtmlViewer;
import org.martus.swing.HyperlinkHandler;
import org.martus.swing.UiScrollPane;

public class NoProjectView extends UmbrellaView implements HyperlinkHandler
{
	public NoProjectView(MainWindow mainWindow) throws Exception
	{
		super(mainWindow);
		
		setToolBar(new NoProjectToolBar(getActions()));
	}
	
	public void linkClicked(String linkDescription)
	{
		try 
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
				EAMAction action = getMainWindow().getActions().get(ActionImportZipFile.class);
				action.doAction();
			}
			else if(linkDescription.equals(NoProjectHtmlText.IMPORT_TNC_CAP_PROJECT))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportTncCapWorkbook.class);
				action.doAction();
			}
			else if(linkDescription.equals("Definition:Project"))
			{
				EAM.okDialog("Definition: Project", new String[] {"A project is..."});
			}
			else
			{
				EAM.okDialog("Not implemented yet", new String[] {"Not implemented yet"});
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to process request: ") + e);
		}
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();

		htmlViewer = new HtmlViewer("", this);
		refreshText();
		wizardPanel = new NoProjectWizardPanel(this);
		
		bigSplitter = new ViewSplitPane(getMainWindow(), wizardPanel, new UiScrollPane(htmlViewer), bigSplitter);
		add(bigSplitter, BorderLayout.CENTER);
	}

	public void becomeInactive() throws Exception
	{
		// nothing to do...would clear all view data
		super.becomeInactive();
	}
	
	public void refreshText()
	{
		htmlViewer.setText(new NoProjectHtmlText().getText());
	}

	public void valueChanged(String widget, String newValue)
	{
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

	JSplitPane bigSplitter;
	NoProjectWizardPanel wizardPanel;
	HtmlViewer htmlViewer;
}

