/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.martus.swing.Utilities;
import org.miradi.main.MainWindow;

public class ModalRenameDialog extends JDialog implements PropertyChangeListener
{
	public ModalRenameDialog(MainWindow mainWindow, String message, String initialValue)
	{
		super(mainWindow, true);
		
		textField = new InputRestrictedTextField(initialValue);
		optionPane = new JOptionPane(new Object[]{new JLabel(message), textField}, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
		
		optionPane.addPropertyChangeListener(this);
        setContentPane(optionPane);
        pack();
        Utilities.centerDlg(this);
        setVisible(true);
        
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
 	}
	
	public String getUserValue()
	{
		return userValue;
	}
	
	public void propertyChange(PropertyChangeEvent e) 
    {
    	Object value = optionPane.getValue();
    	if (value == JOptionPane.UNINITIALIZED_VALUE) 
    		return;
    
    	optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
    	if (Integer.toString(JOptionPane.CANCEL_OPTION).equals(value.toString()))
    		userValue = null;
    	else
    		userValue = textField.getText();	
    
    	clearAndHide();      
    }

    public void clearAndHide() 
    {
        textField.setText(null);
        setVisible(false);
    }
    
    private JOptionPane optionPane;
    private InputRestrictedTextField textField;
    private String userValue;
}
