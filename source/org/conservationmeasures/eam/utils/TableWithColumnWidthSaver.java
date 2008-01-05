/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.utils;

import java.util.Vector;

import javax.swing.table.TableModel;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTable;

abstract public class TableWithColumnWidthSaver extends PanelTable implements TableWithRowHeightManagement
{
	public TableWithColumnWidthSaver(TableModel model)
	{
		super(model);
		rowHeightListeners = new Vector<RowHeightListener>();
		
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
		rowHeightSaver.manage(this, getUniqueTableIdentifier());
	}
	
	public void addRowHeightListener(RowHeightListener listener)
	{
		rowHeightListeners.add(listener);
	}
	
	protected int getSavedColumnWidth(int column)
	{
		return columnWidthSaver.getColumnWidth(column);
	}
	
	public boolean shouldSaveColumnWidth()
	{
		return true;
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(rowHeightListeners == null)
			return;
		
		for(RowHeightListener listener : rowHeightListeners)
		{
			listener.rowHeightChanged(rowHeight);
		}
	}
	
	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
	private Vector<RowHeightListener> rowHeightListeners;
}
