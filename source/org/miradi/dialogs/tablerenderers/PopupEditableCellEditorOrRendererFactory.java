/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.JComponent;
import javax.swing.JTable;

abstract public class PopupEditableCellEditorOrRendererFactory extends ObjectTableCellEditorOrRendererFactory
{
	public PopupEditableCellEditorOrRendererFactory(RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider)
	{
		super(objectProvider, fontProvider);
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return getTableCellRendererComponent(table, value, false, false, row, column).getPreferredSize().height;
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int tableColumn)
	{
		return super.getTableCellEditorComponent(table, value, isSelected, row, tableColumn);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int tableColumn)
	{
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, tableColumn);
	}
	
	@Override
	public JComponent getRendererComponent(JTable table, boolean isSelected, boolean hasFocus, int row, int tableColumn, Object value)
	{
		return (JComponent)getConfiguredComponent(table, value, row, tableColumn);
	}

	/**
	 * TODO: At some point, this should be unified with getRendererComponent,
	 * but that would require resolving the issues about when different font 
	 * modifications are applied.
	 */
	abstract protected Component getConfiguredComponent(JTable table, Object value, int row, int column);
}
