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
package org.miradi.utils;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

import org.miradi.main.MainWindow;

abstract public class UiTableWithAlternatingRows extends TableWithColumnWidthAndSequenceSaver
{
	public UiTableWithAlternatingRows(MainWindow mainWindowToUse, TableModel model)
	{
		super(mainWindowToUse, model);
		initialize();
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
