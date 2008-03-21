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

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;

public class ColumnWidthSaver extends MouseAdapter
{
	public ColumnWidthSaver(JTable tableToUse, ColumnTagProvider tagProviderToUse, String uniqueTableIdentifierToUse)
	{
		table = tableToUse;
		tagProvider = tagProviderToUse;
		uniqueTableIdentifier = uniqueTableIdentifierToUse;
	}
	
	public void restoreColumnWidths()
	{
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{	
			TableColumn column = table.getColumnModel().getColumn(tableColumn);
			int width = getColumnWidth(tableColumn);
			column.setWidth(width);
			column.setPreferredWidth(width);
		}
	}
	
	protected int getColumnWidth(int tableColumn)
	{
		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		String columnTag = tagProvider.getColumnTag(modelColumn);
		int columnWidth = EAM.getMainWindow().getAppPreferences().getTaggedInt(getColumnWidthKey(modelColumn));
		int columnHeaderWidth = TableWithHelperMethods.getColumnHeaderWidth(table, tableColumn);
		if (columnWidth > 0)
			return columnWidth;
		
		else if (isWideColumn(columnTag))
			return DEFAULT_WIDE_COLUMN_WIDTH;
		
		else if (columnHeaderWidth < DEFAULT_NARROW_COLUMN_WIDTH)
			return DEFAULT_NARROW_COLUMN_WIDTH;
		
		else
			return columnHeaderWidth;
	}

	private boolean isWideColumn(String columnTag)
	{
		if (columnTag.equals(GenericTreeTableModel.DEFAULT_COLUMN))
			return true;
		
		if (columnTag.equals(BaseObject.TAG_LABEL))
			return true;
		
		return false;
	}

	public void mouseReleased(MouseEvent e)
	{
		saveColumnWidths();
	}
	
	private void saveColumnWidths()
	{
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{		
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			TableColumn column = table.getColumnModel().getColumn(tableColumn);
			EAM.getMainWindow().getAppPreferences().setTaggedInt(getColumnWidthKey(modelColumn), column.getWidth());
		}
	}
	
	private String getColumnWidthKey(int modelColumn)
	{
		String columnTag = tagProvider.getColumnTag(modelColumn);
		return uniqueTableIdentifier + "." + columnTag;
	}

	private JTable table;
	private ColumnTagProvider tagProvider;
	private String uniqueTableIdentifier;
	public static final int DEFAULT_NARROW_COLUMN_WIDTH = 75;
	public static final int DEFAULT_WIDE_COLUMN_WIDTH = 200;
}
