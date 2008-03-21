/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
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
