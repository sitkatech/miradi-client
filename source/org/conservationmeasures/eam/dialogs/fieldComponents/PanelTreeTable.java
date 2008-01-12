/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.fieldComponents;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableModelEvent;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.tree.TreePath;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

import com.java.sun.jtreetable.JTreeTable;
import com.java.sun.jtreetable.TreeTableModel;

abstract public class PanelTreeTable extends JTreeTable 
{

	public PanelTreeTable(TreeTableModel treeTableModel)
	{
		super(treeTableModel);
		setFont(getMainWindow().getUserDataPanelFont());
		setRowHeight(getFontMetrics(getFont()).getHeight());
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
	
	//TODO: Richard: should not use static ref here
	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
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

	public Object getObjectForRow(int row)
	{
		TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();         
	}

	public TreeTableNode getNodeForRow(int row)
	{
		return (TreeTableNode)getObjectForRow(row);
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
		int toModelColumn = convertColumnIndexToModel(toTableColumn+1);
		if(toTableColumn == 0 && toModelColumn == TREE_COLUMN_INDEX)
			moveColumn(toTableColumn, fromTableIndex);
		else
			super.columnMoved(event);
	}
	
	public static class MouseMoveTreeColumnPreventerHandler extends MouseAdapter
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
	}
	
	public static final int TREE_COLUMN_INDEX = 0;
}
