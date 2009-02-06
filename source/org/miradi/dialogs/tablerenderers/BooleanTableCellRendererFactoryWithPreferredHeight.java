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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTable;

import org.martus.swing.UiCheckBox;

public class BooleanTableCellRendererFactoryWithPreferredHeight extends
		BasicTableCellRendererFactory
{
	public BooleanTableCellRendererFactoryWithPreferredHeight()
	{
		checkBox = new UiCheckBox();
	    checkBox.setHorizontalAlignment(JLabel.CENTER);
        checkBox.setBorderPainted(true);
	}

	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected,
			boolean hasFocus, int row, int tableColumn, Object value)
	{
		Boolean booleanValue = (Boolean)value;
		checkBox.setSelected(value != null && booleanValue.booleanValue());

        checkBox.setForeground(getForeground(table, isSelected));
        checkBox.setBackground(getBackground(table, isSelected));

        return checkBox;
	}
	
	private Color getForeground(JTable table, boolean isSelected)
	{
		if(isSelected)
			return table.getSelectionForeground();
		return table.getForeground();
	}
	
	private Color getBackground(JTable table, boolean isSelected)
	{
		if(isSelected)
			return table.getSelectionBackground();
		return table.getBackground();
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return checkBox.getPreferredSize().height;
	}

	private UiCheckBox checkBox;

}
