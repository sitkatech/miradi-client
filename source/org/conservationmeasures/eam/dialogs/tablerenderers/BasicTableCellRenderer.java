/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;

public class BasicTableCellRenderer extends DefaultTableCellRenderer
{
	public BasicTableCellRenderer()
	{
		backgroundColor = getBackground();
	}
	
	public void setCellBackgroundColor(Color backgroundColorToUse)
	{
		backgroundColor = backgroundColorToUse;
	}

	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		JComponent renderer = (JComponent)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
		
		renderer.setBorder(getCellBorder());
		renderer.setForeground(getCellForegroundColor(table, row, tableColumn));
		renderer.setBackground(getCellBackgroundColor());
		return renderer;
	}
	
	public Border getCellBorder()
	{
		return BorderFactory.createMatteBorder(1, 1, 0, 0, Color.black);
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
	
	private Color backgroundColor;
}
