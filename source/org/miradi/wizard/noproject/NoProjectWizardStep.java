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
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MiradiLogoPanel;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.views.noproject.NoProjectView;
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
	
	public static final String OPEN_PREFIX = "OPEN:";
	private static final String COPY_PREFIX = "COPY:";
	private static final String RENAME_PREFIX = "RENAME:";
	private static final String DELETE_PREFIX = "DELETE:";
	private static final String EXPORT_PREFIX = "EXPORT:";
	
	protected TreeBasedProjectList projectList;
}
