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
package org.miradi.utils;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.martus.swing.Utilities;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ModalRenameDialog
{
	public static String showDialog(MainWindow mainWindow, String message)
	{
		return showDialog(mainWindow, message, "");
	}
	
	public static String showDialog(MainWindow mainWindow, String message, String initialValue)
	{
		ProjectNameRestrictedTextField textField = new ProjectNameRestrictedTextField(initialValue);
		JOptionPane optionPane = new JOptionPane(new Object[]{new JLabel(message), textField}, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		JDialog optionDialog = optionPane.createDialog(mainWindow, EAM.text("Title|Project Filename"));
		optionDialog.pack();
        Utilities.centerDlg(optionDialog);
        optionDialog.addWindowListener(new WindowListenerHandler(textField));
        optionDialog.setVisible(true);
		Object selectedValue = optionPane.getValue();
		if (wasCanceled(selectedValue))
			return null;
        
		return textField.getText();
	}

	private static boolean wasCanceled(Object selectedValue)
	{
		if (!(selectedValue instanceof Integer))
			return true;
		
		Integer userOption = (Integer) selectedValue;
		if (userOption.intValue() ==  JOptionPane.OK_OPTION)
			return false;
		
		return true;
	}	
	
	public static class WindowListenerHandler extends WindowAdapter
	{
		public WindowListenerHandler(JComponent componentToFocusToUse)
		{
			componentToFocus = componentToFocusToUse;	
		}
		
		@Override
		public void windowOpened(WindowEvent e)
		{
			super.windowOpened(e);
			
			componentToFocus.requestFocusInWindow();
		}
		
		private JComponent componentToFocus;
	}
}
