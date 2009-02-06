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
package org.miradi.dialogs.fieldComponents;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import org.martus.swing.UiTextField;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class PanelTextField extends UiTextField
{
	public PanelTextField()
	{
		super();
		initialize();
	}

	public PanelTextField(int columns)
	{
		super(columns);
		initialize();
	}
	
	public PanelTextField(String text)
	{
		super(text);
		initialize();
	}
	
	private void initialize()
	{
		setFont(getMainWindow().getUserDataPanelFont());
        addFocusListener(new TextFieldFocusHandler());
	}
		
	//TODO should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
	
	public class TextFieldFocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
			selectAll();
		}

		public void focusLost(FocusEvent e)
		{
		}
	}
}
