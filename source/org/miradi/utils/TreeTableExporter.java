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

	public int getDepth(int row)
	{
		return getTree().getPathForRow(row).getPath().length - ROOT_PLUS_TOPLEVEL_ADJUSTMENT;
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
		if (column == 0)
		{
			TreeTableNode node = (TreeTableNode) getTree().getPathForRow(row).getLastPathComponent();
			return node.toRawString();
		}
		
		Object value = getTreeTable().getValueAt(row, column);
		return getSafeValue(value);
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
}
