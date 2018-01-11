/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.dialogs.fieldComponents.PanelTextField;

import javax.swing.*;
import java.awt.*;

abstract public class AbstractNumericRestrictedTableCellRendererEditorFactory extends NumericTableCellRendererFactory
{
	public AbstractNumericRestrictedTableCellRendererEditorFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		numericRestrictedTextRenderer = createRestrictedNumericTextField();
		numericRestrictedTextEditor = createRestrictedNumericTextField();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		String cleanedValue = value == null ? "" : value.toString();
		updateTextField(numericRestrictedTextEditor, cleanedValue, row, column);
		return numericRestrictedTextEditor;
	}
	
	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		String cleanedValue = value == null ? "" : value.toString();
		updateTextField(numericRestrictedTextRenderer, cleanedValue, row, tableColumn);
		updateBorderAndColors(numericRestrictedTextRenderer, table, row, tableColumn, isSelected);
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
