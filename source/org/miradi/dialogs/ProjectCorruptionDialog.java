/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.Action;
import javax.swing.BorderFactory;

import org.martus.swing.UiWrappedTextArea;
import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DialogWithButtonBar;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;

public class ProjectCorruptionDialog extends DialogWithButtonBar
{
	public static boolean askUserWhetherToOpen(MainWindow mainWindowToUse, String title, String bodyText, String listOfProblems, Action additionalButtonAction)
	{
		ProjectCorruptionDialog dialog = new ProjectCorruptionDialog(mainWindowToUse, title, bodyText, listOfProblems, additionalButtonAction);
		dialog.setVisible(true);
		return dialog.wasPressed();
	}
	
	public static boolean askUserWhetherToOpen(MainWindow mainWindowToUse, String title, String bodyText, String listOfProblems)
	{
		return askUserWhetherToOpen(mainWindowToUse, title, bodyText, listOfProblems, null);
	}
	
	private ProjectCorruptionDialog(MainWindow mainWindowToUse, String title, String bodyText, String listOfProblems, Action additionalButtonAction)
	{
		super(mainWindowToUse);
		setModal(true);
		setTitle(title);

		MiradiPanel panel = new MiradiPanel(new BorderLayout());
		
		PanelTextArea bodyTextArea = new PanelTextArea(bodyText);
		bodyTextArea.setEditable(false);
		bodyTextArea.setLineWrap(true);
		bodyTextArea.setWrapStyleWord(true);
		bodyTextArea.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
		panel.add(bodyTextArea, BorderLayout.PAGE_START);
		
		UiWrappedTextArea details = new UiWrappedTextArea(listOfProblems);
		details.setWrapStyleWord(true);
		details.setLineWrap(true);
		panel.add(new MiradiScrollPane(details), BorderLayout.CENTER);

		add(panel);

		PanelButton openButton = new PanelButton(EAM.text("Open Anyway"));
		PanelButton closeButton = new PanelButton(EAM.text("Close"));
		Vector<Component> buttons = new Vector<Component>();
		buttons.add(openButton);
		if(additionalButtonAction != null)
			buttons.add(new PanelButton(additionalButtonAction));
		buttons.add(closeButton);

		setButtons(buttons);
		setSimpleCloseButton(closeButton);
		pack();
		Utilities.centerDlg(this);

		openProjectAnywayHandler = new OpenProjectAnywayHandler();
		openButton.addActionListener(openProjectAnywayHandler);
	}
	
	public boolean wasPressed()
	{
		return openProjectAnywayHandler.wasPressed();
	}
	
	class OpenProjectAnywayHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent arg0)
		{
			wasPressed = true;
			setVisible(false);
			dispose();
		}
		
		public boolean wasPressed()
		{
			return wasPressed;
		}

		private boolean wasPressed;
	}
	
	private OpenProjectAnywayHandler openProjectAnywayHandler;
}
