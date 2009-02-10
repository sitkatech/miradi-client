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
package org.miradi.wizard.noproject.projectlist;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.Box;

import org.martus.swing.Utilities;
import org.miradi.dialogs.base.DialogWithButtonBar;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;

public class DirectorySelectionDialog extends DialogWithButtonBar
{
	public DirectorySelectionDialog(MainWindow mainWindowToUse) throws Exception
	{
		super(mainWindowToUse);

		setModal(true);
		setPreferredSize(new Dimension(900,400));
		Utilities.centerDlg(this);
		
		mainWindow = mainWindowToUse;
		setButtons(createButtonComponents());
		addComponents();
	}

	private void addComponents() throws Exception
	{
		ProjectListTreeTableModel model = ProjectListTreeTableModel.createDirectoryListTreeTableModel();
		table = new ProjectListTreeTable(getMainWindow(), model, null);
		add(new MiradiScrollPane(table));
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	public File getSelectedDirectory()
	{
		return table.getSelectedFile();
	}
	
	private Vector<Component> createButtonComponents()
	{
		PanelButton okButton = new PanelButton(EAM.text("Button|Ok"));
		okButton.addActionListener(new ActionHandler());
		getRootPane().setDefaultButton(okButton);
		
		PanelButton cancelButton = new PanelButton(EAM.text("Button|Cancel"));
		cancelButton.addActionListener(new ActionHandler());

		Vector<Component> buttons = new Vector<Component>();
		buttons.add(Box.createHorizontalGlue());
		buttons.add(okButton);
		buttons.add(Box.createHorizontalStrut(10));
		buttons.add(cancelButton);
		
		return buttons;
	}
	
	class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}	
	}
		
	private ProjectListTreeTable table;
	private MainWindow mainWindow;
}
