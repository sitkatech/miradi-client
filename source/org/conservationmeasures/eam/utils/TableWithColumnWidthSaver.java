/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.utils;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTable;

abstract public class TableWithColumnWidthSaver extends PanelTable
{
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
		addColumnWidthSaver();
		addRowHeightSaver();
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	private void addRowHeightSaver()
	{
		TableRowHeightSaver rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(this);
	}
	
	protected int getSavedColumnWidth(int column)
	{
		return columnWidthSaver.getColumnWidth(column);
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
}
