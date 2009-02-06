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
package org.miradi.utils;

import javax.swing.JTree;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.dialogs.treetables.ObjectTreeTable;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objects.BaseObject;

import com.java.sun.jtreetable.JTreeTable;

public class TreeTableExporter extends AbstractTreeTableOrModelExporter
{
	public TreeTableExporter(JTreeTable treeToUse)
	{
		treeTable = treeToUse;
	}
	
	public BaseObject getBaseObjectForRow(int row)
	{
		TreeTableNode node = (TreeTableNode) getTree().getPathForRow(row).getLastPathComponent();
		return node.getObject();
	}
	
	public String getHeaderFor(int column)
	{
		return getTreeTable().getColumnName(column);
	}
	
	@Override
	public int getColumnCount()
	{
		return getTreeTable().getColumnCount();
	}

	public int getDepth(int row, int column)
	{
		if (isTreeColumn(column))
			return getTree().getPathForRow(row).getPath().length - TOPLEVEL_ADJUSTMENT;
		
		return 0;
	}

	@Override
	public int getRowCount()
	{
		return getTreeTable().getRowCount();
	}

	@Override
	public int getRowType(int row)
	{
		TreeTableNode treeTableNode = (TreeTableNode) getTree().getPathForRow(row).getLastPathComponent();
		return treeTableNode.getObjectReference().getObjectType();
	}
	
	@Override
	public String getTextAt(int row, int column)
	{
		BaseObject baseObjectForRowColumn = getTreeTable().getBaseObjectForRowColumn(row, column);
		if (isTreeColumn(column))
			return getTextForTreeColumn(row, baseObjectForRowColumn);
		
		Object value = getTreeTable().getValueAt(row, column);
		return getSafeValue(value);
	}
	
	private String getTextForTreeColumn(int row, BaseObject baseObjectForRowColumn)
	{
		if (baseObjectForRowColumn != null)
			return baseObjectForRowColumn.toString();
	
		TreeTableNode node = getTreeTable().getNodeForRow(row);
		return node.toRawString();
	}

	public JTree getTree()
	{
		return getTreeTable().getTree();
	}
	
	protected ObjectTreeTable getTreeTable()
	{
		return (ObjectTreeTable) treeTable;
	}
	
	public GenericTreeTableModel getModel()
	{
		return getTreeTable().getTreeTableModel();
	}

	private JTreeTable treeTable;
}
