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
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.Utilities;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.utils.MiradiScrollPane;

abstract public class AbstractSelectionDialog extends DialogWithButtonBar implements ListSelectionListener
{
	public AbstractSelectionDialog(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow);
		setTitle(title);
		list = poolTable;
		list.getTable().addListSelectionListener(this);
		Box box = Box.createVerticalBox();
		box.add(new PanelTitleLabel(getPanelTitleInstructions()));
		box.add(list, BorderLayout.AFTER_LAST_LINE);

		Container contents = getContentPane();
		contents.add(new MiradiScrollPane(box), BorderLayout.CENTER);
		setButtons(getButtonComponents());
		setModal(true);
		setPreferredSize(new Dimension(900,400));
		Utilities.centerDlg(this);
	}
	
	public BaseObject getSelectedObject()
	{
		return objectSelected;
	}
	
	private Vector<Component> getButtonComponents()
	{
		PanelButton cancelButton = new PanelButton(new CancelAction());
		customButton = new PanelButton(new CustomAction(createCustomButtonLabel()));
		customButton.setEnabled(false);
		
		Vector<Component> buttons = new Vector<Component>();
		buttons.add(Box.createHorizontalGlue());
		buttons.add(customButton);
		buttons.add(Box.createHorizontalStrut(10));
		buttons.add(cancelButton);
		return buttons;
	}
	
	public void valueChanged(ListSelectionEvent e)
	{
		customButton.setEnabled(true);
	}
	
	class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super(EAM.text("Cancel"));
		}

		public void actionPerformed(ActionEvent arg0)
		{
			dispose();
		}
	}
	
	class CustomAction extends AbstractAction
	{
		public CustomAction(String label)
		{
			super(label);
		}

		public void actionPerformed(ActionEvent arg0)
		{
			objectSelected = list.getSelectedObject();
			dispose();
		}
	}

	
	abstract protected String createCustomButtonLabel();
	
	abstract protected String getPanelTitleInstructions();
	
	private PanelButton customButton;
	protected ObjectTablePanel list;
	protected BaseObject objectSelected;
}

