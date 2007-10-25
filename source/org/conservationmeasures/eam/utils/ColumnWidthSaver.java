package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

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
	
	private int getColumnWidth(int modelColumn)
	{
		String columnTag = tagProvider.getColumnTag(modelColumn);
		int columnWidth = EAM.mainWindow.getAppPreferences().getTaggedInt(getColumnWidthKey(modelColumn));
		if (columnWidth > 0)
			return columnWidth;
		else if (isWideColumn(columnTag))
			return DEFAULT_WIDE_COLUMN_WIDTH;
		else
			return DEFAULT_NARROW_COLUMN_WIDTH;
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
			EAM.mainWindow.getAppPreferences().setTaggedInt(getColumnWidthKey(modelColumn), column.getWidth());
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
