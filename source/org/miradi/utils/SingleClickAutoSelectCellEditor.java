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

import java.util.EventObject;

import javax.swing.DefaultCellEditor;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class SingleClickAutoSelectCellEditor extends DefaultCellEditor 
{
	public SingleClickAutoSelectCellEditor(final JTextField textField) 
	{
		super(textField);
		setClickCountToStart(1);
		delegate = new EditorDeletegate();
	}
	
	final class EditorDeletegate extends EditorDelegate
	{
		public void setValue(Object value) 
		{
			if (value == null)
				return;
			
			((JTextField)editorComponent).setText(value.toString());
		}

		public Object getCellEditorValue() 
		{
			return ((JTextField)editorComponent).getText();
		}

		public boolean isCellEditable(EventObject anEvent) 
		{
			boolean isEditable = super.isCellEditable(anEvent);
			
			if (anEvent == null)
				return isEditable;
			
			if(isEditable)
				SwingUtilities.invokeLater(new LaterRunner());
			
			return isEditable;
		}
	}

	final class LaterRunner implements Runnable
	{
		public void run() 
		{
			((JTextField)editorComponent).selectAll();
		}
	}
}