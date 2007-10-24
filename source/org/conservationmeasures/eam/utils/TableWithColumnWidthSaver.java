package org.conservationmeasures.eam.utils;

import javax.swing.event.TableModelEvent;
import javax.swing.table.TableModel;

abstract public class TableWithColumnWidthSaver extends TableWithHelperMethods
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
		columnWidthSaver = new ColumnWidthSaver(this, (ColumnTagProvider)getModel(), getUniqueTableIdentifier());
		getTableHeader().addMouseListener(columnWidthSaver);
		columnWidthSaver.restoreColumnWidths();
	}
	
	//FIXME is this enough 
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		if (columnWidthSaver != null)
			columnWidthSaver.restoreColumnWidths();
	}
	
	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
}
