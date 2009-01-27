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
package org.miradi.dialogs.tablerenderers;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextArea;


public class MultiLineObjectTableCellRendererFactory extends ObjectTableCellRendererFactory
{
	public MultiLineObjectTableCellRendererFactory(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new TableCellHtmlRendererComponent();
	}
	
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
	
	public static class TableCellHtmlRendererComponent extends JTextArea implements TableCellPreferredHeightProvider
	{
		public TableCellHtmlRendererComponent()
		{
			setLineWrap(true);
			setWrapStyleWord(true);
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
			//Note: overriding to imporove speed
		}
		
		@Override
		public void invalidate()
		{
			//Note: overriding to imporove speed
		}
		
		@Override
		public void revalidate()
		{
			//Note: overriding to imporove speed
		}
		
		@Override
		public void repaint()
		{
			//Note: overriding to imporove speed
		}		
	}

	private TableCellHtmlRendererComponent rendererComponent;

}
