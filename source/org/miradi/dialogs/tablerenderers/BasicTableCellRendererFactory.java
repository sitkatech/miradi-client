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

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import org.martus.util.xml.XmlUtilities;

abstract public class BasicTableCellRendererFactory implements TableCellRenderer, TableCellPreferredHeightProvider
{
	public BasicTableCellRendererFactory()
	{
		backgroundColor = Color.WHITE;
	}
	
	public abstract JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value);

	public void setCellBackgroundColor(Color backgroundColorToUse)
	{
		backgroundColor = backgroundColorToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JComponent renderer = getRendererComponent(table, isSelected, hasFocus, row, tableColumn, value);
		
		renderer.setBorder(getCellBorder());
		
		if(isSelected)
		{
			Color fg = table.getSelectionForeground();
			Color bg = table.getSelectionBackground();
			setColors(renderer, fg, bg);
		}
		else
		{
			Color fg = getCellForegroundColor(table, row, tableColumn);
			Color bg = getCellBackgroundColor();
			setColors(renderer, fg, bg);
		}
			
		return renderer;
	}

	private void setColors(JComponent renderer, Color fg, Color bg)
	{
		renderer.setForeground(fg);
		renderer.setBackground(bg);
	}

	protected String getAsHtmlText(Object value)
	{
		return getAsHtmlText(value, "");
	}
	
	protected String getAsHtmlText(Object value, String initialHtml)
	{
		if(value == null)
			return "";
		String plainText = value.toString();
		return "<html>" + initialHtml + XmlUtilities.getXmlEncoded(plainText);
	}
	
	public Border getCellBorder()
	{
		Border line = BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black);
		Border margin = BorderFactory.createEmptyBorder(CELL_MARGIN, CELL_MARGIN, CELL_MARGIN, CELL_MARGIN);
		return BorderFactory.createCompoundBorder(line, margin);
	}
	
	public Color getCellForegroundColor(JTable table, int row, int tableColumn)
	{
		if(table.isCellEditable(row, tableColumn))
			return Color.BLUE.darker();
		return Color.BLACK;
	}
	
	public Color getCellBackgroundColor()
	{
		return backgroundColor;
	}
	
	public static final int CELL_MARGIN = 2;
	
	private Color backgroundColor;
}
