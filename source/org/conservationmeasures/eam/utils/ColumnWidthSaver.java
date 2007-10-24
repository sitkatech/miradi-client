package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.table.TableColumn;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithColumnWidthSaving;

public class ColumnWidthSaver extends MouseAdapter
{
	public ColumnWidthSaver(TreeTableWithColumnWidthSaving tableToUse)
	{
		table = tableToUse;
		model = table.getTreeTableModel();
	}
	
	public void restoreColumnWidths()
	{
		for (int tableColumn = 0; tableColumn < model.getColumnCount(); ++tableColumn)
		{	
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			table.setColumnWidth(modelColumn, getColumnWidth(modelColumn));
		}
	}
	
	private int getColumnWidth(int modelColumn)
	{
		int columnWidth = EAM.mainWindow.getAppPreferences().getTaggedInt(getColumnWidthKey(modelColumn));
		if (columnWidth > 0)
			return columnWidth;
		else if (modelColumn == 0)
			return 250;
		else
			return 125;
	}

	public void mouseReleased(MouseEvent e)
	{
		saveColumnWidths();
	}
	
	private void saveColumnWidths()
	{
		for (int tableColumn = 0; tableColumn < model.getColumnCount(); ++tableColumn)
		{		
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			TableColumn column = table.getColumnModel().getColumn(modelColumn);
			EAM.mainWindow.getAppPreferences().setTaggedInt(getColumnWidthKey(modelColumn), column.getWidth());
		}
	}
	
	private String getColumnWidthKey(int modelColumn)
	{
		String columnTag = model.getColumnTag(modelColumn);
		String tableIdentifier = table.getUniqueTableIdentifier();
		
		return tableIdentifier + "." + columnTag;
	}

	private TreeTableWithColumnWidthSaving table;
	private GenericTreeTableModel model;
}
