/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.utils.HtmlViewPanel;
import org.conservationmeasures.eam.views.noproject.CopyProject;
import org.conservationmeasures.eam.views.noproject.DeleteProject;
import org.conservationmeasures.eam.views.noproject.NoProjectView;
import org.conservationmeasures.eam.views.noproject.RenameProject;
import org.conservationmeasures.eam.views.umbrella.Definition;
import org.conservationmeasures.eam.views.umbrella.DefinitionCommonTerms;
import org.conservationmeasures.eam.views.umbrella.ExportZippedProjectFileDoer;
import org.conservationmeasures.eam.views.umbrella.UrlZippedProjectFileImporter;
import org.conservationmeasures.eam.wizard.SkeletonWizardStep;
import org.conservationmeasures.eam.wizard.WizardHtmlViewer;
import org.conservationmeasures.eam.wizard.WizardManager;
import org.conservationmeasures.eam.wizard.WizardPanel;
import org.conservationmeasures.eam.wizard.noproject.projectlist.ProjectListPanel;
import org.martus.swing.HyperlinkHandler;

public class NoProjectWizardStep extends SkeletonWizardStep implements KeyListener
{
	public NoProjectWizardStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse, NoProjectView.getViewName());
		String header = EAM.loadResourceFile(getClass(), "WelcomeHeader.html");
		WizardHtmlViewer headerHtmlViewer = new WizardHtmlViewer(getMainWindow(), this);
		headerHtmlViewer.setText(header);

		String introText = EAM.loadResourceFile(getClass(), "WelcomeIntroduction.html");
		introHtmlViewer = new WizardHtmlViewer(getMainWindow(), this);
		introHtmlViewer.setText(introText);

		JPanel headerBox = new JPanel(new BorderLayout());
		headerBox.add(headerHtmlViewer, BorderLayout.BEFORE_FIRST_LINE);
		headerBox.add(introHtmlViewer, BorderLayout.CENTER);
		add(headerBox, BorderLayout.BEFORE_FIRST_LINE);

		projectList = new ProjectListPanel(getMainWindow(), this);
		
		setBackground(AppPreferences.WIZARD_BACKGROUND);
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
	}
	
	public void refresh() throws Exception
	{
		introHtmlViewer.setText(introHtmlViewer.getText());
		projectList.refresh();
	}

	public void linkClicked(String linkDescription)
	{
		if(getMainWindow().mainLinkFunction(linkDescription))
			return;
		
		Cursor prevCursor = getMainWindow().getCursor();
		getMainWindow().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		
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
				HtmlViewPanel htmlViewPanel = new HtmlViewPanel(getMainWindow(), def.term,  def.getDefintion());
				htmlViewPanel.showAsOkDialog();
			}
			else if(linkDescription.startsWith(DOWNLOAD_PREFIX))
			{
				ImporFromUrlZippedProjectFile();
			}
			else
			{
				EAM.okDialog(EAM.text("Not implemented yet"), new String[] {EAM.text("Not implemented yet")});
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Unable to process request: ") + e);
		}
		finally
		{
			getMainWindow().setCursor(prevCursor);
		}
	}
	

	public JPopupMenu getRightClickMenu(String itemText)
	{
		JPopupMenu menu = new JPopupMenu();
		menu.add(new FakeHyperlinkAction(EAM.text("Open"), this, OPEN_PREFIX + itemText));
		menu.addSeparator();
		menu.add(new FakeHyperlinkAction(EAM.text("Rename..."), this, RENAME_PREFIX + itemText));
		menu.add(new FakeHyperlinkAction(EAM.text("Copy to..."), this, COPY_PREFIX + itemText));
		menu.add(new FakeHyperlinkAction(EAM.text("Export to Miradi Zip..."), this, EXPORT_PREFIX + itemText));
		menu.addSeparator();
		menu.add(new FakeHyperlinkAction(EAM.text("Delete..."), this, DELETE_PREFIX + itemText));
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

	
	public void keyPressed(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyCode() ==KeyEvent.VK_ENTER)
		{
			buttonPressed(WizardManager.CONTROL_NEXT);
		}
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent e)
	{
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
		UrlZippedProjectFileImporter.importMarineExample(getMainWindow());
		refresh();
	}
	
	
	public static final String OPEN_PREFIX = "OPEN:";
	private static final String COPY_PREFIX = "COPY:";
	private static final String RENAME_PREFIX = "RENAME:";
	private static final String DELETE_PREFIX = "DELETE:";
	private static final String EXPORT_PREFIX = "EXPORT:";
	private static final String DOWNLOAD_PREFIX = "DOWNLOAD:";
	private static final String DEFINITION_PREFIX = "Definition:";
	
	WizardHtmlViewer introHtmlViewer;
	ProjectListPanel projectList;
}
