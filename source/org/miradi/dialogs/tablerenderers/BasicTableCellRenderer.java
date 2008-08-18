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
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;

import org.martus.util.xml.XmlUtilities;

public class BasicTableCellRenderer implements TableCellRenderer
{
	public BasicTableCellRenderer()
	{
		rendererComponent = new DefaultTableCellRenderer();
		backgroundColor = rendererComponent.getBackground();
	}
	
	protected JLabel getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, String html)
	{
		JLabel renderer = (JLabel)rendererComponent.getTableCellRendererComponent(table, html, isSelected, hasFocus, row, tableColumn);
		renderer.setVerticalAlignment(SwingConstants.TOP);
		return renderer;
	}

	public void setCellBackgroundColor(Color backgroundColorToUse)
	{
		backgroundColor = backgroundColorToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		String html = getAsHtmlText(value);
		JComponent renderer = getRendererComponent(table, isSelected, hasFocus, row, tableColumn, html);
		
		renderer.setBorder(getCellBorder());
		
		if(!isSelected)
		{
			renderer.setForeground(getCellForegroundColor(table, row, tableColumn));
			renderer.setBackground(getCellBackgroundColor());
		}
			
		return renderer;
	}

	private String getAsHtmlText(Object value)
	{
		if(value == null)
			return null;
		String plainText = value.toString();
		return "<html>" + XmlUtilities.getXmlEncoded(plainText);
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
	
	private DefaultTableCellRenderer rendererComponent;
	private Color backgroundColor;
}
