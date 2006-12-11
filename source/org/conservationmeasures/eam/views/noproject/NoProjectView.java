/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.io.File;

import javax.swing.Action;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZipFile;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.views.umbrella.WizardHtmlViewer;
import org.conservationmeasures.eam.views.umbrella.WizardStep;
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
			if(linkDescription.startsWith(ProjectListInHtml.OPEN_PREFIX))
			{
				String projectName = linkDescription.substring(ProjectListInHtml.OPEN_PREFIX.length());
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				getMainWindow().createOrOpenProject(projectDirectory);
			}
			else if(linkDescription.equals("Definition:Project"))
			{
				EAM.okDialog("Definition: Project", new String[] {"A project is..."});
			}
			else if(linkDescription.equals("Definition:CMP"))
			{
				EAM.okDialog("Definition: Conservation Measures Partnership", new String[] {"The Conservation Measures Partnership (CMP) is..."});
			}
			else if(linkDescription.equals("Definition:OpenStandards"))
			{
				EAM.okDialog("Definition: Open Standards", new String[] {"The Open Standards are..."});
			}
			else if(linkDescription.equals("Definition:NewProject"))
			{
				EAM.okDialog("Definition: New Project", new String[] {"A New Project is..."});
			}
			else if(linkDescription.equals("Definition:ImportZip"))
			{
				EAM.okDialog("Definition: Zipped Project", new String[] {"A Zipped Project is..."});
			}
			else if(linkDescription.equals("Definition:ImportCAP"))
			{
				EAM.okDialog("Definition: CAP Workbook", new String[] {"A CAP Workbook is..."});
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

	public void valueChanged(String widget, String newValue)
	{
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals("NewProject"))
			{
				Action action = new ActionNewProject(getMainWindow());
				action.actionPerformed(null);
			}
			else if(buttonName.equals("ImportZip"))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportZipFile.class);
				action.doAction();
			}
			else if(buttonName.equals("ImportCAP"))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportTncCapWorkbook.class);
				action.doAction();
			}
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to process request: ") + e);
		}
	}

	public void becomeActive() throws Exception
	{
		super.becomeActive();
		removeAll();

		String header = WizardStep.loadHtmlFile(getClass(), "WelcomeHeader.html");
		WizardHtmlViewer headerHtmlViewer = new WizardHtmlViewer(this);
		headerHtmlViewer.setText(header);
		add(headerHtmlViewer, BorderLayout.BEFORE_FIRST_LINE);

		JPanel panel = new JPanel(new GridLayout(1, 2));
		String newProject = WizardStep.loadHtmlFile(getClass(), "WelcomeNew.html");
		HtmlViewer newProjectHtmlViewer = new HtmlViewer(newProject, this);
		newProjectHtmlViewer.setText(newProject);
		panel.add(newProjectHtmlViewer);
		
		projectList = new HtmlViewer("", this);
		refreshText();
		panel.add(new UiScrollPane(projectList));
		
		add(panel, BorderLayout.CENTER);
	}

	public void becomeInactive() throws Exception
	{
		// nothing to do...would clear all view data
		super.becomeInactive();
	}
	
	public void refreshText()
	{
		String html = new ProjectListInHtml().getText();
		projectList.setText(html);
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.NO_PROJECT_VIEW_NAME;
	}

	HtmlViewer projectList;
}

