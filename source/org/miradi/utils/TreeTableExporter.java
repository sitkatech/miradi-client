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

import javax.swing.JTree;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objects.BaseObject;

import com.java.sun.jtreetable.JTreeTable;

public class TreeTableExporter extends AbstractTableExporter
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

	@Override
	public String getIconAt(int row, int column)
	{
		return null;
	}

	public int getDepth(int row)
	{
		return getTree().getPathForRow(row).getPath().length - ROOT_PLUS_TOPLEVEL_ADJUSTMENT;
	}

	public int getMaxDepthCount()
	{
		int maxRowDepth = 0;
		int rowCount = getTree().getRowCount();
		for (int row = 0; row < rowCount; ++row)
		{
			int rowDepth = getDepth(row);
			maxRowDepth = Math.max(maxRowDepth, rowDepth);
		}
		
		return maxRowDepth;
	}

	@Override
	public int getRowCount()
	{
		return getTreeTable().getRowCount();
	}

	@Override
	public int getRowType(int row)
	{
		return 0;
	}

	@Override
	public String getTextAt(int row, int column)
	{
		if (column == 0)
			return getTree().getPathForRow(row).getLastPathComponent().toString();
		
		Object value = getTreeTable().getValueAt(row, column);
		if (value == null)
			return "";
		
		return value.toString();
	}

	public JTree getTree()
	{
		return getTreeTable().getTree();
	}
	
	private JTreeTable getTreeTable()
	{
		return treeTable;
	}

	private JTreeTable treeTable;
	private static final int ROOT_PLUS_TOPLEVEL_ADJUSTMENT = 2;
}
