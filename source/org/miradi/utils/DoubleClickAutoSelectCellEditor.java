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

import java.awt.Component;
import javax.swing.DefaultCellEditor;
import javax.swing.JTable;
import javax.swing.JTextField;

import org.miradi.questions.ChoiceItem;

public class DoubleClickAutoSelectCellEditor extends DefaultCellEditor 
{
	public DoubleClickAutoSelectCellEditor(final JTextField textField) 
	{
		super(textField);
		setClickCountToStart(2);
		delegate = new EditorDeletegate();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		ChoiceItem choiceItem = (ChoiceItem) value;
		
		return super.getTableCellEditorComponent(table, choiceItem.getCode(), isSelected, row, column);
	}
	
	final class EditorDeletegate extends EditorDelegate
	{
		@Override
		public void setValue(Object value) 
		{
			if (value == null)
				return;
			
			((JTextField)editorComponent).setText(value.toString());
		}

		@Override
		public Object getCellEditorValue() 
		{
			return ((JTextField)editorComponent).getText();
		}
	}
}