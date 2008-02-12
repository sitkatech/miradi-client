/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.miradi.utils;

import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.table.TableModel;

public class TableWithSunBugWorkarounds extends JTable
{
	public TableWithSunBugWorkarounds()
	{
		super();
	}
	
	public TableWithSunBugWorkarounds(TableModel model)
	{
		super(model);
		
		// this property is set due to a JTable bug#4724980 
		putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
	}

	// this is overridden to work around JTable bug#4330950
	// where if you click on a heading during a cell edit, the 
	// edits are silently discarded
	public void columnMoved(TableColumnModelEvent e) 
	{
		if (isEditing()) {
			cellEditor.stopCellEditing();
		}
		super.columnMoved(e);
	}

	// this is overridden to work around JTable bug#4330950
	// where if you click on a heading during a cell edit, the 
	// edits are silently discarded
	public void columnMarginChanged(ChangeEvent e) 
	{
		if (isEditing()) {
			cellEditor.stopCellEditing();
		}
		super.columnMarginChanged(e);
	}
	
	public int getSelectedRow()
	{
		// NOTE: Java sometimes returns non-empty selected 
		// rows when the table is empty, so check that first.
		// JAVA BUG #4247579,  Java 1.4.2 2006-10-27 kbs
		if(getRowCount() == 0)
			return -1;
		
		return super.getSelectedRow();
	}
	
	public int[] getSelectedRows()
	{
		// NOTE: Java sometimes returns non-empty selected 
		// rows when the table is empty, so check that first.
		// JAVA BUG #4247579,  Java 1.4.2 2006-10-27 kbs
		if(getRowCount() == 0)
			return new int[0];
		
		return super.getSelectedRows();
	}

	public int getSelectedRowCount()
	{
		// NOTE: Java sometimes returns non-empty selected 
		// rows when the table is empty, so check that first.
		// JAVA BUG #4247579,  Java 1.4.2 2006-10-27 kbs
		if(getRowCount() == 0)
			return 0;
		
		return super.getSelectedRowCount();
	}

}
