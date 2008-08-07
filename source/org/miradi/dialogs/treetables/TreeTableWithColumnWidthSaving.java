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

import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Vector;

import javax.swing.JTable;

import org.miradi.main.MainWindow;
import org.miradi.utils.ColumnSequenceSaver;
import org.miradi.utils.ColumnWidthSaver;
import org.miradi.utils.RowHeightListener;
import org.miradi.utils.TableRowHeightSaver;
import org.miradi.utils.TableWithRowHeightManagement;

abstract public class TreeTableWithColumnWidthSaving extends PanelTreeTable implements TableWithRowHeightManagement
{
	public TreeTableWithColumnWidthSaving(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModel)
	{
		super(mainWindowToUse, treeTableModel);
		rowHeightListeners = new Vector<RowHeightListener>();
		columnWidthSaver = new ColumnWidthSaver(this, treeTableModel, getUniqueTableIdentifier());
		columnSequenceSaver = new ColumnSequenceSaver(this, treeTableModel, getUniqueTableIdentifier());

		getTableHeader().addMouseListener(columnWidthSaver);
		getTableHeader().addMouseListener(columnSequenceSaver);
		
		rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(getMainWindow(), this, getUniqueTableIdentifier());
		
		addComponentListener(new ComponentEventHandler());

	}
	
	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public void rebuildTableCompletely() throws Exception
	{
		super.rebuildTableCompletely();
		columnWidthSaver.restoreColumnWidths();
		columnSequenceSaver.restoreColumnSequences();
		adjustRowHeights();

	}

	public void addRowHeightListener(RowHeightListener listener)
	{
		rowHeightListeners.add(listener);
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(getMainWindow() == null)
			return;
		
		if(getMainWindow().isRowHeightModeAutomatic())
			return;
		
		if(rowHeightSaver != null)
			rowHeightSaver.saveRowHeight();
		
		if(rowHeightListeners == null)
			return;
		
		for(RowHeightListener listener : rowHeightListeners)
		{
			listener.rowHeightChanged(rowHeight);
		}
	}
	
	@Override
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);

		if(rowHeightListeners == null)
			return;
		
		for(RowHeightListener listener : rowHeightListeners)
		{
			listener.rowHeightChanged(row, rowHeight);
		}
	}
	
	public JTable asTable()
	{
		return this;
	}
	
	private void adjustRowHeights()
	{
		if(getMainWindow().isRowHeightModeManual())
			return;
		
		for(int row = 0; row < getRowCount(); ++row)
		{
			int height = getPreferredRowHeight(row);
			setRowHeight(row, height);
		}
		getTree().setRowHeight(-1);
	}

	class ComponentEventHandler implements ComponentListener
	{
		public void componentShown(ComponentEvent e)
		{
			adjustRowHeights();
		}
		
		public void componentHidden(ComponentEvent e)
		{
		}

		public void componentMoved(ComponentEvent e)
		{
		}

		public void componentResized(ComponentEvent e)
		{
			adjustRowHeights();
		}

	}
	

	
	abstract public String getUniqueTableIdentifier();
	
	private ColumnWidthSaver columnWidthSaver;
	private ColumnSequenceSaver columnSequenceSaver;
	private TableRowHeightSaver rowHeightSaver;
	private Vector<RowHeightListener> rowHeightListeners;

}
