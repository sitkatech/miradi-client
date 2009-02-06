/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import org.miradi.main.MainWindow;
import org.miradi.utils.ExportableTreeTable;
import org.miradi.utils.TableWithHelperMethods;

import com.java.sun.jtreetable.TreeTableModel;

abstract public class PanelTreeTable extends ExportableTreeTable 
{

	public PanelTreeTable(MainWindow mainWindowToUse, TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		mainWindow = mainWindowToUse;
		setFont(getMainWindow().getUserDataPanelFont());
		getTableHeader().setFont(getMainWindow().getUserDataPanelFont());
		getTableHeader().addMouseMotionListener(new MouseMoveTreeColumnPreventerHandler());
		
		// NOTE: Without this, each node's size is fixed based on the initial text,
		// so if you later change the value to be longer, you'll get ... (ellipsis)
		// and the text will be truncated. With it, it "just works"
		getTree().setLargeModel(true);
	}
	
	public void rebuildTableCompletely() throws Exception
	{
		createDefaultColumnsFromModel();
		tableChanged(new TableModelEvent(getModel(), 0, getModel().getRowCount() - 1));
	}
	
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	
	//note: the four methods below are duplicate copies from UiTable
	public void setColumnWidthToHeaderWidth(int column)
	{
		setColumnWidth(column, getColumnHeaderWidth(column));
	}
	
	public void setColumnWidth(int column, int width) 
	{
		TableColumn columnToAdjust = getColumnModel().getColumn(column);
		columnToAdjust.setPreferredWidth(width);
		columnToAdjust.setWidth(width);
	}
	
	public int getColumnHeaderWidth(int column) 
	{
		TableColumn columnToAdjust = getColumnModel().getColumn(column);
		String padding = "    ";
		String value = (String)columnToAdjust.getHeaderValue() + padding;
		return getRenderedWidth(column, value);
	}
	
	public int getRenderedWidth(int column, String value) 
	{
		TableColumn columnToAdjust = getColumnModel().getColumn(column);
		TableCellRenderer renderer = columnToAdjust.getHeaderRenderer();
		if(renderer == null)
		{
			JTableHeader header = getTableHeader();
			renderer = header.getDefaultRenderer();
		}
		Component c = renderer.getTableCellRendererComponent(this, value, true, true, -1, column);
		int width = c.getPreferredSize().width;
		return width;
	}
	
	public void ensureSelectedRowVisible()
	{
		TableWithHelperMethods.ensureSelectedRowVisible(this);
	}
	

	public Object getRawObjectForRow(int row)
	{
		TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();         
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)getRawObjectForRow(row);
	}
	
	public void columnMoved(TableColumnModelEvent event)
	{
		int toTableColumn = event.getToIndex();
		int fromTableIndex = event.getFromIndex();
		if (fromTableIndex == toTableColumn)
			return;
		
		// NOTE: Move has already happened, to event.from is now at the 
		// to location in the table, and to+1 is the table column that was
		// previously at the "to" table column
		if(toTableColumn+1 >= getColumnCount())
		{
			super.columnMoved(event);
			return;
		}
		int toModelColumn = convertColumnIndexToModel(toTableColumn+1);
		if(toTableColumn == 0 && toModelColumn == TREE_COLUMN_INDEX)
			moveColumn(toTableColumn, fromTableIndex);
		else
			super.columnMoved(event);
	}
	
	public static class MouseMoveTreeColumnPreventerHandler implements MouseMotionListener
	{
		public void mouseMoved(MouseEvent e)
		{
			JTableHeader header = ((JTableHeader)e.getSource());
			int tableColumnAtPoint = header.columnAtPoint(e.getPoint());
			JTable table = header.getTable();
			int modelColumn = table.convertColumnIndexToModel(tableColumnAtPoint);
			boolean isTreeColumn =  modelColumn == TREE_COLUMN_INDEX;
			header.setReorderingAllowed(!isTreeColumn);
		}

		public void mouseDragged(MouseEvent e)
		{
		}
	}
	
	public static final int TREE_COLUMN_INDEX = 0;
	
	private MainWindow mainWindow;
}
