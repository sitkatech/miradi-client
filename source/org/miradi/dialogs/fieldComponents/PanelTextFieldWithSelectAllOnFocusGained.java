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

public class PanelTextFieldWithSelectAllOnFocusGained extends PanelTextField
{
	public PanelTextFieldWithSelectAllOnFocusGained(int columns)
	{
		super(columns);
	}
	
	public PanelTextFieldWithSelectAllOnFocusGained(String text)
	{
		super(text);
	}
	
	protected void initialize()
	{
		super.initialize();
		
        addFocusListener(new TextFieldFocusHandler());
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
