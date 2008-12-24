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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.BaseObject;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;

public class ColumnWidthSaver extends MouseAdapter
{
	public ColumnWidthSaver(Project projectToUse, JTable tableToUse, ColumnTagProvider tagProviderToUse, String uniqueTableIdentifierToUse)
	{
		project = projectToUse;
		table = tableToUse;
		tagProvider = tagProviderToUse;
		uniqueTableIdentifier = uniqueTableIdentifierToUse;
	}
	
	public void restoreColumnWidths()
	{
		try
		{
			for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
			{	
				TableColumn column = table.getColumnModel().getColumn(tableColumn);
				int width = getColumnWidth(tableColumn);
				column.setWidth(width);
				column.setPreferredWidth(width);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	protected int getColumnWidth(int tableColumn) throws Exception
	{
		int modelColumn = table.convertColumnIndexToModel(tableColumn);
		String columnTag = tagProvider.getColumnTag(modelColumn);
		
		String columnWidthAsString = "";
		TableSettings tableSettings = TableSettings.find(getProject(), getUniqueTableIdentifier());
		if (tableSettings != null)
		{
			StringMap columnWidthMap = tableSettings.getColumnWidthMap();
			columnWidthAsString = columnWidthMap.get(columnTag);
		}
		
		return getColumnWidth(tableColumn, columnTag, columnWidthAsString);
	}

	private int getColumnWidth(int tableColumn, String columnTag, String columnWidthAsString)
	{
		int columnHeaderWidth = TableWithHelperMethods.getColumnHeaderWidth(table, tableColumn);
		int defaultColumnWidth = getDefaultColumnWidth(columnTag, columnHeaderWidth);
		if (columnWidthAsString.length() == 0)
			return defaultColumnWidth;
		
		int columnWidth = Integer.parseInt(columnWidthAsString);
		if (columnWidth > 0)
			return columnWidth;
		
		return defaultColumnWidth;
	}

	private int getDefaultColumnWidth(String columnTag, int columnHeaderWidth)
	{
		if (isWideColumn(columnTag))
			return DEFAULT_WIDE_COLUMN_WIDTH;
		
		else if (columnHeaderWidth < DEFAULT_NARROW_COLUMN_WIDTH)
			return DEFAULT_NARROW_COLUMN_WIDTH;
		
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
		try
		{
			saveColumnWidths();
		}
		catch(Exception e1)
		{
			EAM.logException(e1);
		}
	}
	
	private void saveColumnWidths() throws Exception
	{
		
		StringMap columnWidthMap = new StringMap();
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{		
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			TableColumn column = table.getColumnModel().getColumn(tableColumn);
			columnWidthMap.add(getColumnWidthKey(modelColumn), Integer.toString(column.getWidth()));
		}
		
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), getUniqueTableIdentifier());
		CommandSetObjectData setColumnWidths = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_COLUMN_WIDTHS, columnWidthMap.toString());
		getProject().executeCommand(setColumnWidths);
	}
	
	private Project getProject()
	{
		return project;
	}
	
	private String getColumnWidthKey(int modelColumn)
	{
		return tagProvider.getColumnTag(modelColumn);
	}
	
	private String getUniqueTableIdentifier()
	{
		return uniqueTableIdentifier;
	}

	private Project project;
	private JTable table;
	private ColumnTagProvider tagProvider;
	private String uniqueTableIdentifier;
	public static final int DEFAULT_NARROW_COLUMN_WIDTH = 75;
	public static final int DEFAULT_WIDE_COLUMN_WIDTH = 200;
}
