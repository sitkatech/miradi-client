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

package org.miradi.dialogs.tablerenderers;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JTable;

import org.miradi.dialogs.fieldComponents.PanelTextField;

abstract public class AbstractNumericRestrictedTableCellRendererEditorFactory extends NumericTableCellRendererFactory
{
	public AbstractNumericRestrictedTableCellRendererEditorFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		numericRestrictedTextRenderer = createRestrictedNumericTextField();
		numericRestrictedTextEditor = createRestrictedNumericTextField();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		updateTextField(numericRestrictedTextEditor, value.toString(), row, column);
		return numericRestrictedTextEditor;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		updateTextField(numericRestrictedTextRenderer, value.toString(), row, tableColumn);
		return numericRestrictedTextRenderer;
	}
	
	private void updateTextField(PanelTextField textField, String value, int row, int column)
	{
		textField.setText(value);
		Font font = getCellFont(row, column);
		textField.setFont(font);
	}

	@Override
	public Object getCellEditorValue()
	{
		return numericRestrictedTextEditor.getText();
	}
	
	abstract protected PanelTextField createRestrictedNumericTextField();

	private PanelTextField numericRestrictedTextRenderer;
	private PanelTextField numericRestrictedTextEditor;
}
