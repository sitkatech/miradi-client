package org.conservationmeasures.eam.views.noproject.wizard;

import java.awt.event.ActionEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.AbstractAction;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.actions.ActionImportTncCapWorkbook;
import org.conservationmeasures.eam.actions.ActionImportZippedProjectFile;
import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.noproject.CopyProject;
import org.conservationmeasures.eam.views.noproject.DeleteProject;
import org.conservationmeasures.eam.views.noproject.RenameProject;
import org.conservationmeasures.eam.views.umbrella.Definition;
import org.conservationmeasures.eam.views.umbrella.DefinitionCommonTerms;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;
import org.conservationmeasures.eam.views.umbrella.WizardPanel;
import org.martus.swing.HyperlinkHandler;

public class NoProjectWizardPanel extends WizardPanel implements HyperlinkHandler
{
	public NoProjectWizardPanel(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);
		mainWindow = mainWindowToUse;
		WELCOME = addStep(new NoProjectWizardWelcomeStep(this));
		IMPORT = addStep(new NoProjectWizardImportStep(this));
		CREATE =addStep(new NoProjectWizardProjectCreateStep(this));

		setStep(WELCOME);
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
				File projectDirectory = new File(EAM.getHomeDirectory(), projectName);
				renameProject(projectDirectory);
			}
			else if(linkDescription.startsWith(DEFINITION_PREFIX))
			{
				Definition def = DefinitionCommonTerms.getDefintion(linkDescription);
				EAM.okDialog("Definition:" + def.term, new String[] {def.definition});
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

	public void valueChanged(String name, String value)
	{
		nameToValueMap.put(name, value);
	}
	
	public String getValue(String name)
	{
		if (nameToValueMap.containsKey(name))
			return (String)nameToValueMap.get(name);
		return "";
	}

	public void buttonPressed(String buttonName)
	{
		try
		{
			if(buttonName.equals("NewProject"))
			{
				setStep(CREATE);
			}
			else if(buttonName.equals("CreateProject"))
			{
				createProject();
			}
			else if(buttonName.equals("Import"))
			{
				setStep(IMPORT);
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
			else if(buttonName.indexOf("Next") >= 0)
			{
				next();
			}
			else if(buttonName.indexOf("Back") >= 0)
			{
				if (getCurrentStep() == CREATE)
					setStep(WELCOME);
				else
					previous();
			}
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

	public MainWindow getMainWindow()
	{
		return mainWindow;
	}

	HashMap nameToValueMap = new HashMap();
	
	public static final String OPEN_PREFIX = "OPEN:";
	public static final String COPY_PREFIX = "COPY:";
	public static final String RENAME_PREFIX = "RENAME:";
	public static final String DELETE_PREFIX = "DELETE:";
	public static final String EXPORT_PREFIX = "EXPORT:";
	
	public static final String DEFINITION_PREFIX = "Definition:";

	MainWindow mainWindow;
	int WELCOME;
	int IMPORT;
	int CREATE;

}
