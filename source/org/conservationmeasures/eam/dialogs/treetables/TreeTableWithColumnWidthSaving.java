/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.treetables;

import java.util.Vector;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ColumnSequenceSaver;
import org.conservationmeasures.eam.utils.ColumnWidthSaver;
import org.conservationmeasures.eam.utils.RowHeightListener;
import org.conservationmeasures.eam.utils.TableRowHeightSaver;

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
