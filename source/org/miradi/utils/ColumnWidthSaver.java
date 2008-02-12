/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			TableColumn column = table.getColumnModel().getColumn(modelColumn);
			column.setWidth(getColumnWidth(modelColumn));
			column.setPreferredWidth(getColumnWidth(modelColumn));
		}
	}
	
	protected int getColumnWidth(int modelColumn)
	{
		String columnTag = tagProvider.getColumnTag(modelColumn);
		int columnWidth = EAM.getMainWindow().getAppPreferences().getTaggedInt(getColumnWidthKey(modelColumn));
		int columnHeaderWidth = TableWithHelperMethods.getColumnHeaderWidth(table, modelColumn);
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
			TableColumn column = table.getColumnModel().getColumn(modelColumn);
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
