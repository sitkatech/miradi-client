/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.utils;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTable;

abstract public class TableWithColumnWidthSaver extends PanelTable
{
	public TableWithColumnWidthSaver()
	{
		addColumnWidthSaver();
	}
	
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
		addColumnWidthSaver();
	}
	
	private void addColumnWidthSaver()
	{
		if (! shouldSaveColumnWidth())
			return; 
		
		columnWidthSaver = new ColumnWidthSaver(this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	protected int getSavedColumnWidth(int column)
	{
		return columnWidthSaver.getColumnWidth(column);
	}
	
	//FIXME is this enough 
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		if (columnWidthSaver != null)
			columnWidthSaver.restoreColumnWidths();
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
}
