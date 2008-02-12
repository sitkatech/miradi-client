/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.treetables;

import java.util.Vector;

import org.miradi.project.Project;
import org.miradi.utils.ColumnSequenceSaver;
import org.miradi.utils.ColumnWidthSaver;
import org.miradi.utils.RowHeightListener;
import org.miradi.utils.TableRowHeightSaver;

abstract public class TreeTableWithColumnWidthSaving extends TreeTableWithStateSaving
{
	public TreeTableWithColumnWidthSaving(Project projectToUse, GenericTreeTableModel treeTableModel)
	{
		super(projectToUse, treeTableModel);
		rowHeightListeners = new Vector<RowHeightListener>();
		columnWidthSaver = new ColumnWidthSaver(this, treeTableModel, getUniqueTableIdentifier());
		columnSequenceSaver = new ColumnSequenceSaver(this, treeTableModel, getUniqueTableIdentifier());

		getTableHeader().addMouseListener(columnWidthSaver);
		getTableHeader().addMouseListener(columnSequenceSaver);
		addRowHeightSaver();
	}
	
	public void rebuildTableCompletely() throws Exception
	{
		super.rebuildTableCompletely();
		columnWidthSaver.restoreColumnWidths();
		columnSequenceSaver.restoreColumnSequences();
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
	private ColumnSequenceSaver columnSequenceSaver;
	private Vector<RowHeightListener> rowHeightListeners;

}
