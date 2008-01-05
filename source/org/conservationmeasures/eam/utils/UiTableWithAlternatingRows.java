/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

abstract public class UiTableWithAlternatingRows extends TableWithColumnWidthSaver
{
	public UiTableWithAlternatingRows(TableModel model)
	{
		super(model);
		initialize();
	}
	
	public UiTableWithAlternatingRows(Object[][] data, String[] columnNames)
	{
		this(new DefaultTableModel(data, columnNames));
	}

	private void initialize()
	{
		backgrounds = new Color[] { Color.WHITE, new Color(0xf0, 0xf0, 0xf0), };
		setDefaultRenderer(Object.class, new Renderer());
	}
	
	class Renderer extends DefaultTableCellRenderer
	{

		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		{
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
		
	}

	Color[] backgrounds;
}
