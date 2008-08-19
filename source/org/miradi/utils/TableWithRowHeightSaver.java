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
package org.miradi.utils;

import java.awt.Rectangle;

import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;

import org.miradi.dialogs.fieldComponents.PanelTable;
import org.miradi.dialogs.tablerenderers.DefaultTableCellRendererWithPreferredHeightFactory;
import org.miradi.dialogs.tablerenderers.TableCellPreferredHeightProvider;
import org.miradi.main.MainWindow;

import com.java.sun.jtreetable.JTreeTable.TreeTableCellRenderer;

abstract public class TableWithRowHeightSaver extends PanelTable implements TableWithRowHeightManagement
{
	public TableWithRowHeightSaver(MainWindow mainWindowToUse, TableModel model)
	{
		super(mainWindowToUse, model);
		
		cellRendererFactory = new DefaultTableCellRendererWithPreferredHeightFactory();
		
		rowHeightSaver = new TableRowHeightSaver();
		rowHeightSaver.manage(getMainWindow(), this, getUniqueTableIdentifier());
		
		rowHeightController = new SingleTableRowHeightController(getMainWindow(), this);
		
		getTableHeader().addMouseListener(new ColumnChangeHandler(this));
	}
	
	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public void setMultiTableRowHeightController(MultiTableRowHeightController listener)
	{
		rowHeightController.setMultiTableRowHeightController(listener);
		rowHeightSaver.setMultiTableRowHeightController(listener);
	}
	
	public void updateAutomaticRowHeights()
	{
		rowHeightController.updateAutomaticRowHeights();
	}
	
	public void setVariableRowHeight()
	{
		// NOTE: No action required for plain tables
	}
	
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(rowHeight);
	}
	
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(row, rowHeight);
	}
	
	public int getPreferredRowHeight(int row)
	{
		return getPreferredRowHeight(this, row);
	}
	
	public static int getPreferredRowHeight(JTable table, int row)
	{
		int maxPreferredHeight = 1;
		for(int column = 0; column < table.getColumnCount(); ++column)
		{
			TableCellRenderer rawRenderer = table.getCellRenderer(row, column);
			if(isTreeColumn(rawRenderer))
				continue;
				TableCellPreferredHeightProvider provider = (TableCellPreferredHeightProvider)rawRenderer;
			int thisHeight = provider.getPreferredHeight(table, row, column, table.getValueAt(row, column));
			maxPreferredHeight = Math.max(maxPreferredHeight, thisHeight);
		}
		
		final int ESTIMATED_CELL_PADDING_HEIGHT = 4;
		maxPreferredHeight += ESTIMATED_CELL_PADDING_HEIGHT;
		return maxPreferredHeight;
	}
	
	private static boolean isTreeColumn(TableCellRenderer rawRenderer)
	{
		// FIXME: This can go away when we have only single-column treetables
		return (rawRenderer instanceof TreeTableCellRenderer);
	}

	@Override
	public Rectangle getCellRect(int row, int column, boolean includeSpacing)
	{
		Rectangle cellRect = super.getCellRect(row, column, includeSpacing);
		if(!includeSpacing && getMainWindow().isRowHeightModeManual())
		{
			cellRect.height -= TableRowHeightSaver.ROW_RESIZE_MARGIN;
		}
		return cellRect;
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int column)
	{
		TableCellRenderer renderer = getColumnModel().getColumn(column).getCellRenderer();
		if(renderer != null)
			return renderer;
		
		return cellRendererFactory;
	}
	
	public JTable asTable()
	{
		return this;
	}
	

	abstract public String getUniqueTableIdentifier();
	
	private TableRowHeightSaver rowHeightSaver;
	private SingleTableRowHeightController rowHeightController;
	private DefaultTableCellRendererWithPreferredHeightFactory cellRendererFactory;
}
