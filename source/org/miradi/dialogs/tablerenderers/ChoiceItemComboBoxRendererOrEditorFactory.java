/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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
import java.util.Vector;

import javax.swing.JComponent;
import javax.swing.JTable;

import org.miradi.dialogs.fieldComponents.ChoiceItemWithXmlRendererComboBox;
import org.miradi.questions.ChoiceItem;

public class ChoiceItemComboBoxRendererOrEditorFactory extends ObjectTableCellEditorOrRendererFactory
{
	public ChoiceItemComboBoxRendererOrEditorFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse, Vector<ChoiceItem> items)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new HtmlEncodedCellRenderer(items);
		editorComponent = new HtmlEncodedCellEditor(items);
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		rendererComponent.setSelectedItem(value);
		
		return rendererComponent;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return rendererComponent.getPreferredSize().height;
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int tableColumn)
	{
		updateBorderAndColors(editorComponent, table, row, tableColumn, isSelected);
		
		return editorComponent;
	}
	
	@Override
	public Object getCellEditorValue()
	{
		System.out.println("GetCellEditValue");
		return editorComponent.getSelectedItem();
	}
	
	private class HtmlEncodedCellEditor extends ChoiceItemWithXmlRendererComboBox
	{
		public HtmlEncodedCellEditor(Vector<ChoiceItem> items)
		{
			super(items);
		}
	}
	
	private class HtmlEncodedCellRenderer extends ChoiceItemWithXmlRendererComboBox
	{
		public HtmlEncodedCellRenderer(Vector<ChoiceItem> items)
		{
			super(items);
		}
	}

	private HtmlEncodedCellRenderer rendererComponent;
	private HtmlEncodedCellEditor editorComponent;
}
