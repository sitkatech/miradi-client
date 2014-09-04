/* 
Copyright 2005-2014, Foundations of Success, Bethesda, Maryland
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

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

public class MiradiDefaultTableCellHeaderRenderer extends JLabel implements TableCellRenderer
{
	//TODO this code should be replaced with java 1.6's DefaultTableCellHeaderRenderer when we start supporting 1.6
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) 
	{
		if (table != null) 
		{
			JTableHeader header = table.getTableHeader();
			if (header != null) {
				setForeground(header.getForeground());
				setBackground(header.getBackground());
				setFont(header.getFont());
			}
		}
		
		JLabel label = (JLabel) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		label.setText((value == null) ? "" : value.toString());
		
		return label;
	}
}
