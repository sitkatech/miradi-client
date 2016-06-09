/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import org.miradi.dialogs.fieldComponents.ChoiceItemWithXmlTextRendererComboBox;
import org.miradi.questions.ChoiceItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Vector;

public class ChoiceItemComboBoxRendererOrEditorFactory extends ObjectTableCellEditorOrRendererFactory
{
	public ChoiceItemComboBoxRendererOrEditorFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse, Vector<ChoiceItem> items)
	{
		super(providerToUse, fontProviderToUse);

		rendererComponent = new HtmlEncodedCellRenderer(items);
		editorComponent = new HtmlEncodedCellEditor(items);
	}
	
	@Override
	protected void updateBorderAndColors(JComponent renderer, JTable table,	int row, int tableColumn, boolean isSelected)
	{
		super.updateBorderAndColors(renderer, table, row, tableColumn, isSelected);
		
		if(isSelected)
		{
			useSwappedBackgroundAndForegroundColors(table);
		}
		else
		{
			Color fg = getCellForegroundColor(table, row, tableColumn);
			Color bg = getCellBackgroundColor();
			setBothComponentColors(fg, bg);
		}

	}

	private void useSwappedBackgroundAndForegroundColors(JTable table)
	{
		Color foregroundToBeUsedAsBackground = table.getSelectionForeground();
		Color backgroundToBeUsedAsForeground = table.getSelectionBackground();
		setBothComponentColors(backgroundToBeUsedAsForeground, foregroundToBeUsedAsBackground);
	}

	private void setBothComponentColors(Color fg, Color bg)
	{
		setColors(rendererComponent, fg, bg);
		setColors(editorComponent, fg, bg);
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
		return editorComponent.getSelectedItem();
	}

	public void addActionListener(ActionListener actionListener)
	{
		editorComponent.addActionListener(actionListener);
	}

	private class HtmlEncodedCellEditor extends ChoiceItemWithXmlTextRendererComboBox
	{
		public HtmlEncodedCellEditor(Vector<ChoiceItem> items)
		{
			super(items);
		}
	}
	
	private class HtmlEncodedCellRenderer extends ChoiceItemWithXmlTextRendererComboBox
	{
		public HtmlEncodedCellRenderer(Vector<ChoiceItem> items)
		{
			super(items);
		}
	}

	private HtmlEncodedCellRenderer rendererComponent;
	private HtmlEncodedCellEditor editorComponent;
}
