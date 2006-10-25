/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.TreePath;

import com.java.sun.jtreetable.TreeTableModel;

public class EAMTreeTableModelAdapter extends AbstractTableModel
{

	public EAMTreeTableModelAdapter(TreeTableModel treeTableModel, JTree tree)
	{
        this.tree = tree;
        this.treeTableModel = treeTableModel;

        setExpansionListeners(tree);
        setModelListerners(treeTableModel);
	}

	private void setModelListerners(TreeTableModel treeTableModel)
	{
		treeTableModel.addTreeModelListener(new TreeModelListener() 
		{
			public void treeNodesChanged(TreeModelEvent e) 
			{
				delayedFireTableRowsUpdated();
			}

			public void treeNodesInserted(TreeModelEvent e) 
			{
				delayedFireTableRowsInserted();
			}

			public void treeNodesRemoved(TreeModelEvent e) 
			{
				delayedFireTableRowsRemoved();
			}

			public void treeStructureChanged(TreeModelEvent e) 
			{
				delayedFireTableDataChanged();
			}
		});
	}

	private void setExpansionListeners(JTree tree)
	{
		tree.addTreeExpansionListener(new TreeExpansionListener() 
		{
			public void treeExpanded(TreeExpansionEvent event) 
			{
				fireTableDataChanged(); 
			}
			public void treeCollapsed(TreeExpansionEvent event) 
			{
				fireTableDataChanged(); 
			}
		});
	}

	public int getColumnCount() 
	{
		return treeTableModel.getColumnCount();
	}

	public String getColumnName(int column) 
	{
		return treeTableModel.getColumnName(column);
	}

	public Class getColumnClass(int column) 
	{
		return treeTableModel.getColumnClass(column);
	}

	public int getRowCount() 
	{
		return tree.getRowCount();
	}

	protected Object nodeForRow(int row) 
	{
		TreePath treePath = tree.getPathForRow(row);
		return treePath.getLastPathComponent();         
	}

	public Object getValueAt(int row, int column) 
	{
		return treeTableModel.getValueAt(nodeForRow(row), column);
	}

	public boolean isCellEditable(int row, int column) 
	{
		return treeTableModel.isCellEditable(nodeForRow(row), column); 
	}

	public void setValueAt(Object value, int row, int column) 
	{
		treeTableModel.setValueAt(value, nodeForRow(row), column);
	}

	protected void delayedFireTableRowsUpdated() 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				fireTableRowsUpdated(0, getRowCount() - 1);
			}
		});
	}
	
	protected void delayedFireTableRowsRemoved() 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				fireTableDataChanged();			
			}
			
		});
	}

	protected void delayedFireTableRowsInserted() 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				fireTableDataChanged();
			}
		});
	}
	
	protected void delayedFireTableDataChanged() 
	{
		SwingUtilities.invokeLater(new Runnable() 
		{
			public void run() 
			{
				fireTableRowsUpdated(0, getRowCount() - 1);
			}
		});
	}

    JTree tree;
    TreeTableModel treeTableModel;
}
