/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JPopupMenu;
import javax.swing.text.JTextComponent;

import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlFormEventHandler;
import org.conservationmeasures.eam.views.noproject.CopyProject;
import org.conservationmeasures.eam.views.noproject.DeleteProject;
import org.conservationmeasures.eam.views.noproject.RenameProject;
import org.conservationmeasures.eam.views.umbrella.Definition;
import org.conservationmeasures.eam.views.umbrella.DefinitionCommonTerms;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;
import org.conservationmeasures.eam.views.umbrella.ImporFromUrlZippedProjectFileDoer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.martus.swing.HyperlinkHandler;

public class NoProjectWizardPanel extends WizardPanel implements HtmlFormEventHandler
{
	public NoProjectWizardPanel(UmbrellaView view) throws Exception
	{
		super(view.getMainWindow(), view);
	}

	public void linkClicked(String linkDescription)
	{
		if(getMainWindow().mainLinkFunction(linkDescription))
			return;
		
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
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				renameProject(projectDirectory);
			}
			else if(linkDescription.startsWith(DEFINITION_PREFIX))
			{
				Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
				EAM.okDialog(def.term, new String[] {def.definition});
			}
			else if(linkDescription.startsWith(DOWNLOAD_PREFIX))
			{
				ImporFromUrlZippedProjectFile();
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

	
	public void setComponent(String name, JComponent component)
	{
		if (name.equals("NewProjectName"))
			newProjectNameField = (JTextComponent)component;
	}
	
	public String getValue(String name)
	{
		return newProjectNameField.getText();
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals("CreateProject"))
			{
				createProject();
			}
			if(buttonName.equals("ImportZip"))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportZippedProjectFile.class);
				action.doAction();
			}
			else if(buttonName.equals("ImportCAP"))
			{
				EAMAction action = getMainWindow().getActions().get(ActionImportTncCapWorkbook.class);
				action.doAction();
			}
			else control(buttonName);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to process request: ") + e);
		}
	}

	private void createProject() throws Exception
	{
		String newName = getValue("NewProjectName");
		if (newName.length()>0)
		{
			if (getMainWindow().getProject().isValidProjectFilename(newName))
			{
				File newFile = new File(EAM.getHomeDirectory(),newName);
				getMainWindow().createOrOpenProject(newFile);
			}
			else
				EAM.notifyDialog(EAM.text("Invalid project name, choose another name."));
		}
	}

	private void renameProject(File projectDirectory) throws Exception
	{
		RenameProject.doIt(getMainWindow(), projectDirectory);
		refresh();
	}

	private void deleteProjectAfterConfirmation(File projectDirectory) throws Exception
	{
		DeleteProject.doIt(getMainWindow(), projectDirectory);
		refresh();
	}

	private void exportProject(File projectDirectory) throws CommandFailedException
	{
		ExportZippedProjectFileDoer.perform(getMainWindow(), projectDirectory);
	}

	private void copyProject(File projectDirectory) throws Exception
	{
		CopyProject.doIt(getMainWindow(), projectDirectory);
		refresh();
	}
	
	private void ImporFromUrlZippedProjectFile() throws Exception
	{
		new ImporFromUrlZippedProjectFileDoer().doIt(mainWindow);
		refresh();
	}
	
	
	JTextComponent newProjectNameField;
	
	public static final String OPEN_PREFIX = "OPEN:";
	public static final String COPY_PREFIX = "COPY:";
	public static final String RENAME_PREFIX = "RENAME:";
	public static final String DELETE_PREFIX = "DELETE:";
	public static final String EXPORT_PREFIX = "EXPORT:";
	public static final String DOWNLOAD_PREFIX = "DOWNLOAD:";
	
	public static final String DEFINITION_PREFIX = "Definition:";

}
