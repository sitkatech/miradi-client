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

import org.miradi.main.MainWindow;
import org.miradi.utils.EditableHtmlPane;

import javax.swing.*;
import java.awt.*;


public class MultiLineObjectTableCellRendererOnlyFactory extends ObjectTableCellEditorOrRendererFactory
{
	public MultiLineObjectTableCellRendererOnlyFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = createTableCellHtmlRendererComponent(mainWindowToUse);
	}

	protected TableCellHtmlRendererComponent createTableCellHtmlRendererComponent(MainWindow mainWindowToUse)
	{
		return new TableCellHtmlRendererComponent(mainWindowToUse);
	}

	@Override
	public Object getCellEditorValue()
	{
		return rendererComponent.getText();
	}
	
	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		rendererComponent.setText(value.toString());
		Font font = getCellFont(row, column);
		rendererComponent.setFont(font);
		rendererComponent.setEditable(false);
		return rendererComponent;
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		if(value == null)
			value = "";
		String html = value.toString();
		JComponent component = getRendererComponent(table, isSelected, hasFocus, row, tableColumn, html);
		return component;
	}

	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, String html)
	{
		rendererComponent.setText(html);
		
		return rendererComponent;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		TableCellPreferredHeightProvider viewer = (TableCellPreferredHeightProvider)getTableCellRendererComponent(table, value, false, false, row, column);

		return viewer.getPreferredHeight(table, row, column, value);
	}
	
	public static class TableCellHtmlRendererComponent extends EditableHtmlPane implements TableCellPreferredHeightProvider
	{
		public TableCellHtmlRendererComponent(MainWindow mainWindow)
		{
			super(mainWindow);
		}
		
		public int getPreferredHeight(JTable table, int row, int column, Object value)
		{
			int width = table.getCellRect(row, column, false).width;
			setSize(new Dimension(width, Short.MAX_VALUE));
			setText(value.toString());
			return getPreferredSize().height;
		}
		
		@Override
		public void validate()
		{
			//Note: overriding to improve speed
		}
		
		@Override
		public void invalidate()
		{
			//Note: overriding to improve speed
		}
		
		@Override
		public void revalidate()
		{
			//Note: overriding to improve speed
		}
		
		@Override
		public void repaint()
		{
			//Note: overriding to improve speed
		}		
	}

	protected TableCellHtmlRendererComponent getRendererComponent()
	{
		return rendererComponent;
	}
	
	private TableCellHtmlRendererComponent rendererComponent;
}
