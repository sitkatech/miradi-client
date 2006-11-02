/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.threatmatrix;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.conservationmeasures.eam.objects.ValueOption;

class CustomTableCellRenderer extends DefaultTableCellRenderer
{
	public CustomTableCellRenderer()
	{
		setHorizontalAlignment(CENTER);
	}

	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column)
	{
		Component cell = super.getTableCellRendererComponent(table, value,
				isSelected, hasFocus, row, column);

		cell.setBackground( ((ValueOption)value).getColor() );
		cell.setFont(new Font(null,Font.BOLD,12));
		cell.setForeground(Color.BLACK); 
		
		setBoarders(table, row, column, isSelected, hasFocus);
		
		return cell;
	}

	private void setBoarders(JTable table, int row, int column, boolean selected, boolean focused)
	{
		if (row==table.getRowCount()-SUMMARY_ROW_COLUMN_INCR || 
				column==table.getColumnCount()-SUMMARY_ROW_COLUMN_INCR)
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.DARK_GRAY,2),getBorder()));
		else 
		{
			if (!focused)
			setBorder(BorderFactory.createCompoundBorder(
					BorderFactory.createLineBorder(Color.LIGHT_GRAY,1),getBorder()));
			else 
				setBorder(BorderFactory.createCompoundBorder(
						BorderFactory.createLineBorder(Color.BLUE,3),getBorder()));
		}
	}
	

	int SUMMARY_ROW_COLUMN_INCR = 1;
}