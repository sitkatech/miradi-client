package org.conservationmeasures.eam.utils;

import javax.swing.table.TableModel;

abstract public class TableWithColumnWidthSaver extends TableWithHelperMethods
{
	public TableWithColumnWidthSaver()
	{
	}
	
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
	}
	
	abstract public String getUniqueTableIdentifier();
	
	ColumnWidthSaver columnWidthSaver;
}
