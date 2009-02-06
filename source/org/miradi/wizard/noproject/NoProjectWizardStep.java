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
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.martus.swing.HyperlinkHandler;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MiradiLogoPanel;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.HtmlViewPanel;
import org.miradi.utils.HtmlViewPanelWithMargins;
import org.miradi.views.noproject.CopyProject;
import org.miradi.views.noproject.DeleteProject;
import org.miradi.views.noproject.NoProjectView;
import org.miradi.views.noproject.RenameProjectDoer;
import org.miradi.views.umbrella.Definition;
import org.miradi.views.umbrella.DefinitionCommonTerms;
import org.miradi.views.umbrella.ExportZippedProjectFileDoer;
import org.miradi.wizard.SkeletonWizardStep;
import org.miradi.wizard.WizardHtmlViewer;
import org.miradi.wizard.WizardManager;
import org.miradi.wizard.WizardPanel;
import org.miradi.wizard.noproject.projectlist.TreeBasedProjectList;

public class NoProjectWizardStep extends SkeletonWizardStep implements KeyListener
{
	public NoProjectWizardStep(WizardPanel wizardToUse) throws Exception
	{
		super(wizardToUse, NoProjectView.getViewName());
		
		WizardHtmlViewer introHtmlViewer = new WizardHtmlViewer(getMainWindow(), this);
		introHtmlViewer.setText(getTextBelowLogo());

		JPanel headerBox = new JPanel(new BorderLayout());
		headerBox.add(new MiradiLogoPanel(), BorderLayout.BEFORE_FIRST_LINE);
		headerBox.add(introHtmlViewer, BorderLayout.CENTER);
		add(headerBox, BorderLayout.BEFORE_FIRST_LINE);

		projectList = new TreeBasedProjectList(getMainWindow(), this);
		
		setBackground(AppPreferences.getWizardBackgroundColor());
	}

	protected String getTextBelowLogo() throws Exception
	{
		return "";
	}
	
	public void refresh() throws Exception
	{
		projectList.refresh();
		getTopLevelAncestor().validate();
	}

	protected FlexibleWidthHtmlViewer createNextPreviousButtonPanel(HyperlinkHandler hyperlinkHandler)
	{
		String buttonsText = EAM.text("<div class='WizardText'>" +	
				"<p><table>" + 
				"<tr>" +
				"<td><input type='submit' name='Back' value='&lt; Previous'></input></td>" +
				"<td><input type='submit' name='Next' value='Next &gt;'></input></td>" +
				"</tr>") ; 

		return new FlexibleWidthHtmlViewer(getMainWindow(), hyperlinkHandler, buttonsText);

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
				HtmlViewPanel htmlViewPanel = HtmlViewPanelWithMargins.createFromTextString(getMainWindow(), def.term, def.getDefintion());
				htmlViewPanel.showAsOkDialog();
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
	}

	public void keyReleased(KeyEvent e)
	{
	}

	public void keyTyped(KeyEvent keyEvent)
	{
		if (keyEvent.getKeyChar() == KeyEvent.VK_ENTER)
		{
			buttonPressed(WizardManager.CONTROL_NEXT);
		}
	}
	
	private void renameProject(File projectDirectory) throws Exception
	{
		RenameProjectDoer.doIt(getMainWindow(), projectDirectory);
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
	
	public static final String OPEN_PREFIX = "OPEN:";
	private static final String COPY_PREFIX = "COPY:";
	private static final String RENAME_PREFIX = "RENAME:";
	private static final String DELETE_PREFIX = "DELETE:";
	private static final String EXPORT_PREFIX = "EXPORT:";
	private static final String DEFINITION_PREFIX = "Definition:";
	
	TreeBasedProjectList projectList;
}
