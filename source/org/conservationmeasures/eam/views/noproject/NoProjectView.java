/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.noproject;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.ActionNewProject;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;
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
			if(linkDescription.startsWith(OPEN_PREFIX))
			{
				String projectName = linkDescription.substring(OPEN_PREFIX.length());
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				getMainWindow().createOrOpenProject(projectDirectory);
			}
			else if(linkDescription.startsWith(COPY_PREFIX))
			{
				String projectName = linkDescription.substring(COPY_PREFIX.length());
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				copyProject(projectDirectory);
			}
			else if(linkDescription.startsWith(EXPORT_PREFIX))
			{
				String projectName = linkDescription.substring(EXPORT_PREFIX.length());
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				exportProject(projectDirectory);
			}
			else if(linkDescription.startsWith(DELETE_PREFIX))
			{
				String projectName = linkDescription.substring(DELETE_PREFIX.length());
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				deleteProjectAfterConfirmation(projectDirectory);
			}
			else if(linkDescription.startsWith(RENAME_PREFIX))
			{
				String projectName = linkDescription.substring(RENAME_PREFIX.length());
				renameProject(projectName);
			}
			else if(linkDescription.startsWith(DEFINITION_PREFIX))
			{
				showDefinition(linkDescription);
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
	
	void showDefinition(String linkDescription)
	{
		String itemToDefine = linkDescription.substring(DEFINITION_PREFIX.length());
		String[] definition = new String[] {"Not available"};
		
		if(itemToDefine.equals("Project"))
		{
			definition = new String[] {"A project is..."};
		}
		else if(itemToDefine.equals("CMP"))
		{
			definition = new String[] {"The Conservation Measures Partnership (CMP) is..."};
		}
		else if(itemToDefine.equals("OpenStandards"))
		{
			definition = new String[] {"The Open Standards are..."};
		}
		else if(itemToDefine.equals("NewProject"))
		{
			definition = new String[] {"A New Project is..."};
		}
		else if(itemToDefine.equals("ImportZip"))
		{
			definition = new String[] {"A Zipped Project is..."};
		}
		else if(itemToDefine.equals("ImportCAP"))
		{
			definition = new String[] {"A CAP Workbook is..."};
		}
		
		EAM.okDialog("Definition", definition);
	}
	
	public JPopupMenu getRightClickMenu(String itemText)
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add(new FakeHyperlinkAction("Open", this, OPEN_PREFIX + itemText));
		menu.addSeparator();
		menu.add(new FakeHyperlinkAction("Rename...", this, RENAME_PREFIX + itemText));
		menu.add(new FakeHyperlinkAction("Copy to...", this, COPY_PREFIX + itemText));
		menu.add(new FakeHyperlinkAction("Export to Zip...", this, EXPORT_PREFIX + itemText));
		menu.addSeparator();
		menu.add(new FakeHyperlinkAction("Delete...", this, DELETE_PREFIX + itemText));
		return menu;
	}
	
	static class FakeHyperlinkAction extends AbstractAction
	{
		public FakeHyperlinkAction(String menuLabel, HyperlinkHandler handlerToUse, String urlToUse)
		{
			super(menuLabel);
			handler = handlerToUse;
			url = urlToUse;
		}
		
		public void actionPerformed(ActionEvent e)
		{
			handler.linkClicked(url);
		}
		
		HyperlinkHandler handler;
		String url;
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
				EAMAction action = getMainWindow().getActions().get(ActionImportZippedProjectFile.class);
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
		
		projectList = new ProjectList(this);
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
		projectList.refresh();
	}

	private void renameProject(String projectName)
	{
		// TODO implement this
	}

	private void deleteProjectAfterConfirmation(File projectDirectory) throws Exception
	{
		DeleteProject.doIt(getMainWindow(), projectDirectory);
	}

	private void exportProject(File projectDirectory) throws CommandFailedException
	{
		ExportZippedProjectFileDoer.perform(getMainWindow(), projectDirectory);
	}

	private void copyProject(File projectDirectory)
	{
		// TODO implement this
	}

	public String cardName()
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.NO_PROJECT_VIEW_NAME;
	}

	public static final String OPEN_PREFIX = "OPEN:";
	public static final String COPY_PREFIX = "COPY:";
	public static final String RENAME_PREFIX = "RENAME:";
	public static final String DELETE_PREFIX = "DELETE:";
	public static final String EXPORT_PREFIX = "EXPORT:";
	
	public static final String DEFINITION_PREFIX = "Definition:";

	ProjectList projectList;
}

