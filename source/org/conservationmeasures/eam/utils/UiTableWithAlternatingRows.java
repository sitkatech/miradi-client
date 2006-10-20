/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.martus.swing.UiTable;

public class UiTableWithAlternatingRows extends UiTable
{
	public UiTableWithAlternatingRows()
	{
		super();
		initialize();
	}

	public UiTableWithAlternatingRows(TableModel model)
	{
		super(model);
		initialize();
	}

	private void initialize()
	{
		setRowHeight(getRowHeight() * 3);
		backgrounds = new Color[] { Color.WHITE, new Color(0xf0, 0xf0, 0xf0), };
		setDefaultRenderer(Object.class, new Renderer());
	}
	
	class Renderer extends DefaultTableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
			//workAroundJavaBug(table, isSelected);
			
			Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (!isSelected)
			{
				component.setBackground(backgrounds[row%2]);
				component.setForeground(Color.BLACK);
			}
			
			if(component instanceof JLabel)
				((JLabel)component).setVerticalAlignment(JLabel.TOP);
				
			int height = component.getPreferredSize().height;
			if (height > table.getRowHeight(row))
				table.setRowHeight (row, height);
			
			return component;
		}

		private void workAroundJavaBug(JTable table, boolean isSelected)
		{
			if (isSelected) 
				   table.setForeground(null);
		}
		
	}

	Color[] backgrounds;
}
