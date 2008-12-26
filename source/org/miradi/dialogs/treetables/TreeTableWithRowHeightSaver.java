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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TableModelEvent;
import javax.swing.tree.TreePath;

import org.miradi.main.MainWindow;
import org.miradi.utils.ColumnChangeHandler;
import org.miradi.utils.MultiTableRowHeightController;
import org.miradi.utils.SingleTableRowHeightController;
import org.miradi.utils.TableRowHeightManager;
import org.miradi.utils.TableWithRowHeightManagement;
import org.miradi.utils.TableWithRowHeightSaver;

abstract public class TreeTableWithRowHeightSaver extends PanelTreeTable implements TableWithRowHeightManagement
{
	public TreeTableWithRowHeightSaver(MainWindow mainWindowToUse, GenericTreeTableModel treeTableModelToUse)
	{
		super(mainWindowToUse, treeTableModelToUse);

		treeTableModel = treeTableModelToUse;
		rowHeightSaver = new TableRowHeightManager(getMainWindow().getProject());
		rowHeightSaver.manage(getMainWindow(), this, getUniqueTableIdentifier());
		
		rowHeightController = new SingleTableRowHeightController(getMainWindow(), this);
		
		getTableHeader().addMouseListener(new ColumnChangeHandler(this));
	}

	public GenericTreeTableModel getTreeTableModel()
	{
		return treeTableModel;
	}
	
	public boolean allowUserToSetRowHeight()
	{
		return true;
	}
	
	public boolean shouldSaveRowHeight()
	{
		return true;
	}
	
	public void rebuildTableCompletely() throws Exception
	{
		super.rebuildTableCompletely();
		
		rowHeightSaver.restoreRowHeight();
	}

	public void updateAutomaticRowHeights()
	{
		if(getParent() == null || rowHeightController == null)
			return;
		
		rowHeightController.updateAutomaticRowHeights();
	}

	public void setMultiTableRowHeightController(MultiTableRowHeightController listener)
	{
		rowHeightController.setMultiTableRowHeightController(listener);
		rowHeightSaver.setMultiTableRowHeightController(listener);
	}
	
	public void setVariableRowHeight()
	{
		getTree().setRowHeight(-1);
		horribleHackToForceJavaToForgetCachedTreeCellHeights();
	}

	private void horribleHackToForceJavaToForgetCachedTreeCellHeights()
	{
		JTree thisTree = getTree();
		boolean originalSetting = thisTree.getShowsRootHandles();
		thisTree.setShowsRootHandles(!originalSetting);
		thisTree.setShowsRootHandles(originalSetting);
	}
	
	public int getPreferredRowHeight(int row)
	{
		final int FIRST_NON_TREE_COLUMN = 1;
		int preferredHeight = 1;
		for(int column = 0; column < getColumnCount(); ++column)
		{
			int thisHeight = 1;
			if(column < FIRST_NON_TREE_COLUMN)
				thisHeight = getPreferredTreeCellHeight(row);
			else
				thisHeight = TableWithRowHeightSaver.getPreferredRowHeight(this, row, FIRST_NON_TREE_COLUMN);
			
			preferredHeight = Math.max(preferredHeight, thisHeight);
		}
		
		return preferredHeight;
	}
	
	public int getPreferredTreeCellHeight(int row)
	{
		TreePath pathForRow = tree.getPathForRow(row);
		if(pathForRow == null)
			return 1;
		
		Object value = pathForRow.getLastPathComponent();
		boolean selected = false;
		boolean expanded = tree.isExpanded(row);
		boolean leaf = false;
		boolean hasFocus = false;

		VariableHeightTreeCellRenderer rendererFactory = new VariableHeightTreeCellRenderer(this);
		Component rendererComponent = rendererFactory.getTreeCellRendererComponentWithPreferredHeight(tree, value, selected, expanded, leaf, row, hasFocus);
		return rendererComponent.getPreferredSize().height;
	}
	
	public void saveRowHeight(int newRowHeight)
	{
		rowHeightSaver.saveRowHeightIgnoreExceptions();
	}

	@Override
	public void tableChanged(TableModelEvent e)
	{
		super.tableChanged(e);
		updateAutomaticRowHeights();
	}
	
	@Override
	public void setRowHeight(int row, int rowHeight)
	{
		super.setRowHeight(row, rowHeight);
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(row, rowHeight);
	}
	
	@Override
	public void setRowHeight(int rowHeight)
	{
		super.setRowHeight(rowHeight);
		if(getMainWindow() == null)
			return;
		
		if(getMainWindow().isRowHeightModeAutomatic())
			return;
		
		if(rowHeightSaver == null)
			return;
		
		rowHeightSaver.rowHeightChanged(rowHeight);
	}
		
	public JTable asTable()
	{
		return this;
	}
	
	
	abstract public String getUniqueTableIdentifier();
	
	private GenericTreeTableModel treeTableModel;
	private TableRowHeightManager rowHeightSaver;
	private SingleTableRowHeightController rowHeightController;
}
