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

import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.main.EAM;

abstract public class PopupEditableCellEditorOrRendererFactory extends AbstractCellEditor implements TableCellEditor, TableCellRenderer, TableCellPreferredHeightProvider
{
	public PopupEditableCellEditorOrRendererFactory(RowColumnBaseObjectProvider objectProvider, FontForObjectProvider fontProvider)
	{
	}

	public int getPreferredHeight(JTable table, int row, int column, Object value)
	{
		return getTableCellRendererComponent(table, value, false, false, row, column).getPreferredSize().height;
	}
	
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		if(isRenderer)
			EAM.logError("Factory used for both editor and renderer: " + getClass().getName());
		isEditor = true;
		return getConfiguredComponent(table, value, row, column);
	}
	
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if(isEditor)
			EAM.logError("Factory used for both editor and renderer: " + getClass().getName());
		isRenderer = true;
		return getConfiguredComponent(table, value, row, column);
	}

	abstract protected Component getConfiguredComponent(JTable table, Object value, int row, int column);

	private boolean isEditor;
	private boolean isRenderer;
}
