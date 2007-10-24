package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.EAM;
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
		else if (columnTag.equals(GenericTreeTableModel.DEFAULT_COLUMN))
			return 200;
		else
			return 75;
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
}
