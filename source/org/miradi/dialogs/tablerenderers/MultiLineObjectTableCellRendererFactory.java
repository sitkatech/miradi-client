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

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextPane;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.MainWindow;

public class MultiLineObjectTableCellRendererFactory extends
		ObjectTableCellRendererFactory implements TableCellPreferredHeightProvider
{
	public MultiLineObjectTableCellRendererFactory(MainWindow mainWindowToUse, RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
		
		rendererComponent = new TableCellHtmlRendererComponent(mainWindowToUse, null);
	}
	
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		if(value == null)
			value = "";
		String html = value.toString();
		JComponent component = getRendererComponent(table, isSelected, hasFocus, row, tableColumn, html);
		return component;
	}

	private Color getForegroundColor(JTable table, int row, int tableColumn, boolean isSelected)
	{
		if(isSelected)
			return table.getSelectionForeground();
		
		return getCellForegroundColor(table, row, tableColumn);
	}

	private Color getBackgroundColor(JTable table, int row, int tableColumn, boolean isSelected)
	{
		if(isSelected) 
			return table.getSelectionBackground();
		
		return getCellBackgroundColor();
	}

	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, String html)
	{
		rendererComponent.setText(html);
		rendererComponent.setForeground(getForegroundColor(table, row, tableColumn, isSelected));
		rendererComponent.setBackground(getBackgroundColor(table, row, tableColumn, isSelected));
		return rendererComponent;
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		TableCellPreferredHeightProvider viewer = (TableCellPreferredHeightProvider)getRendererComponent(table, false, false, row, column, value);

		return viewer.getPreferredHeight(table, row, column, value);
	}
	
	class TableCellHtmlRendererComponent extends JTextPane implements TableCellPreferredHeightProvider
	{
		public TableCellHtmlRendererComponent(MainWindow mainWindowToUse, HyperlinkHandler hyperLinkHandler)
		{
			super();
		}

		public int getPreferredHeight(JTable table, int row, int column, Object value)
		{
			int width = table.getCellRect(row, column, false).width;
			setSize(new Dimension(width, Short.MAX_VALUE));
			setMaximumSize(new Dimension(width, Short.MAX_VALUE));
			setText(value.toString());
			return getPreferredSize().height;
		}

	}

	private TableCellHtmlRendererComponent rendererComponent;

}
