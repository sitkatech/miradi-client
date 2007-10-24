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
			int columnWidth = EAM.mainWindow.getAppPreferences().getTaggedInt(getKey(modelColumn));
			table.setColumnWidth(modelColumn, columnWidth);
		}
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
			EAM.mainWindow.getAppPreferences().setTaggedInt(getKey(modelColumn), column.getWidth());
		}
	}
	
	private String getKey(int modelColumn)
	{
		String columnTag = model.getColumnTag(modelColumn);
		String tableIdentifier = table.getUniqueTableIdentifier();
		
		return tableIdentifier + "." + columnTag;
	}

	private TreeTableWithColumnWidthSaving table;
	private GenericTreeTableModel model;
}
