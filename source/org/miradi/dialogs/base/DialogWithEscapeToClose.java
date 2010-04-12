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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.KeyStroke;

public class DialogWithEscapeToClose extends MiradiDialog
{
	public DialogWithEscapeToClose(JFrame parent)
	{
		super(parent);
		
		setUndecorated(shouldBeUndecorated());
	}

	public DialogWithEscapeToClose(JDialog owner)
	{
		super(owner);
		
		setUndecorated(shouldBeUndecorated());
	}

	protected boolean shouldBeUndecorated()
	{
		return false;
	}
	
	@Override
	protected JRootPane createRootPane() 
	{
		rootPane = new JRootPane();
		KeyStroke stroke = KeyStroke.getKeyStroke("ESCAPE");
		InputMap inputMap = rootPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.put(stroke, "ESCAPE");
		rootPane.getActionMap().put("ESCAPE", new DialogCloseListener());

		return rootPane;
	} 
	
	public class DialogCloseListener extends AbstractAction implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			setVisible(false);
			dispose();
		}
	}
}
